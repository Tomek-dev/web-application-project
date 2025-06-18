import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Angular Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTooltipModule } from '@angular/material/tooltip';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ToastComponent } from './components/toast/toast.component';
import { DashboardComponent, WorkplaceDialogComponent } from './components/dashboard/dashboard.component';
import { WorkplaceRolesComponent, AddWorkplaceRoleDialogComponent, ProjectsDialogComponent } from './components/workplace/workplace-roles.component';
import { WorkplaceDetailsComponent } from './components/workplace/workplace-details.component';
import { ProjectListComponent, ProjectDialogComponent } from './components/project/project-list.component';
import { ProjectBoardComponent, TaskDialogComponent, SprintDialogComponent } from './components/project/project-board.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { UserService } from './services/user.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ToastComponent,
    DashboardComponent,
    WorkplaceDialogComponent,
    WorkplaceRolesComponent,
    AddWorkplaceRoleDialogComponent,
    WorkplaceDetailsComponent,
    ProjectListComponent,
    ProjectDialogComponent,
    ProjectsDialogComponent,
    ProjectBoardComponent,
    TaskDialogComponent,
    SprintDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatTableModule,
    MatDialogModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTooltipModule,
    RouterModule.forRoot([
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'workplace/:id', component: WorkplaceDetailsComponent },
      { path: 'project/:projectId/board', component: ProjectBoardComponent },
      { path: '', redirectTo: '/login', pathMatch: 'full' }
    ])
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
