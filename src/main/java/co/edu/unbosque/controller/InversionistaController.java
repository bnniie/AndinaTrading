package co.edu.unbosque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.service.InversionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/inversionistas")
public class InversionistaController {

    @Autowired
    private InversionistaService service;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@Valid @RequestBody Inversionista inversionista) {
        boolean exito = service.registrar(inversionista);
        if (exito) {
            return ResponseEntity.ok("Registro exitoso");
        } else {
            return ResponseEntity.badRequest().body("Error: datos inválidos o duplicados");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Inversionista inversionista = service.buscarPorUsuario(login.getUsuario());
            return ResponseEntity.ok(inversionista);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestParam String usuario) {
        Inversionista inversionista = service.buscarPorUsuario(usuario);
        if (inversionista != null) {
            return ResponseEntity.ok(inversionista);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }
}
