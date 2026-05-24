import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Product } from '../models/product.model';
import { CatalogResponse } from '../models/catalog.model';
import { PageResponse } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private readonly productsUrl = `${environment.apiUrl}/products`;
  private readonly catalogsUrl = `${environment.apiUrl}/catalogs`;

  constructor(private http: HttpClient) {}

  getProducts(page: number = 0, size: number = 12, query: string = '', collection?: string): Observable<PageResponse<Product>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    if (query) {
      params = params.set('query', query);
    }
    if (collection) {
      params = params.set('collection', collection);
    }
    return this.http.get<PageResponse<Product>>(this.productsUrl, { params });
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

  createCatalog(request: { name: string; slug: string }): Observable<CatalogResponse> {
    return this.http.post<CatalogResponse>(this.catalogsUrl, request);
  }

  updateCatalog(id: number, request: { name: string; slug: string }): Observable<CatalogResponse> {
    return this.http.put<CatalogResponse>(`${this.catalogsUrl}/${id}`, request);
  }

  deleteCatalog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.catalogsUrl}/${id}`);
  }

  createProduct(request: any): Observable<Product> {
    return this.http.post<Product>(this.productsUrl, request);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.productsUrl}/${id}`);
  }
}
