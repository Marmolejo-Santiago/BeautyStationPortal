package com.beautystation.modelo;

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
@Table(name = "Notificacion")
public class Notificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Notif")
    private Integer id;
    @Column(name = "Tipo")
    private String tipo;
    @Column(name = "Titulo")
    private String titulo;
    @Column(name = "Mensaje")
    private String mensaje;
    @Column(name = "Fecha")
    private LocalDateTime fecha;
    @Column(name = "Leida")
    private Boolean leida = false;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario")
    private Usuario usuario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Boolean getLeida() { return leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getEmoji() {
        switch (tipo) {
            case "promo":  return "🏷️";
            case "pedido": return "📦";
            case "cita":   return "📅";
            default:       return "💄";
        }
    }

    /** 17 nov. 2026 · 10:30 */
    public String getFechaTexto() {
        return com.beautystation.util.Fmt.fechaCorta(fecha.toLocalDate()) + " · "
                + String.format("%02d:%02d", fecha.getHour(), fecha.getMinute());
    }
}
