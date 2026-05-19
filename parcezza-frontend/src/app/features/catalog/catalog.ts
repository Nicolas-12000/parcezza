import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CatalogService } from '../../core/services/catalog.service';
import { AuthService } from '../../core/services/auth.service';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../core/services/toast.service';
import { Product } from '../../core/models/product.model';
import { Router } from '@angular/router';
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
  allProducts = signal<Product[]>([]);
  searchQuery = signal('');
  loading = signal(true);
  error = signal<string | null>(null);
  addingToCart = signal<number | null>(null);

  filteredProducts = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    const products = this.allProducts();
    if (!query) return products;
    return products.filter(p =>
      p.name.toLowerCase().includes(query) ||
      p.description?.toLowerCase().includes(query) ||
      p.sku.toLowerCase().includes(query)
    );
  });

  constructor(
    private catalogService: CatalogService,
    private authService: AuthService,
    private cartService: CartService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.catalogService.getProducts().subscribe({
      next: (data) => {
        this.allProducts.set(data.filter(p => p.active));
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load catalog. Please try again later.');
        this.loading.set(false);
      }
    });
  }

  onSearch(value: string): void {
    this.searchQuery.set(value);
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
        this.toastService.success(`${product.name} added to cart`);
      },
      error: (err) => {
        this.addingToCart.set(null);
        this.toastService.error(err.error?.message || 'Failed to add to cart');
      }
    });
  }

  getStockStatus(stock: number): string {
    if (stock === 0) return 'out-of-stock';
    if (stock <= 5) return 'low-stock';
    return 'in-stock';
  }

  getStockLabel(stock: number): string {
    if (stock === 0) return 'Out of Stock';
    if (stock <= 5) return `Only ${stock} left`;
    return 'In Stock';
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
