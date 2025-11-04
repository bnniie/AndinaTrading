package co.edu.unbosque.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para habilitar CORS en controladores gestionados por Spring MVC.
 * Esta configuración complementa la seguridad definida en {@link SecurityConfig} y aplica a endpoints no protegidos por filtros de seguridad.
 */
@Configuration
public class WebConfig {

    /**
     * Bean que define las reglas de CORS para el contexto MVC.
     * Permite solicitudes desde el frontend local (http://localhost:3000) hacia cualquier endpoint de la aplicación.
     * Se habilitan métodos HTTP comunes, headers genéricos y el envío de credenciales.
     *
     * @return instancia de WebMvcConfigurer con la configuración CORS aplicada.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configura los mapeos CORS para todas las rutas.
             * Esta configuración es útil para controladores que no están protegidos por filtros de seguridad (como recursos estáticos o controladores públicos).
             *
             * @param registry objeto CorsRegistry para registrar las reglas.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas las rutas.
                    .allowedOrigins("http://localhost:3000") // Permite solicitudes desde el frontend local.
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
                    .allowedHeaders("*") // Permite todos los headers.
                    .allowCredentials(true); // Permite envío de cookies y headers de autenticación.
            }
        };
    }
}
