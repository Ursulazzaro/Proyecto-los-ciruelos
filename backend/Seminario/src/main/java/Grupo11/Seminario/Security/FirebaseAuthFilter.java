package Grupo11.Seminario.Security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import Grupo11.Seminario.Entities.Usuario;
import Grupo11.Seminario.Service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class FirebaseAuthFilter extends GenericFilterBean {

    private final UsuarioService usuarioService;

    // Constructor que recibe UsuarioService
    public FirebaseAuthFilter(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Excluir rutas públicas
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/public/") | requestURI.startsWith("/configuracion_general/public/")) {
            // Continuar la cadena de filtros sin verificar el token
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Token no proporcionado\"}");
            return;  // Termina la ejecución del filtro aquí
        }

        String idToken = authHeader.substring(7); // Remover "Bearer "

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            System.out.println("Email del tokenFireBase: "+email);

            // Verificar si el usuario está en la base de datos
            Optional<Usuario> usuarioOptional = usuarioService.buscar_usuario(email);
            System.out.println("Resultado de buscar_usuario: " + usuarioOptional);

            
            if (usuarioOptional.isEmpty()) {
                System.out.println("Usuario con email "+email+" no registrado en la base de datos");
                // Usuario no está registrado en la base de datos
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Usuario no registrado\"}");
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );

            // Establecer la autenticación en el contexto de seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Email: "+email);
            httpRequest.setAttribute("email", email);
            chain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Error: "+e);  // Log adicional
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Token inválido o no autorizado\"}");
        }
    }
}

