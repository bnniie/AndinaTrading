package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Pais;

public interface PaisRepository extends JpaRepository<Pais, Integer> {

    Pais findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
