import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home').then(m => m.HomeComponent)
  },
  {
    path: 'catalog',
    loadComponent: () => import('./features/catalog/catalog').then(m => m.CatalogComponent)
  },
  {
    path: 'product/:id',
    loadComponent: () => import('./features/product-detail/product-detail').then(m => m.ProductDetailComponent)
  },
  {
    path: 'order/:id',
    loadComponent: () => import('./features/order-detail/order-detail').then(m => m.OrderDetailComponent),
    canActivate: [authGuard]
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.RegisterComponent)
  },
  {
    path: 'checkout',
    loadComponent: () => import('./features/checkout/checkout').then(m => m.CheckoutComponent),
    canActivate: [authGuard]
  },
  {
    path: 'profile',
    loadComponent: () => import('./features/profile/profile').then(m => m.ProfileComponent),
    canActivate: [authGuard]
  },
  {
    path: 'admin/returns',
    loadComponent: () => import('./features/admin-returns/admin-returns').then(m => m.AdminReturnsComponent),
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
