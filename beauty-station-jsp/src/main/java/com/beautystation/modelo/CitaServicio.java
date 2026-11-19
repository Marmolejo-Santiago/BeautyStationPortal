package com.beautystation.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Cita_Servicio")
public class CitaServicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Cita_Serv")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Id_Cita")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "Id_Serv")
    private Servicio servicio;

    public CitaServicio() { }

    public CitaServicio(Cita cita, Servicio servicio) {
        this.cita = cita;
        this.servicio = servicio;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}
