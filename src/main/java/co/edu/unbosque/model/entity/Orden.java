package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import co.edu.unbosque.model.entity.Empresa;

@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "numero_acciones")
    private int numeroAcciones;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "comisionista_id")
    private Comisionista comisionista;

    @ManyToOne
    @JoinColumn(name = "inversionista_id")
    private Inversionista inversionista;

    @Column(name = "tipo_orden")
    private String tipoOrden;

    @Column(name = "valor_orden")
    private double valorOrden;

    @Column(name = "valor_comision")
    private double valorComision;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "fecha_ejecucion")
    private LocalDateTime fechaEjecucion;

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