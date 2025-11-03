package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    Contrato findByInversionistaId(Long inversionistaId);
}