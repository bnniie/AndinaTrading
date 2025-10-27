package co.edu.unbosque.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.DTO.PrecioDTO;
import co.edu.unbosque.model.entity.Ciudad;
import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Movimiento;
import co.edu.unbosque.model.entity.Pais;
import co.edu.unbosque.repository.CiudadRepository;
import co.edu.unbosque.repository.InversionistaRepository;
import co.edu.unbosque.repository.MovimientoRepository;
import co.edu.unbosque.repository.PaisRepository;

import java.util.List;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los inversionistas.
 * Incluye registro, autenticación, gestión de contraseñas, operaciones financieras y consulta de movimientos.
 */
@Service
public class InversionistaService {

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas (BCrypt).

    @Autowired
    private InversionistaRepository repo; // Repositorio JPA para inversionistas.

    @Autowired
    private MovimientoRepository movimientoRepository; // Repositorio JPA para movimientos financieros.

    @Autowired
    private PrecioService precioService; // Servicio para obtener precios de activos financieros.

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

    /**
     * Registra una operación de compra de activos por parte del inversionista.
     * Descuenta el monto del saldo y guarda el movimiento.
     * @param usuario nombre de usuario.
     * @param symbol símbolo del activo.
     * @param cantidad cantidad adquirida.
     * @return true si la operación fue registrada, false si el usuario no existe.
     */
    public boolean registrarCompra(String usuario, String symbol, int cantidad) {
        Inversionista i = repo.findByUsuario(usuario);
        if (i == null) return false;

        PrecioDTO precio = precioService.obtenerPrecio(symbol);
        double monto = precio.getPrecio_actual() * cantidad;

        i.setSaldo(i.getSaldo() - monto);
        movimientoRepository.save(new Movimiento("COMPRA", symbol, monto, LocalDateTime.now(), i));
        repo.save(i);
        return true;
    }

    /**
     * Registra una operación de venta de activos por parte del inversionista.
     * Aumenta el saldo y guarda el movimiento.
     * @param usuario nombre de usuario.
     * @param symbol símbolo del activo.
     * @param cantidad cantidad vendida.
     * @return true si la operación fue registrada, false si el usuario no existe.
     */
    public boolean registrarVenta(String usuario, String symbol, int cantidad) {
        Inversionista i = repo.findByUsuario(usuario);
        if (i == null) return false;

        PrecioDTO precio = precioService.obtenerPrecio(symbol);
        double monto = precio.getPrecio_actual() * cantidad;

        i.setSaldo(i.getSaldo() + monto);
        movimientoRepository.save(new Movimiento("VENTA", symbol, monto, LocalDateTime.now(), i));
        repo.save(i);
        return true;
    }

    /**
     * Registra un ajuste manual en el saldo del inversionista.
     * Puede representar correcciones, bonificaciones u otros movimientos.
     * @param usuario nombre de usuario.
     * @param symbol referencia del ajuste (ej. motivo o activo).
     * @param variacion monto a ajustar (positivo o negativo).
     * @return true si el ajuste fue registrado, false si el usuario no existe.
     */
    public boolean registrarAjuste(String usuario, String symbol, double variacion) {
        Inversionista i = repo.findByUsuario(usuario);
        if (i == null) return false;

        i.setSaldo(i.getSaldo() + variacion);
        movimientoRepository.save(new Movimiento("AJUSTE", symbol, variacion, LocalDateTime.now(), i));
        repo.save(i);
        return true;
    }

    /**
     * Obtiene la lista de movimientos financieros realizados por el inversionista.
     * @param usuario nombre de usuario.
     * @return lista de movimientos asociados al usuario.
     */
    public List<Movimiento> obtenerMovimientos(String usuario) {
        Inversionista i = repo.findByUsuario(usuario);
        return movimientoRepository.findByInversionista(i);
    }
}
