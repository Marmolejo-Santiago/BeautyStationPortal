package com.beautystation.impl;

import com.beautystation.dao.ResenaDAO;
import com.beautystation.modelo.Producto;
import com.beautystation.modelo.Resena;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.time.LocalDateTime;
import java.util.List;

public class ResenaDAOImpl implements ResenaDAO {

    @Override
    public List<Resena> recientes(int max) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Resena r order by r.fecha desc, r.id desc", Resena.class).setMaxResults(max).list());
    }

    @Override
    public void crear(int idUsuario, int idProducto, int calificacion, String comentario) {
        HibernateUtil.ejecutarSinResultado(s -> {
            Usuario u = s.get(Usuario.class, idUsuario);
            Producto p = s.get(Producto.class, idProducto);
            if (u == null || p == null) throw new IllegalStateException("No encontramos ese producto");
            long previas = s.createQuery(
                    "select count(r) from Resena r where r.usuario.id = :u and r.producto.id = :p", Long.class)
                    .setParameter("u", idUsuario).setParameter("p", idProducto).uniqueResult();
            if (previas > 0) throw new IllegalStateException("Ya dejaste una reseña de este producto");

            Resena r = new Resena();
            r.setUsuario(u);
            r.setProducto(p);
            r.setCalificacion(calificacion);
            r.setComentario(comentario == null || comentario.isBlank() ? null : comentario.trim());
            r.setFecha(LocalDateTime.now());
            s.persist(r);

            // El promedio del producto incluye la nueva calificación
            int n = p.getResenas();
            double promedio = (p.getRating() * n + calificacion) / (n + 1);
            p.setRating(Math.round(promedio * 10) / 10.0);
            p.setResenas(n + 1);
        });
    }
}
