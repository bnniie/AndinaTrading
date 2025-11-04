package co.edu.unbosque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.model.entity.Inversionista;
import co.edu.unbosque.service.InversionistaService;
import co.edu.unbosque.model.DTO.LoginDTO;
import co.edu.unbosque.model.DTO.ContratoDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con inversionistas.
 * Incluye endpoints para registro, autenticación, perfil, gestión de órdenes, contrato y datos de contacto.
 */
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/inversionistas")
public class InversionistaController {

    @Autowired
    private InversionistaService service;

    /**
     * Registra un nuevo inversionista en el sistema.
     * @param datos mapa con los campos requeridos: usuario, contraseña, nombre, apellido, correo, teléfono, documentoIdentidad, ciudad.
     * @return 200 OK si el registro fue exitoso, 400 BAD REQUEST si hay datos inválidos o duplicados.
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
        return exito
            ? ResponseEntity.ok("Registro exitoso")
            : ResponseEntity.badRequest().body("Error: ciudad no válida o datos duplicados");
    }

    /**
     * Autentica a un inversionista y guarda su sesión.
     * @param login DTO con usuario y contraseña.
     * @param session sesión HTTP para persistencia temporal.
     * @return 200 OK con URL del dashboard si es exitoso, 401 UNAUTHORIZED si falla.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login, HttpSession session) {
        boolean valido = service.validarCredenciales(login.getUsuario(), login.getContrasena());
        if (valido) {
            Inversionista inversionista = service.buscarPorUsuario(login.getUsuario());
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
     * Obtiene el perfil del inversionista autenticado.
     * @param session sesión HTTP con el usuario activo.
     * @return 200 OK con datos del perfil, 401 si no hay sesión, 404 si el usuario no existe.
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) return ResponseEntity.status(401).body("No hay sesión activa");

        Inversionista inversionista = service.buscarPorUsuario(usuario);
        if (inversionista == null) return ResponseEntity.status(404).body("Usuario no encontrado");

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
     * Actualiza la contraseña de un inversionista.
     * @param datos mapa con 'usuario' y 'nuevaContrasena'.
     * @return 200 OK si se actualiza, 404 si el usuario no existe.
     */
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(@RequestBody Map<String, String> datos) {
        boolean actualizado = service.actualizarContrasena(datos.get("usuario"), datos.get("nuevaContrasena"));
        return actualizado
            ? ResponseEntity.ok("Contraseña actualizada")
            : ResponseEntity.status(404).body("Usuario no encontrado");
    }

    /**
     * Cierra la sesión actual del inversionista.
     * @param session sesión HTTP activa.
     * @return 200 OK al cerrar sesión.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Sesión cerrada");
    }

    /**
     * Obtiene estadísticas de órdenes por estado para el inversionista autenticado.
     * @param session sesión HTTP con el usuario activo.
     * @return 200 OK con mapa de conteo por estado, 401 si no hay sesión.
     */
    @GetMapping("/ordenes/estadisticas")
    public ResponseEntity<?> estadisticasOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) return ResponseEntity.status(401).body("No hay sesión activa");

        Map<String, Long> conteo = service.contarOrdenesPorEstado(usuario);
        return ResponseEntity.ok(conteo);
    }

    /**
     * Obtiene los movimientos de órdenes del inversionista autenticado.
     * @param session sesión HTTP con el usuario activo.
     * @return 200 OK con lista de movimientos, 401 si no hay sesión.
     */
    @GetMapping("/ordenes/movimientos")
    public ResponseEntity<?> movimientosOrdenes(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) return ResponseEntity.status(401).body("No hay sesión activa");

        List<Map<String, Object>> movimientos = service.obtenerMovimientosOrdenes(usuario);
        return ResponseEntity.ok(movimientos);
    }

    /**
     * Actualiza el usuario y teléfono del inversionista autenticado.
     * @param datos mapa con nuevos valores.
     * @param session sesión HTTP con el usuario actual.
     * @return 200 OK si se actualiza, 400 si falla, 401 si no hay sesión.
     */
    @PutMapping("/actualizar-contacto")
    public ResponseEntity<?> actualizarContacto(@RequestBody Map<String, String> datos, HttpSession session) {
        String usuarioActual = (String) session.getAttribute("usuario");
        if (usuarioActual == null) return ResponseEntity.status(401).body("No hay sesión activa");

        boolean actualizado = service.actualizarUsuarioYTelefono(usuarioActual, datos.get("usuario"), datos.get("telefono"));
        if (actualizado) {
            session.setAttribute("usuario", datos.get("usuario"));
            return ResponseEntity.ok("Datos actualizados correctamente");
        } else {
            return ResponseEntity.status(400).body("No se pudo actualizar los datos");
        }
    }

    /**
     * Actualiza el saldo del inversionista autenticado.
     * @param datos mapa con nuevo saldo.
     * @param session sesión HTTP con el usuario activo.
     * @return 200 OK si se actualiza, 400 si falla, 401 si no hay sesión.
     */
    @PutMapping("/actualizar-saldo")
    public ResponseEntity<?> actualizarSaldo(@RequestBody Map<String, Object> datos, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) return ResponseEntity.status(401).body("No hay sesión activa");

        Double nuevoSaldo = Double.valueOf(datos.get("saldo").toString());
        boolean actualizado = service.actualizarSaldo(usuario, nuevoSaldo);
        return actualizado
            ? ResponseEntity.ok("Saldo actualizado correctamente")
            : ResponseEntity.status(400).body("No se pudo actualizar el saldo");
    }

    /**
     * Edita el contrato del inversionista autenticado.
     * @param datos DTO con porcentaje y duración del contrato.
     * @param session sesión HTTP con el usuario activo.
     * @return 200 OK si se actualiza, 404 si falla, 401 si no hay sesión.
     */
    @PutMapping("/editar-contrato")
    public ResponseEntity<?> editarContrato(@RequestBody ContratoDTO datos, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        boolean actualizado = service.actualizarContrato(usuario, datos.getPorcentaje(), datos.getDuracion());
        return actualizado
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }
}