import { Component } from '@angular/core';
import { ApiService } from '../../../api.service';

@Component({
  selector: 'app-profesores',
  templateUrl: './profesores.component.html',
  styleUrls: ['./profesores.component.css']
})
export class ProfesoresComponent {
  profesores: any[] = [];
  profesoresFiltrados: any[] = [];
  email: string = '';
  nombre: string = '';
  apellido: string = '';
  categorias = ["Todas", "Primera", "Segunda", "Tercera", "Cuarta", "Quinta", "Sexta", "Séptima", "Principiante"];
  categoriaSeleccionada: string = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void { 
    this.obtenerProfesores(); 
  }

  obtenerProfesores(): void {
    this.api.getProfesores(this.email, this.nombre, this.apellido).subscribe(data => {
      this.profesores = data;
      this.profesoresFiltrados = data; // Inicialmente, todos los profesores se muestran
    });
  }

  // Método para filtrar los profesores con los valores de búsqueda
  buscarProfesores(): void {
    this.api.getProfesores(this.email, this.nombre, this.apellido).subscribe(data => {
      this.profesores = data; // Filtrar según los criterios ingresados
    });
  }
}
