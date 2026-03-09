import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FirebaseErrorService {

  constructor() { }

  codeError(code: string): string {
    const errorMessages: { [key: string]: string } = {
      'auth/invalid-email': 'Correo o contraseña incorrectos.',
      'auth/invalid-credential': 'Correo o contraseña incorrectos.',
      'auth/user-not-found': 'El usuario no está registrado.',
      'auth/wrong-password': 'Contraseña incorrecta.',
      'auth/email-already-in-use': 'El email ya está registrado.',
      'auth/email-not-verified':   'Debe verificar su correo antes de iniciar sesión.<br>Correo de verificación enviado.<br>Revisa tu correo electrónico.',
      'auth/too-many-requests': 'Demasiados intentos fallidos. Intente más tarde.',
    };
  
    return errorMessages[code] || 'Error desconocido. Intente nuevamente.';
  }
  
}
