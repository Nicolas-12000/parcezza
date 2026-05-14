import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CatalogService, Product } from '../../core/services/catalog.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.html',
  styleUrls: ['./catalog.scss']
})
export class CatalogComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private catalogService: CatalogService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.catalogService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load catalog. Please try again later.';
        this.loading = false;
      }
    });
  }

  addToCart(product: Product) {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    console.log('Adding product to cart:', product);
    // TODO: Implement cart service logic
  }
}
