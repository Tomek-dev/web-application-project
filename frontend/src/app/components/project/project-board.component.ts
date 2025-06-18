import { Component, Input, OnInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService, Project } from '../../services/project.service';
import { TaskService, Task, TaskRequest, UserDto } from '../../services/task.service';
import { SprintService, Sprint, SprintRequest } from '../../services/sprint.service';
import { UserService } from '../../services/user.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast/toast.service';

@Component({
  selector: 'app-project-board',
  templateUrl: './project-board.component.html',
  styleUrls: ['./project-board.component.scss']
})
export class ProjectBoardComponent implements OnInit {
  projectId!: string;
  project?: Project;
  tasks: Task[] = [];
  sprints: Sprint[] = [];
  users: UserDto[] = [];
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private taskService: TaskService,
    private sprintService: SprintService,
    private userService: UserService,
    private dialog: MatDialog,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.projectId = this.route.snapshot.paramMap.get('projectId')!;
    this.loadProject();
    this.loadTasks();
    this.loadSprints();
  }

  loadProject() {
    this.projectService.getProjectById(this.projectId).subscribe({
      next: (data) => { 
        this.project = data; 
        this.loadUsers(data.workplaceId);
      },
      error: () => { this.toast.show('Failed to load project', 'error'); }
    });
  }

  loadTasks() {
    this.loading = true;
    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: (data) => { this.tasks = data; this.loading = false; },
      error: () => { this.toast.show('Failed to load tasks', 'error'); this.loading = false; }
    });
  }

  loadSprints() {
    this.sprintService.getSprintsByProject(this.projectId).subscribe({
      next: (data) => { this.sprints = data; },
      error: () => { this.toast.show('Failed to load sprints', 'error'); }
    });
  }

  loadUsers(workplaceId: string) {
    this.userService.getUsersByWorkplace(workplaceId).subscribe({
      next: (data) => { this.users = data; },
      error: () => { this.toast.show('Failed to load users', 'error'); }
    });
  }

  openAddTaskDialog() {
    const dialogRef = this.dialog.open(TaskDialogComponent, {
      width: '500px',
      data: { 
        title: '', 
        description: '', 
        deadline: null, 
        sprintId: null, 
        assignedUserId: null,
        sprints: this.sprints,
        users: this.users
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.createTask(this.projectId, result).subscribe({
          next: () => { this.toast.show('Task created', 'success'); this.loadTasks(); },
          error: () => { this.toast.show('Failed to create task', 'error'); }
        });
      }
    });
  }

  openAddSprintDialog() {
    const dialogRef = this.dialog.open(SprintDialogComponent, {
      width: '500px',
      data: { name: '', description: '', startDate: null, endDate: null }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.sprintService.createSprint(this.projectId, result).subscribe({
          next: () => { this.toast.show('Sprint created', 'success'); this.loadSprints(); },
          error: () => { this.toast.show('Failed to create sprint', 'error'); }
        });
      }
    });
  }

  editTask(task: Task) {
    const dialogRef = this.dialog.open(TaskDialogComponent, {
      width: '500px',
      data: { 
        title: task.title, 
        description: task.description, 
        deadline: new Date(task.deadline), 
        sprintId: task.sprintId, 
        assignedUserId: task.assignedUserId,
        sprints: this.sprints,
        users: this.users
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.taskService.updateTask(task.id, result).subscribe({
          next: () => { this.toast.show('Task updated', 'success'); this.loadTasks(); },
          error: () => { this.toast.show('Failed to update task', 'error'); }
        });
      }
    });
  }

  assignUser(task: Task, userId: string) {
    this.taskService.assignUser(task.id, userId).subscribe({
      next: () => { this.toast.show('User assigned to task', 'success'); this.loadTasks(); },
      error: () => { this.toast.show('Failed to assign user', 'error'); }
    });
  }

  deassignUser(task: Task) {
    this.taskService.deassignUser(task.id).subscribe({
      next: () => { this.toast.show('User removed from task', 'success'); this.loadTasks(); },
      error: () => { this.toast.show('Failed to remove user', 'error'); }
    });
  }

  deleteTask(task: Task) {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(task.id).subscribe({
        next: () => { this.toast.show('Task deleted', 'success'); this.loadTasks(); },
        error: () => { this.toast.show('Failed to delete task', 'error'); }
      });
    }
  }

  getTasksBySprint(sprintId: string): Task[] {
    return this.tasks.filter(task => task.sprintId === sprintId);
  }

  getUnassignedTasks(): Task[] {
    return this.tasks.filter(task => !task.sprintId);
  }

  getAssignedUser(task: Task): UserDto | undefined {
    if (task.assignedUserId) {
      return this.users.find(user => user.id === task.assignedUserId);
    }
    return undefined;
  }
}

