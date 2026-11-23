package com.beautystation.servlet;

import com.beautystation.modelo.EstadoApp;
import com.beautystation.modelo.Pedido;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pedidos")
public class PedidosServlet extends PaginaServlet {

    private static final List<String[]> FILTROS = List.of(
            new String[]{"Todos", "Todos"}, new String[]{"En camino", "En camino"},
            new String[]{"Entregado", "Entregados"}, new String[]{"Cancelado", "Cancelados"});

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstadoApp e = estado(req);

        String filtro = req.getParameter("estado");
        if (filtro != null) {
            for (String[] f : FILTROS) {
                if (f[0].equals(filtro)) e.setFiltroPedidos(filtro);
            }
        }

        List<Pedido> lista = new ArrayList<>();
        for (Pedido p : pedidoDAO.listarPorUsuario(usuarioId(req))) {
            if (e.getFiltroPedidos().equals("Todos") || p.getEstado().equals(e.getFiltroPedidos())) lista.add(p);
        }

        String rastreo = req.getParameter("rastreo");
        if (rastreo != null) {
            Pedido encontrado = pedidoDAO.buscarPorCodigo(usuarioId(req), rastreo);
            req.setAttribute("rastreoPedido", encontrado);
        }

        req.setAttribute("pedidos", lista);
        req.setAttribute("filtros", FILTROS);
        mostrar(req, resp, "pedidos");
    }
}
