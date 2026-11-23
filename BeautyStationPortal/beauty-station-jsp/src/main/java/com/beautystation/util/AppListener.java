package com.beautystation.util;

import com.beautystation.datos.Datos;
import com.beautystation.impl.CategoriaDAOImpl;
import com.beautystation.impl.ServicioDAOImpl;
import com.beautystation.modelo.Ruta;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext app = evento.getServletContext();
        Map<String, Ruta> porClave = new LinkedHashMap<>();
        for (Ruta r : Datos.RUTAS) porClave.put(r.getClave(), r);

        app.setAttribute("rutas", Datos.RUTAS);
        app.setAttribute("rutasPorClave", porClave);
        app.setAttribute("rutasAdmin", Datos.RUTAS_ADMIN);
        app.setAttribute("iconos", Iconos.todos());
        app.setAttribute("horarios", Datos.HORARIOS);
        app.setAttribute("direccion", Datos.DIRECCION);
        app.setAttribute("categorias", new CategoriaDAOImpl().listar());
        app.setAttribute("servicios", new ServicioDAOImpl().listar());
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        HibernateUtil.cerrar();
    }
}
