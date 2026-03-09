package Grupo11.Seminario.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Grupo11.Seminario.Entities.Empleado;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Entities.Enum.Categoria;
import Grupo11.Seminario.Repository.IUsuarioRepository;

@Service
@Transactional
public class PerfilService {
    
    @Autowired
    JugadorService jugador_service;
    @Autowired
    EmpleadoService empleado_service;
    @Autowired
    RegistroService registro_service;
    @Autowired
    IUsuarioRepository i_usuario_repository;

    public void guardar_jugador(Jugador jugador){
        jugador_service.guardar_jugador(jugador);
    }

    public void guardar_empleado(Empleado empleado){
        empleado_service.guardar_empleado(empleado);
    }

    public Boolean existe_usuario(Integer id_usuario){
        if (i_usuario_repository.findById(id_usuario).isEmpty()) {
            return false;
        }
        return true;
    }

    public Empleado buscar_empleado(Integer id_usuario){
        if (empleado_service.existe_empleado(id_usuario)) {
            return empleado_service.buscar_empleado(id_usuario);
        }
        return null;
    }

    public Jugador buscar_jugador(Integer id_usuario){
        if (jugador_service.existe_jugador(id_usuario)) {
            return jugador_service.buscar_jugador(id_usuario);
        }
        return null;
    }

    public Categoria verificar_categoria(String categoria_jugador){
        for (Categoria categoria : Categoria.values()){
            if (categoria.name().equalsIgnoreCase(categoria_jugador)){
                return categoria;
            }
        }
        return null;
    }
}
