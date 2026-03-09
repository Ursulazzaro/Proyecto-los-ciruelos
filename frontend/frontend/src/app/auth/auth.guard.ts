import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  // Inyectar el AuthService y Router usando la función inject()
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificar si el usuario está autenticado
  if (authService.isAuthenticated()) {
    return true;
  } else {
    const returnUrl = state.url === '/reserva' ? '/home' : state.url; // Evita guardar reserva como returnUrl
    router.navigate(['/login'], { queryParams: { returnUrl } });
    return false;
  }
};
