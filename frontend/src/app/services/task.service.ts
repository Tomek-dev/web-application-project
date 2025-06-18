import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserDto {
  id: string;
  username: string;
  email: string;
}

export interface Task {
  id: string;
  title: string;
  description: string;
  deadline: Date;
  projectId: string;
  sprintId?: string;
  assignedUserId?: string;
  assignedUser?: UserDto;
}

export interface TaskRequest {
  title: string;
  description: string;
  deadline: Date;
  sprintId?: string;
  assignedUserId?: string;
}

@Injectable({ providedIn: 'root' })
export class TaskService {
  private apiUrl = environment.apiUrl + '/v1/tasks';

  constructor(private http: HttpClient) {}

  getTasksByProject(projectId: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/project/${projectId}`);
  }

  getMyTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/my`);
  }

  createTask(projectId: string, data: TaskRequest): Observable<Task> {
    return this.http.post<Task>(`${this.apiUrl}/project/${projectId}`, data);
  }

  updateTask(taskId: string, data: TaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${taskId}`, data);
  }

  deleteTask(taskId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${taskId}`);
  }

  assignUser(taskId: string, userId: string): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${taskId}/assign-user/${userId}`, {});
  }

  assignSprint(taskId: string, sprintId: string): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${taskId}/assign-sprint/${sprintId}`, {});
  }

  deassignUser(taskId: string): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${taskId}/deassign-user`, {});
  }

  deassignSprint(taskId: string): Observable<Task> {
    return this.http.patch<Task>(`${this.apiUrl}/${taskId}/deassign-sprint`, {});
  }
} 