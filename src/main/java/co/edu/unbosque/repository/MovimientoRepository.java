package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByInversionista(Inversionista inversionista);
}
