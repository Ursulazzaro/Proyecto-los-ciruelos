import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute } from '@angular/router';
import { getAuth, confirmPasswordReset } from 'firebase/auth'; 

@Component({
  selector: 'app-reestablecer-contrasenia',
  templateUrl: './reestablecer_contrasenia.component.html',
  styleUrls: ['./reestablecer_contrasenia.component.scss']
})
export class ReestablecerContraseniaComponent implements OnInit {
  cambioContraseniaForm: FormGroup;
  hidePassword: boolean = true;
  hideConfirmPassword: boolean = true;
  showPasswordRequirements: boolean = false;

  passwordRequirements = {
    minLength: false,
    hasUpperCase: false,
    hasNumber: false
  };

  oobCode: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService,
    private route: ActivatedRoute, 
    private router: Router
  ) {
    this.cambioContraseniaForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6), Validators.pattern(/^(?=.*[A-Z])/)]],
      confirmNewPassword: ['', Validators.required]
    }, { validators: this.passwordsMatch });

    this.cambioContraseniaForm.get('newPassword')?.valueChanges.subscribe(value => {
      this.checkPasswordStrength(value);
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.oobCode = params['oobCode']; 
      if (!this.oobCode) {
        this.toastr.error('No se proporcionó un código de restablecimiento.');
      }
    });
  }

  passwordsMatch(form: FormGroup) {
    const newPassword = form.get('newPassword')?.value;
    const confirmNewPassword = form.get('confirmNewPassword')?.value;
    return newPassword && confirmNewPassword && newPassword !== confirmNewPassword ? { mismatch: true } : null;
  }

  checkPasswordStrength(password: string) {
    this.passwordRequirements.minLength = password.length >= 6;
    this.passwordRequirements.hasUpperCase = /[A-Z]/.test(password);
    this.passwordRequirements.hasNumber = /\d/.test(password);
  }

  onSubmit() {
    if (this.cambioContraseniaForm.invalid) {
      this.toastr.error('Por favor, completa el formulario correctamente.');
  
      // Marca todos los campos como tocados para mostrar errores
      Object.keys(this.cambioContraseniaForm.controls).forEach(field => {
        const control = this.cambioContraseniaForm.get(field);
        control?.markAsTouched();
      });
  
      return;
    }
  
    const { newPassword } = this.cambioContraseniaForm.value;
  
    if (this.oobCode) {
      const auth = getAuth();
      confirmPasswordReset(auth, this.oobCode, newPassword)
        .then(() => {
          alert('Contraseña actualizada con éxito.');
          this.router.navigate(['/login']);
          this.cambioContraseniaForm.reset();
        })
        .catch(error => {
          this.toastr.error(error.message, 'Error al cambiar la contraseña');
        });
    } else {
      this.toastr.error('El código de restablecimiento no es válido.');
    }
  }
  

  togglePasswordVisibility(field: string) {
    if (field === 'newPassword') {
      this.hidePassword = !this.hidePassword;
    } else if (field === 'confirmNewPassword') {
      this.hideConfirmPassword = !this.hideConfirmPassword;
    }
  }

  onFocusPassword() {
    this.showPasswordRequirements = true;
  }

  onBlurPassword() {
    this.showPasswordRequirements = false;
  }
}
