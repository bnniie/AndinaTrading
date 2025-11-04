package co.edu.unbosque.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Ciudad;
import co.edu.unbosque.model.entity.Contrato;
import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Orden;
import co.edu.unbosque.model.entity.Pais;
import co.edu.unbosque.repository.CiudadRepository;
import co.edu.unbosque.repository.InversionistaRepository;
import co.edu.unbosque.repository.OrdenRepository;
import co.edu.unbosque.repository.PaisRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Map;
import java.util.HashMap;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InversionistaRepository repo;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PaisRepository paisRepository;

    /**
     * Registra un nuevo inversionista si no existe previamente por documento o usuario.
     * Asocia ciudad y país, y encripta la contraseña antes de guardar.
     *
     * @param inversionista datos del nuevo usuario.
     * @param ciudadNombre nombre de la ciudad asociada.
     * @return true si el registro fue exitoso, false si ya existe o la ciudad no es válida.
     */
    public boolean registrar(Inversionista inversionista, String ciudadNombre) {
        Ciudad ciudad = ciudadRepository.findByNombre(ciudadNombre);
        if (ciudad == null) return false;

        Pais pais = ciudad.getPais();
        inversionista.setCiudad(ciudad);
        inversionista.setPais(pais);

        if (repo.existsByDocumentoIdentidad(inversionista.getDocumentoIdentidad()) ||
            repo.existsByUsuario(inversionista.getUsuario())) {
            return false;
        }

        inversionista.setContrasena(passwordEncoder.encode(inversionista.getContrasena()));
        repo.save(inversionista);
        return true;
    }

    /**
     * Valida las credenciales de inicio de sesión.
     *
     * @param usuario nombre de usuario.
     * @param contrasena contraseña en texto plano.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarCredenciales(String usuario, String contrasena) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        return inversionista != null && passwordEncoder.matches(contrasena, inversionista.getContrasena());
    }

    /**
     * Busca un inversionista por su nombre de usuario.
     *
     * @param usuario nombre de usuario.
     * @return instancia de {@link Inversionista} si existe, o null si no se encuentra.
     */
    public Inversionista buscarPorUsuario(String usuario) {
        return repo.findByUsuario(usuario);
    }

    /**
     * Actualiza la contraseña de un inversionista.
     *
     * @param usuario nombre de usuario.
     * @param nuevaContrasena nueva contraseña en texto plano.
     * @return true si se actualizó correctamente, false si el usuario no existe.
     */
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista == null) return false;

        inversionista.setContrasena(passwordEncoder.encode(nuevaContrasena));
        repo.save(inversionista);
        return true;
    }

    /**
     * Cuenta la cantidad de órdenes por estado asociadas a un inversionista.
     *
     * @param usuario nombre de usuario.
     * @return mapa con el estado como clave y el conteo como valor.
     */
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

    /**
     * Obtiene una lista de movimientos de órdenes del inversionista autenticado.
     * Cada movimiento incluye la fecha de creación y el valor de la orden.
     *
     * @param usuario nombre de usuario.
     * @return lista de mapas con los campos 'fechaCreacion' y 'precio'.
     */
    public List<Map<String, Object>> obtenerMovimientosOrdenes(String usuario) {
        Inversionista inv = repo.findByUsuario(usuario);
        if (inv == null) return List.of();

        List<Orden> ordenes = ordenRepository.findByInversionista(inv);

        return ordenes.stream()
            .map(o -> {
                Map<String, Object> m = new HashMap<>();
                m.put("fechaCreacion", o.getFechaCreacion().toLocalDate().toString());
                m.put("precio", o.getValorOrden());
                return m;
            })
            .collect(Collectors.toList());
    }

    /**
     * Actualiza el nombre de usuario y teléfono de un inversionista.
     *
     * @param usuarioActual usuario actual.
     * @param nuevoUsuario nuevo nombre de usuario.
     * @param nuevoTelefono nuevo número de teléfono.
     * @return true si se actualizó correctamente, false si el usuario no existe.
     */
    public boolean actualizarUsuarioYTelefono(String usuarioActual, String nuevoUsuario, String nuevoTelefono) {
        Inversionista inversionista = repo.findByUsuario(usuarioActual);
        if (inversionista == null) return false;

        inversionista.setUsuario(nuevoUsuario);
        inversionista.setTelefono(nuevoTelefono);
        repo.save(inversionista);
        return true;
    }

    /**
     * Actualiza el saldo del inversionista sumando un monto adicional.
     *
     * @param usuario nombre de usuario.
     * @param montoAdicional monto a agregar al saldo actual.
     * @return true si se actualizó correctamente, false si el usuario no existe.
     */
    public boolean actualizarSaldo(String usuario, Double montoAdicional) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista == null) return false;

        inversionista.setSaldo(inversionista.getSaldo() + montoAdicional);
        repo.save(inversionista);
        return true;
    }

    /**
     * Actualiza o crea el contrato asociado al inversionista.
     *
     * @param usuario nombre de usuario.
     * @param porcentaje nuevo porcentaje de comisión.
     * @param duracion nueva duración en meses.
     * @return true si se actualizó correctamente, false si el usuario no existe.
     */
    public boolean actualizarContrato(String usuario, Double porcentaje, Integer duracion) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista == null) return false;

        inversionista.setPorcentajeComision(porcentaje);

        Contrato contrato = inversionista.getContrato();
        if (contrato == null) {
            contrato = new Contrato();
            contrato.setInversionista(inversionista);
        }

        contrato.setDuracionMeses(duracion);
        inversionista.setContrato(contrato);

        repo.save(inversionista);
        return true;
    }
}