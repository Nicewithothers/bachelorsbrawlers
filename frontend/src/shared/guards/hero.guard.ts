import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { HeroService } from '../services/hero.service';

export const heroGuard: CanActivateFn = async (route, state) => {
  const heroService = inject(HeroService);
  const router = inject(Router);
  const token = sessionStorage.getItem('token') as string;
  const profileName = sessionStorage.getItem('profileName') as string;

  if (token && profileName) {
    try {
      const hero = await firstValueFrom(heroService.getHero());

      if (route.url[0].path == 'newcharacter') {
        if (hero != null) {
          await router.navigate(['/map']);
          return false;
        } else {
          return true;
        }
      } else {
        if (hero) {
          return true;
        } else {
          await router.navigate(['/newcharacter']);
          return false;
        }
      }
    } catch (error) {
      console.error('Error fetching hero:', error);
      return false;
    }
  } else {
    await router.navigate(['/login']);
    return false;
  }
};
