import { Component } from '@angular/core';
import { MercadopagoService } from '../../../services/mercadopago.service';

@Component({
  selector: 'app-mercadopago',
  templateUrl: './mercadopago.component.html',
  styleUrls: ['./mercadopago.component.css']
})
export class MercadopagoComponent {
  ngOnInit(): void {
    // Inicializa Mercado Pago con tu clave pública 
    const mp = new (window as any).MercadoPago('APP_USR-e221c611-71c8-4c31-aa97-fbdc91b11784', {
      locale: 'es-AR'
    });

    // Configura el contenedor donde se mostrará el botón
    const bricksBuilder = mp.bricks();
    bricksBuilder.create("wallet", "wallet_container", {
      initialization: {
        preferenceId: "1954862914-072c78f8-c150-4d0b-9bc9-79df0b8528e7", // Reemplaza con tu ID de preferencia es del back
      },
      customization: {
        texts: {
          valueProp: 'smart_option',
        },
      },
    });
  }
}
