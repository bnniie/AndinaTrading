package co.edu.unbosque.service;

import co.edu.unbosque.model.DTO.PrecioDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio encargado de consumir precios de activos financieros desde una API externa.
 * Utiliza RestTemplate para realizar solicitudes HTTP y mapear la respuesta a un DTO.
 */
@Service
public class PrecioService {

    // Cliente HTTP para realizar peticiones REST.
    private final RestTemplate restTemplate = new RestTemplate();

    // URL base del servicio externo que proporciona precios.
    private final String baseUrl = "http://localhost:8000";

    /**
     * Obtiene el precio actual de un activo financiero dado su símbolo.
     * Realiza una solicitud GET al endpoint remoto y convierte la respuesta en un objeto PrecioDTO.
     * @param symbol símbolo del activo (ej. "AAPL", "BTC", "USD").
     * @return instancia de PrecioDTO con los datos del activo.
     */
    public PrecioDTO obtenerPrecio(String symbol) {
        String url = baseUrl + "/precio/" + symbol;
        return restTemplate.getForObject(url, PrecioDTO.class);
    }
}

