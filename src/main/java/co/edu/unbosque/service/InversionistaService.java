package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.repository.InversionistaRepository;

@Service
public class InversionistaService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InversionistaRepository repo;

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

}
