package Grupo11.Seminario.Service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import Grupo11.Seminario.Entities.Turno;
import Grupo11.Seminario.Entities.Enum.EstadoTurno;
import Grupo11.Seminario.Repository.ITurnoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TurnoService {
    
    @Autowired
    ITurnoRepository i_turno_repository;

    public void guardar_turno(Turno turno){
        i_turno_repository.save(turno);
    }

    // Método para eliminar turnos bloqueados cada 5 minutos
    @Scheduled(fixedRate = 300000) // Cada 5 minutos (300,000 ms)
    public void eliminarTurnosBloqueados() {
        System.out.println("Se estan por borrar los turnos bloqueados");
        LocalTime tiempoLimite = LocalTime.now().minusMinutes(10);
        
        // Buscar turnos bloqueados antes del tiempo límite
        List<Turno> turnosBloqueados = i_turno_repository.findByEstadoAndHorarioBloqueoBefore(EstadoTurno.Bloqueado, tiempoLimite);

        i_turno_repository.deleteAll(turnosBloqueados);
    }

}
