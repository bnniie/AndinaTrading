package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Inversionista;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Inversionista.
 * Proporciona acceso a operaciones CRUD y consultas personalizadas sobre la tabla 'inversionista'.
 * Extiende JpaRepository, lo que permite usar métodos como save, findAll, findById, delete, etc.
 */
public interface InversionistaRepository extends JpaRepository<Inversionista, Long> {

    /**
     * Verifica si existe un inversionista con el documento de identidad especificado.
     * Útil para validar duplicados antes de registrar un nuevo usuario.
     * @param documentoIdentidad número de documento único.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Verifica si existe un inversionista con el nombre de usuario especificado.
     * Útil para validar disponibilidad de usuario en el registro.
     * @param usuario nombre de usuario único.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByUsuario(String usuario);

    /**
     * Busca un inversionista por su nombre de usuario.
     * @param usuario nombre de usuario único.
     * @return instancia de Inversionista si existe, null si no se encuentra.
     */
    Inversionista findByUsuario(String usuario);
}
