package co.edu.unbosque.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad JPA que representa a un inversionista dentro del sistema.
 * Contiene información personal, credenciales de acceso, ubicación geográfica,
 * datos financieros y relación contractual.
 */
@Entity
@Table(name = "inversionista")
public class Inversionista {

    // Identificador único autogenerado
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del inversionista (requerido)
    @NotBlank
    private String nombre;

    // Apellido del inversionista (requerido)
    @NotBlank
    private String apellido;

    // Documento de identidad único (requerido)
    @NotBlank
    @Column(unique = true)
    private String documentoIdentidad;

    // Correo electrónico válido (requerido)
    @Email
    @NotBlank
    private String correo;

    // Teléfono de contacto (requerido)
    @NotBlank
    private String telefono;

    // Ciudad asociada (relación muchos-a-uno)
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    @JsonManagedReference
    private Ciudad ciudad;

    // País asociado (relación muchos-a-uno)
    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    // Nombre de usuario único (requerido)
    @NotBlank
    @Column(unique = true)
    private String usuario;

    // Contraseña con mínimo de 6 caracteres (requerido)
    @NotBlank
    @Size(min = 6)
    private String contrasena;

    /**
     * Saldo disponible del inversionista.
     * No puede ser nulo ni negativo. Se inicializa en 0.0.
     */
    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double saldo = 0.0;

    /**
     * Porcentaje de comisión asignado al inversionista.
     * Se almacena directamente en esta entidad.
     */
    @NotNull
    @DecimalMin("0.0")
    @Column(name = "porcentaje_comision", nullable = false)
    private Double porcentajeComision = 5.0;

    /**
     * Contrato asociado al inversionista.
     * Relación uno-a-uno con gestión en cascada.
     */
    @OneToOne(mappedBy = "inversionista", cascade = CascadeType.ALL)
    private Contrato contrato;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(Double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}