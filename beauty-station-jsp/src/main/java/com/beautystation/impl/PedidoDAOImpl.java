package com.beautystation.impl;

import com.beautystation.dao.PedidoDAO;
import com.beautystation.modelo.CarritoItem;
import com.beautystation.modelo.DetallePedido;
import com.beautystation.modelo.Direccion;
import com.beautystation.modelo.MetodoPago;
import com.beautystation.modelo.Pedido;
import com.beautystation.modelo.Producto;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.Fmt;
import com.beautystation.util.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PedidoDAOImpl implements PedidoDAO {

    private static final Pattern CODIGO = Pattern.compile("(?i)BS-?0*(\\d{1,9})");
    private static final int DIAS_DE_ENTREGA = 5;

    @Override
    public List<Pedido> listarPorUsuario(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Pedido p where p.usuario.id = :u order by p.idPedido desc", Pedido.class)
                .setParameter("u", idUsuario).list());
    }

    @Override
    public Pedido buscarPorCodigo(int idUsuario, String codigo) {
        if (codigo == null) return null;
        Matcher m = CODIGO.matcher(codigo.trim());
        if (!m.matches()) return null;
        int id = Integer.parseInt(m.group(1));
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Pedido p where p.idPedido = :id and p.usuario.id = :u", Pedido.class)
                .setParameter("id", id).setParameter("u", idUsuario).uniqueResult());
    }

    @Override
    public Pedido crearDesdeCarrito(int idUsuario, int idDireccion, int idMetodo) {
        return HibernateUtil.ejecutar(s -> {
            Usuario u = s.get(Usuario.class, idUsuario);
            Direccion dir = s.get(Direccion.class, idDireccion);
            MetodoPago met = s.get(MetodoPago.class, idMetodo);
            if (u == null || dir == null || met == null
                    || !dir.getUsuario().getId().equals(idUsuario) || !met.getUsuario().getId().equals(idUsuario)) {
                throw new IllegalStateException("Elige una dirección y un método de pago");
            }
            List<CarritoItem> items = s.createQuery(
                    "from CarritoItem i where i.usuario.id = :u order by i.id", CarritoItem.class)
                    .setParameter("u", idUsuario).list();
            if (items.isEmpty()) throw new IllegalStateException("Tu carrito está vacío");

            Pedido pedido = new Pedido();
            pedido.setUsuario(u);
            pedido.setDireccion(dir);
            pedido.setMetodoPago(met);
            pedido.setFechaPedido(LocalDate.now());
            pedido.setFechaEntrega(LocalDate.now().plusDays(DIAS_DE_ENTREGA));
            pedido.setEstado("En camino");

            BigDecimal total = BigDecimal.ZERO;
            for (CarritoItem item : items) {
                Producto producto = item.getProducto();
                int stock = producto.getStock() == null ? 0 : producto.getStock();
                if (stock < item.getCantidad()) {
                    throw new IllegalStateException("No hay suficiente stock de " + producto.getNombre());
                }
                producto.setStock(stock - item.getCantidad());

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnit(producto.getPrecioFinal());
                pedido.getDetalles().add(detalle);
                total = total.add(item.getSubtotal());
                s.remove(item);
            }
            pedido.setTotal(total);
            s.persist(pedido);

            NotificacionDAOImpl.crear(s, u, "pedido", "Pedido realizado",
                    "Tu pedido " + pedido.getCodigo() + " va en camino y llegará el " + Fmt.fechaCorta(pedido.getFechaEntrega()) + ".");
            return pedido;
        });
    }
}
