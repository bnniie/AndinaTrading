package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad JPA que representa a un comisionista en la base de datos.
 * Mapeada a la tabla 'comisionista'.
 * Incluye validaciones para campos obligatorios y formato de correo.
 */
@Entity
@Table(name = "comisionista")
public class Comisionista {

    /**
     * Identificador único del comisionista.
     * Se genera automáticamente con estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario del comisionista.
     * Campo obligatorio, no puede estar en blanco.
     */
    @NotBlank
    private String usuario;

    /**
     * Contraseña del comisionista.
     * Campo obligatorio, no puede estar en blanco.
     */
    @NotBlank
    private String contrasena;

    /**
     * Nombre completo del comisionista.
     * Campo obligatorio, no puede estar en blanco.
     */
    @NotBlank
    private String nombreCompleto;

    /**
     * Correo electrónico del comisionista.
     * Validado con formato de email.
     */
    @Email
    private String correo;

    /**
     * Ciudad de residencia del comisionista.
     * Campo opcional.
     */
    private String ciudad;

    /**
     * País de residencia del comisionista.
     * Campo opcional.
     */
    private String pais;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Comisionista() {}

    /**
     * Constructor con todos los campos excepto el ID.
     * @param usuario nombre de usuario
     * @param contrasena contraseña
     * @param nombreCompleto nombre completo
     * @param correo correo electrónico
     * @param ciudad ciudad de residencia
     * @param pais país de residencia
     */
    public Comisionista(String usuario, String contrasena, String nombreCompleto, String correo, String ciudad, String pais) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Métodos getter y setter para cada campo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
}