@Component({
  selector: 'app-task-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.title ? 'Edit Task' : 'Create Task' }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-form-field class="full-width">
        <mat-label>Task Title</mat-label>
        <input matInput formControlName="title" placeholder="Enter task title">
        <mat-error *ngIf="form.get('title')?.hasError('required')">
          Task title is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description" placeholder="Enter task description" rows="3"></textarea>
        <mat-error *ngIf="form.get('description')?.hasError('required')">
          Description is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Deadline</mat-label>
        <input matInput [matDatepicker]="deadlinePicker" formControlName="deadline">
        <mat-datepicker-toggle matSuffix [for]="deadlinePicker"></mat-datepicker-toggle>
        <mat-datepicker #deadlinePicker></mat-datepicker>
        <mat-error *ngIf="form.get('deadline')?.hasError('required')">
          Deadline is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Sprint (Optional)</mat-label>
        <mat-select formControlName="sprintId">
          <mat-option [value]="null">No Sprint</mat-option>
          <mat-option *ngFor="let sprint of data.sprints" [value]="sprint.id">
            {{ sprint.name }}
          </mat-option>
        </mat-select>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Assigned User (Optional)</mat-label>
        <mat-select formControlName="assignedUserId">
          <mat-option [value]="null">No User</mat-option>
          <mat-option *ngFor="let user of data.users" [value]="user.id">
            {{ user.username }} ({{ user.email }})
          </mat-option>
        </mat-select>
      </mat-form-field>

      <div mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ data.title ? 'Update' : 'Create' }}
        </button>
      </div>
    </form>
  `
})
export class TaskDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TaskRequest & { sprints: Sprint[]; users: UserDto[] }
  ) {
    this.form = this.fb.group({
      title: [data.title, Validators.required],
      description: [data.description, Validators.required],
      deadline: [data.deadline, Validators.required],
      sprintId: [data.sprintId],
      assignedUserId: [data.assignedUserId]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
}

@Component({
  selector: 'app-sprint-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.name ? 'Edit Sprint' : 'Create Sprint' }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-form-field class="full-width">
        <mat-label>Sprint Name</mat-label>
        <input matInput formControlName="name" placeholder="Enter sprint name">
        <mat-error *ngIf="form.get('name')?.hasError('required')">
          Sprint name is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description" placeholder="Enter sprint description" rows="3"></textarea>
        <mat-error *ngIf="form.get('description')?.hasError('required')">
          Description is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Start Date</mat-label>
        <input matInput [matDatepicker]="startPicker" formControlName="startDate">
        <mat-datepicker-toggle matSuffix [for]="startPicker"></mat-datepicker-toggle>
        <mat-datepicker #startPicker></mat-datepicker>
        <mat-error *ngIf="form.get('startDate')?.hasError('required')">
          Start date is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>End Date</mat-label>
        <input matInput [matDatepicker]="endPicker" formControlName="endDate">
        <mat-datepicker-toggle matSuffix [for]="endPicker"></mat-datepicker-toggle>
        <mat-datepicker #endPicker></mat-datepicker>
        <mat-error *ngIf="form.get('endDate')?.hasError('required')">
          End date is required
        </mat-error>
      </mat-form-field>

      <div mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ data.name ? 'Update' : 'Create' }}
        </button>
      </div>
    </form>
  `
})
export class SprintDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<SprintDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SprintRequest
  ) {
    this.form = this.fb.group({
      name: [data.name, Validators.required],
      description: [data.description, Validators.required],
      startDate: [data.startDate, Validators.required],
      endDate: [data.endDate, Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
} 