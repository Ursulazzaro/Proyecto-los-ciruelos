package Grupo11.Seminario.Repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import Grupo11.Seminario.Entities.Jugador;

public interface IJugadorRepository extends CrudRepository<Jugador, Integer> {
    List<Jugador> findByEmail(String email);
    List<Jugador> findByNombreAndApellidoOrderByApellidoAscNombreAsc(String nombre, String apellido);
    List<Jugador> findByNombreOrderByApellidoAscNombreAsc(String nombre);
    List<Jugador> findByApellidoOrderByApellidoAscNombreAsc(String apellido);
    List<Jugador> findAllByOrderByApellidoAscNombreAsc();
    // Añadir método para buscar profesores
    List<Jugador> findByProfesorTrue();
}
