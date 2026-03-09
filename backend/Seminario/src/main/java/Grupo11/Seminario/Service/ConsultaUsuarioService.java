package Grupo11.Seminario.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Grupo11.Seminario.DTO.JugadorDTO;
import Grupo11.Seminario.DTO.EmpleadoDTO;
import Grupo11.Seminario.Entities.Empleado;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Repository.IEmpleadoRepository;
import Grupo11.Seminario.Repository.IJugadorRepository;

@Service
public class ConsultaUsuarioService {

    @Autowired
    private IJugadorRepository jugadorRepository;

    @Autowired
    private IEmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoService empleado_service;


    // Se busca si existe el empleado
    public Boolean existe_empleado(Integer id_empleado){
        return empleado_service.existe_empleado(id_empleado);
    }

    // Método para buscar usuarios (Jugadores y Empleados) con filtros y devolver una lista de DTOs
    public List<Object> buscar_usuarios(Optional<String> nombre, Optional<String> apellido, Optional<String> email) {
        List<Jugador> jugadores;
        List<Empleado> empleados;

        // Si se busca por email, hacer la búsqueda solo por email
        if (email.isPresent()) {
            jugadores = jugadorRepository.findByEmail(email.get());
            empleados = empleadoRepository.findByEmail(email.get());
        }
        // Filtrar y ordenar por Nombre y Apellido si hay criterios, de lo contrario, buscar todos
        else if (nombre.isPresent() && apellido.isPresent()) {
            jugadores = jugadorRepository.findByNombreAndApellidoOrderByApellidoAscNombreAsc(nombre.get(), apellido.get());
            empleados = empleadoRepository.findByNombreAndApellidoOrderByApellidoAscNombreAsc(nombre.get(), apellido.get());
        } else if (nombre.isPresent()) {
            jugadores = jugadorRepository.findByNombreOrderByApellidoAscNombreAsc(nombre.get());
            empleados = empleadoRepository.findByNombreOrderByApellidoAscNombreAsc(nombre.get());
        } else if (apellido.isPresent()) {
            jugadores = jugadorRepository.findByApellidoOrderByApellidoAscNombreAsc(apellido.get());
            empleados = empleadoRepository.findByApellidoOrderByApellidoAscNombreAsc(apellido.get());
        } else {
            // Si no hay criterios, devolver todos y ordenar por Apellido y Nombre
            jugadores = jugadorRepository.findAllByOrderByApellidoAscNombreAsc();
            empleados = empleadoRepository.findAllByOrderByApellidoAscNombreAsc();
        }

        // Combinar las listas de jugadores y empleados y convertirlas a DTOs
        List<Object> usuarios = jugadores.stream()
            .map(JugadorDTO::fromEntity)
            .collect(Collectors.toList());

        usuarios.addAll(empleados.stream()
            .map(EmpleadoDTO::fromEntity)
            .collect(Collectors.toList()));

        return usuarios;
    }

    // Método para buscar profesores
    public List<Object> buscar_profesores(Optional<String> nombre, Optional<String> apellido, Optional<String> email) {
        List<Jugador> profesores;

        // Si se busca por email, hacer la búsqueda solo por email y verificar si es profesor
        if (email.isPresent()) {
            profesores = jugadorRepository.findByEmail(email.get())
                .stream()
                .filter(Jugador::getProfesor) // Filtrar solo los que son profesores
                .collect(Collectors.toList());
        }
        // Filtrar y ordenar por Nombre y Apellido si hay criterios, de lo contrario, buscar todos
        else if (nombre.isPresent() && apellido.isPresent()) {
            profesores = jugadorRepository.findByNombreAndApellidoOrderByApellidoAscNombreAsc(nombre.get(), apellido.get())
                .stream()
                .filter(Jugador::getProfesor) // Filtrar solo los que son profesores
                .collect(Collectors.toList());
        } else if (nombre.isPresent()) {
            profesores = jugadorRepository.findByNombreOrderByApellidoAscNombreAsc(nombre.get())
                .stream()
                .filter(Jugador::getProfesor) // Filtrar solo los que son profesores
                .collect(Collectors.toList());
        } else if (apellido.isPresent()) {
            profesores = jugadorRepository.findByApellidoOrderByApellidoAscNombreAsc(apellido.get())
                .stream()
                .filter(Jugador::getProfesor) // Filtrar solo los que son profesores
                .collect(Collectors.toList());
        } else {
            // Si no hay criterios, devolver todos los profesores
            profesores = jugadorRepository.findByProfesorTrue();
        }

        // Convertir la lista de profesores a DTOs
        List<Object> profesList = profesores.stream()
            .map(JugadorDTO::fromEntity)
            .collect(Collectors.toList());

        return profesList;
    }
}
