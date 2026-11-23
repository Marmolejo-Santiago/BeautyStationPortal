package com.beautystation.servlet;

import com.beautystation.modelo.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/promociones")
public class PromocionesServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("limpiar") != null) {
            estado(req).limpiarFiltros();
            irA(req, resp, "/promociones");
            return;
        }
        String cat = req.getParameter("cat");
        if (cat == null || !categoriaValida(cat)) cat = "Todas";

        List<Producto> promos = productoDAO.promociones(cat.equals("Todas") ? null : cat);
        req.setAttribute("cat", cat);
        req.setAttribute("promos", promos);
        mostrar(req, resp, "promociones");
    }
}
