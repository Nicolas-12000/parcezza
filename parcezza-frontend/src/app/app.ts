import { Component, signal, computed, OnInit, Inject, PLATFORM_ID, HostListener } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { CartService } from './core/services/cart.service';
import { ToastComponent } from './shared/components/toast/toast';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  mobileMenuOpen = signal(false);
  headerScrolled = signal(false);
  userMenuOpen = signal(false);
  private isBrowser: boolean;

  isAuthenticated = computed(() => this.authService.isAuthenticated());
  cartCount = computed(() => this.cartService.itemCount());
  isCartOpen = computed(() => this.cartService.isOpen());
  cartData = computed(() => this.cartService.cart());

  constructor(
    public authService: AuthService,
    public cartService: CartService,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    if (this.isBrowser && this.authService.isAuthenticated()) {
      this.cartService.loadCart();
    }
  }

  @HostListener('window:scroll')
  onScroll(): void {
    if (this.isBrowser) {
      this.headerScrolled.set(window.scrollY > 20);
    }
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen.update(v => !v);
  }

  closeMobileMenu(): void {
    this.mobileMenuOpen.set(false);
  }

  toggleUserMenu(): void {
    this.userMenuOpen.update(v => !v);
  }

  closeUserMenu(): void {
    this.userMenuOpen.set(false);
  }

  toggleCart(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cartService.toggle();
  }

  closeCart(): void {
    this.cartService.close();
  }

  removeCartItem(itemId: number): void {
    this.cartService.removeItem(itemId).subscribe();
  }

  logout(): void {
    this.authService.logout();
    this.cartService.resetCart();
    this.userMenuOpen.set(false);
    this.router.navigate(['/']);
  }
}
