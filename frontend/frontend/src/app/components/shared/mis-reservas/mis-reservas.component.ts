import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ApiService } from '../../../api.service';

@Component({
  selector: 'app-mis-reservas',
  templateUrl: './mis-reservas.component.html',
  styleUrl: './mis-reservas.component.css'
})
export class MisReservasComponent {

  usuarioActual: any;

  reservas: any[] = [];
  reservasFiltradas: any[] = [];

  filtro = {
    fecha: '',
    cancha: ''
  };

  canchas: string[] = [
    "Cancha 1",
    "Cancha 2",
    "Cancha 3",
    "Cancha 4"
  ];

  constructor(
    private authService: AuthService,
    private api: ApiService
  ) {}

  ngOnInit(): void {
    this.obtenerReservas();
  }

  obtenerReservas(): void {
    this.api.getResrvas().subscribe((reservas) => {

      this.reservas = reservas;
      this.reservasFiltradas = reservas;

      console.log(reservas);
    });
  }

  cancelarReserva(reserva_id: number): void {

    this.api.cancelarReserva(reserva_id).subscribe(() => {

      this.reservas = this.reservas.map(reserva => {

        if (reserva.id === reserva_id) {
          reserva.estado = 'Cancelada';
        }

        return reserva;
      });

      this.filtrarReservas();
    });
  }

  esReservaCanceladaOExpirada(reserva: any): boolean {

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    const partesFecha = reserva.turno.fecha.split('-');

    const fechaReserva = new Date(
      parseInt(partesFecha[0], 10),
      parseInt(partesFecha[1], 10) - 1,
      parseInt(partesFecha[2], 10)
    );

    return reserva.estado === 'Cancelada' || fechaReserva < hoy;
  }

  getPrecioTotal(pagos: any[]): number {
    return pagos.reduce((total, pago) => total + pago.monto, 0);
  }

  filtrarReservas(): void {

    this.reservasFiltradas = this.reservas.filter(reserva => {

      const coincideFecha =
        !this.filtro.fecha ||
        reserva.turno.fecha === this.filtro.fecha;

      const coincideCancha =
        !this.filtro.cancha ||
        `Cancha ${reserva.turno.cancha.numero}` === this.filtro.cancha;

      return coincideFecha && coincideCancha;
    });
  }

  seleccionarCancha(cancha: string): void {

    this.filtro.cancha =
      this.filtro.cancha === cancha ? '' : cancha;

    this.filtrarReservas();
  }
}