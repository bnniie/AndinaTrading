package co.edu.unbosque.model.DTO;

/**
 * Data Transfer Object (DTO) para representar los datos de un contrato.
 * Utilizado en operaciones de edición o actualización de contrato por parte del inversionista.
 */
public class ContratoDTO {

    /**
     * Porcentaje de comisión o rentabilidad asociado al contrato.
     * Este valor puede representar el retorno esperado o la participación del inversionista.
     */
    private Double porcentaje;

    /**
     * Duración del contrato en días.
     * Define el tiempo de vigencia del acuerdo entre el inversionista y la plataforma.
     */
    private Integer duracion;

    // Getters y setters

    /**
     * Obtiene el porcentaje del contrato.
     * @return valor decimal del porcentaje.
     */
    public Double getPorcentaje() {
        return porcentaje;
    }

    /**
     * Establece el porcentaje del contrato.
     * @param porcentaje valor decimal a asignar.
     */
    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    /**
     * Obtiene la duración del contrato en días.
     * @return número entero que representa la duración.
     */
    public Integer getDuracion() {
        return duracion;
    }

    /**
     * Establece la duración del contrato.
     * @param duracion número entero en días.
     */
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}