package co.edu.unbosque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.service.InversionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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
    public ResponseEntity<?> login(@RequestBody LoginDTO login, HttpSession session) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Inversionista inversionista = service.buscarPorUsuario(login.getUsuario());

            // Guardar el usuario en la sesión
            session.setAttribute("usuario", inversionista.getUsuario());

            String dashboardUrl = "http://localhost:8000/dashboard?usuario=" + inversionista.getUsuario();

            return ResponseEntity.ok(Map.of(
                "mensaje", "Login correcto",
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
    public ResponseEntity<?> obtenerPerfil(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        Inversionista inversionista = service.buscarPorUsuario(usuario);
        if (inversionista == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        Map<String, Object> perfil = Map.of(
            "usuario", inversionista.getUsuario(),
            "nombre", inversionista.getNombre(),
            "apellido", inversionista.getApellido(),
            "correo", inversionista.getCorreo(),
            "telefono", inversionista.getTelefono(),
            "saldo", inversionista.getSaldo(),
            "ciudad", inversionista.getCiudad() != null ? inversionista.getCiudad().getNombre() : "Sin ciudad",
            "pais", inversionista.getPais() != null ? inversionista.getPais().getNombre() : "Sin país"
        );

        return ResponseEntity.ok(perfil);
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Sesión cerrada");
    }

    @GetMapping("/ordenes/estadisticas")
    public ResponseEntity<?> estadisticasOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        System.out.println("----------------- "+usuario);
        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        Map<String, Long> conteo = service.contarOrdenesPorEstado(usuario);
        return ResponseEntity.ok(conteo); 
    }

    @GetMapping("/ordenes/movimientos")
    public ResponseEntity<?> movimientosOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        List<Map<String, Object>> movimientos = service.obtenerMovimientosOrdenes(usuario);
        return ResponseEntity.ok(movimientos);
    }

    @PutMapping("/actualizar-contacto")
    public ResponseEntity<?> actualizarContacto(@RequestBody Map<String, String> datos, HttpSession session) {
        String usuarioActual = (String) session.getAttribute("usuario");
        if (usuarioActual == null) {
            return ResponseEntity.status(401).body("No hay sesión activa");
        }

        String nuevoUsuario = datos.get("usuario");
        String nuevoTelefono = datos.get("telefono");

        boolean actualizado = service.actualizarUsuarioYTelefono(usuarioActual, nuevoUsuario, nuevoTelefono);
        if (actualizado) {
            session.setAttribute("usuario", nuevoUsuario); // actualiza la sesión si el usuario cambió
            return ResponseEntity.ok("Datos actualizados correctamente");
        } else {
            return ResponseEntity.status(400).body("No se pudo actualizar los datos");
        }
    }
}
