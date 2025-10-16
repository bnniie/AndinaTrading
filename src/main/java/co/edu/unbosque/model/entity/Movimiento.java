package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un movimiento financiero realizado por un inversionista.
 * Puede ser una inversión, retiro, compra o cualquier transacción registrada.
 */
@Entity
public class Movimiento {

    /**
     * Identificador único del movimiento.
     * Se genera automáticamente con estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo de movimiento (ej. "compra", "venta", "retiro", "depósito").
     */
    private String tipo;

    /**
     * Nombre de la empresa o activo relacionado con el movimiento.
     */
    private String empresa;

    /**
     * Monto involucrado en la transacción.
     */
    private Double monto;

    /**
     * Fecha y hora en que se realizó el movimiento.
     */
    private LocalDateTime fecha;

    /**
     * Relación muchos-a-uno con la entidad Inversionista.
     * Indica quién realizó el movimiento.
     */
    @ManyToOne
    private Inversionista inversionista;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Movimiento() {}

    /**
     * Constructor completo para inicializar todos los campos.
     * @param tipo tipo de movimiento
     * @param empresa empresa relacionada
     * @param monto monto de la transacción
     * @param fecha fecha del movimiento
     * @param inversionista inversionista asociado
     */
    public Movimiento(String tipo, String empresa, Double monto, LocalDateTime fecha, Inversionista inversionista) {
        this.tipo = tipo;
        this.empresa = empresa;
        this.monto = monto;
        this.fecha = fecha;
        this.inversionista = inversionista;
    }

    // Métodos getter y setter para cada campo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Inversionista getInversionista() {
        return inversionista;
    }

    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }
}
