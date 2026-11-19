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
@Table(name = "Direccion")
public class Direccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Direccion")
    private Integer id;
    @Column(name = "Etiqueta")
    private String etiqueta;
    @Column(name = "Calle")
    private String calle;
    @Column(name = "Colonia")
    private String colonia;
    @Column(name = "Ciudad")
    private String ciudad;
    @Column(name = "Estado")
    private String estado;
    @Column(name = "CodigoPostal")
    private String codigoPostal;
    @Column(name = "Predeterminada")
    private Boolean predeterminada = false;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public Boolean getPredeterminada() { return predeterminada; }
    public void setPredeterminada(Boolean predeterminada) { this.predeterminada = predeterminada; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTexto() {
        StringBuilder sb = new StringBuilder(calle);
        if (colonia != null && !colonia.isBlank()) sb.append(", ").append(colonia);
        return sb.append(", ").append(ciudad).append(", ").append(estado).append(' ').append(codigoPostal).toString();
    }
}
