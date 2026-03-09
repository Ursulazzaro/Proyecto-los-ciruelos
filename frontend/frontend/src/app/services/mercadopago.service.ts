import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MercadopagoService {
  // Usa tu token de acceso en la URL de la API
  private accessToken = 'APP_USR-5356347604739108-020916-65561a50ec097c174f39c33d1e554d9b-1954862914';
  private baseUrl = 'https://api.mercadopago.com/checkout/preferences';

  constructor(private http: HttpClient) { }

  createPreference(preferenceData: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.accessToken}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<any>(this.baseUrl, preferenceData, { headers });
  }
}