import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-postregister',
  templateUrl: './postregister.component.html',
  styleUrls: ['./postregister.component.scss'],
})
export class PostRegisterComponent implements OnInit {
  form: FormGroup;
  playerCategories = [
    'Principiante', 'Primera', 'Segunda', 'Tercera', 'Cuarta', 'Quinta', 'Sexta', 'Séptima'
  ];
  maxPhones = 4;
  errorMessages: string[] = [];

  email: string | null = null; // Para almacenar el token de Firebase
  name: string | null = null; // Para almacenar el token de Firebase

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    // Inicialización del formulario
    this.form = this.formBuilder.group({
      phones: this.formBuilder.array([ 
        this.formBuilder.control('', {
          validators: [Validators.required, Validators.pattern(/^\d{6,15}$/)],
          nonNullable: true,
        }),
      ]),
      playerCategory: this.formBuilder.control('', {
        validators: Validators.required,
        nonNullable: true,
      }),
    });
  }

  ngOnInit(): void {
    // Obtener parámetros desde la URL
    this.activatedRoute.queryParams.subscribe(params => {
      this.email = params['email'] || '';
      this.name = params['name'] || '';
    });
  }

  get phones(): FormArray {
    return this.form.get('phones') as FormArray;
  }

  submit(): void {
    if (this.form.invalid) {
      this.markAllAsTouched();
      return;
    }
  
    const phones = this.phones.value;
    const playerCategory = this.form.get('playerCategory')?.value;
  
    console.log('Datos del postregistro:', { phones, playerCategory });
    alert('Datos guardados correctamente.');
    // Enviar los datos al backend para registrarlos
    this.router.navigate(['/login']);
  }

    // Función para marcar todos los campos como tocados
    markAllAsTouched(): void {
      Object.keys(this.form.controls).forEach((controlName) => {
        const control = this.form.get(controlName);
        if (control) {
          control.markAsTouched();
        }
      });
    }

  addPhone(): void {
    if (this.phones.length < this.maxPhones) {
      this.phones.push(
        this.formBuilder.control('', {
          validators: [Validators.required, Validators.pattern(/^\d{6,15}$/)],
          nonNullable: true,
        })
      );
    }
  }

  removePhone(index: number): void {
    if (this.phones.length > 1) {
      this.phones.removeAt(index); 
    } else {
      this.phones.at(0).setValue(''); 
    }
  }

  getFormErrors(): string[] {
    const messages: string[] = [];
    const controls = this.form.controls;
  
    // Verificar la categoría de jugador
    if (controls['playerCategory'].invalid) {
      messages.push('Debe seleccionar una categoría de jugador.');
    }
  
    // Verificar si los teléfonos están vacíos o son inválidos
    if (this.phones.length < 1) {
      messages.push('Debe agregar al menos un número de teléfono.');
    } else {
      this.phones.controls.forEach((control, index) => {
        if (control.invalid && control.value) {
          messages.push(`Número de teléfono ${index + 1} inválido.`);
        }
      });
    }
    return messages;
  }
}
