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
@Table(name = "DetallePedido")
public class DetallePedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Detalle")
    private Integer id;
    @Column(name = "Cantidad")
    private Integer cantidad;
    @Column(name = "PrecioUnit")
    private BigDecimal precioUnit;

    @ManyToOne
    @JoinColumn(name = "Id_Pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "Id_Prod")
    private Producto producto;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnit() { return precioUnit; }
    public void setPrecioUnit(BigDecimal precioUnit) { this.precioUnit = precioUnit; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
