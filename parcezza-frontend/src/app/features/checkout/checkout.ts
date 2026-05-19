import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProfileService } from '../../core/services/profile.service';
import { OrderService } from '../../core/services/order.service';
import { PaymentService } from '../../core/services/payment.service';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../core/services/toast.service';
import { AddressResponse } from '../../core/models/user.model';
import { SecurityValidators } from '../../shared/validators/security.validators';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './checkout.html',
  styleUrls: ['./checkout.scss']
})
export class CheckoutComponent implements OnInit {
  currentStep = signal<'address' | 'review' | 'payment' | 'success'>(    'address');
  addresses = signal<AddressResponse[]>([]);
  selectedAddressId = signal<number | null>(null);
  orderId = signal<number | null>(null);
  isLoading = signal(false);
  loadingAddresses = signal(true);

  addressForm: FormGroup;
  paymentForm: FormGroup;
  showAddressForm = signal(false);

  cart = computed(() => this.cartService.cart());
  cartItems = computed(() => this.cart()?.items ?? []);
  cartTotal = computed(() => this.cart()?.totalAmount ?? 0);
  cartCurrency = computed(() => this.cart()?.currency ?? 'USD');

  steps = ['address', 'review', 'payment', 'success'] as const;

  stepIndex = computed(() => this.steps.indexOf(this.currentStep()));

  constructor(
    private fb: FormBuilder,
    private profileService: ProfileService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    public cartService: CartService,
    private toastService: ToastService,
    private router: Router
  ) {
    this.addressForm = this.fb.group({
      line1: ['', [Validators.required, Validators.maxLength(255), SecurityValidators.noHtmlTags()]],
      line2: ['', [Validators.maxLength(255)]],
      postalCode: ['', [Validators.required, Validators.maxLength(50)]],
      administrativeArea: ['', [Validators.required, Validators.maxLength(100)]],
      country: ['', [Validators.required, Validators.maxLength(100)]],
      primary: [false]
    });

    this.paymentForm = this.fb.group({
      cardNumber: ['', [Validators.required, Validators.minLength(13), Validators.maxLength(19)]],
      cardHolder: ['', [Validators.required, SecurityValidators.safeTextOnly()]],
      expMonth: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      expYear: ['', [Validators.required, Validators.min(2026)]],
      cvv: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(4)]]
    });
  }

  ngOnInit(): void {
    if (!this.cart() || this.cartItems().length === 0) {
      this.cartService.loadCart();
    }
    this.loadAddresses();
  }

  private loadAddresses(): void {
    this.loadingAddresses.set(true);
    this.profileService.getAddresses().subscribe({
      next: (addrs) => {
        this.addresses.set(addrs);
        this.loadingAddresses.set(false);
        const primary = addrs.find(a => a.primary);
        if (primary) this.selectedAddressId.set(primary.id);
        else if (addrs.length > 0) this.selectedAddressId.set(addrs[0].id);
      },
      error: () => {
        this.loadingAddresses.set(false);
      }
    });
  }

  selectAddress(id: number): void {
    this.selectedAddressId.set(id);
  }

  toggleAddressForm(): void {
    this.showAddressForm.update(v => !v);
  }

  saveNewAddress(): void {
    if (this.addressForm.invalid) return;
    this.isLoading.set(true);
    this.profileService.addAddress(this.addressForm.value).subscribe({
      next: (addr) => {
        this.addresses.update(list => [...list, addr]);
        this.selectedAddressId.set(addr.id);
        this.showAddressForm.set(false);
        this.addressForm.reset();
        this.isLoading.set(false);
        this.toastService.success('Address saved');
      },
      error: (err) => {
        this.isLoading.set(false);
        this.toastService.error(err.error?.message || 'Failed to save address');
      }
    });
  }

  goToReview(): void {
    if (!this.selectedAddressId()) {
      this.toastService.error('Please select a shipping address');
      return;
    }
    this.currentStep.set('review');
  }

  placeOrder(): void {
    this.isLoading.set(true);
    this.orderService.checkout({ shippingAddressId: this.selectedAddressId()! }).subscribe({
      next: (order) => {
        this.orderId.set(order.id);
        this.isLoading.set(false);
        this.currentStep.set('payment');
      },
      error: (err) => {
        this.isLoading.set(false);
        this.toastService.error(err.error?.message || 'Checkout failed');
      }
    });
  }

  submitPayment(): void {
    if (this.paymentForm.invalid) {
      Object.keys(this.paymentForm.controls).forEach(key => {
        this.paymentForm.controls[key].markAsTouched();
      });
      return;
    }

    this.isLoading.set(true);
    const formVal = this.paymentForm.value;
    this.paymentService.confirmPayment({
      orderId: this.orderId()!,
      provider: 'CARD',
      cardNumber: formVal.cardNumber,
      cardHolder: formVal.cardHolder,
      expMonth: Number(formVal.expMonth),
      expYear: Number(formVal.expYear),
      cvv: formVal.cvv
    }).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        if (response.status === 'SUCCESS') {
          this.cartService.resetCart();
          this.currentStep.set('success');
        } else {
          this.toastService.error('Payment was declined. Please try again.');
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        this.toastService.error(err.error?.message || 'Payment processing failed');
      }
    });
  }

  goBack(): void {
    const idx = this.stepIndex();
    if (idx > 0) {
      this.currentStep.set(this.steps[idx - 1]);
    }
  }

  getPaymentControl(name: string) {
    return this.paymentForm.get(name);
  }
}
