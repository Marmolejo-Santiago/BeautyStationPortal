package com.beautystation.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/resenas")
public class ResenasServlet extends PaginaServlet {

    private static final int MAXIMO_COMENTARIO = 500;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("resenas", resenaDAO.recientes(30));
        req.setAttribute("productos", productoDAO.todos());
        req.setAttribute("seleccionado", entero(req.getParameter("producto")));
        mostrar(req, resp, "resenas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer producto = entero(req.getParameter("producto"));
        Integer calificacion = entero(req.getParameter("calificacion"));
        String comentario = texto(req, "comentario");

        if (producto == null) {
            aviso(req, "Elige un producto");
        } else if (calificacion == null || calificacion < 1 || calificacion > 5) {
            aviso(req, "Elige una calificación de 1 a 5");
        } else if (comentario.length() > MAXIMO_COMENTARIO) {
            aviso(req, "El comentario es muy largo (máximo " + MAXIMO_COMENTARIO + " caracteres)");
        } else {
            try {
                resenaDAO.crear(usuarioId(req), producto, calificacion, comentario);
                aviso(req, "¡Gracias por tu reseña! ☆");
            } catch (IllegalStateException e) {
                aviso(req, e.getMessage());
            }
        }
        irA(req, resp, "/resenas");
    }
}
