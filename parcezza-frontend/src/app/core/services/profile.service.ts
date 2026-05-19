import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProfileResponse, ProfileUpdateRequest, AddressResponse, AddressRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private readonly apiUrl = `${environment.apiUrl}/me`;

  constructor(private http: HttpClient) {}

  getProfile(): Observable<ProfileResponse> {
    return this.http.get<ProfileResponse>(this.apiUrl);
  }

  updateProfile(request: ProfileUpdateRequest): Observable<ProfileResponse> {
    return this.http.put<ProfileResponse>(this.apiUrl, request);
  }

  getAddresses(): Observable<AddressResponse[]> {
    return this.http.get<AddressResponse[]>(`${this.apiUrl}/addresses`);
  }

  addAddress(request: AddressRequest): Observable<AddressResponse> {
    return this.http.post<AddressResponse>(`${this.apiUrl}/addresses`, request);
  }

  updateAddress(id: number, request: AddressRequest): Observable<AddressResponse> {
    return this.http.put<AddressResponse>(`${this.apiUrl}/addresses/${id}`, request);
  }

  deleteAddress(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/addresses/${id}`);
  }

  registerSeller(request: { companyName: string, contactEmail: string, taxId: string, logoUrl?: string }): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/sellers`, request);
  }

  getMySeller(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/sellers/me`);
  }

  listSellers(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/sellers`);
  }

  updateSellerStatus(id: number, status: string): Observable<any> {
    return this.http.patch<any>(`${environment.apiUrl}/sellers/${id}/status`, { status });
  }
}
