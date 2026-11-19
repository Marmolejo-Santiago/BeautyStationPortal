package com.beautystation.modelo;

import java.util.LinkedHashSet;
import java.util.Set;

public class FormCita {
    private String fecha;
    private final Set<Integer> servicios = new LinkedHashSet<>();
    private String hora;
    private String nota = "";

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public Set<Integer> getServicios() { return servicios; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
