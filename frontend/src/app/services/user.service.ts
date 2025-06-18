import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserDto {
  id: string;
  username: string;
  email: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = environment.apiUrl + '/v1/users';

  constructor(private http: HttpClient) {}

  getUsersByWorkplace(workplaceId: string): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(`${environment.apiUrl}/v1/workplaces/${workplaceId}/users`);
  }

  getAllUsers(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(`${this.apiUrl}`);
  }
} 