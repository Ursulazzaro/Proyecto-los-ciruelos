package Grupo11.Seminario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Grupo11.Seminario.Entities.Asociacion;
import Grupo11.Seminario.Entities.Empleado;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Repository.IAsociacionRepository;
import Grupo11.Seminario.Repository.IJugadorRepository;

@Service
@Transactional
public class AsociacionService {
    
    @Autowired
    IAsociacionRepository i_asociacion_repository;
    @Autowired
    JugadorService jugador_service;
    @Autowired
    EmpleadoService empleado_service;
    @Autowired
    private IJugadorRepository i_jugador_repository;


    public void guardar_asociacion(Asociacion asociacion){
        i_asociacion_repository.save(asociacion);
    }

    public Jugador buscar_jugador(Integer id_jugador){
        return jugador_service.buscar_jugador(id_jugador);
    }

    public Empleado buscar_empleado(Integer id_empleado){
        return empleado_service.buscar_empleado(id_empleado);
    }

    public Boolean existe_empleado(Integer id_empleado){
        return empleado_service.existe_empleado(id_empleado);
    }

    public Boolean existe_jugador(Integer id_jugador){
        return jugador_service.existe_jugador(id_jugador);
    }

    public Boolean verificar_duenio(Integer id_empleado){
        return empleado_service.verificar_duenio(id_empleado);
    }

    public Boolean verificar_asociacion(Integer id_jugador){
        Jugador jugador = this.buscar_jugador(id_jugador);
        if (jugador.getSocio()) {
            return false;
        }
        return true;
    }
    
    public Asociacion buscar_asociacion_por_jugador(Integer id_jugador) {
        return i_asociacion_repository.findByJugadorId(id_jugador);
    }
    
    public void eliminar_asociacion(Asociacion asociacion) {
        i_asociacion_repository.delete(asociacion);
    }
    
    // Nueva función para actualizar el jugador
    public void guardar_jugador(Jugador jugador) {
        i_jugador_repository.save(jugador);
    }
    
}
