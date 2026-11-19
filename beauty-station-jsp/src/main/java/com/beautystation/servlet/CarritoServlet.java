package com.beautystation.servlet;

import com.beautystation.modelo.Pedido;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/carrito")
public class CarritoServlet extends PaginaServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int uid = usuarioId(req);
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";
        String ancla = null;

        try {
            if (accion.startsWith("agregar:")) {
                Integer id = entero(accion.substring(8));
                if (id != null && productoDAO.buscarPorId(id) != null) {
                    carritoDAO.agregar(uid, id, 1);
                    aviso(req, "Producto agregado al carrito 🛍️");
                    ancla = "p-" + id;
                }
            } else if (accion.startsWith("recomprar:")) {
                Pedido p = pedidoDAO.buscarPorCodigo(uid, accion.substring(10));
                if (p != null) {
                    carritoDAO.agregarPedido(uid, p.getIdPedido());
                    aviso(req, "Productos agregados al carrito");
                    ancla = "o-" + p.getCodigo();
                }
            } else if (accion.startsWith("mas:") || accion.startsWith("menos:")) {
                boolean suma = accion.startsWith("mas:");
                Integer id = entero(accion.substring(accion.indexOf(':') + 1));
                if (id != null) carritoDAO.cambiarCantidad(uid, id, suma ? 1 : -1);
                ancla = "carrito";
            } else if (accion.startsWith("quitar:")) {
                Integer id = entero(accion.substring(7));
                if (id != null) carritoDAO.quitar(uid, id);
                ancla = "carrito";
            } else if (accion.equals("vaciar")) {
                carritoDAO.vaciar(uid);
                ancla = "carrito";
            } else if (accion.equals("comprar")) {
                Integer direccion = entero(req.getParameter("direccion"));
                Integer metodo = entero(req.getParameter("metodo"));
                if (direccion == null || metodo == null) {
                    aviso(req, "Elige una dirección y un método de pago");
                    ancla = "carrito";
                } else {
                    Pedido p = pedidoDAO.crearDesdeCarrito(uid, direccion, metodo);
                    aviso(req, "¡Pedido " + p.getCodigo() + " realizado! 🛍️");
                    irA(req, resp, "/pedidos");
                    return;
                }
            }
        } catch (IllegalStateException e) {
            aviso(req, e.getMessage());
            ancla = "carrito";
        }
        volver(req, resp, ancla);
    }
}
