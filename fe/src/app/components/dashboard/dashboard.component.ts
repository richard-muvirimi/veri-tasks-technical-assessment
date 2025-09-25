import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import { Auth } from '../../services/auth.service';
import { TaskService } from '../../services/task.service';
import { User, Task } from '../../interfaces';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  tasks: Task[] = [];
  currentUser: User | null = null;
  taskForm: FormGroup;
  isLoading = false;

  constructor(
    private auth: Auth,
    private taskService: TaskService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required]],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.currentUser = this.auth.getCurrentUser();

    // Listen for authentication state changes
    this.auth.currentUser$.subscribe(user => {
      this.currentUser = user;
      // Load tasks when user becomes authenticated
      if (user && this.auth.isAuthenticated() && this.auth.getToken()) {
        this.loadTasks();
      }
    });

    // Add a small delay to ensure browser platform detection works properly
    setTimeout(() => {
      // Also load tasks immediately if already authenticated
      const hasToken = !!this.auth.getToken();
      const isAuth = this.auth.isAuthenticated();

      // Load tasks if we have a valid token, even if user object is missing
      if (hasToken && isAuth) {
        this.loadTasks();
      }
    }, 100); // Small delay to ensure platform detection works
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
      },
      error: (error) => {
        console.error('Error loading tasks:', error);

        // If we get a 403 Forbidden error, it likely means the JWT token is invalid or expired
        if (error.status === 403) {
          this.auth.logout();
          this.router.navigate(['/login']);
        }
      }
    });
  }

  addTask(): void {
    if (this.taskForm.valid) {
      this.isLoading = true;
      const newTask: Omit<Task, 'id'> = {
        title: this.taskForm.value.title,
        description: this.taskForm.value.description,
        completed: false
      };

      this.taskService.createTask(newTask).subscribe({
        next: (task) => {
          this.tasks.push(task);
          this.taskForm.reset();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error creating task:', error);
          this.isLoading = false;

          // If we get a 403 Forbidden error, it likely means the JWT token is invalid or expired
          if (error.status === 403) {
            console.error('403 Forbidden during task creation - clearing session and redirecting to login.');
            this.auth.logout();
            this.router.navigate(['/login']);
          }
        }
      });
    }
  }

  toggleTaskStatus(task: Task): void {
    const newCompleted = !task.completed;
    this.isLoading = true;

    this.taskService.updateTask(task.id!, { completed: newCompleted }).subscribe({
      next: (updatedTask) => {
        const index = this.tasks.findIndex(t => t.id === task.id);
        if (index !== -1) {
          this.tasks[index] = updatedTask;
        }
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error updating task:', error);
        this.isLoading = false;
      }
    });
  }

  deleteTask(taskId: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.isLoading = true;
      this.taskService.deleteTask(taskId).subscribe({
        next: () => {
          this.tasks = this.tasks.filter(t => t.id !== taskId);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error deleting task:', error);
          this.isLoading = false;
        }
      });
    }
  }

  getPendingCount(): number {
    return this.tasks.filter(task => !task.completed).length;
  }

  getCompletedCount(): number {
    return this.tasks.filter(task => task.completed).length;
  }
}
