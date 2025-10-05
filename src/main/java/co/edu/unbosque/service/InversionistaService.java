package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.repository.InversionistaRepository;

@Service
public class InversionistaService {

    @Autowired
    private InversionistaRepository repo;

    public boolean registrar(Inversionista inversionista) {
        if (repo.existsByDocumentoIdentidad(inversionista.getDocumentoIdentidad()) ||
            repo.existsByUsuario(inversionista.getUsuario())) {
            return false;
        }
        repo.save(inversionista);
        return true;
    }
}
