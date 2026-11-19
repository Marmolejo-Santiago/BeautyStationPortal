package com.beautystation.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notificaciones")
public class NotificacionesServlet extends PaginaServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("leer".equals(req.getParameter("accion"))) notificacionDAO.marcarLeidas(usuarioId(req));
        volver(req, resp, "notificaciones");
    }
}
