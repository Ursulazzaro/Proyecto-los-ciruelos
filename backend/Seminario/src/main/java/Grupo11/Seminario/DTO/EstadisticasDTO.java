package Grupo11.Seminario.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasDTO {
    private String rol;
    private Map<String, Object> metricas;
}