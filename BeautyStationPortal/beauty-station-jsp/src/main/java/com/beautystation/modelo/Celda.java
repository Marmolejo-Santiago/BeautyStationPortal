package com.beautystation.modelo;

public class Celda {
    private final boolean fuera;
    private final int numero;
    private final String clave;        // yyyy-MM-dd
    private final String clases;       // clases CSS del botón
    private final boolean deshabilitado;

    public Celda(boolean fuera, int numero, String clave, String clases, boolean deshabilitado) {
        this.fuera = fuera;
        this.numero = numero;
        this.clave = clave;
        this.clases = clases;
        this.deshabilitado = deshabilitado;
    }

    public boolean isFuera() { return fuera; }
    public int getNumero() { return numero; }
    public String getClave() { return clave; }
    public String getClases() { return clases; }
    public boolean isDeshabilitado() { return deshabilitado; }
}
