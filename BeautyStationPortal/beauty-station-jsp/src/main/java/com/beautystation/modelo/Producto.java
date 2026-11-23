package com.beautystation.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Producto")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Prod")
    private Integer id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Stock")
    private Integer stock;
    @Column(name = "Precio")
    private BigDecimal precio;
    @Column(name = "Emoji")
    private String emoji;
    @Column(name = "Etiqueta")
    private String etiqueta;
    @Column(name = "Descuento")
    private Integer descuento;
    @Column(name = "Rating")
    private Double rating;
    @Column(name = "NumResenas")
    private Integer resenas;
    @Column(name = "Imagen")
    private String imagen;
    @Column(name = "ImagenPromo")
    private String imagenPromo;
    @Column(name = "Activo")
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "Id_Categoria")
    private Categoria categoria;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public Integer getDescuento() { return descuento == null ? 0 : descuento; }
    public void setDescuento(Integer descuento) { this.descuento = descuento; }
    public Double getRating() { return rating == null ? 0 : rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getResenas() { return resenas == null ? 0 : resenas; }
    public void setResenas(Integer resenas) { this.resenas = resenas; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /** Ruta relativa de la foto (img/... o media/...); vacía si el producto aún no tiene foto. */
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    /** Foto de la tarjeta de promociones; si no hay una propia se usa la normal. */
    public String getImagenPromo() { return imagenPromo == null || imagenPromo.isEmpty() ? imagen : imagenPromo; }
    public void setImagenPromo(String imagenPromo) { this.imagenPromo = imagenPromo; }
    /** false = eliminado del catálogo (se conserva para no romper pedidos y reseñas anteriores). */
    public boolean isActivo() { return activo == null || activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public BigDecimal getPrecioFinal() {
        BigDecimal factor = BigDecimal.valueOf(100 - getDescuento()).divide(BigDecimal.valueOf(100));
        return precio.multiply(factor).setScale(0, RoundingMode.HALF_UP);
    }
}
