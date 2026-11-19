package com.beautystation.servlet;

import com.beautystation.dao.CarritoDAO;
import com.beautystation.dao.CategoriaDAO;
import com.beautystation.dao.CitaDAO;
import com.beautystation.dao.DireccionDAO;
import com.beautystation.dao.MetodoPagoDAO;
import com.beautystation.dao.NotificacionDAO;
import com.beautystation.dao.PedidoDAO;
import com.beautystation.dao.ProductoDAO;
import com.beautystation.dao.ResenaDAO;
import com.beautystation.dao.ServicioDAO;
import com.beautystation.dao.UsuarioDAO;
import com.beautystation.filtro.AuthFilter;
import com.beautystation.impl.CarritoDAOImpl;
import com.beautystation.impl.CategoriaDAOImpl;
import com.beautystation.impl.CitaDAOImpl;
import com.beautystation.impl.DireccionDAOImpl;
import com.beautystation.impl.MetodoPagoDAOImpl;
import com.beautystation.impl.NotificacionDAOImpl;
import com.beautystation.impl.PedidoDAOImpl;
import com.beautystation.impl.ProductoDAOImpl;
import com.beautystation.impl.ResenaDAOImpl;
import com.beautystation.impl.ServicioDAOImpl;
import com.beautystation.impl.UsuarioDAOImpl;
import com.beautystation.modelo.CarritoItem;
import com.beautystation.modelo.Categoria;
import com.beautystation.modelo.EstadoApp;
import com.beautystation.modelo.Servicio;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class PaginaServlet extends HttpServlet {

    protected static final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    protected static final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    protected static final ProductoDAO productoDAO = new ProductoDAOImpl();
    protected static final ServicioDAO servicioDAO = new ServicioDAOImpl();
    protected static final CitaDAO citaDAO = new CitaDAOImpl();
    protected static final DireccionDAO direccionDAO = new DireccionDAOImpl();
    protected static final MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAOImpl();
    protected static final CarritoDAO carritoDAO = new CarritoDAOImpl();
    protected static final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    protected static final NotificacionDAO notificacionDAO = new NotificacionDAOImpl();
    protected static final ResenaDAO resenaDAO = new ResenaDAOImpl();

    protected EstadoApp estado(HttpServletRequest req) {
        return EstadoApp.de(req.getSession());
    }

    protected int usuarioId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute(AuthFilter.SESION);
    }

    protected void iniciarSesion(HttpServletRequest req, int idUsuario) {
        HttpSession anterior = req.getSession(false);
        if (anterior != null) anterior.invalidate();
        HttpSession sesion = req.getSession(true);
        sesion.setAttribute(AuthFilter.SESION, idUsuario);
        EstadoApp.de(sesion);
    }

    protected void mostrar(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        String consulta = req.getQueryString();
        req.setAttribute("volver", req.getRequestURI() + (consulta == null ? "" : "?" + consulta));
        cargarEncabezado(req);
        req.getRequestDispatcher("/WEB-INF/jsp/" + vista + ".jsp").forward(req, resp);
    }

    private void cargarEncabezado(HttpServletRequest req) {
        int uid = usuarioId(req);
        List<CarritoItem> items = carritoDAO.listarPorUsuario(uid);
        int cantidad = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem i : items) {
            cantidad += i.getCantidad();
            total = total.add(i.getSubtotal());
        }
        req.setAttribute("carritoItems", items);
        req.setAttribute("carritoCantidad", cantidad);
        req.setAttribute("carritoTotal", total);
        req.setAttribute("notificaciones", notificacionDAO.recientes(uid, 15));
        req.setAttribute("noLeidas", notificacionDAO.contarNoLeidas(uid));
        req.setAttribute("direcciones", direccionDAO.listarPorUsuario(uid));
        req.setAttribute("metodosPago", metodoPagoDAO.listarPorUsuario(uid));
    }

    protected void irA(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    protected void aviso(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("aviso", mensaje);
    }

    protected void volver(HttpServletRequest req, HttpServletResponse resp, String ancla) throws IOException {
        String ctx = req.getContextPath();
        String url = req.getParameter("volver");
        boolean valida = url != null && url.startsWith(ctx + "/") && !url.startsWith(ctx + "//")
                && url.indexOf('\\') < 0 && url.indexOf('\r') < 0 && url.indexOf('\n') < 0;
        if (!valida) url = ctx + "/inicio";
        if (ancla != null && ancla.matches("[A-Za-z0-9-]+")) url += "#" + ancla;
        resp.sendRedirect(url);
    }

    protected static Integer entero(String texto) {
        if (texto == null) return null;
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected static String texto(HttpServletRequest req, String nombre) {
        String v = req.getParameter(nombre);
        return v == null ? "" : v.trim();
    }

    @SuppressWarnings("unchecked")
    protected boolean categoriaValida(String nombre) {
        for (Categoria c : (List<Categoria>) getServletContext().getAttribute("categorias")) {
            if (c.getNombre().equals(nombre)) return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected Servicio servicio(int id) {
        for (Servicio s : (List<Servicio>) getServletContext().getAttribute("servicios")) {
            if (s.getId() == id) return s;
        }
        return null;
    }
}
