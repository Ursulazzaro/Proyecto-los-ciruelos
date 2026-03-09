package Grupo11.Seminario.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Grupo11.Seminario.Entities.Usuario;
import Grupo11.Seminario.Service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/private/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/usuario_id")
    public ResponseEntity<?> obtener_id_usuario(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
    
        Optional<Usuario> usuario = usuarioService.buscar_usuario(email);
    
        return ResponseEntity.ok().body(usuario.get().getId());
    }
}
