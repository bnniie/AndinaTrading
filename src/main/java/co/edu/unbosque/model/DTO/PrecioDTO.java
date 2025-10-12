package co.edu.unbosque.model.DTO;

public class PrecioDTO {
    private String symbol;
    private Double precio_actual;
    private Double alto_del_dia;
    private Double bajo_del_dia;
    private Double apertura;
    private Double precio_previo;
    private String fuente;

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Double getPrecio_actual() { return precio_actual; }
    public void setPrecio_actual(Double precio_actual) { this.precio_actual = precio_actual; }

    public Double getAlto_del_dia() { return alto_del_dia; }
    public void setAlto_del_dia(Double alto_del_dia) { this.alto_del_dia = alto_del_dia; }

    public Double getBajo_del_dia() { return bajo_del_dia; }
    public void setBajo_del_dia(Double bajo_del_dia) { this.bajo_del_dia = bajo_del_dia; }

    public Double getApertura() { return apertura; }
    public void setApertura(Double apertura) { this.apertura = apertura; }

    public Double getPrecio_previo() { return precio_previo; }
    public void setPrecio_previo(Double precio_previo) { this.precio_previo = precio_previo; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }
}
