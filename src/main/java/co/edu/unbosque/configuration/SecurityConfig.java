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
 * Define políticas de acceso, codificación de contraseñas y configuración CORS.
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean que define el algoritmo de codificación de contraseñas.
     * Se utiliza BCrypt, recomendado por Spring Security por su robustez.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que configura la cadena de filtros de seguridad HTTP.
     * - Habilita CORS.
     * - Desactiva CSRF (útil para APIs REST).
     * - Permite acceso público a rutas específicas.
     * - Requiere autenticación para cualquier otra ruta.
     * - Desactiva autenticación básica y formularios de login.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/comisionistas/login", "/api/inversionistas/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic().disable()
            .formLogin().disable();

        return http.build();
    }

    /**
     * Bean que configura las políticas CORS para permitir solicitudes desde el frontend.
     * - Permite origen http://localhost:3000 (desarrollo local).
     * - Permite métodos HTTP comunes.
     * - Permite cualquier encabezado.
     * - Habilita el envío de credenciales (cookies, headers de autenticación).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
