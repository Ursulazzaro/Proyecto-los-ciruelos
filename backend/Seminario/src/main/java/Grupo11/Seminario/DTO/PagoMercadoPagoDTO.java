package Grupo11.Seminario.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import Grupo11.Seminario.Entities.Enum.EstadoPago;
import lombok.Data;

@Data
public class PagoMercadoPagoDTO {
    private String email_pagador;
    private String nombre_pagador;
    private String apellido_pagador;
    private String tipo_identificacion;
    private String numero_identificacion;
    private String motivo;
    private String metodo_pago;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoPago estado;

    public PagoMercadoPagoDTO(
        String email_pagador, String nombre_pagador, String apellido_pagador, 
        String tipo_identificacion, String numero_identificacion, 
        String motivo, String metodo_pago,
        LocalDate fecha, LocalTime hora, EstadoPago estado) 
        {
        this.email_pagador = email_pagador;
        this.nombre_pagador = nombre_pagador;
        this.apellido_pagador = apellido_pagador;
        this.tipo_identificacion = tipo_identificacion;
        this.numero_identificacion = numero_identificacion;
        this.motivo = motivo;
        this.metodo_pago = metodo_pago;
        this.fecha = fecha;
        this.hora = hora;
        this.estado=estado;
    }

    
}
