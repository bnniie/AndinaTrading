package co.edu.unbosque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Clase principal de arranque de la aplicación Spring Boot.
 * Marca el punto de entrada del sistema y configura el escaneo de entidades JPA.
 */
@SpringBootApplication // Habilita la configuración automática, escaneo de componentes y soporte para Spring Boot.
@EntityScan(basePackages = "co.edu.unbosque.model.entity") // Indica el paquete donde se encuentran las entidades JPA.
public class AndinatradingApplication {

    /**
     * Método principal que lanza la aplicación.
     * @param args argumentos de línea de comandos (opcional).
     */
    public static void main(String[] args) {
        SpringApplication.run(AndinatradingApplication.class, args);
    }
}
