package com.beautystation.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Base de las pantallas del panel de administración (solo accesibles con la cuenta ADMIN, ver AuthFilter). */
public abstract class AdminServlet extends PaginaServlet {

    protected void mostrarAdmin(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/admin/" + vista + ".jsp").forward(req, resp);
    }
}
