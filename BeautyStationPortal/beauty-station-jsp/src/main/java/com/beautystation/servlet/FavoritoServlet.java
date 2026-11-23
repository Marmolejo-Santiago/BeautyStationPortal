package com.beautystation.servlet;

import com.beautystation.modelo.EstadoApp;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/favorito")
public class FavoritoServlet extends PaginaServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EstadoApp e = estado(req);
        String ancla = null;
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if (productoDAO.buscarPorId(id) != null) {
                if (!e.getFavoritos().remove(id)) e.getFavoritos().add(id);
                ancla = "p-" + id;
            }
        } catch (NumberFormatException ignorada) {
        }
        volver(req, resp, ancla);
    }
}
