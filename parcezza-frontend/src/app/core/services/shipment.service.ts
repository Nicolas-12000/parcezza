import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ShipmentResponse, ShipmentStatus } from '../models/shipment.model';

@Injectable({ providedIn: 'root' })
export class ShipmentService {
  private readonly apiUrl = `${environment.apiUrl}/shipments`;
  constructor(private http: HttpClient) {}

  listAll(): Observable<ShipmentResponse[]> {
    return this.http.get<ShipmentResponse[]>(`${this.apiUrl}`);
  }

  updateStatus(shipmentId: number, status: ShipmentStatus, trackingCode?: string) {
    return this.http.patch<ShipmentResponse>(`${this.apiUrl}/${shipmentId}/status`, { status, trackingCode });
  }
}
