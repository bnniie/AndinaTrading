package co.edu.unbosque.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unbosque.model.DTO.PrecioDTO;
import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.model.entity.Movimiento;
import co.edu.unbosque.repository.InversionistaRepository;
import co.edu.unbosque.repository.MovimientoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InversionistaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InversionistaRepository repo;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private PrecioService precioService;

    public boolean registrar(Inversionista inversionista) {
        if (repo.existsByDocumentoIdentidad(inversionista.getDocumentoIdentidad()) ||
            repo.existsByUsuario(inversionista.getUsuario())) {
            return false;
        }

        String contraseñaEncriptada = passwordEncoder.encode(inversionista.getContrasena());
        inversionista.setContrasena(contraseñaEncriptada);

        repo.save(inversionista);
        return true;
    }

    public boolean validarCredenciales(String usuario, String contrasena) {
        Inversionista inversionista = repo.findByUsuario(usuario);
        if (inversionista == null) {
            return false;
        }
        return passwordEncoder.matches(contrasena, inversionista.getContrasena());
    }

    public Inversionista buscarPorUsuario(String usuario) {
        return repo.findByUsuario(usuario);
    }

    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
    Inversionista inversionista = repo.findByUsuario(usuario);
    if (inversionista != null) {
        inversionista.setContrasena(passwordEncoder.encode(nuevaContrasena));
        repo.save(inversionista);
        return true;
    }
        return false;
    }
    
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

    
    public boolean registrarAjuste(String usuario, String symbol, double variacion) {
        Inversionista i = repo.findByUsuario(usuario);
        if (i == null) return false;

        i.setSaldo(i.getSaldo() + variacion);
        movimientoRepository.save(new Movimiento("AJUSTE", symbol, variacion, LocalDateTime.now(), i));
        repo.save(i);
        return true;
    }

    
    public List<Movimiento> obtenerMovimientos(String usuario) {
        Inversionista i = repo.findByUsuario(usuario);
        return movimientoRepository.findByInversionista(i);
    }
}
