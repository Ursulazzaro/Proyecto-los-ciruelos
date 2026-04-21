import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MercadopagoService {
  // Usa tu token de acceso en la URL de la API
  private accessToken = 'TEST-6156321432564867-041223-d98d17a237558ddb99e27ba6ac7651d8-409557085';
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