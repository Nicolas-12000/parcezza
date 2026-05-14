import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogService, Product } from '../../core/services/catalog.service';

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

  constructor(private catalogService: CatalogService) {}

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
}
