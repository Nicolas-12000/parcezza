import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info';
  exiting?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts = signal<Toast[]>([]);
  private nextId = 0;

  show(message: string, type: 'success' | 'error' | 'info' = 'info', duration = 3500): void {
    const id = this.nextId++;
    this.toasts.update(list => [...list, { id, message, type }]);

    setTimeout(() => {
      this.toasts.update(list =>
        list.map(t => t.id === id ? { ...t, exiting: true } : t)
      );
      setTimeout(() => this.dismiss(id), 350);
    }, duration);
  }

  success(message: string): void {
    this.show(message, 'success');
  }

  error(message: string): void {
    this.show(message, 'error', 5000);
  }

  dismiss(id: number): void {
    this.toasts.update(list => list.filter(t => t.id !== id));
  }
}
