package co.edu.unbosque.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA que representa una ciudad dentro del sistema.
 * Cada ciudad está asociada a un país y puede contener información económica adicional.
 */
@Entity
public class Ciudad {

    /**
     * Identificador único de la ciudad.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la ciudad.
     * Este campo es obligatorio y no puede estar en blanco.
     */
    @NotBlank
    private String nombre;

    /**
     * País al que pertenece la ciudad.
     * Relación muchos-a-uno con la entidad {@link Pais}.
     * Se utiliza {@code JsonBackReference} para evitar ciclos de serialización JSON.
     */
    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Pais pais;

    /**
     * Información adicional sobre la situación económica de la ciudad.
     * Se almacena como texto largo en la base de datos.
     */
    @Column(name = "situacion_economica", columnDefinition = "TEXT")
    private String situacionEconomica;

    // Getters y setters

    /**
     * Obtiene el ID de la ciudad.
     * @return identificador único.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la ciudad.
     * @param id identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la ciudad.
     * @return nombre como cadena de texto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ciudad.
     * @param nombre nombre como cadena de texto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el país asociado a la ciudad.
     * @return entidad {@link Pais}.
     */
    public Pais getPais() {
        return pais;
    }

    /**
     * Establece el país asociado a la ciudad.
     * @param pais entidad {@link Pais}.
     */
    public void setPais(Pais pais) {
        this.pais = pais;
    }

    /**
     * Obtiene la descripción de la situación económica de la ciudad.
     * @return texto descriptivo.
     */
    public String getSituacionEconomica() {
        return situacionEconomica;
    }

    /**
     * Establece la descripción de la situación económica de la ciudad.
     * @param situacionEconomica texto descriptivo.
     */
    public void setSituacionEconomica(String situacionEconomica) {
        this.situacionEconomica = situacionEconomica;
    }
}