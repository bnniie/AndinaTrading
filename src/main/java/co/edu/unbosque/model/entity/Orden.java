package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una orden de compra o venta de acciones en el sistema.
 * Mapeada a la tabla 'orden' en la base de datos.
 * Contiene información sobre la empresa involucrada, el inversionista, el comisionista, el estado y fechas clave.
 */
@Entity
@Table(name = "orden")
public class Orden {

    /**
     * Identificador único de la orden.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora en que se creó la orden.
     */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /**
     * Estado actual de la orden (ej. PENDIENTE, APROBADA, RECHAZADA, EJECUTADA).
     */
    @Column(name = "estado")
    private String estado;

    /**
     * Número de acciones involucradas en la orden.
     */
    @Column(name = "numero_acciones")
    private int numeroAcciones;

    /**
     * Empresa sobre la cual se realiza la orden.
     * Relación muchos-a-uno con la entidad {@link Empresa}.
     */
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    /**
     * Comisionista que gestiona la orden.
     * Relación muchos-a-uno con la entidad {@link Comisionista}.
     */
    @ManyToOne
    @JoinColumn(name = "comisionista_id")
    private Comisionista comisionista;

    /**
     * Inversionista que genera la orden.
     * Relación muchos-a-uno con la entidad {@link Inversionista}.
     */
    @ManyToOne
    @JoinColumn(name = "inversionista_id")
    private Inversionista inversionista;

    /**
     * Tipo de orden (ej. COMPRA, VENTA).
     */
    @Column(name = "tipo_orden")
    private String tipoOrden;

    /**
     * Valor total de la orden (acciones × precio).
     */
    @Column(name = "valor_orden")
    private double valorOrden;

    /**
     * Valor de la comisión aplicada a la orden.
     */
    @Column(name = "valor_comision")
    private double valorComision;

    /**
     * Fecha en que la orden fue aprobada.
     */
    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    /**
     * Fecha en que la orden fue ejecutada.
     */
    @Column(name = "fecha_ejecucion")
    private LocalDateTime fechaEjecucion;

    /**
     * Fecha en que la orden fue rechazada.
     */
    @Column(name = "fecha_rechazo")
    private LocalDateTime fechaRechazo;

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumeroAcciones() { return numeroAcciones; }
    public void setNumeroAcciones(int numeroAcciones) { this.numeroAcciones = numeroAcciones; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Comisionista getComisionista() { return comisionista; }
    public void setComisionista(Comisionista comisionista) { this.comisionista = comisionista; }

    public Inversionista getInversionista() { return inversionista; }
    public void setInversionista(Inversionista inversionista) { this.inversionista = inversionista; }

    public String getTipoOrden() { return tipoOrden; }
    public void setTipoOrden(String tipoOrden) { this.tipoOrden = tipoOrden; }

    public double getValorOrden() { return valorOrden; }
    public void setValorOrden(double valorOrden) { this.valorOrden = valorOrden; }

    public double getValorComision() { return valorComision; }
    public void setValorComision(double valorComision) { this.valorComision = valorComision; }

    public LocalDateTime getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }

    public LocalDateTime getFechaEjecucion() { return fechaEjecucion; }
    public void setFechaEjecucion(LocalDateTime fechaEjecucion) { this.fechaEjecucion = fechaEjecucion; }

    public LocalDateTime getFechaRechazo() { return fechaRechazo; }
    public void setFechaRechazo(LocalDateTime fechaRechazo) { this.fechaRechazo = fechaRechazo; }
}