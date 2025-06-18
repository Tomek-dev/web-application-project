import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Sprint {
  id: string;
  name: string;
  description: string;
  startDate: Date;
  endDate: Date;
  projectId: string;
}

export interface SprintRequest {
  name: string;
  description: string;
  startDate: Date;
  endDate: Date;
}

@Injectable({ providedIn: 'root' })
export class SprintService {
  private apiUrl = environment.apiUrl + '/v1/sprints';

  constructor(private http: HttpClient) {}

  getSprintsByProject(projectId: string): Observable<Sprint[]> {
    return this.http.get<Sprint[]>(`${this.apiUrl}/project/${projectId}`);
  }

  createSprint(projectId: string, data: SprintRequest): Observable<Sprint> {
    return this.http.post<Sprint>(`${this.apiUrl}/project/${projectId}`, data);
  }

  updateSprint(sprintId: string, data: SprintRequest): Observable<Sprint> {
    return this.http.put<Sprint>(`${this.apiUrl}/${sprintId}`, data);
  }

  deleteSprint(sprintId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${sprintId}`);
  }
} 