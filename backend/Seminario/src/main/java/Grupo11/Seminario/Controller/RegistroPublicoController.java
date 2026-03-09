package Grupo11.Seminario.Controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Grupo11.Seminario.DTO.JugadorDTO;
import Grupo11.Seminario.DTO.TurnoDTO;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Entities.Turno;
import Grupo11.Seminario.Entities.Enum.EstadoTurno;
import Grupo11.Seminario.Service.RegistroService;
import Grupo11.Seminario.Service.ReservaService;
import Grupo11.Seminario.Service.TurnoService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/public/")
public class RegistroPublicoController {
    
    @Autowired
    RegistroService registro_service;
    @Autowired
    ReservaService reserva_service;
    @Autowired
    TurnoService turnoService;

    @PutMapping(path = "/bloquear/turno")
    ResponseEntity<Map<String, String>> bloquear_turno(HttpServletRequest request, @RequestBody TurnoDTO turnoDTO){
        String email = (String) request.getAttribute("email");

        Turno turno = new Turno();
        turno.setCancha(reserva_service.buscar_cancha(turnoDTO.getId_cancha()));
        turno.setFecha(turnoDTO.getFecha());
        turno.setHorarioInicio(turnoDTO.getHorario_inicio_ocupado());
        turno.setHorario_fin(turnoDTO.getHorario_fin_ocupado());
        turno.setEstado(EstadoTurno.Bloqueado);
        turno.setHorarioBloqueo(LocalTime.now());
        turnoService.guardar_turno(turno);
        // Responder con un JSON que contenga un mensaje
        Map<String, String> response = new HashMap<>();
        response.put("message", "Se bloqueo el turno");
        return ResponseEntity.ok(response);
    }

    // Controlador publico para registrarse como jugador en la aplicacion
    @PostMapping(path = "/registro/jugador")
    public ResponseEntity<?> registro_jugador(@RequestBody JugadorDTO jugadorDTO){
        
        // Verifica que el mail no este registrado
        if (registro_service.verificar_email(jugadorDTO.getEmail())){
            Jugador jugador = new Jugador();
            jugador.setEmail(jugadorDTO.getEmail());
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());
            jugador.setCategoria(registro_service.verificar_categoria(jugadorDTO.getCategoria()));

            jugador.setTelefonos(jugadorDTO.getTelefonos());

            registro_service.guardar_jugador(jugador);

            JugadorDTO jugadorDTO_front = new JugadorDTO
                (jugador.getEmail(), jugador.getNombre(), jugador.getApellido(), 
                jugador.getCategoria().toString(), jugador.getTelefonos()
                );

            // Le devolvemos los datos del jugador al FrontEnd
            return ResponseEntity.ok(jugadorDTO_front);
        }
        return ResponseEntity.badRequest().body("El mail ya esta registrado");
    }
}
