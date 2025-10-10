package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.repository.ComisionistaRepository;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Service
public class ComisionistaService {

    @Autowired
    private ComisionistaRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validarCredenciales(String usuario, String contrasena) {
        Comisionista c = repository.findByUsuario(usuario);
        return c != null && passwordEncoder.matches(contrasena, c.getContrasena());
    }


    public Comisionista buscarPorUsuario(String usuario) {
        return repository.findByUsuario(usuario);
    }

    public void crearComisionista(Comisionista c) {
        String encriptada = passwordEncoder.encode(c.getContrasena());
        c.setContrasena(encriptada);
        repository.save(c);
    }

    @PostConstruct
    public void encriptarClavesExistentes() {
        List<Comisionista> lista = repository.findAll();
        for (Comisionista c : lista) {
            String actual = c.getContrasena();
            if (!actual.startsWith("$2a$")) {
                String encriptada = passwordEncoder.encode(actual);
                c.setContrasena(encriptada);
                repository.save(c);
            }
        }
    }
}
