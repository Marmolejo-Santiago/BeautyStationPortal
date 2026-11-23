package com.beautystation.impl;

import com.beautystation.dao.NotificacionDAO;
import com.beautystation.modelo.Notificacion;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;

public class NotificacionDAOImpl implements NotificacionDAO {

    @Override
    public List<Notificacion> recientes(int idUsuario, int max) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Notificacion n where n.usuario.id = :u order by n.fecha desc, n.id desc", Notificacion.class)
                .setParameter("u", idUsuario).setMaxResults(max).list());
    }

    @Override
    public long contarNoLeidas(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "select count(n) from Notificacion n where n.usuario.id = :u and n.leida = false", Long.class)
                .setParameter("u", idUsuario).uniqueResult());
    }

    @Override
    public void marcarLeidas(int idUsuario) {
        HibernateUtil.ejecutarSinResultado(s -> s.createQuery(
                "update Notificacion n set n.leida = true where n.usuario.id = :u and n.leida = false")
                .setParameter("u", idUsuario).executeUpdate());
    }

    /**
     * Crea una notificación dentro de la transacción en curso, solo si la persona
     * tiene activado ese tipo en sus preferencias (promo, pedido, cita o tienda).
     */
    static void crear(Session s, Usuario u, String tipo, String titulo, String mensaje) {
        if (!permite(u, tipo)) return;
        Notificacion n = new Notificacion();
        n.setUsuario(u);
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setFecha(LocalDateTime.now());
        s.persist(n);
    }

    private static boolean permite(Usuario u, String tipo) {
        switch (tipo) {
            case "promo":  return Boolean.TRUE.equals(u.getNotifPromos());
            case "pedido": return Boolean.TRUE.equals(u.getNotifPedidos());
            case "cita":   return Boolean.TRUE.equals(u.getNotifCitas());
            default:       return Boolean.TRUE.equals(u.getNotifNovedades());
        }
    }
}
