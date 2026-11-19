package com.beautystation.filtro;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public static final String SESION = "usuario";   // guarda el id (Integer) de la persona

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");

        String ruta = req.getServletPath();
        boolean publica = ruta.equals("/") || ruta.equals("/index.jsp") || ruta.equals("/login") || ruta.equals("/registro")
                || ruta.startsWith("/css/") || ruta.startsWith("/img/");

        HttpSession sesion = req.getSession(false);
        boolean autenticado = sesion != null && sesion.getAttribute(SESION) != null;

        if (publica || autenticado) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
