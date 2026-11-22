package com.beautystation.servlet;

import com.beautystation.filtro.AuthFilter;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.Clave;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion != null && sesion.getAttribute(AuthFilter.SESION) != null) {
            irA(req, resp, "ADMIN".equals(sesion.getAttribute(AuthFilter.ROL)) ? "/admin/citas" : "/inicio");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("google") != null) {
            error(req, resp, "El inicio de sesión con Google aún no está disponible");
            return;
        }
        String correo = texto(req, "correo");
        String clave = req.getParameter("clave");
        Usuario u = correo.isEmpty() ? null : usuarioDAO.buscarPorEmail(correo);
        if (u == null || !Clave.verificar(clave, u.getContrasena())) {
            error(req, resp, "Correo o contraseña incorrectos");
            return;
        }
        iniciarSesion(req, u);
        irA(req, resp, u.isAdmin() ? "/admin/citas" : "/inicio");
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws ServletException, IOException {
        req.setAttribute("error", mensaje);
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }
}
