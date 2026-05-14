import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.RegisterComponent)
  },
  {
    path: 'catalog',
    loadComponent: () => import('./features/catalog/catalog').then(m => m.CatalogComponent)
  },
  {
    path: '',
    redirectTo: '/catalog',
    pathMatch: 'full'
  }
];
