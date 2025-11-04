package co.edu.unbosque.model.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para actualizar el contrato del inversionista.
 * El porcentaje se guarda en la tabla 'inversionista' y la duración en la tabla 'contrato'.
 */
public class ContratoDTO {

    /**
     * Porcentaje de comisión que se asignará al inversionista.
     * Este valor se guarda directamente en la tabla 'inversionista'.
     */
    @NotNull(message = "El porcentaje de comisión no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El porcentaje debe ser mayor o igual a 0")
    private Double porcentaje;

    /**
     * Duración del contrato en meses.
     * Este valor se guarda en la tabla 'contrato'.
     */
    @NotNull(message = "La duración no puede ser nula")
    @Min(value = 1, message = "La duración debe ser al menos de 1 mes")
    private Integer duracion;

    // Constructor vacío
    public ContratoDTO() {}

    // Constructor con parámetros
    public ContratoDTO(Double porcentaje, Integer duracion) {
        this.porcentaje = porcentaje;
        this.duracion = duracion;
    }

    // Getters y setters
    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}