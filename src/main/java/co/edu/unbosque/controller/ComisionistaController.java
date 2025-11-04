package co.edu.unbosque.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import co.edu.unbosque.model.entity.Comisionista;
import co.edu.unbosque.repository.ComisionistaRepository;
import co.edu.unbosque.service.ComisionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con comisionistas.
 * Expone endpoints para autenticación, consulta de perfil y estadísticas de órdenes.
 */
@CrossOrigin(origins = "http://localhost:3000") // Permite solicitudes CORS desde el frontend local.
@RestController
@RequestMapping("/api/comisionistas")
public class ComisionistaController {

    @Autowired
    private ComisionistaService service; // Servicio con la lógica de negocio para comisionistas.

    @Autowired
    private ComisionistaRepository comisionistaRepository; // Repositorio para operaciones directas con la base de datos.

    /**
     * Endpoint para iniciar sesión como comisionista.
     * URL: POST /api/comisionistas/login
     *
     * @param login objeto LoginDTO con usuario y contraseña.
     * @param session sesión HTTP para almacenar el usuario autenticado.
     * @return ResponseEntity con mensaje y URL del dashboard si es exitoso, o error 401 si falla.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login, HttpSession session) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Comisionista comisionista = service.buscarPorUsuario(login.getUsuario());

            // Guarda el usuario en sesión para futuras consultas.
            session.setAttribute("usuario", comisionista.getUsuario());

            // URL personalizada para redirigir al dashboard del comisionista.
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
     * Endpoint para obtener el perfil del comisionista autenticado.
     * URL: GET /api/comisionistas/perfil
     *
     * @param session sesión HTTP que contiene el usuario autenticado.
     * @return ResponseEntity con datos del perfil o error si no hay sesión o el usuario no existe.
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        Comisionista comisionista = service.buscarPorUsuario(usuario);
        if (comisionista == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        // Construye el mapa de datos del perfil.
        Map<String, Object> perfil = Map.of(
            "usuario", comisionista.getUsuario(),
            "nombreCompleto", comisionista.getNombreCompleto(),
            "correo", comisionista.getCorreo(),
            "ciudad", comisionista.getCiudad() != null ? comisionista.getCiudad().getNombre() : "Sin ciudad",
            "pais", comisionista.getPais() != null ? comisionista.getPais().getNombre() : "Sin país"
        );

        return ResponseEntity.ok(perfil);
    }

    /**
     * Endpoint para obtener estadísticas de órdenes por estado del comisionista autenticado.
     * URL: GET /api/comisionistas/ordenes/estadisticas
     *
     * @param session sesión HTTP que contiene el usuario autenticado.
     * @return ResponseEntity con conteo de órdenes por estado o error si no hay sesión.
     */
    @GetMapping("/ordenes/estadisticas")
    public ResponseEntity<?> estadisticasOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        System.out.println("----------------- " + usuario); // Log de depuración.

        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        Map<String, Long> conteo = service.contarOrdenesPorEstado(usuario);
        return ResponseEntity.ok(conteo);
    }

    /**
     * Endpoint para obtener los movimientos de órdenes del comisionista autenticado.
     * URL: GET /api/comisionistas/ordenes/movimientos
     *
     * @param session sesión HTTP que contiene el usuario autenticado.
     * @return ResponseEntity con lista de movimientos o error si no hay sesión.
     */
    @GetMapping("/ordenes/movimientos")
    public ResponseEntity<?> movimientosOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        List<Map<String, Object>> movimientos = service.obtenerMovimientosOrdenes(usuario);
        return ResponseEntity.ok(movimientos);
    }
}