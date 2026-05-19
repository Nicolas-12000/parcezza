import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProfileService } from '../../core/services/profile.service';
import { OrderService } from '../../core/services/order.service';
import { ToastService } from '../../core/services/toast.service';
import { ProfileResponse, AddressResponse } from '../../core/models/user.model';
import { OrderResponse } from '../../core/models/order.model';
import { SecurityValidators } from '../../shared/validators/security.validators';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss']
})
export class ProfileComponent implements OnInit {
  profile = signal<ProfileResponse | null>(null);
  addresses = signal<AddressResponse[]>([]);
  orders = signal<OrderResponse[]>([]);
  loadingProfile = signal(true);
  loadingOrders = signal(true);
  editing = signal(false);
  savingProfile = signal(false);
  activeTab = signal<'orders' | 'addresses'>('orders');

  profileForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private profileService: ProfileService,
    private orderService: OrderService,
    private toastService: ToastService
  ) {
    this.profileForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(4), SecurityValidators.safeTextOnly()]]
    });
  }

  ngOnInit(): void {
    this.profileService.getProfile().subscribe({
      next: (p) => {
        this.profile.set(p);
        this.profileForm.patchValue({ fullName: p.fullName });
        this.loadingProfile.set(false);
      },
      error: () => this.loadingProfile.set(false)
    });

    this.profileService.getAddresses().subscribe({
      next: (addrs) => this.addresses.set(addrs),
      error: () => {}
    });

    this.orderService.getOrders().subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.loadingOrders.set(false);
      },
      error: () => this.loadingOrders.set(false)
    });
  }

  toggleEdit(): void {
    this.editing.update(v => !v);
    if (!this.editing()) {
      const p = this.profile();
      if (p) this.profileForm.patchValue({ fullName: p.fullName });
    }
  }

  saveProfile(): void {
    if (this.profileForm.invalid) return;
    this.savingProfile.set(true);
    this.profileService.updateProfile(this.profileForm.value).subscribe({
      next: (p) => {
        this.profile.set(p);
        this.editing.set(false);
        this.savingProfile.set(false);
        this.toastService.success('Profile updated');
      },
      error: (err) => {
        this.savingProfile.set(false);
        this.toastService.error(err.error?.message || 'Failed to update profile');
      }
    });
  }

  deleteAddress(id: number): void {
    this.profileService.deleteAddress(id).subscribe({
      next: () => {
        this.addresses.update(list => list.filter(a => a.id !== id));
        this.toastService.success('Address removed');
      },
      error: () => this.toastService.error('Failed to remove address')
    });
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

  getInitial(): string {
    const p = this.profile();
    return p?.fullName?.charAt(0)?.toUpperCase() || 'U';
  }

  setTab(tab: 'orders' | 'addresses'): void {
    this.activeTab.set(tab);
  }
}
