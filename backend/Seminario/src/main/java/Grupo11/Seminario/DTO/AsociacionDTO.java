package Grupo11.Seminario.DTO;

import lombok.Data;

@Data
public class AsociacionDTO {
    private Long id_pago;
    private Integer id_jugador;

    public AsociacionDTO(Long id_pago, Integer id_jugador) {
        this.id_pago = id_pago;
        this.id_jugador = id_jugador;
    }

    
}
