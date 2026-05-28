import { Injectable, signal, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
}

const ACCESS_TOKEN_KEY = 'access_token';
const ACCESS_TOKEN_PERSIST_KEY = 'access_token_persist';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Servicio de autenticación: login, registro y manejo del token
  // Comentarios simples en español para facilitar lectura
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private isBrowser: boolean;
  
  // Use Angular Signals for state management
  isAuthenticated = signal<boolean>(false);

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    this.restoreAuthState();
  }

  login(credentials: any, rememberMe = false): Observable<AuthResponse> {
    const body = { ...credentials, rememberMe };

    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, body).pipe(
      tap(response => {
        this.setToken(response.accessToken, rememberMe);
        this.isAuthenticated.set(true);
      })
    );
  }

  register(details: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, details).pipe(
      tap(response => {
        this.setToken(response.accessToken, false);
        this.isAuthenticated.set(true);
      })
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      sessionStorage.removeItem(ACCESS_TOKEN_KEY);
      localStorage.removeItem(ACCESS_TOKEN_PERSIST_KEY);
    }
    this.isAuthenticated.set(false);
  }

  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }

    const token = sessionStorage.getItem(ACCESS_TOKEN_KEY) ?? localStorage.getItem(ACCESS_TOKEN_KEY);
    if (!token) {
      this.isAuthenticated.set(false);
      return null;
    }

    if (this.isTokenExpired(token)) {
      this.logout();
      return null;
    }

    // Keep signal state in sync with persisted token state.
    if (!this.isAuthenticated()) {
      this.isAuthenticated.set(true);
    }

    return token;
  }

  private setToken(token: string, rememberMe: boolean): void {
    if (!this.isBrowser) {
      return;
    }

    localStorage.removeItem(ACCESS_TOKEN_KEY);
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);

    if (rememberMe) {
      localStorage.setItem(ACCESS_TOKEN_KEY, token);
      localStorage.setItem(ACCESS_TOKEN_PERSIST_KEY, 'true');
    } else {
      sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
      localStorage.removeItem(ACCESS_TOKEN_PERSIST_KEY);
    }
  }

  private restoreAuthState(): void {
    if (!this.isBrowser) {
      return;
    }

    this.isAuthenticated.set(!!this.getToken());
  }

  private isTokenExpired(token: string): boolean {
    const payload = this.decodePayload(token);
    if (!payload?.exp) {
      return true;
    }

    return Date.now() >= payload.exp * 1000;
  }

  private decodePayload(token: string): { exp?: number } | null {
    try {
      const payloadPart = token.split('.')[1];
      if (!payloadPart) {
        return null;
      }

      const normalized = payloadPart.replace(/-/g, '+').replace(/_/g, '/');
      const padded = normalized + '='.repeat((4 - (normalized.length % 4)) % 4);
      return JSON.parse(atob(padded));
    } catch {
      return null;
    }
  }
}
