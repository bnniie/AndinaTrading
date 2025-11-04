package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Contrato}.
 * Proporciona métodos CRUD y consultas personalizadas sobre la tabla de contratos.
 */
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    /**
     * Busca un contrato por el ID del inversionista asociado.
     * Esta consulta permite acceder directamente al contrato sin necesidad de navegar por la relación.
     *
     * @param inversionistaId identificador único del inversionista.
     * @return instancia de {@link Contrato} si existe, o null si no se encuentra.
     */
    Contrato findByInversionistaId(Long inversionistaId);
}