package com.beautystation.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Servicio")
public class Servicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Serv")
    private Integer id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Emoji")
    private String emoji;
    @Column(name = "Costo")
    private BigDecimal costo;
    @Column(name = "EspecificacionServ")
    private String especificacion;
    @Column(name = "DurAprox")
    private Integer min;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public String getEspecificacion() { return especificacion; }
    public void setEspecificacion(String especificacion) { this.especificacion = especificacion; }
    public Integer getMin() { return min == null ? 0 : min; }
    public void setMin(Integer min) { this.min = min; }

    public int getPrecio() { return costo == null ? 0 : costo.intValue(); }
}
