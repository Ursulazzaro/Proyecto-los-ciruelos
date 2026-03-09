package Grupo11.Seminario.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Grupo11.Seminario.DTO.TurnoDTO;
import Grupo11.Seminario.Service.ConsultarTurnosService;

@RestController
@RequestMapping(path = "/public")
public class ConsultarTurnosController {

    @Autowired
    ConsultarTurnosService consultar_turnos_service;
    
    @GetMapping(path = "/consultar_turnos")
    public ResponseEntity<List<TurnoDTO>> consultar_turnos(){
        return ResponseEntity.ok().body(consultar_turnos_service.obtener_turnos_ocupados());

    }
}
