import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CatalogService } from '../../core/services/catalog.service';
import { Product } from '../../core/models/product.model';
import { CatalogResponse } from '../../core/models/catalog.model';
import { SkeletonCardComponent } from '../../shared/components/skeleton-card/skeleton-card';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, SkeletonCardComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent implements OnInit {
  featuredProducts = signal<Product[]>([]);
  catalogs = signal<CatalogResponse[]>([]);
  loading = signal(true);

  constructor(private catalogService: CatalogService) {}

  ngOnInit(): void {
    this.catalogService.getProducts().subscribe({
      next: (products) => {
        this.featuredProducts.set(products.filter(p => p.active).slice(0, 6));
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });

    this.catalogService.getCatalogs().subscribe({
      next: (cats) => this.catalogs.set(cats),
      error: () => {}
    });
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

  getCatalogGradient(index: number): string {
    const gradients = [
      'linear-gradient(135deg, #2C1011 0%, #7A3B3D 100%)',
      'linear-gradient(135deg, #4A2224 0%, #9E5558 100%)',
      'linear-gradient(135deg, #1a0a0a 0%, #4A2224 100%)',
      'linear-gradient(135deg, #7A3B3D 0%, #E53935 100%)',
    ];
    return gradients[index % gradients.length];
  }
}
