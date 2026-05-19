import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProfileService } from '../../core/services/profile.service';
import { CatalogService } from '../../core/services/catalog.service';
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
  sellerProfile = signal<any | null>(null);
  sellerRequests = signal<any[]>([]);

  loadingProfile = signal(true);
  loadingOrders = signal(true);
  loadingSeller = signal(false);
  loadingAdmin = signal(false);

  editing = signal(false);
  savingProfile = signal(false);
  showAddressForm = signal(false);
  showProductForm = signal(false);

  activeTab = signal<'orders' | 'addresses' | 'seller' | 'admin'>('orders');

  profileForm: FormGroup;
  addressForm: FormGroup;
  sellerForm: FormGroup;
  productForm: FormGroup;

  isAdmin = computed(() => this.profile()?.roles?.includes('ROLE_ADMIN') || false);
  isSeller = computed(() => this.profile()?.roles?.includes('ROLE_SELLER') || false);

  constructor(
    private fb: FormBuilder,
    private profileService: ProfileService,
    private catalogService: CatalogService,
    private orderService: OrderService,
    private toastService: ToastService
  ) {
    this.profileForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(4), SecurityValidators.safeTextOnly()]]
    });

    this.addressForm = this.fb.group({
      line1: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      line2: ['', [SecurityValidators.safeTextOnly()]],
      administrativeArea: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      postalCode: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      country: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      primary: [false]
    });

    this.sellerForm = this.fb.group({
      companyName: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      contactEmail: ['', [Validators.required, Validators.email]],
      taxId: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      logoUrl: ['', [SecurityValidators.safeTextOnly()]]
    });

    this.productForm = this.fb.group({
      name: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      sku: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      description: ['', [SecurityValidators.safeTextOnly()]],
      basePrice: [0, [Validators.required, Validators.min(0.01)]],
      stock: [10, [Validators.required, Validators.min(0)]],
      currency: ['USD'],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.loadProfile();
    this.loadAddresses();
    this.loadOrders();
    this.loadSellerProfile();
  }

  loadProfile(): void {
    this.profileService.getProfile().subscribe({
      next: (p) => {
        this.profile.set(p);
        this.profileForm.patchValue({ fullName: p.fullName });
        this.loadingProfile.set(false);
        if (p.roles?.includes('ROLE_ADMIN')) {
          this.loadAdminRequests();
        }
      },
      error: () => this.loadingProfile.set(false)
    });
  }

  loadAddresses(): void {
    this.profileService.getAddresses().subscribe({
      next: (addrs) => this.addresses.set(addrs),
      error: () => {}
    });
  }

  loadOrders(): void {
    this.orderService.getOrders().subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.loadingOrders.set(false);
      },
      error: () => this.loadingOrders.set(false)
    });
  }

  loadSellerProfile(): void {
    this.loadingSeller.set(true);
    this.profileService.getMySeller().subscribe({
      next: (seller) => {
        this.sellerProfile.set(seller);
        this.loadingSeller.set(false);
      },
      error: () => {
        this.sellerProfile.set(null);
        this.loadingSeller.set(false);
      }
    });
  }

  loadAdminRequests(): void {
    this.loadingAdmin.set(true);
    this.profileService.listSellers().subscribe({
      next: (requests) => {
        this.sellerRequests.set(requests);
        this.loadingAdmin.set(false);
      },
      error: () => this.loadingAdmin.set(false)
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
        this.toastService.success('Perfil actualizado correctamente');
      },
      error: (err) => {
        this.savingProfile.set(false);
        this.toastService.error(err.error?.message || 'Error al actualizar el perfil');
      }
    });
  }

  toggleAddressForm(): void {
    this.showAddressForm.update(v => !v);
    if (!this.showAddressForm()) {
      this.addressForm.reset({ primary: false });
    }
  }

  saveNewAddress(): void {
    if (this.addressForm.invalid) {
      this.toastService.error('Complete todos los campos requeridos válidamente');
      return;
    }

    this.profileService.addAddress(this.addressForm.value).subscribe({
      next: (newAddr) => {
        this.addresses.update(list => [...list, newAddr]);
        this.toggleAddressForm();
        this.toastService.success('Dirección agregada con éxito');
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al guardar la dirección');
      }
    });
  }

  deleteAddress(id: number): void {
    this.profileService.deleteAddress(id).subscribe({
      next: () => {
        this.addresses.update(list => list.filter(a => a.id !== id));
        this.toastService.success('Dirección eliminada');
      },
      error: () => this.toastService.error('Error al eliminar la dirección')
    });
  }

  applyForSeller(): void {
    if (this.sellerForm.invalid) {
      this.toastService.error('Por favor complete los datos requeridos');
      return;
    }

    this.profileService.registerSeller(this.sellerForm.value).subscribe({
      next: (res) => {
        this.sellerProfile.set(res);
        this.toastService.success('Solicitud enviada con éxito');
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al enviar la solicitud');
      }
    });
  }

  toggleProductForm(): void {
    this.showProductForm.update(v => !v);
    if (!this.showProductForm()) {
      this.productForm.reset({ stock: 10, currency: 'USD', active: true });
    }
  }

  createProduct(): void {
    if (this.productForm.invalid) {
      this.toastService.error('Complete los datos del producto válidamente');
      return;
    }

    this.catalogService.createProduct(this.productForm.value).subscribe({
      next: (newProd) => {
        this.toastService.success(`Producto "${newProd.name}" creado con éxito`);
        this.toggleProductForm();
      },
      error: (err) => {
        this.toastService.error(err.error?.message || 'Error al crear el producto');
      }
    });
  }

  approveSeller(id: number): void {
    this.profileService.updateSellerStatus(id, 'APPROVED').subscribe({
      next: () => {
        this.toastService.success('Solicitud aprobada con éxito');
        this.loadAdminRequests();
        this.loadProfile(); // Reload user roles in case they approved themselves!
        this.loadSellerProfile();
      },
      error: () => this.toastService.error('Error al aprobar la solicitud')
    });
  }

  rejectSeller(id: number): void {
    this.profileService.updateSellerStatus(id, 'REJECTED').subscribe({
      next: () => {
        this.toastService.success('Solicitud rechazada');
        this.loadAdminRequests();
      },
      error: () => this.toastService.error('Error al rechazar la solicitud')
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

  getInitial(): string {
    const p = this.profile();
    return p?.fullName?.charAt(0)?.toUpperCase() || 'U';
  }

  setTab(tab: 'orders' | 'addresses' | 'seller' | 'admin'): void {
    this.activeTab.set(tab);
  }
}
