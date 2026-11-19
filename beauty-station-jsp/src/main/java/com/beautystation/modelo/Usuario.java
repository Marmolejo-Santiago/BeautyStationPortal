package com.beautystation.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Genero")
    private String genero;
    @Column(name = "Edad")
    private Integer edad;
    @Column(name = "Email")
    private String email;
    @Column(name = "Telefono")
    private String telefono;
    @Column(name = "Contrasena")
    private String contrasena;
    @Column(name = "FechaRegistro")
    private LocalDate fechaRegistro;
    @Column(name = "Frase")
    private String frase;
    @Column(name = "NotifPromos")
    private Boolean notifPromos = true;
    @Column(name = "NotifPedidos")
    private Boolean notifPedidos = true;
    @Column(name = "NotifCitas")
    private Boolean notifCitas = true;
    @Column(name = "NotifNovedades")
    private Boolean notifNovedades = false;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getFrase() { return frase; }
    public void setFrase(String frase) { this.frase = frase; }
    public Boolean getNotifPromos() { return notifPromos; }
    public void setNotifPromos(Boolean v) { this.notifPromos = v; }
    public Boolean getNotifPedidos() { return notifPedidos; }
    public void setNotifPedidos(Boolean v) { this.notifPedidos = v; }
    public Boolean getNotifCitas() { return notifCitas; }
    public void setNotifCitas(Boolean v) { this.notifCitas = v; }
    public Boolean getNotifNovedades() { return notifNovedades; }
    public void setNotifNovedades(Boolean v) { this.notifNovedades = v; }

    public int getAnioRegistro() {
        return (fechaRegistro != null ? fechaRegistro : LocalDate.now()).getYear();
    }
}
