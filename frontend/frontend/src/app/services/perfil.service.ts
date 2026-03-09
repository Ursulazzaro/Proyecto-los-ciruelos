import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = 'http://tu-api.com/perfil'; // Cambia por la URL real

  constructor(private http: HttpClient) {}

  obtenerPerfil(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/obtener`);
  }

  actualizarPerfil(datos: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/actualizar`, datos);
  }
}
