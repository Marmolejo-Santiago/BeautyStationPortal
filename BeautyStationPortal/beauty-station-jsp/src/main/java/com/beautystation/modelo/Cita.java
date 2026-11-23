package com.beautystation.modelo;

import com.beautystation.util.Fmt;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "Citas")
public class Cita implements Serializable {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Cita")
    private Integer id;
    @Column(name = "Fecha")
    private LocalDateTime fecha;
    @Column(name = "Especificacion")
    private String especificacion;
    @Column(name = "Costo")
    private BigDecimal costo;
    @Column(name = "Duracion")
    private Integer duracion;
    @Column(name = "Servicio")
    private String servicio;
    @Column(name = "Area")
    private String area;
    @Column(name = "Cliente")
    private String cliente;
    @Column(name = "Estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<CitaServicio> servicios = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEspecificacion() { return especificacion; }
    public void setEspecificacion(String especificacion) { this.especificacion = especificacion; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public Integer getDuracion() { return duracion == null ? 0 : duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<CitaServicio> getServicios() { return servicios; }
    public void setServicios(List<CitaServicio> servicios) { this.servicios = servicios; }

    public String getEmoji() {
        return servicios.isEmpty() ? "✨" : servicios.get(0).getServicio().getEmoji();
    }

    public String getImagen() {
        return servicios.isEmpty() ? null : servicios.get(0).getServicio().getImagen();
    }

    public String getTipoBadge() {
        switch (estado) {
            case "Confirmada": return "ok";
            case "Pendiente":  return "wait";
            default:           return "off";
        }
    }

    public LocalDate getFechaDia()  { return fecha.toLocalDate(); }
    public String getFechaIso()     { return fecha.toLocalDate().toString(); }
    public String getHora()         { return fecha.format(HORA); }
    public String getFechaLarga()   { return Fmt.fechaLarga(fecha.toLocalDate()); }
    public String getDiaSemana()    { return Fmt.diaSemana(fecha.toLocalDate()); }
    public int getDiaNumero()       { return fecha.getDayOfMonth(); }
    public String getMesCorto()     { return Fmt.mesCorto(fecha.toLocalDate()); }
    public int getAnio()            { return fecha.getYear(); }
}
