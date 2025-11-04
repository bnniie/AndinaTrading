package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.model.entity.Orden;
import co.edu.unbosque.repository.ComisionistaRepository;
import co.edu.unbosque.repository.OrdenRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los comisionistas.
 * Incluye autenticación, creación de usuarios, encriptación de contraseñas y análisis de órdenes.
 */
@Service
public class ComisionistaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ComisionistaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrdenRepository ordenRepository;

    /**
     * Valida las credenciales de un comisionista.
     * Compara la contraseña ingresada con la almacenada encriptada.
     *
     * @param usuario nombre de usuario.
     * @param contrasena contraseña en texto plano.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarCredenciales(String usuario, String contrasena) {
        Comisionista c = repository.findByUsuario(usuario);
        return c != null && passwordEncoder.matches(contrasena, c.getContrasena());
    }

    /**
     * Busca un comisionista por su nombre de usuario.
     *
     * @param usuario nombre de usuario.
     * @return instancia de {@link Comisionista} si existe, o null si no se encuentra.
     */
    public Comisionista buscarPorUsuario(String usuario) {
        return repository.findByUsuario(usuario);
    }

    /**
     * Crea un nuevo comisionista en la base de datos.
     * La contraseña se encripta antes de ser almacenada.
     *
     * @param c objeto {@link Comisionista} con los datos a registrar.
     */
    public void crearComisionista(Comisionista c) {
        String encriptada = passwordEncoder.encode(c.getContrasena());
        c.setContrasena(encriptada);
        repository.save(c);
    }

    /**
     * Método que se ejecuta automáticamente al iniciar el servicio.
     * Recorre todos los comisionistas existentes y encripta sus contraseñas
     * si aún no están en formato BCrypt.
     */
    @PostConstruct
    public void encriptarClavesExistentes() {
        List<Comisionista> lista = repository.findAll();
        for (Comisionista c : lista) {
            String actual = c.getContrasena();
            // Verifica si la contraseña ya está encriptada (formato BCrypt inicia con "$2a$")
            if (!actual.startsWith("$2a$")) {
                String encriptada = passwordEncoder.encode(actual);
                c.setContrasena(encriptada);
                repository.save(c);
            }
        }
    }

    /**
     * Cuenta la cantidad de órdenes por estado asociadas a un comisionista.
     *
     * @param usuario nombre de usuario del comisionista.
     * @return mapa con el estado como clave y el conteo como valor.
     */
    public Map<String, Long> contarOrdenesPorEstado(String usuario) {
        Comisionista comisionista = repository.findByUsuario(usuario);
        if (comisionista == null) return Map.of();

        List<Object[]> resultados = entityManager.createQuery("""
            SELECT o.estado, COUNT(o)
            FROM Orden o
            WHERE o.comisionista.id = :comId
            GROUP BY o.estado
        """).setParameter("comId", comisionista.getId()).getResultList();

        return resultados.stream().collect(Collectors.toMap(
            r -> ((String) r[0]).toLowerCase().trim(),
            r -> (Long) r[1]
        ));
    }

    /**
     * Obtiene una lista de movimientos de órdenes del comisionista autenticado.
     * Cada movimiento incluye la fecha de creación y el valor de la orden.
     *
     * @param usuario nombre de usuario del comisionista.
     * @return lista de mapas con los campos 'fechaCreacion' y 'precio'.
     */
    public List<Map<String, Object>> obtenerMovimientosOrdenes(String usuario) {
        Comisionista comisionista = repository.findByUsuario(usuario);
        if (comisionista == null) return List.of();

        List<Orden> ordenes = ordenRepository.findByComisionista(comisionista);

        return ordenes.stream()
            .map(o -> {
                Map<String, Object> m = new HashMap<>();
                m.put("fechaCreacion", o.getFechaCreacion().toLocalDate().toString());
                m.put("precio", o.getValorOrden());
                return m;
            })
            .collect(Collectors.toList());
    }
}