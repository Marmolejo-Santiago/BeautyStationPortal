package com.beautystation.modelo;

import com.beautystation.util.Fmt;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Resena")
public class Resena implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Resena")
    private Integer id;
    @Column(name = "Calificacion")
    private Integer calificacion;
    @Column(name = "Comentario")
    private String comentario;
    @Column(name = "Fecha")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "Id_Prod")
    private Producto producto;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getFechaTexto() { return Fmt.fechaCorta(fecha.toLocalDate()); }
}
