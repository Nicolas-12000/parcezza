import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // In SSR/non-browser context there is no access to web storage.
  // Let browser-side navigation perform the real auth check.
  if (!isPlatformBrowser(platformId)) {
    return true;
  }

  if (authService.getToken()) {
    return true;
  }

  router.navigate(['/login'], {
    queryParams: { redirect: state.url }
  });
  return false;
};
