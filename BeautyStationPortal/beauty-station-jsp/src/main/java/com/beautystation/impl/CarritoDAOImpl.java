package com.beautystation.impl;

import com.beautystation.dao.CarritoDAO;
import com.beautystation.modelo.CarritoItem;
import com.beautystation.modelo.DetallePedido;
import com.beautystation.modelo.Pedido;
import com.beautystation.modelo.Producto;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

public class CarritoDAOImpl implements CarritoDAO {

    private static final int MAXIMO = 99;

    @Override
    public List<CarritoItem> listarPorUsuario(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from CarritoItem i where i.usuario.id = :u order by i.id", CarritoItem.class)
                .setParameter("u", idUsuario).list());
    }

    @Override
    public void agregar(int idUsuario, int idProducto, int cantidad) {
        HibernateUtil.ejecutarSinResultado(s -> sumar(s, idUsuario, idProducto, cantidad));
    }

    private static void sumar(Session s, int idUsuario, int idProducto, int cantidad) {
        CarritoItem item = s.createQuery(
                "from CarritoItem i where i.usuario.id = :u and i.producto.id = :p", CarritoItem.class)
                .setParameter("u", idUsuario).setParameter("p", idProducto).uniqueResult();
        if (item != null) {
            item.setCantidad(Math.min(MAXIMO, item.getCantidad() + cantidad));
            return;
        }
        Producto producto = s.get(Producto.class, idProducto);
        if (producto == null || !producto.isActivo()) return;
        item = new CarritoItem();
        item.setUsuario(s.get(Usuario.class, idUsuario));
        item.setProducto(producto);
        item.setCantidad(Math.min(MAXIMO, cantidad));
        s.persist(item);
    }

    @Override
    public void cambiarCantidad(int idUsuario, int idItem, int delta) {
        HibernateUtil.ejecutarSinResultado(s -> {
            CarritoItem item = s.get(CarritoItem.class, idItem);
            if (item == null || !item.getUsuario().getId().equals(idUsuario)) return;
            int nueva = item.getCantidad() + delta;
            if (nueva <= 0) s.remove(item); else item.setCantidad(Math.min(MAXIMO, nueva));
        });
    }

    @Override
    public void quitar(int idUsuario, int idItem) {
        HibernateUtil.ejecutarSinResultado(s -> {
            CarritoItem item = s.get(CarritoItem.class, idItem);
            if (item != null && item.getUsuario().getId().equals(idUsuario)) s.remove(item);
        });
    }

    @Override
    public void vaciar(int idUsuario) {
        HibernateUtil.ejecutarSinResultado(s -> s.createQuery(
                "delete from CarritoItem i where i.usuario.id = :u").setParameter("u", idUsuario).executeUpdate());
    }

    @Override
    public void agregarPedido(int idUsuario, int idPedido) {
        HibernateUtil.ejecutarSinResultado(s -> {
            Pedido p = s.get(Pedido.class, idPedido);
            if (p == null || !p.getUsuario().getId().equals(idUsuario)) return;
            for (DetallePedido d : p.getDetalles()) sumar(s, idUsuario, d.getProducto().getId(), d.getCantidad());
        });
    }
}
