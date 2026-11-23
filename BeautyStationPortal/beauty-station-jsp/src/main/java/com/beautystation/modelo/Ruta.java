package com.beautystation.modelo;

public class Ruta {
    private final String clave;
    private final String etiqueta;
    private final String icono;
    private final String placeholder;
    private final boolean oculta;

    public Ruta(String clave, String etiqueta, String icono, String placeholder, boolean oculta) {
        this.clave = clave;
        this.etiqueta = etiqueta;
        this.icono = icono;
        this.placeholder = placeholder;
        this.oculta = oculta;
    }

    public String getClave() { return clave; }
    public String getEtiqueta() { return etiqueta; }
    public String getIcono() { return icono; }
    public String getPlaceholder() { return placeholder; }
    public boolean isOculta() { return oculta; }
}
