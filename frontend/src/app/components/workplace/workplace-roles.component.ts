import { Component, Input, OnInit, Inject } from '@angular/core';
import { WorkplaceRoleService, WorkplaceRole, UserRoleType } from '../../services/workplace-role.service';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast/toast.service';

@Component({
  selector: 'app-workplace-roles',
  templateUrl: './workplace-roles.component.html',
  styleUrls: ['./workplace-roles.component.scss']
})
export class WorkplaceRolesComponent implements OnInit {
  @Input() workplaceId!: string;
  roles: WorkplaceRole[] = [];
  loading = false;
  roleTypes: UserRoleType[] = ['ADMIN', 'PROJECT_MANAGER', 'USER', 'VIEWER'];

  constructor(
    private roleService: WorkplaceRoleService,
    private dialog: MatDialog,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles() {
    this.loading = true;
    this.roleService.getRolesByWorkplace(this.workplaceId).subscribe({
      next: (data) => { this.roles = data; this.loading = false; },
      error: () => { this.toast.show('Failed to load roles', 'error'); this.loading = false; }
    });
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(AddWorkplaceRoleDialogComponent, {
      width: '400px',
      data: { workplaceId: this.workplaceId, roleTypes: this.roleTypes }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.roleService.addRole(this.workplaceId, result.userId, result.roleType).subscribe({
          next: () => { this.toast.show('User added to workplace', 'success'); this.loadRoles(); },
          error: () => { this.toast.show('Failed to add user', 'error'); }
        });
      }
    });
  }

  changeRole(role: WorkplaceRole, newRole: UserRoleType) {
    this.roleService.updateRole(this.workplaceId, role.id, newRole).subscribe({
      next: () => { this.toast.show('Role updated', 'success'); this.loadRoles(); },
      error: () => { this.toast.show('Failed to update role', 'error'); }
    });
  }

  removeRole(role: WorkplaceRole) {
    this.roleService.deleteRole(this.workplaceId, role.id).subscribe({
      next: () => { this.toast.show('User removed from workplace', 'success'); this.loadRoles(); },
      error: () => { this.toast.show('Failed to remove user', 'error'); }
    });
  }
}

@Component({
  selector: 'app-add-workplace-role-dialog',
  template: `
    <h2 mat-dialog-title>Add User to Workplace</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <mat-form-field class="full-width">
        <mat-label>User ID</mat-label>
        <input matInput formControlName="userId" placeholder="Enter user ID">
      </mat-form-field>
      <mat-form-field class="full-width">
        <mat-label>Role</mat-label>
        <mat-select formControlName="roleType">
          <mat-option *ngFor="let r of data.roleTypes" [value]="r">{{ r }}</mat-option>
        </mat-select>
      </mat-form-field>
      <div mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Add</button>
      </div>
    </form>
  `
})
export class AddWorkplaceRoleDialogComponent {
  form: FormGroup;
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddWorkplaceRoleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { workplaceId: string; roleTypes: UserRoleType[] }
  ) {
    this.form = this.fb.group({
      userId: ['', Validators.required],
      roleType: ['', Validators.required]
    });
  }
  onSubmit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
}

@Component({
  selector: 'app-projects-dialog',
  template: `
    <h2 mat-dialog-title>Zarządzanie projektami</h2>
    <mat-dialog-content>
      <app-project-list [workplaceId]="data.workplaceId"></app-project-list>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Zamknij</button>
    </mat-dialog-actions>
  `
})
export class ProjectsDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { workplaceId: string }) {}
} 