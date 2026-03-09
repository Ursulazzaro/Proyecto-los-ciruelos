package Grupo11.Seminario.Security;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import Grupo11.Seminario.Service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UsuarioService usuarioService;

    // Inyecta UsuarioService en el constructor
    public WebSecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf->csrf.disable()) // Opcional: desactiva CSRF si no es necesario
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                .requestMatchers("/public/**").permitAll() // Permite acceso sin autenticación a "/public/**"
                .requestMatchers("/configuracion_general/public/**").permitAll() // Permite acceso sin autenticación a "/public/**"
                .requestMatchers("/private/**").authenticated()
                .requestMatchers("/configuracion_general/private/**").authenticated()
            )
            .addFilterBefore(new FirebaseAuthFilter(usuarioService), CorsFilter.class);
            http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // Habilita CORS

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
