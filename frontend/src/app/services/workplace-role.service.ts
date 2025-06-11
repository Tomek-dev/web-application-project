import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type UserRoleType = 'ADMIN' | 'PROJECT_MANAGER' | 'USER' | 'VIEWER';

export interface WorkplaceRole {
  id: string;
  userId: string;
  username: string;
  email: string;
  roleType: UserRoleType;
}

@Injectable({ providedIn: 'root' })
export class WorkplaceRoleService {
  private apiUrl = environment.apiUrl + '/v1/workplaces';

  constructor(private http: HttpClient) {}

  getRolesByWorkplace(workplaceId: string): Observable<WorkplaceRole[]> {
    return this.http.get<WorkplaceRole[]>(`${this.apiUrl}/${workplaceId}/roles`);
  }

  addRole(workplaceId: string, userId: string, roleType: UserRoleType): Observable<WorkplaceRole> {
    const params = new HttpParams().set('userId', userId).set('roleType', roleType);
    return this.http.post<WorkplaceRole>(`${this.apiUrl}/${workplaceId}/roles`, null, { params });
  }

  updateRole(workplaceId: string, roleId: string, roleType: UserRoleType): Observable<WorkplaceRole> {
    const params = new HttpParams().set('roleType', roleType);
    return this.http.put<WorkplaceRole>(`${this.apiUrl}/${workplaceId}/roles/${roleId}`, null, { params });
  }

  deleteRole(workplaceId: string, roleId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${workplaceId}/roles/${roleId}`);
  }
} 