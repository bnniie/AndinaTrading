package co.edu.unbosque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unbosque.model.entity.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    Ciudad findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}