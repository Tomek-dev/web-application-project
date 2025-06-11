import { Component, OnInit, Inject } from '@angular/core';
import { WorkplaceService, Workplace } from '../../services/workplace.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast/toast.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  workplaces: Workplace[] = [];
  loading = false;

  constructor(
    private workplaceService: WorkplaceService,
    private dialog: MatDialog,
    private fb: FormBuilder,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadWorkplaces();
  }

  loadWorkplaces() {
    this.loading = true;
    this.workplaceService.getMyWorkplaces().subscribe({
      next: (data) => { this.workplaces = data; this.loading = false; },
      error: () => { this.toast.show('Failed to load workplaces', 'error'); this.loading = false; }
    });
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(WorkplaceDialogComponent, {
      width: '400px',
      data: { name: '', description: '' }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.workplaceService.addWorkplace(result).subscribe({
          next: () => { this.toast.show('Workplace added', 'success'); this.loadWorkplaces(); },
          error: () => { this.toast.show('Failed to add workplace', 'error'); }
        });
      }
    });
  }
}

@Component({
  selector: 'app-workplace-dialog',
  template: `
    <h2 mat-dialog-title>Add Workplace</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-form-field class="full-width">
        <mat-label>Name</mat-label>
        <input matInput formControlName="name">
      </mat-form-field>
      <mat-form-field class="full-width">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description"></textarea>
      </mat-form-field>
      <div mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Add</button>
      </div>
    </form>
  `
})
export class WorkplaceDialogComponent {
  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<WorkplaceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { name: string; description: string }
  ) {
    this.form = this.fb.group({
      name: [data.name, Validators.required],
      description: [data.description]
    });
  }
  onSubmit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
} 