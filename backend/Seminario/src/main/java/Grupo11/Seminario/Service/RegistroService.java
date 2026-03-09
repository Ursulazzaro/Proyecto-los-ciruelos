package Grupo11.Seminario.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Grupo11.Seminario.Entities.Empleado;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Entities.Telefono;
import Grupo11.Seminario.Entities.Enum.Categoria;
import Grupo11.Seminario.Repository.IUsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistroService {

    @Autowired
    JugadorService jugador_service;
    @Autowired
    EmpleadoService empleado_service;
    @Autowired
    IUsuarioRepository i_usuario_repository;
    
    // Se guarda el jugador en la BD
    public void guardar_jugador(Jugador jugador){
        jugador_service.guardar_jugador(jugador);
    }

    // Se guarda el empleado en la BD
    public void guardar_empleado(Empleado empleado){
        empleado_service.guardar_empleado(empleado);
    }

    // Se busca si existe el empleado
    public Boolean existe_empleado(Integer id_empleado){
        return empleado_service.existe_empleado(id_empleado);
    }

    // Se busca si existe el jugador
    public Boolean existe_jugador(Integer id_jugador){
        return jugador_service.existe_jugador(id_jugador);
    }

    // Se verifica si el empleado es un dueño
    public Boolean verificar_duenio(Integer id_empleado){
        return empleado_service.verificar_duenio(id_empleado);
    }

    // Se verifica que el emial no este repetido
    public Boolean verificar_email(String email){
        if (i_usuario_repository.findByEmail(email).isEmpty()){
            return true;
        }
        return false;
    }
    // Se verifica que la categoria sea una de las aptas
    public Categoria verificar_categoria(String categoria_jugador){
        for (Categoria categoria : Categoria.values()){
            if (categoria.name().equalsIgnoreCase(categoria_jugador)){
                return categoria;
            }
        }
        return null;
    }

    // Se establecen los telefonos al usuario
    public List<Telefono> establecer_telefonos(List<Telefono> telefonosDTO){
        // Crea y establece la lista de teléfonos
            return telefonosDTO.stream().map(telefonoDTO->
            {
                Telefono telefono = new Telefono();
                telefono.setCodigo(telefonoDTO.getCodigo());
                telefono.setNumero(telefonoDTO.getNumero());
                return telefono;
            }
            ).collect(Collectors.toList());

    }
}
