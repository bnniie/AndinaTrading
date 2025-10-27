package co.edu.unbosque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.service.InversionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con inversionistas.
 * Expone endpoints para registro, autenticación, perfil, cambio de contraseña y estado de cuenta.
 */
@CrossOrigin(origins = "http://localhost:3000") // Permite solicitudes CORS desde el frontend local.
@RestController // Indica que esta clase es un controlador REST.
@RequestMapping("/api/inversionistas") // Prefijo común para todos los endpoints de este controlador.
public class InversionistaController {

    @Autowired
    private InversionistaService service; // Servicio que contiene la lógica de negocio para inversionistas.

    /**
     * Endpoint para registrar un nuevo inversionista.
     * URL: POST /api/inversionistas/registro
     * Entrada: objeto Inversionista en el cuerpo de la solicitud.
     * Salida:
     *   - 200 OK si el registro fue exitoso.
     *   - 400 BAD REQUEST si los datos son inválidos o el usuario ya existe.
     */
    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody Map<String, String> datos) {
        Inversionista inversionista = new Inversionista();
        inversionista.setUsuario(datos.get("usuario"));
        inversionista.setContrasena(datos.get("contrasena"));
        inversionista.setNombre(datos.get("nombre"));
        inversionista.setApellido(datos.get("apellido"));
        inversionista.setCorreo(datos.get("correo"));
        inversionista.setTelefono(datos.get("telefono"));
        inversionista.setDocumentoIdentidad(datos.get("documentoIdentidad"));
        inversionista.setSaldo(0.0);

        String ciudadNombre = datos.get("ciudad");

        boolean exito = service.registrar(inversionista, ciudadNombre);
        if (exito) {
            return ResponseEntity.ok("Registro exitoso");
        } else {
            return ResponseEntity.badRequest().body("Error: ciudad no válida o datos duplicados");
    }
}

    /**
     * Endpoint para iniciar sesión como inversionista.
     * URL: POST /api/inversionistas/login
     * Entrada: objeto LoginDTO con usuario y contraseña.
     * Salida:
     *   - 200 OK con datos del inversionista y URL del dashboard si las credenciales son válidas.
     *   - 401 UNAUTHORIZED si las credenciales son incorrectas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Inversionista inversionista = service.buscarPorUsuario(login.getUsuario());
            String dashboardUrl = "http://localhost:8000/dashboard?usuario=" + inversionista.getUsuario();

            return ResponseEntity.ok(Map.of(
                "mensaje", "Login correcto",
                "inversionista", inversionista,
                "dashboard_url", dashboardUrl
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    /**
     * Endpoint para obtener el perfil de un inversionista por su nombre de usuario.
     * URL: GET /api/inversionistas/perfil?usuario={usuario}
     * Entrada: parámetro de consulta 'usuario'.
     * Salida:
     *   - 200 OK con datos del inversionista si existe.
     *   - 404 NOT FOUND si no se encuentra el usuario.
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@RequestParam String usuario) {
        Inversionista inversionista = service.buscarPorUsuario(usuario);
        if (inversionista != null) {
            return ResponseEntity.ok(inversionista);
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    /**
     * Endpoint para cambiar la contraseña de un inversionista.
     * URL: PUT /api/inversionistas/cambiar-contrasena
     * Entrada: JSON con campos 'usuario' y 'nuevaContrasena'.
     * Salida:
     *   - 200 OK si la contraseña fue actualizada.
     *   - 404 NOT FOUND si el usuario no existe.
     */
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(@RequestBody Map<String, String> datos) {
        String usuario = datos.get("usuario");
        String nuevaContrasena = datos.get("nuevaContrasena");

        boolean actualizado = service.actualizarContrasena(usuario, nuevaContrasena);
        if (actualizado) {
            return ResponseEntity.ok("Contraseña actualizada");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    /**
     * Endpoint para consultar el estado de cuenta de un inversionista.
     * URL: GET /api/inversionistas/estado-cuenta?usuario={usuario}
     * Entrada: parámetro de consulta 'usuario'.
     * Salida:
     *   - 200 OK con saldo y lista de movimientos si el usuario existe.
     *   - 404 NOT FOUND si el usuario no existe.
     */
    @GetMapping("/estado-cuenta")
    public ResponseEntity<?> estadoCuenta(@RequestParam String usuario) {
        Inversionista inversionista = service.buscarPorUsuario(usuario);
        if (inversionista == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        var movimientos = service.obtenerMovimientos(usuario);
        return ResponseEntity.ok(Map.of(
            "usuario", inversionista.getUsuario(),
            "saldo", inversionista.getSaldo(),
            "movimientos", movimientos
        ));
    }
}
