package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Pais;

/**
 * Repositorio JPA para la entidad {@link Pais}.
 * Proporciona métodos CRUD y consultas personalizadas sobre la tabla de países.
 */
public interface PaisRepository extends JpaRepository<Pais, Integer> {

    /**
     * Busca un país por su nombre exacto.
     * @param nombre nombre del país.
     * @return instancia de {@link Pais} si existe, o null si no se encuentra.
     */
    Pais findByNombre(String nombre);

    /**
     * Verifica si existe un país con el nombre especificado.
     * @param nombre nombre del país.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNombre(String nombre);
}