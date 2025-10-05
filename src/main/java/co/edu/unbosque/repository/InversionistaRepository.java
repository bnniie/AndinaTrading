package co.edu.unbosque.repository;

import co.edu.unbosque.model.entity.Inversionista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InversionistaRepository extends JpaRepository<Inversionista, Long> {
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
    boolean existsByUsuario(String usuario);
}
