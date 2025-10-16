package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Comisionista;

import java.util.List;

/**
 * Repositorio JPA para la entidad Comisionista.
 * Proporciona métodos CRUD y consultas personalizadas sobre la tabla 'comisionista'.
 * Extiende JpaRepository, lo que permite acceso a métodos como save, findAll, findById, delete, etc.
 */
public interface ComisionistaRepository extends JpaRepository<Comisionista, Long> {

    /**
     * Busca un comisionista por su nombre de usuario.
     * @param usuario nombre de usuario único.
     * @return instancia de Comisionista si existe, null si no se encuentra.
     */
    Comisionista findByUsuario(String usuario);

    /**
     * Elimina múltiples comisionistas cuyos nombres de usuario coincidan con los proporcionados.
     * @param usuarios lista de nombres de usuario a eliminar.
     */
    void deleteByUsuarioIn(List<String> usuarios);
}
