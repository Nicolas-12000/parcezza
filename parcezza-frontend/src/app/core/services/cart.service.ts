import { Injectable, signal, computed, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CartResponse, CartItemRequest } from '../models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly apiUrl = `${environment.apiUrl}/cart`;
  private isBrowser: boolean;

  cart = signal<CartResponse | null>(null);
  isOpen = signal(false);
  isLoading = signal(false);

  itemCount = computed(() => {
    const c = this.cart();
    if (!c || !c.items) return 0;
    return c.items.reduce((sum, item) => sum + item.quantity, 0);
  });

  totalAmount = computed(() => {
    const c = this.cart();
    return c?.totalAmount ?? 0;
  });

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  open(): void {
    this.isOpen.set(true);
  }

  close(): void {
    this.isOpen.set(false);
  }

  toggle(): void {
    this.isOpen.update(v => !v);
  }

  loadCart(): void {
    this.isLoading.set(true);
    this.http.get<CartResponse>(this.apiUrl).subscribe({
      next: (cart) => {
        this.cart.set(cart);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  addItem(request: CartItemRequest): Observable<CartResponse> {
    this.isLoading.set(true);
    return this.http.post<CartResponse>(`${this.apiUrl}/items`, request).pipe(
      tap(cart => {
        this.cart.set(cart);
        this.isLoading.set(false);
      })
    );
  }

  updateItem(itemId: number, request: CartItemRequest): Observable<CartResponse> {
    this.isLoading.set(true);
    return this.http.put<CartResponse>(`${this.apiUrl}/items/${itemId}`, request).pipe(
      tap(cart => {
        this.cart.set(cart);
        this.isLoading.set(false);
      })
    );
  }

  removeItem(itemId: number): Observable<CartResponse> {
    this.isLoading.set(true);
    return this.http.delete<CartResponse>(`${this.apiUrl}/items/${itemId}`).pipe(
      tap(cart => {
        this.cart.set(cart);
        this.isLoading.set(false);
      })
    );
  }

  clearCart(): Observable<CartResponse> {
    this.isLoading.set(true);
    return this.http.delete<CartResponse>(this.apiUrl).pipe(
      tap(cart => {
        this.cart.set(cart);
        this.isLoading.set(false);
      })
    );
  }

  resetCart(): void {
    this.cart.set(null);
  }
}
