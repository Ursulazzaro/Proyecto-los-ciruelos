package Grupo11.Seminario.DTO;

import java.util.List;

import Grupo11.Seminario.Entities.Telefono;
import Grupo11.Seminario.Entities.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String email;
    private String nombre;
    private String apellido;
    private List<Telefono> telefonos;
    private String categoria;

    // Constructor con argumentos
    public UsuarioDTO(String email, String nombre, String apellido, List<Telefono> telefonos) {
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefonos = telefonos;
    }

    // Constructor sin argumentos
    public UsuarioDTO() {}

    // Método estático para convertir de Usuario (entidad) a UsuarioDTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getTelefonos()
        );
    }

    // Método estático para convertir de UsuarioDTO a Usuario (entidad)
    public static Usuario toEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefonos(usuarioDTO.getTelefonos());
        return usuario;
    }
}
