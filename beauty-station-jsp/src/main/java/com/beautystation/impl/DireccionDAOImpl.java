package com.beautystation.impl;

import com.beautystation.dao.DireccionDAO;
import com.beautystation.modelo.Direccion;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.util.List;

public class DireccionDAOImpl implements DireccionDAO {

    @Override
    public List<Direccion> listarPorUsuario(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Direccion d where d.usuario.id = :u order by d.predeterminada desc, d.id", Direccion.class)
                .setParameter("u", idUsuario).list());
    }

    @Override
    public Direccion guardar(int idUsuario, Direccion direccion, boolean predeterminada) {
        return HibernateUtil.ejecutar(s -> {
            Usuario u = s.get(Usuario.class, idUsuario);
            long existentes = s.createQuery("select count(d) from Direccion d where d.usuario.id = :u", Long.class)
                    .setParameter("u", idUsuario).uniqueResult();
            boolean principal = predeterminada || existentes == 0;
            if (principal) {
                s.createQuery("update Direccion d set d.predeterminada = false where d.usuario.id = :u")
                        .setParameter("u", idUsuario).executeUpdate();
            }
            direccion.setUsuario(u);
            direccion.setPredeterminada(principal);
            s.persist(direccion);
            return direccion;
        });
    }
}
