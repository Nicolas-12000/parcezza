import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReturnsService } from '../../core/services/returns.service';
import { ReturnResponse, ReturnStatus } from '../../core/models/return.model';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-admin-returns',
  templateUrl: './admin-returns.html',
  styleUrls: ['./admin-returns.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AdminReturnsComponent implements OnInit {
  returns = signal<ReturnResponse[]>([]);
  loading = signal(true);
  noteById = new Map<number, string>();
  // admin UI state
  searchTerm = signal('');
  page = signal(1);
  pageSize = 10;
  pendingAction = signal<{ id: number; status: ReturnStatus } | null>(null);

  get filtered(): ReturnResponse[] {
    const q = this.searchTerm().toLowerCase().trim();
    const list = this.returns();
    if (!q) return list;
    return list.filter(r => String(r.id).includes(q) || String(r.orderId).includes(q) || r.reason.toLowerCase().includes(q) || (r.note || '').toLowerCase().includes(q));
  }

  get pageCount(): number {
    return Math.max(1, Math.ceil(this.filtered.length / this.pageSize));
  }

  pageItems(): ReturnResponse[] {
    const p = this.page();
    const start = (p - 1) * this.pageSize;
    return this.filtered.slice(start, start + this.pageSize);
  }

  constructor(private returnsService: ReturnsService, private toast: ToastService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.returnsService.listAll().subscribe({
      next: r => { this.returns.set(r); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  confirmUpdate(returnId: number, status: ReturnStatus) {
    this.pendingAction.set({ id: returnId, status });
  }

  cancelPending() {
    this.pendingAction.set(null);
  }

  performUpdate() {
    const pa = this.pendingAction();
    if (!pa) return;
    const note = this.noteById.get(pa.id);
    this.returnsService.updateStatus(pa.id, pa.status, note).subscribe({
      next: () => {
        this.toast.success('Estado actualizado');
        this.pendingAction.set(null);
        this.load();
      },
      error: (e) => {
        this.toast.error(e?.error?.message || 'Error al actualizar estado');
      }
    });
  }

  goToPage(n: number) {
    if (n < 1) n = 1;
    if (n > this.pageCount) n = this.pageCount;
    this.page.set(n);
  }
}
