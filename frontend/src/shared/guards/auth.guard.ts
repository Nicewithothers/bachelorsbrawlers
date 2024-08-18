import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = async (route, state) => {
  const router = inject(Router);
  const token = sessionStorage.getItem('token') as string;
  if (token) {    
    return true;
  } else {
    await router.navigate(['/login']);
    return false;
  }
};
