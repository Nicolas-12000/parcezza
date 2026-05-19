import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product } from '../models/product.model';
import { CatalogResponse } from '../models/catalog.model';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private readonly productsUrl = `${environment.apiUrl}/products`;
  private readonly catalogsUrl = `${environment.apiUrl}/catalogs`;

  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.productsUrl}/${id}`);
  }

  getCatalogs(): Observable<CatalogResponse[]> {
    return this.http.get<CatalogResponse[]>(this.catalogsUrl);
  }

  getCatalogById(id: number): Observable<CatalogResponse> {
    return this.http.get<CatalogResponse>(`${this.catalogsUrl}/${id}`);
  }
}
