package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad JPA que representa a un inversionista en el sistema.
 * Mapeada a una tabla con nombre por defecto 'inversionista'.
 * Incluye validaciones para campos obligatorios, formato de correo y unicidad de usuario y documento.
 */
@Entity
public class Inversionista {

    /**
     * Identificador único del inversionista.
     * Se genera automáticamente con estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del inversionista.
     * Campo obligatorio, no puede estar en blanco.
     */
    @NotBlank
    private String nombre;

    /**
     * Apellido del inversionista.
     * Campo obligatorio, no puede estar en blanco.
     */
    @NotBlank
    private String apellido;

    /**
     * Documento de identidad del inversionista.
     * Campo obligatorio, debe ser único en la base de datos.
     */
    @NotBlank
    @Column(unique = true)
    private String documentoIdentidad;

    /**
     * Correo electrónico del inversionista.
     * Campo obligatorio, debe tener formato válido.
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
     * Ciudad de residencia del inversionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String ciudad;

    /**
     * País de residencia del inversionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String pais;

    /**
     * Nombre de usuario del inversionista.
     * Campo obligatorio, debe ser único en la base de datos.
     */
    @NotBlank
    @Column(unique = true)
    private String usuario;

    /**
     * Contraseña del inversionista.
     * Campo obligatorio, debe tener al menos 6 caracteres.
     */
    @NotBlank
    @Size(min = 6)
    private String contrasena;

    /**
     * Saldo actual del inversionista.
     * Campo opcional, puede ser null si no se ha definido.
     */
    private Double saldo;

    // Métodos getter y setter para cada campo

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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
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
}
