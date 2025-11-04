package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.model.entity.Orden;
import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Comisionista;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Orden}.
 * Proporciona métodos CRUD y consultas personalizadas sobre órdenes de compra/venta.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    /**
     * Obtiene todas las órdenes asociadas a un inversionista específico.
     * @param inversionista entidad {@link Inversionista}.
     * @return lista de órdenes vinculadas al inversionista.
     */
    List<Orden> findByInversionista(Inversionista inversionista);

    /**
     * Cuenta cuántas órdenes tiene un inversionista en un estado determinado.
     * @param inversionista entidad {@link Inversionista}.
     * @param estado estado de la orden (ej. PENDIENTE, APROBADA).
     * @return número de órdenes que cumplen con los criterios.
     */
    long countByInversionistaAndEstado(Inversionista inversionista, String estado);

    /**
     * Obtiene todas las órdenes gestionadas por un comisionista específico.
     * @param comisionista entidad {@link Comisionista}.
     * @return lista de órdenes vinculadas al comisionista.
     */
    List<Orden> findByComisionista(Comisionista comisionista);

    /**
     * Cuenta cuántas órdenes tiene un comisionista en un estado determinado.
     * @param comisionista entidad {@link Comisionista}.
     * @param estado estado de la orden (ej. PENDIENTE, EJECUTADA).
     * @return número de órdenes que cumplen con los criterios.
     */
    long countByComisionistaAndEstado(Comisionista comisionista, String estado);
}