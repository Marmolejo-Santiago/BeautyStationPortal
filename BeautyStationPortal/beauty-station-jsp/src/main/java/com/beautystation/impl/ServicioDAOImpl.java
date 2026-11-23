package com.beautystation.impl;

import com.beautystation.dao.ServicioDAO;
import com.beautystation.modelo.Servicio;
import com.beautystation.util.HibernateUtil;
import java.util.List;

public class ServicioDAOImpl implements ServicioDAO {

    @Override
    public List<Servicio> listar() {
        return HibernateUtil.ejecutar(s -> s.createQuery("from Servicio v order by v.id", Servicio.class).list());
    }

    @Override
    public Servicio buscarPorId(int id) {
        return HibernateUtil.ejecutar(s -> s.get(Servicio.class, id));
    }
}
