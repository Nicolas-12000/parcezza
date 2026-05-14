import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private readonly apiUrl = `${environment.apiUrl}/catalog`;

  constructor(private http: HttpClient) {}

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/products`);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }
}
