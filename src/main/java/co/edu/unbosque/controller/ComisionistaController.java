package co.edu.unbosque.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.service.ComisionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comisionistas")
public class ComisionistaController {

    @Autowired
    private ComisionistaService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Comisionista comisionista = service.buscarPorUsuario(login.getUsuario());
            return ResponseEntity.ok(comisionista);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestParam String usuario) {
        Comisionista comisionista = service.buscarPorUsuario(usuario);
        if (comisionista != null) {
            return ResponseEntity.ok(comisionista);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
