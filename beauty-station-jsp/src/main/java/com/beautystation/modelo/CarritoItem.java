package com.beautystation.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Carrito")
public class CarritoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Carrito")
    private Integer id;
    @Column(name = "Cantidad")
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "Id_Prod")
    private Producto producto;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public BigDecimal getSubtotal() {
        return producto.getPrecioFinal().multiply(BigDecimal.valueOf(cantidad));
    }
}
