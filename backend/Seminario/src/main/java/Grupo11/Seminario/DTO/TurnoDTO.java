package Grupo11.Seminario.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class TurnoDTO {
    
    private Integer id_cancha;
    private LocalDate fecha;
    private LocalTime horario_inicio_ocupado;
    private LocalTime horario_fin_ocupado;

    public TurnoDTO(
        Integer id_cancha, LocalDate fecha, 
        LocalTime horario_inicio_ocupado, LocalTime horario_fin_ocupado) {
        this.id_cancha = id_cancha;
        this.fecha = fecha;
        this.horario_inicio_ocupado = horario_inicio_ocupado;
        this.horario_fin_ocupado = horario_fin_ocupado;
    }
}
