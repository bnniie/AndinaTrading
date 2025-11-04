package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entidad JPA que representa a un comisionista en la base de datos.
 * Mapeada a la tabla 'comisionista'.
 * Incluye validaciones para campos obligatorios y relaciones con ciudad y país.
 */
@Entity
@Table(name = "comisionista")
public class Comisionista {

    /**
     * Identificador único del comisionista.
     * Se genera automáticamente mediante estrategia de incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario del comisionista.
     * Debe ser único y no puede estar en blanco.
     */
    @NotBlank
    @Column(unique = true)
    private String usuario;

    /**
     * Contraseña del comisionista.
     * Debe tener al menos 6 caracteres y no puede estar en blanco.
     */
    @NotBlank
    @Size(min = 6)
    private String contrasena;

    /**
     * Nombre completo del comisionista.
     * Campo obligatorio.
     */
    @NotBlank
    private String nombreCompleto;

    /**
     * Correo electrónico del comisionista.
     * Debe tener formato válido y no puede estar en blanco.
     */
    @Email
    @NotBlank
    private String correo;

    /**
     * Ciudad asociada al comisionista.
     * Relación muchos-a-uno con la entidad {@link Ciudad}.
     */
    @ManyToOne
    @JoinColumn(name = "ciudad_id")
    private Ciudad ciudad;

    /**
     * País asociado al comisionista.
     * Relación muchos-a-uno con la entidad {@link Pais}.
     */
    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    // Constructores

    /**
     * Constructor vacío requerido por JPA.
     */
    public Comisionista() {}

    /**
     * Constructor completo para inicializar un comisionista.
     * @param usuario nombre de usuario.
     * @param contrasena contraseña.
     * @param nombreCompleto nombre completo.
     * @param correo correo electrónico.
     * @param ciudad ciudad asociada.
     * @param pais país asociado.
     */
    public Comisionista(String usuario, String contrasena, String nombreCompleto, String correo, Ciudad ciudad, Pais pais) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters y setters

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
}