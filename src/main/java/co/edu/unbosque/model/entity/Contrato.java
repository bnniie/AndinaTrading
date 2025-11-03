package co.edu.unbosque.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "porcentaje_comision", nullable = false)
    private Double porcentajeComision;

    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @OneToOne
    @JoinColumn(name = "inversionista_id", referencedColumnName = "id", nullable = false)
    private Inversionista inversionista;

    // Constructor vacío
    public Contrato() {}

    // Constructor con parámetros
    public Contrato(Double porcentajeComision, Integer duracionMeses, Inversionista inversionista) {
        this.porcentajeComision = porcentajeComision;
        this.duracionMeses = duracionMeses;
        this.inversionista = inversionista;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public Double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(Double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public Inversionista getInversionista() {
        return inversionista;
    }

    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }
}
