package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.model.entity.Orden;
import co.edu.unbosque.model.entity.Inversionista;

import java.util.List;

@Repository

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByInversionista(Inversionista inversionista);

    long countByInversionistaAndEstado(Inversionista inversionista, String estado);
}