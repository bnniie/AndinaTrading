package co.edu.unbosque.model.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un contrato asociado a un inversionista.
 * Mapeada a la tabla 'contrato' en la base de datos.
 * Contiene información sobre el porcentaje de comisión y la duración del contrato.
 */
@Entity
@Table(name = "contrato")
public class Contrato {

    /**
     * Identificador único del contrato.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Porcentaje de comisión pactado en el contrato.
     * Campo obligatorio.
     */
    @Column(name = "porcentaje_comision", nullable = false)
    private Double porcentajeComision;

    /**
     * Duración del contrato en meses.
     * Campo obligatorio.
     */
    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    /**
     * Inversionista asociado al contrato.
     * Relación uno-a-uno con la entidad {@link Inversionista}.
     * Cada contrato pertenece a un único inversionista.
     */
    @OneToOne
    @JoinColumn(name = "inversionista_id", referencedColumnName = "id", nullable = false)
    private Inversionista inversionista;

    // Constructores

    /**
     * Constructor vacío requerido por JPA.
     */
    public Contrato() {}

    /**
     * Constructor completo para inicializar un contrato.
     * @param porcentajeComision porcentaje de comisión pactado.
     * @param duracionMeses duración del contrato en meses.
     * @param inversionista inversionista asociado.
     */
    public Contrato(Double porcentajeComision, Integer duracionMeses, Inversionista inversionista) {
        this.porcentajeComision = porcentajeComision;
        this.duracionMeses = duracionMeses;
        this.inversionista = inversionista;
    }

    // Getters y setters

    /**
     * Obtiene el ID del contrato.
     * @return identificador único.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtiene el porcentaje de comisión del contrato.
     * @return valor decimal del porcentaje.
     */
    public Double getPorcentajeComision() {
        return porcentajeComision;
    }

    /**
     * Establece el porcentaje de comisión del contrato.
     * @param porcentajeComision valor decimal a asignar.
     */
    public void setPorcentajeComision(Double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    /**
     * Obtiene la duración del contrato en meses.
     * @return número entero que representa la duración.
     */
    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    /**
     * Establece la duración del contrato.
     * @param duracionMeses número entero en meses.
     */
    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    /**
     * Obtiene el inversionista asociado al contrato.
     * @return entidad {@link Inversionista}.
     */
    public Inversionista getInversionista() {
        return inversionista;
    }

    /**
     * Asocia un inversionista al contrato.
     * @param inversionista entidad {@link Inversionista}.
     */
    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }
}