package co.edu.unbosque.controller;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.service.InversionistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
}
