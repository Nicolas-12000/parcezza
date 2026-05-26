import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ShipmentService } from '../../core/services/shipment.service';
import { ShipmentResponse, ShipmentStatus } from '../../core/models/shipment.model';
import { ToastService } from '../../core/services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-shipments',
  templateUrl: './admin-shipments.html',
  styleUrls: ['./admin-shipments.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AdminShipmentsComponent implements OnInit {
  shipments = signal<ShipmentResponse[]>([]);
  loading = signal(true);
  page = signal(1);
  pageSize = 10;
  pendingAction = signal<{ id: number; status: ShipmentStatus } | null>(null);

  constructor(private shipmentService: ShipmentService, private toast: ToastService, private router: Router) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.shipmentService.listAll().subscribe({
      next: s => { this.shipments.set(s); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  get pageCount(): number {
    return Math.max(1, Math.ceil(this.shipments().length / this.pageSize));
  }

  pageItems(): ShipmentResponse[] {
    const p = this.page();
    const start = (p - 1) * this.pageSize;
    return this.shipments().slice(start, start + this.pageSize);
  }

  confirmUpdate(shipmentId: number, status: ShipmentStatus) {
    this.pendingAction.set({ id: shipmentId, status });
  }

  cancelPending() { this.pendingAction.set(null); }

  performUpdate() {
    const pa = this.pendingAction();
    if (!pa) return;
    this.shipmentService.updateStatus(pa.id, pa.status).subscribe({
      next: () => { this.toast.success('Estado de envío actualizado'); this.pendingAction.set(null); this.load(); },
      error: (e) => this.toast.error(e?.error?.message || 'Error al actualizar envío')
    });
  }

  goToPage(n: number) { if (n < 1) n = 1; if (n > this.pageCount) n = this.pageCount; this.page.set(n); }

  // Map shipment status to a CSS class for the badge
  getShipmentStatusClass(status: ShipmentStatus): string {
    switch (status) {
      case 'PENDING': return 'badge--muted';
      case 'SHIPPED': return 'badge--info';
      case 'IN_TRANSIT': return 'badge--warning';
      case 'DELIVERED': return 'badge--success';
      case 'CANCELLED': return 'badge--danger';
      default: return 'badge--muted';
    }
  }

  // Human-friendly labels for shipment statuses
  getShipmentStatusLabel(status: ShipmentStatus): string {
    switch (status) {
      case 'PENDING': return 'Pendiente';
      case 'SHIPPED': return 'Enviado';
      case 'IN_TRANSIT': return 'En tránsito';
      case 'DELIVERED': return 'Entregado';
      case 'CANCELLED': return 'Cancelado';
      default: return status as string;
    }
  }

  // Navigate back to the previous page
  goBack() { window.history.back(); }

  // Explicit navigation to the returns admin page
  goToReturns() { this.router.navigate(['/admin/returns']); }
}
