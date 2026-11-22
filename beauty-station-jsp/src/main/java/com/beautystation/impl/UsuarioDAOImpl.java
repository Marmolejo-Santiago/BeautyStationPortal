package com.beautystation.impl;

import com.beautystation.dao.UsuarioDAO;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.HibernateUtil;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorId(int id) {
        return HibernateUtil.ejecutar(s -> s.get(Usuario.class, id));
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Usuario u where lower(u.email) = :e", Usuario.class)
                .setParameter("e", email.trim().toLowerCase())
                .uniqueResult());
    }

    @Override
    public List<Usuario> listarClientes() {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Usuario u where u.rol = 'CLIENTE' order by u.nombre", Usuario.class).list());
    }

    @Override
    public void guardar(Usuario usuario) {
        HibernateUtil.ejecutarSinResultado(s -> s.persist(usuario));
    }

    @Override
    public void actualizar(Usuario usuario) {
        HibernateUtil.ejecutarSinResultado(s -> s.merge(usuario));
    }
}
