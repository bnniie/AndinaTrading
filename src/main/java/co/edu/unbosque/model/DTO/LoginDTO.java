package co.edu.unbosque.model.DTO;

/**
 * Clase DTO (Data Transfer Object) utilizada para transportar los datos de inicio de sesión.
 * Se emplea en los controladores para recibir el usuario y la contraseña desde el frontend.
 */
public class LoginDTO {

    // Nombre de usuario ingresado por el cliente.
    private String usuario;

    // Contraseña ingresada por el cliente.
    private String contrasena;

    /**
     * Obtiene el nombre de usuario.
     * @return usuario como cadena de texto.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el nombre de usuario.
     * @param usuario nombre de usuario a asignar.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la contraseña.
     * @return contraseña como cadena de texto.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña.
     * @param contrasena contraseña a asignar.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
