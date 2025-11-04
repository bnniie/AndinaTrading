package co.edu.unbosque.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Define políticas de acceso, codificación de contraseñas, gestión de sesiones y configuración CORS.
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean para la codificación de contraseñas.
     * Utiliza BCrypt, un algoritmo robusto y ampliamente recomendado para almacenar contraseñas de forma segura.
     *
     * @return instancia de PasswordEncoder basada en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para las solicitudes HTTP.
     * - Habilita CORS con configuración personalizada.
     * - Desactiva CSRF (Cross-Site Request Forgery) para simplificar el desarrollo con APIs REST.
     * - Permite acceso público a rutas específicas.
     * - Requiere autenticación para cualquier otra ruta.
     * - Limita las sesiones activas por usuario a una.
     * - Desactiva autenticación básica y formulario de login.
     *
     * @param http objeto HttpSecurity para construir la configuración.
     * @return instancia de SecurityFilterChain con la configuración aplicada.
     * @throws Exception en caso de error durante la construcción de la cadena de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/inversionistas/**", "/api/comisionistas/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .maximumSessions(1) // Solo se permite una sesión activa por usuario.
                .maxSessionsPreventsLogin(false) // No bloquea el login si ya hay una sesión activa.
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // Desactiva autenticación básica.
            .formLogin(form -> form.disable()); // Desactiva formulario de login.

        return http.build();
    }

    /**
     * Configura las políticas de CORS (Cross-Origin Resource Sharing).
     * Permite solicitudes desde el frontend local (http://localhost:3000) con métodos comunes.
     * Habilita el envío de credenciales (cookies, headers de autenticación).
     *
     * @return fuente de configuración CORS aplicada a todas las rutas.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Origen permitido (frontend local).
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos HTTP permitidos.
        configuration.setAllowedHeaders(List.of("*")); // Permite todos los headers.
        configuration.setAllowCredentials(true); // Permite envío de credenciales.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica configuración a todas las rutas.
        return source;
    }
}