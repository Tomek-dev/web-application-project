import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Project {
  id: string;
  name: string;
  description: string;
  startDate: Date;
  endDate: Date;
  workplaceId: string;
}

export interface ProjectRequest {
  name: string;
  description: string;
  startDate: Date;
  endDate: Date;
}

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private apiUrl = environment.apiUrl + '/v1/projects';

  constructor(private http: HttpClient) {}

  getProjectById(projectId: string): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${projectId}`);
  }

  getProjectsByWorkplace(workplaceId: string): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/workplace/${workplaceId}`);
  }

  getMyProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/my`);
  }

  createProject(workplaceId: string, data: ProjectRequest): Observable<Project> {
    return this.http.post<Project>(`${this.apiUrl}/workplace/${workplaceId}`, data);
  }

  updateProject(projectId: string, data: ProjectRequest): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${projectId}`, data);
  }

  deleteProject(projectId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${projectId}`);
  }
} 