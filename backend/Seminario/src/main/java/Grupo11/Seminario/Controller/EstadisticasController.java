package Grupo11.Seminario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Grupo11.Seminario.DTO.EstadisticasDTO;
import Grupo11.Seminario.Service.EstadisticasService;

@RestController
@RequestMapping("/public")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticas(@RequestParam String email) {
        return ResponseEntity.ok(estadisticasService.obtenerEstadisticas(email));
    }
}