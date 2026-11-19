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
@Table(name = "MetodoPago")
public class MetodoPago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Metodo")
    private Integer id;
    @Column(name = "Marca")
    private String marca;
    @Column(name = "Titular")
    private String titular;
    @Column(name = "Ultimos4")
    private String ultimos4;
    @Column(name = "Vence")
    private String vence;
    @Column(name = "Predeterminado")
    private Boolean predeterminado = false;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getUltimos4() { return ultimos4; }
    public void setUltimos4(String ultimos4) { this.ultimos4 = ultimos4; }
    public String getVence() { return vence; }
    public void setVence(String vence) { this.vence = vence; }
    public Boolean getPredeterminado() { return predeterminado; }
    public void setPredeterminado(Boolean predeterminado) { this.predeterminado = predeterminado; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
