import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfiguracionGeneral, ConfiguracionService } from '../../../services/configuracion-general.service';
import { MercadopagoService } from '../../../services/mercadopago.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.css'
})
export class TicketComponent {
  @Input() esAsociacion: boolean = false; // Nuevo: Saber si es para asociación

  date!: string;
  court?: string;
  price!: number;
  senia?: string;
  horario_inicio_ocupado?: string;
  horario_fin_ocupado?: string;
  informacion!: string;

  configuracion: ConfiguracionGeneral | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private configuracionService: ConfiguracionService,
    private mercadopagoService: MercadopagoService,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {

    this.configuracion = this.configuracionService.getStoredConfiguracion();

    // Si no la tenemos, la obtenemos del backend
    if (!this.configuracion) {
      this.configuracionService.getConfiguracion().subscribe(config => {
        // Guardamos la configuración para poder usarla más tarde
        this.configuracionService.setConfiguracion(config);
        this.configuracion = config;
        this.price = config.monto_asociacion;
      });
    }

    this.route.queryParams.subscribe(params => {
      // Si el query param `asociacion` es `true`, activamos el modo asociación
      this.esAsociacion = params['asociacion'] === 'true';


      const fechaInicio = new Date();
      const fechaFin = new Date();
      fechaFin.setMonth(fechaFin.getMonth() + 1);

      const opciones = { day: '2-digit', month: '2-digit', year: 'numeric' } as const;
      const fechaInicioStr = fechaInicio.toLocaleDateString('es-AR', opciones);
      const fechaFinStr = fechaFin.toLocaleDateString('es-AR', opciones);
      this.date = `${fechaInicioStr} `;
      this.informacion = `Usted está asociado desde el ${fechaInicioStr} hasta el ${fechaFinStr}.`;

      if (!this.esAsociacion) {
        this.date = params['fecha'];
        this.court = params['id_cancha'];
        this.senia = params['senia'];
        this.price = params['precio'];
        this.horario_inicio_ocupado = params['horario_inicio_ocupado'];
        this.horario_fin_ocupado = params['horario_fin_ocupado'];
        this.informacion = 'Detalles sobre la cancha';
      }

      // Verificar si el usuario está autenticado antes de proceder con el pago
      if (!this.authService.isAuthenticated()) {
        // Si el usuario no está autenticado, redirigirlo al login
        this.router.navigate(['/login']);
      } else {
        // Si está autenticado, proceder con la creación de la preferencia
        this.redirectToMercadoPago();
      }
    });

    /* CODIGO DUPLICADO
    // Verificar si el usuario está autenticado antes de proceder con el pago
    if (!this.authService.isAuthenticated()) {
      // Si el usuario no está autenticado, redirigirlo al login
      this.router.navigate(['/login']);
    } else {
      // Si está autenticado, proceder con la creación de la preferencia
      this.redirectToMercadoPago();
    }
    */
  }

  goHome(): void {
    this.router.navigate(['/home']);
  }

  // Función para redirigir al usuario a Mercado Pago
  redirectToMercadoPago() {

    console.log('redirectToMercadoPago - Es asociar: ', this.esAsociacion);

    if (this.esAsociacion) {
      if (this.configuracion) {
        const preference = {
          items: [
            {
              title: 'Asociación - Los Ciruelos Padel Club',
              quantity: 1,
              unit_price:parseFloat(this.configuracion.monto_asociacion.toString()),
              currency_id: 'ARS',
            }
          ],
          back_urls: {
            success: `https://ivanyromero.com.ar/redir/procesar-pago?asociacion=true`,
            failure: 'https://ivanyromero.com.ar/redir/ticket',
            pending: 'https://ivanyromero.com.ar/redir/ticket'
          },
          auto_return: 'approved',
        };
  
        this.mercadopagoService.createPreference(preference).subscribe(response => {
          this.loadMercadoPago(response.id);
        }, error => {
          console.error('Error creando la preferencia:', error);
        });
      }

    } else {
      const params = new URLSearchParams({
        date: this.date,
        horario_inicio_ocupado: this.horario_inicio_ocupado!,
        court: this.court!,
        price: this.price.toString(),
        senia: this.senia!,
        horario_fin_ocupado: this.horario_fin_ocupado!
      }).toString();
      const preference = {
        items: [
          {
            title: 'Reserva cancha - Los Ciruelos Padel Club',
            quantity: 1,
            unit_price: parseFloat(this.price.toString()),
            currency_id: 'ARS',
          }
        ],
        back_urls: {
          success: `https://ivanyromero.com.ar/redir/procesar-pago?${params}`,
          failure: 'https://ivanyromero.com.ar/redir/ticket',
          pending: 'https://ivanyromero.com.ar/redir/ticket'
        },
        auto_return: 'approved',
      };

      this.mercadopagoService.createPreference(preference).subscribe(response => {
        this.loadMercadoPago(response.id);
      }, error => {
        console.error('Error creando la preferencia:', error);
      });
    }

  }

  loadMercadoPago(preferenceId: string) {

    console.log('loadMercadoPago - ID de preferencia:', preferenceId);

    const mp = new (window as any).MercadoPago('APP_USR-3fe674c6-f1f4-4ad5-948b-94660f9511cb', {
      locale: 'es-AR'
    });

    const bricksBuilder = mp.bricks();
    bricksBuilder.create('wallet', 'wallet_container', {
      initialization: { preferenceId: preferenceId
        // Comento para que redireccione sin modal
        // , redirectMode: 'modal'
       },
      customization: {
        texts: {
          action: 'buy',
          valueProp: 'security_details'
        }
      },
    });
  }
}
