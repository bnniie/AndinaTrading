package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Ciudad;

/**
 * Repositorio JPA para la entidad {@link Ciudad}.
 * Proporciona métodos CRUD y consultas personalizadas sobre la tabla de ciudades.
 */
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    /**
     * Busca una ciudad por su nombre exacto.
     * @param nombre nombre de la ciudad.
     * @return instancia de {@link Ciudad} si existe, o null si no se encuentra.
     */
    Ciudad findByNombre(String nombre);

    /**
     * Verifica si existe una ciudad con el nombre especificado.
     * @param nombre nombre de la ciudad.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNombre(String nombre);
}