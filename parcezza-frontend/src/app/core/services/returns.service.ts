import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReturnResponse, ReturnStatus } from '../models/return.model';

@Injectable({
  providedIn: 'root'
})
export class ReturnsService {
  private readonly apiUrl = `${environment.apiUrl}/returns`;

  constructor(private http: HttpClient) {}

  requestReturn(orderId: number, reason: string): Observable<ReturnResponse> {
    return this.http.post<ReturnResponse>(`${this.apiUrl}/orders/${orderId}`, { reason });
  }

  getByOrder(orderId: number): Observable<ReturnResponse> {
    return this.http.get<ReturnResponse>(`${this.apiUrl}/orders/${orderId}`);
  }

  listAll() {
    return this.http.get<ReturnResponse[]>(`${this.apiUrl}`);
  }

  updateStatus(returnId: number, status: ReturnStatus, note?: string) {
    return this.http.patch<ReturnResponse>(`${this.apiUrl}/${returnId}/status`, { status, note });
  }
}
