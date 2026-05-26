import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../core/services/order.service';
import { ReturnsService } from '../../core/services/returns.service';
import { OrderResponse } from '../../core/models/order.model';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.html',
  styleUrls: ['./order-detail.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class OrderDetailComponent implements OnInit {
  order = signal<OrderResponse | null>(null);
  loading = signal(true);
  requesting = signal(false);
  // UI state for return request
  selectedItems = signal<Record<number, boolean>>({});
  selectedReason = signal<string>('');
  otherReason = signal<string>('');
  showConfirm = signal(false);
  confirmLines = signal<{label: string; value: string}[]>([]);
  returnInfo = signal<any | null>(null);
  showCancelConfirm = signal(false);

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private orderService: OrderService,
    private returnsService: ReturnsService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/profile']);
      return;
    }

    this.load(id);
  }

  getOrderStatusClass(status: string): string {
    switch (status) {
      case 'PAID': return 'badge-success';
      case 'DELIVERED': return 'badge-success';
      case 'PENDING': return 'badge-warning';
      case 'SHIPPED': return 'badge-neutral';
      case 'CANCELLED': return 'badge-danger';
      case 'REFUNDED': return 'badge-danger';
      default: return 'badge-neutral';
    }
  }

  getOrderStatusLabel(status: string): string {
    switch (status) {
      case 'PAID': return 'Pagado';
      case 'DELIVERED': return 'Entregado';
      case 'PENDING': return 'Pendiente';
      case 'SHIPPED': return 'Enviado';
      case 'CANCELLED': return 'Cancelado';
      case 'REFUNDED': return 'Reembolsado';
      default: return status;
    }
  }

  private load(id: number) {
    this.loading.set(true);
    this.orderService.getOrderById(id).subscribe({
      next: (o) => {
        this.order.set(o);
        this.loading.set(false);
        this.loadReturn(o.id);
      },
      error: () => this.loading.set(false)
    });
  }

  private loadReturn(orderId: number) {
    this.returnsService.getByOrder(orderId).subscribe({
      next: r => this.returnInfo.set(r),
      error: () => this.returnInfo.set(null)
    });
  }

  canRequestReturn(): boolean {
    const o = this.order();
    if (!o) return false;
    return o.status === 'DELIVERED';
  }

  canCancelOrder(): boolean {
    const o = this.order();
    if (!o) return false;
    return o.status === 'CREATED' || o.status === 'PAID' || o.status === 'PROCESSING';
  }

  openCancelConfirm() {
    this.showCancelConfirm.set(true);
  }

  cancelOrderConfirmed() {
    if (!this.order()) return;
    this.showCancelConfirm.set(false);
    this.requesting.set(true);
    this.orderService.cancelOrder(this.order()!.id).subscribe({
      next: (o) => {
        this.order.set(o);
        this.requesting.set(false);
        this.toast.success('Pedido cancelado con éxito');
      },
      error: (e) => {
        this.requesting.set(false);
        this.toast.error(e?.error?.message || 'Error al cancelar el pedido');
      }
    });
  }

  toggleItem(itemId: number) {
    const map = { ...this.selectedItems() };
    map[itemId] = !map[itemId];
    this.selectedItems.set(map);
  }

  selectedCount(): number {
    return Object.values(this.selectedItems()).filter(Boolean).length;
  }

  validateRequest(): string | null {
    if (this.selectedCount() === 0) return 'Debe seleccionar al menos un artículo a devolver.';
    const reason = this.selectedReason() || this.otherReason();
    if (!reason || reason.trim().length < 5) return 'Proporcione un motivo válido (al menos 5 caracteres).';
    return null;
  }

  prepareSubmit() {
    const err = this.validateRequest();
    if (err) {
      this.toast.error(err);
      return;
    }
    const reason = this.selectedReason() || this.otherReason();
    const ids = Object.entries(this.selectedItems()).filter(([_, v]) => v).map(([k]) => k).join(', ');
    this.confirmLines.set([
      { label: 'Artículos', value: ids },
      { label: 'Motivo', value: reason }
    ]);
    this.showConfirm.set(true);
  }

  submitReturnConfirmed() {
    if (!this.order()) return;
    this.showConfirm.set(false);
    this.requesting.set(true);
    const reason = this.selectedReason() || this.otherReason();
    this.returnsService.requestReturn(this.order()!.id, reason).subscribe({
      next: (r) => {
        this.returnInfo.set(r);
        this.requesting.set(false);
        this.toast.success('Solicitud de devolución enviada con éxito');
        // reset selections
        this.selectedItems.set({});
        this.selectedReason.set('');
        this.otherReason.set('');
      },
      error: (e) => {
        this.requesting.set(false);
        this.toast.error(e?.error?.message || 'Error al solicitar la devolución');
      }
    });
  }
}
