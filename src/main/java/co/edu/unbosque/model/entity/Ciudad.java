package co.edu.unbosque.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Pais pais;


    @Column(name = "situacion_economica", columnDefinition = "TEXT")
    private String situacionEconomica;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getSituacionEconomica() {
        return situacionEconomica;
    }

    public void setSituacionEconomica(String situacionEconomica) {
        this.situacionEconomica = situacionEconomica;
    }
}