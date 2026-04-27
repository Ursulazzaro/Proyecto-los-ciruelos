import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MercadopagoService {
  // Usa tu token de acceso en la URL de la API
  private accessToken = 'APP_USR-1027101980086324-042708-0b4e2bd6960961c34e4b71d2eaf8c20c-1915271026';
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