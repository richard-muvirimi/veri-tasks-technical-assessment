import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../interfaces';

@Component({
  selector: 'app-task-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.scss']
})
export class TaskEditComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private taskService = inject(TaskService);

  form: FormGroup;
  taskId: number | null = null;
  loading = true;
  saving = false;
  error: string | null = null;

  constructor() {
    this.form = this.fb.group({
      title: ['', [Validators.required]],
      description: [''],
      completed: [false]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;
    if (!id || isNaN(id)) {
      this.error = 'Invalid task id';
      this.loading = false;
      return;
    }
    this.taskId = id;

    this.taskService.getTask(id).subscribe({
      next: (t) => {
        this.form.patchValue({
          title: t.title,
          description: t.description,
          completed: t.completed
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load task for edit', err);
        this.error = 'Failed to load task for editing';
        this.loading = false;
      }
    });
  }

  save(): void {
    if (!this.taskId) return;
    if (this.form.invalid) return;
    this.saving = true;
    const payload: Partial<Task> = {
      title: this.form.value.title,
      description: this.form.value.description,
      completed: this.form.value.completed
    };

    this.taskService.updateTask(this.taskId, payload).subscribe({
      next: (updated) => {
        this.saving = false;
        // Navigate to view page after save
        this.router.navigate(['/tasks', this.taskId]);
      },
      error: (err) => {
        console.error('Failed to save task', err);
        this.error = 'Failed to save task';
        this.saving = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard']);
  }
}
