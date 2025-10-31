package co.edu.unbosque.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Ciudad;
import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Pais;
import co.edu.unbosque.repository.CiudadRepository;
import co.edu.unbosque.repository.InversionistaRepository;
import co.edu.unbosque.repository.PaisRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los inversionistas.
 * Incluye registro, autenticación, gestión de contraseñas, operaciones financieras y consulta de movimientos.
 */
@Service
public class InversionistaService {

    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas (BCrypt).

    @Autowired
    private InversionistaRepository repo; // Repositorio JPA para inversionistas.

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PaisRepository paisRepository;



    /**
     * Registra un nuevo inversionista si no existe previamente por documento o usuario.
     * Encripta la contraseña antes de guardar.
     * @param inversionista datos del nuevo usuario.
     * @return true si el registro fue exitoso, false si ya existe.
     */
    public boolean registrar(Inversionista inversionista, String ciudadNombre) {
        // Buscar la ciudad por nombre
        Ciudad ciudad = ciudadRepository.findByNombre(ciudadNombre);
        if (ciudad == null) {
            return false; // ciudad no válida
        }

        // Obtener el país asociado a la ciudad
        Pais pais = ciudad.getPais();

        // Asignar ciudad y país como objetos
        inversionista.setCiudad(ciudad);
        inversionista.setPais(pais);

        // Validar si ya existe por documento o usuario
        if (repo.existsByDocumentoIdentidad(inversionista.getDocumentoIdentidad()) ||
            repo.existsByUsuario(inversionista.getUsuario())) {
            return false;
        }

        String contraseñaEncriptada = passwordEncoder.encode(inversionista.getContrasena());
        inversionista.setContrasena(contraseñaEncriptada);

        repo.save(inversionista);
        return true;
    }

    /**
     * Valida las credenciales de inicio de sesión.
     * Compara la contraseña ingresada con la almacenada encriptada.
     * @param usuario nombre de usuario.
     * @param contrasena contraseña en texto plano.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarCredenciales(String usuario, String contrasena) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista == null) {
            return false;
        }
        return passwordEncoder.matches(contrasena, inversionista.getContrasena());
    }

    /**
     * Busca un inversionista por su nombre de usuario.
     * @param usuario nombre de usuario.
     * @return instancia de Inversionista si existe, null si no se encuentra.
     */
    public Inversionista buscarPorUsuario(String usuario) {
        return repo.findByUsuario(usuario);
    }

    /**
     * Actualiza la contraseña de un inversionista.
     * Encripta la nueva contraseña antes de guardar.
     * @param usuario nombre de usuario.
     * @param nuevaContrasena nueva contraseña en texto plano.
     * @return true si se actualizó correctamente, false si el usuario no existe.
     */
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista != null) {
            inversionista.setContrasena(passwordEncoder.encode(nuevaContrasena));
            repo.save(inversionista);
            return true;
        }
        return false;
    }

    public Map<String, Long> contarOrdenesPorEstado(String usuario) {
        Inversionista inv = repo.findByUsuario(usuario);
        if (inv == null) return Map.of();

        List<Object[]> resultados = entityManager.createQuery("""
            SELECT o.estado, COUNT(o)
            FROM Orden o
            WHERE o.inversionista.id = :invId
            GROUP BY o.estado
        """).setParameter("invId", inv.getId()).getResultList();

        return resultados.stream().collect(Collectors.toMap(
            r -> ((String) r[0]).toLowerCase().trim(),
            r -> (Long) r[1]
        ));
    } 
}
