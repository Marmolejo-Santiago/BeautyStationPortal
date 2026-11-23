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
    public static final String ROL = "rol";          // "ADMIN" o "CLIENTE"

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");

        String ruta = req.getServletPath();
        boolean estatica = ruta.startsWith("/css/") || ruta.startsWith("/js/") || ruta.startsWith("/img/") || ruta.equals("/media");
        boolean publica = estatica || ruta.equals("/") || ruta.equals("/index.jsp") || ruta.equals("/login")
                || ruta.equals("/registro");

        HttpSession sesion = req.getSession(false);
        boolean autenticado = sesion != null && sesion.getAttribute(SESION) != null;
        boolean admin = autenticado && "ADMIN".equals(sesion.getAttribute(ROL));
        boolean zonaAdmin = ruta.equals("/admin") || ruta.startsWith("/admin/");

        if (estatica) {
            chain.doFilter(request, response);
        } else if (!autenticado) {
            if (publica) chain.doFilter(request, response);
            else resp.sendRedirect(req.getContextPath() + "/login");
        } else if (zonaAdmin && !admin) {
            resp.sendRedirect(req.getContextPath() + "/inicio");          // el panel es solo para administración
        } else if (admin && !zonaAdmin && !ruta.equals("/logout") && !ruta.equals("/login")) {
            resp.sendRedirect(req.getContextPath() + "/admin/citas");     // la cuenta admin trabaja solo en su panel
        } else {
            chain.doFilter(request, response);
        }
    }
}
