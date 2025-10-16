package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.repository.ComisionistaRepository;
import jakarta.annotation.PostConstruct;

import java.util.List;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los comisionistas.
 * Incluye autenticación, creación de usuarios y encriptación de contraseñas.
 */
@Service
public class ComisionistaService {

    @Autowired
    private ComisionistaRepository repository; // Repositorio JPA para acceder a datos de comisionistas.

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas (BCrypt).

    /**
     * Valida las credenciales de un comisionista.
     * Compara la contraseña ingresada con la almacenada encriptada.
     * @param usuario nombre de usuario.
     * @param contrasena contraseña en texto plano.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarCredenciales(String usuario, String contrasena) {
        Comisionista c = repository.findByUsuario(usuario);
        return c != null && passwordEncoder.matches(contrasena, c.getContrasena());
    }

    /**
     * Busca un comisionista por su nombre de usuario.
     * @param usuario nombre de usuario.
     * @return instancia de Comisionista si existe, null si no se encuentra.
     */
    public Comisionista buscarPorUsuario(String usuario) {
        return repository.findByUsuario(usuario);
    }

    /**
     * Crea un nuevo comisionista en la base de datos.
     * La contraseña se encripta antes de ser almacenada.
     * @param c objeto Comisionista con datos a registrar.
     */
    public void crearComisionista(Comisionista c) {
        String encriptada = passwordEncoder.encode(c.getContrasena());
        c.setContrasena(encriptada);
        repository.save(c);
    }

    /**
     * Método que se ejecuta automáticamente al iniciar el servicio.
     * Recorre todos los comisionistas existentes y encripta sus contraseñas
     * si aún no están en formato BCrypt.
     */
    @PostConstruct
    public void encriptarClavesExistentes() {
        List<Comisionista> lista = repository.findAll();
        for (Comisionista c : lista) {
            String actual = c.getContrasena();
            // Verifica si la contraseña ya está encriptada (formato BCrypt inicia con "$2a$")
            if (!actual.startsWith("$2a$")) {
                String encriptada = passwordEncoder.encode(actual);
                c.setContrasena(encriptada);
                repository.save(c);
            }
        }
    }
}
