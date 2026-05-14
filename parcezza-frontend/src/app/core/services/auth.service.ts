import { Injectable, signal, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private isBrowser: boolean;
  
  // Use Angular Signals for state management
  isAuthenticated = signal<boolean>(false);

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    if (this.isBrowser) {
      this.isAuthenticated.set(this.hasToken());
    }
  }

  login(credentials: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        this.setToken(response.accessToken);
        this.isAuthenticated.set(true);
      })
    );
  }

  register(details: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, details).pipe(
      tap(response => {
        this.setToken(response.accessToken);
        this.isAuthenticated.set(true);
      })
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('access_token');
    }
    this.isAuthenticated.set(false);
  }

  getToken(): string | null {
    return this.isBrowser ? localStorage.getItem('access_token') : null;
  }

  private setToken(token: string): void {
    if (this.isBrowser) {
      localStorage.setItem('access_token', token);
    }
  }

  private hasToken(): boolean {
    return this.isBrowser ? !!localStorage.getItem('access_token') : false;
  }
}
