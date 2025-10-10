package co.edu.unbosque.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "comisionista")
public class Comisionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String usuario;

    @NotBlank
    private String contrasena;

    @NotBlank
    private String nombreCompleto;

    @Email
    private String correo;

    private String ciudad;

    private String pais;

    public Comisionista() {}

    public Comisionista(String usuario, String contrasena, String nombreCompleto, String correo, String ciudad, String pais) {
    this.usuario = usuario;
    this.contrasena = contrasena;
    this.nombreCompleto = nombreCompleto;
    this.correo = correo;
    this.ciudad = ciudad;
    this.pais = pais;
}

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
