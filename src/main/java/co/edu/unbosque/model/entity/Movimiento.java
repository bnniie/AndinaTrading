package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String empresa;
    private Double monto;
    private LocalDateTime fecha;

    @ManyToOne
    private Inversionista inversionista;

    public Movimiento() {}

    public Movimiento(String tipo, String empresa, Double monto, LocalDateTime fecha, Inversionista inversionista) {
        this.tipo = tipo;
        this.empresa = empresa;
        this.monto = monto;
        this.fecha = fecha;
        this.inversionista = inversionista;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Inversionista getInversionista() { return inversionista; }
    public void setInversionista(Inversionista inversionista) { this.inversionista = inversionista; }
}
