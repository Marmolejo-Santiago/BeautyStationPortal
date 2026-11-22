package com.beautystation.modelo;

import com.beautystation.util.Fmt;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "Pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Pedido")
    private Integer idPedido;
    @Column(name = "Fecha")
    private LocalDate fechaPedido;
    @Column(name = "Estado")
    private String estado;
    @Column(name = "Total")
    private BigDecimal total;
    @Column(name = "FechaEntrega")
    private LocalDate fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "Id_Direccion")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "Id_Metodo")
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }


    public String getCodigo() { return String.format("BS-%06d", idPedido); }

    public String getFecha() { return Fmt.fechaCorta(fechaPedido); }

    public String getEta() { return fechaEntrega == null ? null : Fmt.fechaCorta(fechaEntrega); }

    public List<Producto> getItems() {
        List<Producto> l = new ArrayList<>();
        for (DetallePedido d : detalles) l.add(d.getProducto());
        return l;
    }

    public int getCantidad() {
        int n = 0;
        for (DetallePedido d : detalles) n += d.getCantidad();
        return n;
    }

    public String getTipoBadge() {
        switch (estado) {
            case "En camino": return "way";
            case "Entregado": return "ok";
            default:          return "cancel";
        }
    }
}
