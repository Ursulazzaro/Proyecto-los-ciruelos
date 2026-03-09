package Grupo11.Seminario.Repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import Grupo11.Seminario.Entities.Empleado;

public interface IEmpleadoRepository extends CrudRepository<Empleado, Integer> {
    List<Empleado> findByEmail(String email);
    List<Empleado> findByNombreAndApellidoOrderByApellidoAscNombreAsc(String nombre, String apellido);
    List<Empleado> findByNombreOrderByApellidoAscNombreAsc(String nombre);
    List<Empleado> findByApellidoOrderByApellidoAscNombreAsc(String apellido);
    List<Empleado> findAllByOrderByApellidoAscNombreAsc();
}
