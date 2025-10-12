package co.edu.unbosque.service;

import co.edu.unbosque.model.DTO.PrecioDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PrecioService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8000";

    public PrecioDTO obtenerPrecio(String symbol) {
        String url = baseUrl + "/precio/" + symbol;
        return restTemplate.getForObject(url, PrecioDTO.class);
    }
}

