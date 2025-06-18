import { Component, Input, OnInit, Inject } from '@angular/core';
import { ProjectService, Project, ProjectRequest } from '../../services/project.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit {
  @Input() workplaceId!: string;
  projects: Project[] = [];
  loading = false;

  constructor(
    private projectService: ProjectService,
    private dialog: MatDialog,
    private toast: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects() {
    this.loading = true;
    this.projectService.getProjectsByWorkplace(this.workplaceId).subscribe({
      next: (data) => { this.projects = data; this.loading = false; },
      error: () => { this.toast.show('Failed to load projects', 'error'); this.loading = false; }
    });
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      data: { name: '', description: '', startDate: null, endDate: null }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.projectService.createProject(this.workplaceId, result).subscribe({
          next: () => { this.toast.show('Project created', 'success'); this.loadProjects(); },
          error: () => { this.toast.show('Failed to create project', 'error'); }
        });
      }
    });
  }

  openEditDialog(project: Project) {
    const dialogRef = this.dialog.open(ProjectDialogComponent, {
      width: '500px',
      data: { 
        name: project.name, 
        description: project.description, 
        startDate: new Date(project.startDate), 
        endDate: new Date(project.endDate) 
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.projectService.updateProject(project.id, result).subscribe({
          next: () => { this.toast.show('Project updated', 'success'); this.loadProjects(); },
          error: () => { this.toast.show('Failed to update project', 'error'); }
        });
      }
    });
  }

  openBoard(project: Project) {
    this.router.navigate(['/project', project.id, 'board']);
  }

  deleteProject(project: Project) {
    if (confirm('Are you sure you want to delete this project?')) {
      this.projectService.deleteProject(project.id).subscribe({
        next: () => { this.toast.show('Project deleted', 'success'); this.loadProjects(); },
        error: () => { this.toast.show('Failed to delete project', 'error'); }
      });
    }
  }
}

@Component({
  selector: 'app-project-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.name ? 'Edit Project' : 'Create Project' }}</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-form-field class="full-width">
        <mat-label>Project Name</mat-label>
        <input matInput formControlName="name" placeholder="Enter project name">
        <mat-error *ngIf="form.get('name')?.hasError('required')">
          Project name is required
        </mat-error>
      </mat-form-field>

      <mat-form-field class="full-width">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description" placeholder="Enter project description" rows="3"></textarea>
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
export class ProjectDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ProjectDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ProjectRequest
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