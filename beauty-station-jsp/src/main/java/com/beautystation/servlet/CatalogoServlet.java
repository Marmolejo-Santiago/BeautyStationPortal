package com.beautystation.servlet;

import com.beautystation.modelo.EstadoApp;
import com.beautystation.modelo.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/catalogo")
public class CatalogoServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstadoApp e = estado(req);

        if (req.getParameter("limpiar") != null) {
            e.limpiarFiltros();
            irA(req, resp, "/catalogo");
            return;
        }

        String q = req.getParameter("q");
        if (q != null) e.setBusqueda(q.trim());

        if (req.getParameter("aplicar") != null) {
            e.getCatChecks().clear();
            String[] marcadas = req.getParameterValues("chk");
            if (marcadas != null) {
                for (String c : marcadas) {
                    if (categoriaValida(c)) e.getCatChecks().add(c);
                }
            }
            try {
                int precio = Integer.parseInt(req.getParameter("price"));
                e.setPrecioMax(Math.max(300, Math.min(2000, precio)));
            } catch (NumberFormatException ignorada) {
            }
            String orden = req.getParameter("sort");
            if ("recientes".equals(orden) || "menor".equals(orden) || "mayor".equals(orden)) e.setOrden(orden);
        }

        String cat = req.getParameter("cat");
        if (cat == null || !categoriaValida(cat)) cat = "Todas";

        List<Producto> lista = productoDAO.filtrar(e.getPrecioMax(), cat.equals("Todas") ? null : cat,
                e.getCatChecks(), e.getBusqueda(), e.getOrden());

        req.setAttribute("cat", cat);
        req.setAttribute("productos", lista);
        mostrar(req, resp, "catalogo");
    }
}
