import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Workplace {
  id: string;
  name: string;
  description: string;
}

@Injectable({ providedIn: 'root' })
export class WorkplaceService {
  private apiUrl = environment.apiUrl + '/v1/workplaces';

  constructor(private http: HttpClient) {}

  getMyWorkplaces(): Observable<Workplace[]> {
    return this.http.get<Workplace[]>(`${this.apiUrl}/my`);
  }

  addWorkplace(data: { name: string; description: string }): Observable<Workplace> {
    return this.http.post<Workplace>(this.apiUrl, data);
  }

  updateWorkplace(id: string, data: { name: string; description: string }): Observable<Workplace> {
    return this.http.put<Workplace>(`${this.apiUrl}/${id}`, data);
  }

  deleteWorkplace(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 