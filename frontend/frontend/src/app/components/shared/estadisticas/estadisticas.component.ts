import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../api.service';

@Component({
  selector: 'app-estadisticas',
  templateUrl: './estadisticas.component.html',
  styleUrl: './estadisticas.component.css'
})
export class EstadisticasComponent implements OnInit {

  estadisticasJugador: any[] = [];

  reservasPorMesData: any;
  reservasPorCanchaData: any;
  reservasPorHorarioData: any;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {

    this.api.getEstadisticas().subscribe({

      next: (data) => {

        const metricas = data.metricas;

        this.estadisticasJugador = [

          {
            titulo: 'Reservas realizadas',
            valor: metricas.reservasRealizadas,
            descripcion: 'Cantidad total de reservas hechas.'
          },

          {
            titulo: 'Reservas activas',
            valor: metricas.reservasActivas,
            descripcion: 'Turnos próximos o pendientes.'
          },

          {
            titulo: 'Reservas canceladas',
            valor: metricas.reservasCanceladas,
            descripcion: 'Turnos cancelados por el usuario.'
          },

          {
            titulo: 'Cancha favorita',
            valor: metricas.canchaFavorita,
            descripcion: 'La cancha más utilizada.'
          }

        ];

        this.reservasPorMesData = this.crearGrafico(
          metricas.reservasPorMes,
          'Reservas por mes'
        );

        this.reservasPorCanchaData = this.crearGrafico(
          metricas.reservasPorCancha,
          'Reservas por cancha'
        );

        this.reservasPorHorarioData = this.crearGrafico(
          metricas.reservasPorHorario,
          'Horarios más utilizados'
        );
      },

      error: (error) => {
        console.error('Error al cargar estadísticas:', error);
      }

    });

  }

  crearGrafico(datos: any, label: string): any {

    return {
      labels: Object.keys(datos),

      datasets: [
        {
          data: Object.values(datos),
          label: label
        }
      ]
    };

  }

}