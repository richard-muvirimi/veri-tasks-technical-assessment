import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../interfaces';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.scss']
})
export class TaskDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private taskService = inject(TaskService);

  task: Task | null = null;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;
    if (!id || isNaN(id)) {
      this.error = 'Invalid task id';
      this.loading = false;
      return;
    }

    this.taskService.getTask(id).subscribe({
      next: (t) => {
        this.task = t;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load task', err);
        this.error = 'Failed to load task';
        this.loading = false;
      }
    });
  }

  back() {
    this.router.navigate(['/dashboard']);
  }
}
