package co.edu.unbosque.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.service.ComisionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con comisionistas.
 * Expone endpoints para autenticación y consulta de perfil.
 */
@CrossOrigin(origins = "http://localhost:3000") // Permite solicitudes CORS desde el frontend local.
@RestController // Indica que esta clase es un controlador REST.
@RequestMapping("/api/comisionistas") // Prefijo común para todos los endpoints de este controlador.
public class ComisionistaController {

    @Autowired
    private ComisionistaService service; // Servicio que contiene la lógica de negocio para comisionistas.

    /**
     * Endpoint para iniciar sesión como comisionista.
     * URL: POST /api/comisionistas/login
     * Entrada: objeto LoginDTO con usuario y contraseña.
     * Salida:
     *   - 200 OK con datos del comisionista si las credenciales son válidas.
     *   - 401 UNAUTHORIZED si las credenciales son incorrectas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login, HttpSession session) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Comisionista comisionista = service.buscarPorUsuario(login.getUsuario());

            session.setAttribute("usuario", comisionista.getUsuario());
            String dashboardUrl = "http://localhost:8000/dashboard/comisionista?usuario=" + comisionista.getUsuario();

            return ResponseEntity.ok(Map.of(
                "mensaje", "Login correcto",
                "dashboard_url", dashboardUrl
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    /**
     * Endpoint para obtener el perfil de un comisionista por su nombre de usuario.
     * URL: GET /api/comisionistas/perfil?usuario={usuario}
     * Entrada: parámetro de consulta 'usuario'.
     * Salida:
     *   - 200 OK con datos del comisionista si existe.
     *   - 404 NOT FOUND si no se encuentra el usuario.
     */
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
