package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una empresa registrada en el sistema.
 * Mapeada a la tabla 'empresas' en la base de datos.
 * Contiene información básica como nombre, descripción, precio y fecha de última actualización.
 */
@Entity
@Table(name = "empresas")
public class Empresa {

    /**
     * Identificador único de la empresa.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre comercial de la empresa.
     * Campo obligatorio.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Descripción general de la empresa.
     * Campo opcional.
     */
    @Column(name = "descripcion")
    private String descripcion;

    /**
     * Precio actual de la acción o valor de referencia de la empresa.
     * Campo obligatorio.
     */
    @Column(name = "precio", nullable = false)
    private double precio;

    /**
     * Fecha y hora de la última actualización del precio o datos de la empresa.
     * Campo opcional, útil para auditoría o sincronización.
     */
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    // Getters y setters

    /**
     * Obtiene el ID de la empresa.
     * @return identificador único.
     */
    public Long getId() { return id; }

    /**
     * Establece el ID de la empresa.
     * @param id identificador único.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el nombre de la empresa.
     * @return nombre comercial.
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre de la empresa.
     * @param nombre nombre comercial.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la descripción de la empresa.
     * @return texto descriptivo.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción de la empresa.
     * @param descripcion texto descriptivo.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene el precio actual de la empresa.
     * @return valor numérico.
     */
    public double getPrecio() { return precio; }

    /**
     * Establece el precio actual de la empresa.
     * @param precio valor numérico.
     */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     * Obtiene la fecha de última actualización.
     * @return fecha y hora como {@link LocalDateTime}.
     */
    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }

    /**
     * Establece la fecha de última actualización.
     * @param ultimaActualizacion fecha y hora como {@link LocalDateTime}.
     */
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}