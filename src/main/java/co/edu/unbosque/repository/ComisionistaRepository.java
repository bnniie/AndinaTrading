package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Comisionista;

import java.util.List;

public interface ComisionistaRepository extends JpaRepository<Comisionista, Long> {
    Comisionista findByUsuario(String usuario);
    void deleteByUsuarioIn(List<String> usuarios);
}

