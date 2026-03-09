package Grupo11.Seminario.DTO;

import java.util.List;
import Grupo11.Seminario.Entities.Empleado;
import Grupo11.Seminario.Entities.Telefono;
import lombok.Data;

@Data
public class EmpleadoDTO {
    private Integer id;
    private String email;
    private String nombre;
    private String apellido;
    private Boolean duenio;
    private List<Telefono> telefonos;

    // Constructor con argumentos
    public EmpleadoDTO(
    String email, String nombre, String apellido, Boolean duenio, List<Telefono> telefonos) 
    {
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.duenio = duenio;
        this.telefonos = telefonos;
    }    

    // Método estático para convertir de Empleado (entidad) a EmpleadoDTO
    public static EmpleadoDTO fromEntity(Empleado empleado) {
        EmpleadoDTO empleadoDTO= new EmpleadoDTO(
            empleado.getEmail(),
            empleado.getNombre(),
            empleado.getApellido(),
            empleado.getDuenio(),
            empleado.getTelefonos()
        );

        empleadoDTO.setId(empleado.getId());
        return empleadoDTO;
    }
}
