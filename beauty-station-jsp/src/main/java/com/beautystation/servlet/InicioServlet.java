package com.beautystation.servlet;

import com.beautystation.modelo.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/inicio")
public class InicioServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Integer> ids = List.of(9, 6, 8, 10, 11, 12);
        List<Producto> encontrados = productoDAO.buscarPorIds(ids);
        List<Producto> destacados = new ArrayList<>();
        for (int id : ids) {
            for (Producto p : encontrados) {
                if (p.getId() == id) destacados.add(p);
            }
        }
        req.setAttribute("destacados", destacados);
        req.setAttribute("categoriasInicio", List.of(
                new String[]{"Ropa", "👗"}, new String[]{"Calzado", "👟"}, new String[]{"Accesorios", "🕶️"},
                new String[]{"Belleza", "💄"}, new String[]{"Nuevos ingresos", "👛"}));
        mostrar(req, resp, "inicio");
    }
}
