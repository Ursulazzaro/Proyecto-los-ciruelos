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
  modoEdicion: boolean = false;
  datosOriginales: any = {};
  maxPhones: number = 3; // Máximo de teléfonos permitidos

  nivelesDeJuego: string[] = [
    'Primera', 'Segunda', 'Tercera', 'Cuarta', 'Quinta', 'Sexta', 'Séptima', 'Principiante'
  ];

  constructor(private fb: FormBuilder, private api: ApiService) {}

  ngOnInit(): void {
    this.perfilForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(25)]],
      apellido: ['', [Validators.required, Validators.maxLength(30)]],
      email: ['', [
        Validators.required,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$/)
      ]],
      codigoArea: ['', [
        Validators.required,
        Validators.pattern(/^\d{1,3}$/) // Hasta 3 dígitos numéricos
      ]],
      nivelJuego: [{ value: '', disabled: true }, [Validators.required]],
      phones: this.fb.array([this.createPhoneControl()]) // Lista de teléfonos
    });

    this.cargarDatosIniciales();
  }

  // Función para crear un control de teléfono
  createPhoneControl(): any {
    return this.fb.control('', [
      Validators.required,
      Validators.pattern(/^\d{6,15}$/) // Teléfono entre 6 y 15 dígitos
    ]);
  }

  // Función para agregar un teléfono
  addPhone(): void {
    if (this.phones.length < this.maxPhones) {
      this.phones.push(this.createPhoneControl());
    }
  }

  // Función para eliminar un teléfono
  removePhone(index: number): void {
    if (this.phones.length > 1) {
      this.phones.removeAt(index);
    }
  }

  // Función para obtener los teléfonos del formulario
  get phones(): FormArray {
    return this.perfilForm.get('phones') as FormArray;
  }

  activarEdicion(): void {
    this.modoEdicion = true;
    this.datosOriginales = { ...this.perfilForm.value }; // Guardamos los datos originales
  }

  guardarPerfil(): void {
    if (this.perfilForm.valid) {
      console.log('Perfil actualizado con éxito:', this.perfilForm.value);
      this.modoEdicion = false;
    } else {
      this.perfilForm.markAllAsTouched();
    }
  }

  cancelarEdicion(): void {
    this.perfilForm.patchValue(this.datosOriginales); // Restauramos los valores originales
    this.modoEdicion = false;
  }

  cargarDatosIniciales(): void {
    this.api.getPerfil().subscribe({
      next: (datosUsuario) => {
        this.perfilForm.patchValue({
          nombre: datosUsuario.nombre,
          apellido: datosUsuario.apellido,
          email: datosUsuario.email,
          telefono: 2214375254,
          nivelJuego: datosUsuario.categoria
        });
      },
      error: (error) => {
        console.error('Error al cargar el perfil:', error);
      }
    });
  }
}
