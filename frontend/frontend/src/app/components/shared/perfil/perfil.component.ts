import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../api.service';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  perfilForm!: FormGroup;
  modoEdicion = false;
  datosOriginales: any = {};
  fotoPerfil: string | null = null;
  maxPhones = 3;
  fotoOriginal: string | null = null;

  nivelesDeJuego: string[] = [
    'Primera', 'Segunda', 'Tercera', 'Cuarta', 'Quinta', 'Sexta', 'Séptima', 'Principiante'
  ];

constructor(
  private fb: FormBuilder,
  private api: ApiService
) {}

  ngOnInit(): void {
    this.perfilForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(25)]],
      apellido: ['', [Validators.required, Validators.maxLength(30)]],
      email: ['', [Validators.required]],
      nivelJuego: ['', [Validators.required]],
      phones: this.fb.array([])
    });

    this.cargarDatosIniciales();
  }

  createPhoneControl(valor: string = '') {
    return this.fb.control(valor, [
      Validators.required,
      Validators.pattern(/^\d{6,15}$/)
    ]);
  }

  get phones(): FormArray {
    return this.perfilForm.get('phones') as FormArray;
  }

  cargarDatosIniciales(): void {
    this.api.getPerfil().subscribe({
      next: (datosUsuario) => {
        this.perfilForm.patchValue({
          nombre: datosUsuario.nombre,
          apellido: datosUsuario.apellido,
          email: datosUsuario.email,
          nivelJuego: datosUsuario.categoria
        });

        this.phones.clear();

        if (datosUsuario.telefonos && datosUsuario.telefonos.length > 0) {
          datosUsuario.telefonos.forEach((tel: any) => {
            this.phones.push(this.createPhoneControl(String(tel.numero)));
          });
        } else {
          this.phones.push(this.createPhoneControl());
        }

        this.datosOriginales = this.perfilForm.getRawValue();
      },
      error: (error) => {
        console.error('Error al cargar el perfil:', error);
      }
    });
  }

  activarEdicion(): void {
    this.modoEdicion = true;
    this.datosOriginales = this.perfilForm.getRawValue();
    this.fotoOriginal = this.fotoPerfil;
  }

  eliminarFoto(): void {
    this.fotoPerfil = null;
  }
  cancelarEdicion(): void {
    this.perfilForm.patchValue(this.datosOriginales);

    this.phones.clear();
    this.datosOriginales.phones.forEach((tel: string) => {
      this.phones.push(this.createPhoneControl(tel));
    });

    this.fotoPerfil = this.fotoOriginal;
    this.modoEdicion = false;
  }

  addPhone(): void {
    if (this.phones.length < this.maxPhones) {
      this.phones.push(this.createPhoneControl());
    }
  }

  removePhone(index: number): void {
    if (this.phones.length > 1) {
      this.phones.removeAt(index);
    }
  }

  cambiarFoto(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      this.fotoPerfil = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

guardarPerfil(): void {
  console.log('Click en guardar');
  console.log('Form válido:', this.perfilForm.valid);
  console.log('Form completo:', this.perfilForm.getRawValue());
  console.log('Phones:', this.phones.value);

  if (this.perfilForm.invalid) {
    this.perfilForm.markAllAsTouched();
    return;
  }

  const usuarioDTO = {
    nombre: this.perfilForm.get('nombre')?.value,
    apellido: this.perfilForm.get('apellido')?.value,
    categoria: this.perfilForm.get('nivelJuego')?.value,
    telefonos: this.phones.value.map((telefono: string) => ({
      codigo: 0,
      numero: Number(telefono)
    }))
  };

  console.log('Enviando usuarioDTO:', usuarioDTO);

  this.api.modificarPerfil(usuarioDTO).subscribe({
    next: () => {
      console.log('Perfil actualizado correctamente');
      this.modoEdicion = false;
      this.datosOriginales = this.perfilForm.getRawValue();
      this.fotoOriginal = this.fotoPerfil;
    },
    error: (error) => {
      console.error('Status:', error.status);
console.error('Error body:', error.error);
console.error('Message:', error.message);
console.error('URL:', error.url);
    }
  });
}
}