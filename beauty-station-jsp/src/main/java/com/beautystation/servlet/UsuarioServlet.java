package com.beautystation.servlet;

import com.beautystation.modelo.Cita;
import com.beautystation.modelo.Direccion;
import com.beautystation.modelo.MetodoPago;
import com.beautystation.modelo.Pedido;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.Validar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@WebServlet("/usuario")
public class UsuarioServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int uid = usuarioId(req);
        LocalDate hoy = LocalDate.now();
        int proximas = 0;
        for (Cita c : citaDAO.listarPorUsuario(uid)) {
            if (!c.getEstado().equals("Cancelada") && !c.getFechaDia().isBefore(hoy)) proximas++;
        }
        int enCamino = 0;
        for (Pedido p : pedidoDAO.listarPorUsuario(uid)) {
            if (p.getEstado().equals("En camino")) enCamino++;
        }
        req.setAttribute("usuario", usuarioDAO.buscarPorId(uid));
        req.setAttribute("generos", Validar.GENEROS);
        req.setAttribute("proximas", proximas);
        req.setAttribute("enCamino", enCamino);
        req.setAttribute("anioActual", hoy.getYear());
        mostrar(req, resp, "usuario");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int uid = usuarioId(req);
        String accion = texto(req, "accion");
        String resultado;
        try {
            switch (accion) {
                case "perfil":       resultado = editarPerfil(req, uid); break;
                case "datos":        resultado = editarDatos(req, uid); break;
                case "preferencias": resultado = guardarPreferencias(req, uid); break;
                case "direccion":    resultado = agregarDireccion(req, uid); break;
                case "pago":         resultado = agregarPago(req, uid); break;
                default:             resultado = null;
            }
        } catch (RuntimeException e) {
            getServletContext().log("No se pudo guardar (" + accion + ")", e);
            resultado = "No se pudo guardar, intenta de nuevo";
        }
        if (resultado != null) aviso(req, resultado);
        irA(req, resp, "/usuario");
    }


    private String editarPerfil(HttpServletRequest req, int uid) {
        String nombre = texto(req, "nombre");
        String frase = texto(req, "frase");
        String genero = texto(req, "genero");
        Integer edad = entero(req.getParameter("edad"));
        if (!Validar.nombre(nombre)) return "Escribe tu nombre";
        if (frase.length() > 150) return "La frase es muy larga (máximo 150 caracteres)";
        if (!Validar.GENEROS.contains(genero)) return "Elige una opción de género";
        if (edad == null || !Validar.edad(edad)) return "Escribe una edad válida";

        Usuario u = usuarioDAO.buscarPorId(uid);
        u.setNombre(nombre);
        u.setFrase(frase.isEmpty() ? "Siempre hermosa, siempre tú ♡" : frase);
        u.setGenero(genero);
        u.setEdad(edad);
        usuarioDAO.actualizar(u);
        return "Perfil actualizado ✨";
    }

    private String editarDatos(HttpServletRequest req, int uid) {
        String nombre = texto(req, "nombre");
        String correo = texto(req, "correo").toLowerCase();
        String telefono = texto(req, "telefono");
        if (!Validar.nombre(nombre)) return "Escribe tu nombre completo";
        if (!Validar.email(correo)) return "Escribe un correo válido";
        if (!Validar.telefono(telefono)) return "Escribe un teléfono válido";

        Usuario otro = usuarioDAO.buscarPorEmail(correo);
        if (otro != null && otro.getId() != uid) return "Ese correo ya está en uso";

        Usuario u = usuarioDAO.buscarPorId(uid);
        u.setNombre(nombre);
        u.setEmail(correo);
        u.setTelefono(telefono.isEmpty() ? null : telefono);
        usuarioDAO.actualizar(u);
        return "Datos actualizados ✨";
    }

    private String guardarPreferencias(HttpServletRequest req, int uid) {
        Usuario u = usuarioDAO.buscarPorId(uid);
        u.setNotifPromos(req.getParameter("promos") != null);
        u.setNotifPedidos(req.getParameter("pedidos") != null);
        u.setNotifCitas(req.getParameter("citas") != null);
        u.setNotifNovedades(req.getParameter("novedades") != null);
        usuarioDAO.actualizar(u);
        return "Preferencias guardadas";
    }

    private String agregarDireccion(HttpServletRequest req, int uid) {
        String etiqueta = texto(req, "etiqueta");
        String calle = texto(req, "calle");
        String colonia = texto(req, "colonia");
        String ciudad = texto(req, "ciudad");
        String estado = texto(req, "estado");
        String cp = texto(req, "cp");
        if (calle.isEmpty() || calle.length() > 150) return "Escribe la calle y el número";
        if (colonia.length() > 100) return "La colonia es muy larga";
        if (ciudad.isEmpty() || ciudad.length() > 100) return "Escribe la ciudad";
        if (estado.isEmpty() || estado.length() > 50) return "Escribe el estado";
        if (!Validar.codigoPostal(cp)) return "El código postal debe tener 5 dígitos";

        Direccion d = new Direccion();
        d.setEtiqueta(etiqueta.isEmpty() ? "Casa" : etiqueta.substring(0, Math.min(50, etiqueta.length())));
        d.setCalle(calle);
        d.setColonia(colonia.isEmpty() ? null : colonia);
        d.setCiudad(ciudad);
        d.setEstado(estado);
        d.setCodigoPostal(cp);
        direccionDAO.guardar(uid, d, req.getParameter("predeterminada") != null);
        return "Dirección agregada 📍";
    }

    private String agregarPago(HttpServletRequest req, int uid) {
        String titular = texto(req, "titular");
        String numero = texto(req, "numero").replaceAll("[\\s-]", "");
        Integer mes = entero(req.getParameter("mes"));
        Integer anio = entero(req.getParameter("anio"));
        if (!Validar.nombre(titular)) return "Escribe el nombre del titular";
        if (!Validar.tarjeta(numero)) return "El número de tarjeta no es válido";
        if (mes == null || anio == null || mes < 1 || mes > 12) return "Elige el vencimiento";
        if (YearMonth.of(anio, mes).isBefore(YearMonth.now())) return "La tarjeta está vencida";

        MetodoPago m = new MetodoPago();
        m.setMarca(Validar.marcaTarjeta(numero));
        m.setTitular(titular);
        m.setUltimos4(numero.substring(numero.length() - 4));
        m.setVence(String.format("%02d/%02d", mes, anio % 100));
        metodoPagoDAO.guardar(uid, m, req.getParameter("predeterminado") != null);
        return "Método de pago agregado 💳";
    }
}
