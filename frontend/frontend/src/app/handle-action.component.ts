import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-action',
  template: `<p>Procesando solicitud...</p>`,
})
export class AuthActionComponent implements OnInit {

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.handleAuthActionUrl();
  }

  handleAuthActionUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const mode = urlParams.get('mode');  
    const actionCode = urlParams.get('oobCode');

    if (mode && actionCode) {
      switch (mode) {
        case 'verifyEmail':
  
          this.router.navigate(['/verificar-correo'], { queryParams: { oobCode: actionCode } });
          break;

          
        case 'resetPassword':

          this.router.navigate(['/reestablecer-contrasenia'], { queryParams: { oobCode: actionCode } });
          break;
        default:
          console.error('Acción desconocida:', mode);
      }
    } else {
      console.error('Parámetros insuficientes en la URL');
    }
  }
}
