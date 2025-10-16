package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Movimiento.
 * Proporciona acceso a operaciones CRUD y consultas personalizadas sobre la tabla 'movimiento'.
 * Extiende JpaRepository, lo que permite usar métodos como save, findAll, findById, delete, etc.
 */
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * Busca todos los movimientos asociados a un inversionista específico.
     * @param inversionista instancia de Inversionista para filtrar los movimientos.
     * @return lista de movimientos realizados por el inversionista.
     */
    List<Movimiento> findByInversionista(Inversionista inversionista);
}
