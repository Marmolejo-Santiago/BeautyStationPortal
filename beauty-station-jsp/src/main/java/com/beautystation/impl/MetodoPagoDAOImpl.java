package com.beautystation.impl;

import com.beautystation.dao.MetodoPagoDAO;
import com.beautystation.modelo.MetodoPago;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.util.List;

public class MetodoPagoDAOImpl implements MetodoPagoDAO {

    @Override
    public List<MetodoPago> listarPorUsuario(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from MetodoPago m where m.usuario.id = :u order by m.predeterminado desc, m.id", MetodoPago.class)
                .setParameter("u", idUsuario).list());
    }

    @Override
    public MetodoPago guardar(int idUsuario, MetodoPago metodo, boolean predeterminado) {
        return HibernateUtil.ejecutar(s -> {
            Usuario u = s.get(Usuario.class, idUsuario);
            long existentes = s.createQuery("select count(m) from MetodoPago m where m.usuario.id = :u", Long.class)
                    .setParameter("u", idUsuario).uniqueResult();
            boolean principal = predeterminado || existentes == 0;
            if (principal) {
                s.createQuery("update MetodoPago m set m.predeterminado = false where m.usuario.id = :u")
                        .setParameter("u", idUsuario).executeUpdate();
            }
            metodo.setUsuario(u);
            metodo.setPredeterminado(principal);
            s.persist(metodo);
            return metodo;
        });
    }
}
