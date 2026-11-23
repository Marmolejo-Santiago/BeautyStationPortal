package com.beautystation.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/aviso")
public class AvisoServlet extends PaginaServlet {

    private static final Map<String, String> MENSAJES = Map.of(
            "pronto", "Esta función estará disponible pronto",
            "solicitud", "Solicitud enviada");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String mensaje = MENSAJES.get(req.getParameter("msg"));
        if (mensaje != null) aviso(req, mensaje);
        volver(req, resp, null);
    }
}
