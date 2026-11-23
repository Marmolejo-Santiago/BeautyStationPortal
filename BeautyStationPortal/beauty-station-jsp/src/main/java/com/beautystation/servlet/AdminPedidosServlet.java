package com.beautystation.servlet;

import com.beautystation.modelo.Pedido;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Administración: consulta de los pedidos de todas las clientas. */
@WebServlet("/admin/pedidos")
public class AdminPedidosServlet extends AdminServlet {

    private static final List<String[]> FILTROS = List.of(
            new String[]{"Todos", "Todos"}, new String[]{"En camino", "En camino"},
            new String[]{"Entregado", "Entregados"}, new String[]{"Cancelado", "Cancelados"});

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String estado = "Todos";
        String pedido = req.getParameter("estado");
        for (String[] f : FILTROS) {
            if (f[0].equals(pedido)) estado = pedido;
        }

        List<Pedido> todos = pedidoDAO.listarTodos(null);
        List<Pedido> lista = new ArrayList<>();
        BigDecimal vendido = BigDecimal.ZERO;
        int enCamino = 0;
        for (Pedido p : todos) {
            if (p.getEstado().equals("En camino")) enCamino++;
            if (!p.getEstado().equals("Cancelado")) vendido = vendido.add(p.getTotal());
            if (estado.equals("Todos") || p.getEstado().equals(estado)) lista.add(p);
        }

        req.setAttribute("pedidos", lista);
        req.setAttribute("filtros", FILTROS);
        req.setAttribute("estadoActual", estado);
        req.setAttribute("totalPedidos", todos.size());
        req.setAttribute("enCamino", enCamino);
        req.setAttribute("vendido", vendido);
        req.setAttribute("rutaAdmin", "admin/pedidos");
        mostrarAdmin(req, resp, "pedidos");
    }
}
