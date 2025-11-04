package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Entidad JPA que representa un país dentro del sistema.
 * Cada país puede tener múltiples ciudades asociadas.
 * Mapeada a la tabla 'pais' en la base de datos.
 */
@Entity
public class Pais {

    /**
     * Identificador único del país.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del país.
     * Campo obligatorio.
     */
    @NotBlank
    private String nombre;

    /**
     * Lista de ciudades asociadas al país.
     * Relación uno-a-muchos con la entidad {@link Ciudad}.
     * Se gestiona en cascada desde el país.
     * Se utiliza {@link com.fasterxml.jackson.annotation.JsonManagedReference} para evitar ciclos de serialización.
     */
    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Ciudad> ciudades;

    // Getters y setters

    /**
     * Obtiene el ID del país.
     * @return identificador único.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del país.
     * @param id identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del país.
     * @return nombre como cadena de texto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del país.
     * @param nombre nombre como cadena de texto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de ciudades asociadas al país.
     * @return lista de entidades {@link Ciudad}.
     */
    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    /**
     * Establece la lista de ciudades asociadas al país.
     * @param ciudades lista de entidades {@link Ciudad}.
     */
    public void setCiudades(List<Ciudad> ciudades) {
        this.ciudades = ciudades;
    }
}