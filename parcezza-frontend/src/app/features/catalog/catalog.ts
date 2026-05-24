import { Component, OnInit, signal, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CatalogService } from '../../core/services/catalog.service';
import { AuthService } from '../../core/services/auth.service';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../core/services/toast.service';
import { Product } from '../../core/models/product.model';
import { SkeletonCardComponent } from '../../shared/components/skeleton-card/skeleton-card';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, SkeletonCardComponent, EmptyStateComponent],
  templateUrl: './catalog.html',
  styleUrls: ['./catalog.scss']
})
export class CatalogComponent implements OnInit {
  products = signal<Product[]>([]);
  catalogs = signal<any[]>([]);
  searchQuery = signal('');
  loading = signal(true);
  error = signal<string | null>(null);
  addingToCart = signal<number | null>(null);

  page = signal(0);
  pageSize = 12;
  hasMore = signal(true);
  loadingMore = signal(false);
  collection = signal<string | null>(null);

  constructor(
    private catalogService: CatalogService,
    private authService: AuthService,
    private cartService: CartService,
    private toastService: ToastService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.queryParamMap.get('collection');
    this.collection.set(slug);
    this.loadCatalogs();
    this.loadProducts(true);
  }

  loadCatalogs(): void {
    this.catalogService.getCatalogs().subscribe({
      next: (cats) => this.catalogs.set(cats),
      error: () => {}
    });
  }

  onCollectionChange(slug: string | null): void {
    const queryParams: any = {};
    if (slug) queryParams.collection = slug;
    else queryParams.collection = null;
    this.router.navigate([], { relativeTo: this.route, queryParams, queryParamsHandling: 'merge' });
    this.collection.set(slug);
    this.page.set(0);
    this.loadProducts(true);
  }

  loadProducts(reset: boolean = false): void {
    if (reset) {
      this.page.set(0);
      this.products.set([]);
      this.hasMore.set(true);
      this.loading.set(true);
    } else {
      this.loadingMore.set(true);
    }

    this.catalogService.getProducts(this.page(), this.pageSize, this.searchQuery(), this.collection() ?? undefined).subscribe({
      next: (response) => {
        const newItems = response.content.filter(p => p.active);
        if (reset) {
          this.products.set(newItems);
        } else {
          this.products.update(current => [...current, ...newItems]);
        }
        this.hasMore.set(!response.last);
        this.loading.set(false);
        this.loadingMore.set(false);
      },
      error: () => {
        if (reset) this.error.set('Error al cargar el catálogo. Por favor intenta de nuevo más tarde.');
        this.loading.set(false);
        this.loadingMore.set(false);
      }
    });
  }

  onSearch(value: string): void {
    this.searchQuery.set(value);
    this.loadProducts(true);
  }

  @HostListener('window:scroll')
  onScroll(): void {
    if (this.loading() || this.loadingMore() || !this.hasMore()) return;

    const pos = (document.documentElement.scrollTop || document.body.scrollTop) + document.documentElement.offsetHeight;
    const max = document.documentElement.scrollHeight;

    if (pos > max - 400) {
      this.page.update(p => p + 1);
      this.loadProducts();
    }
  }

  addToCart(product: Product, event: Event): void {
    event.preventDefault();
    event.stopPropagation();

    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    this.addingToCart.set(product.id);
    this.cartService.addItem({
      productId: product.id,
      quantity: 1
    }).subscribe({
      next: () => {
        this.addingToCart.set(null);
        this.toastService.success(`${product.name} agregado al carrito`);
      },
      error: (err) => {
        this.addingToCart.set(null);
        this.toastService.error(err.error?.message || 'Error al agregar al carrito');
      }
    });
  }

  getStockStatus(stock: number): string {
    if (stock === 0) return 'out-of-stock';
    if (stock <= 5) return 'low-stock';
    return 'in-stock';
  }

  getStockLabel(stock: number): string {
    if (stock === 0) return 'Agotado';
    if (stock <= 5) return `Solo quedan ${stock}`;
    return 'Disponible';
  }

  getProductGradient(index: number): string {
    const gradients = [
      'linear-gradient(135deg, #f5e6e6 0%, #e8d0d0 100%)',
      'linear-gradient(135deg, #e6e8f5 0%, #d0d4e8 100%)',
      'linear-gradient(135deg, #e6f5e8 0%, #d0e8d4 100%)',
      'linear-gradient(135deg, #f5f0e6 0%, #e8e0d0 100%)',
      'linear-gradient(135deg, #f0e6f5 0%, #e0d0e8 100%)',
      'linear-gradient(135deg, #e6f2f5 0%, #d0e4e8 100%)',
    ];
    return gradients[index % gradients.length];
  }

  retry(): void {
    this.error.set(null);
    this.loading.set(true);
    this.ngOnInit();
  }
}
