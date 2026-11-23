package com.beautystation.impl;

import com.beautystation.dao.CategoriaDAO;
import com.beautystation.modelo.Categoria;
import com.beautystation.util.HibernateUtil;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public List<Categoria> listar() {
        return HibernateUtil.ejecutar(s -> s.createQuery("from Categoria c order by c.id", Categoria.class).list());
    }
}
