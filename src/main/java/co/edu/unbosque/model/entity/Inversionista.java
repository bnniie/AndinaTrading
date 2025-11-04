package co.edu.unbosque.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad JPA que representa a un inversionista dentro del sistema.
 * Contiene información personal, credenciales de acceso, ubicación geográfica y relación con su contrato.
 */
@Entity
public class Inversionista {

    /**
     * Identificador único del inversionista.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del inversionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String nombre;

    /**
     * Apellido del inversionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String apellido;

    /**
     * Documento de identidad del inversionista.
     * Debe ser único y no puede estar en blanco.
     */
    @NotBlank
    @Column(unique = true)
    private String documentoIdentidad;

    /**
     * Correo electrónico del inversionista.
     * Debe tener formato válido y no puede estar en blanco.
     */
    @Email
    @NotBlank
    private String correo;

    /**
     * Número de teléfono del inversionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String telefono;

    /**
     * Ciudad asociada al inversionista.
     * Relación muchos-a-uno con la entidad {@link Ciudad}.
     * Se utiliza {@link JsonManagedReference} para evitar ciclos de serialización.
     */
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    @JsonManagedReference
    private Ciudad ciudad;

    /**
     * País asociado al inversionista.
     * Relación muchos-a-uno con la entidad {@link Pais}.
     */
    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    /**
     * Nombre de usuario del inversionista.
     * Debe ser único y no puede estar en blanco.
     */
    @NotBlank
    @Column(unique = true)
    private String usuario;

    /**
     * Contraseña del inversionista.
     * Debe tener al menos 6 caracteres.
     */
    @NotBlank
    @Size(min = 6)
    private String contrasena;

    /**
     * Saldo disponible del inversionista.
     * Puede ser nulo inicialmente.
     */
    private Double saldo;

    /**
     * Contrato asociado al inversionista.
     * Relación uno-a-uno con la entidad {@link Contrato}.
     * Se gestiona en cascada desde esta entidad.
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

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}