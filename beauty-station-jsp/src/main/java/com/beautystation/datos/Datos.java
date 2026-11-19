package com.beautystation.datos;

import com.beautystation.modelo.Ruta;
import java.util.List;

public final class Datos {

    public static final String DIRECCION = "Av. Sullivan, Cuauhtemoc, CDMX.";

    public static final List<String> HORARIOS = List.of(
            "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00");

    public static final List<Ruta> RUTAS = List.of(
            new Ruta("inicio", "Inicio", "home", "Buscar productos, categorías...", false),
            new Ruta("citas", "Agendar Citas", "calendar", "Buscar servicios, fechas, o sucursales...", false),
            new Ruta("catalogo", "Catálogo", "tag", "Buscar productos, categorías...", false),
            new Ruta("acerca", "Acerca de", "info", "Buscar en nuestra tienda...", false),
            new Ruta("promociones", "Promociones", "percent", "Buscar promociones, productos, categorías...", false),
            new Ruta("miscitas", "Mis citas", "calendar", "Buscar en mis citas...", false),
            new Ruta("pedidos", "Mis ordenes", "box", "Buscar en mis pedidos...", false),
            new Ruta("resenas", "Reseñas", "star", "Buscar en las reseñas...", false),
            new Ruta("usuario", "Usuario", "user", "Buscar en mi cuenta...", true));

    private Datos() { }
}
