import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-cambiar-contrasenia',
  templateUrl: './cambiar-contrasenia.component.html',
  styleUrls: ['./cambiar-contrasenia.component.scss'],
})
export class CambiarContraseniaComponent {
  passwordResetForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private router: Router, 
    private authService: AuthService,
    private toastrService: ToastrService
  ) {

    this.passwordResetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  get email() {
    return this.passwordResetForm.get('email');
  }

  async onSubmit() {
    if (this.passwordResetForm.valid) {
        const emailValue = this.passwordResetForm.value.email;
        try {
            await this.authService.resetPassword(emailValue); 
            this.toastrService.info("Correo de confirmación enviado. Revisa tu bandeja de entrada para cambiar la contraseña.", 'Correo enviado');
            this.router.navigate(['/login']);
        } catch (error) {
            console.error('Error al enviar el correo de recuperación:', error);
            this.toastrService.error("No se pudo enviar el correo de recuperación. Inténtalo nuevamente.", "Error");
        }
    }
}

  goBack(): void {
    this.router.navigate(['/login']);
  }
}
