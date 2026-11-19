package com.beautystation.impl;

import com.beautystation.dao.ProductoDAO;
import com.beautystation.modelo.Producto;
import com.beautystation.util.HibernateUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.hibernate.query.Query;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Producto buscarPorId(int id) {
        return HibernateUtil.ejecutar(s -> s.get(Producto.class, id));
    }

    @Override
    public List<Producto> buscarPorIds(List<Integer> ids) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Producto p where p.id in (:ids)", Producto.class)
                .setParameterList("ids", ids).list());
    }

    @Override
    public List<Producto> todos() {
        return HibernateUtil.ejecutar(s -> s.createQuery("from Producto p order by p.nombre", Producto.class).list());
    }

    @Override
    public List<Producto> filtrar(int precioMax, String categoria, Set<String> categorias, String busqueda, String orden) {
        StringBuilder hql = new StringBuilder("from Producto p where p.precio <= :max");
        if (categoria != null) hql.append(" and p.categoria.nombre = :cat");
        if (!categorias.isEmpty()) hql.append(" and p.categoria.nombre in (:cats)");
        boolean buscar = busqueda != null && !busqueda.isBlank();
        if (buscar) hql.append(" and (lower(p.nombre) like :q or lower(p.categoria.nombre) like :q)");
        switch (orden) {
            case "menor": hql.append(" order by p.precio asc, p.id desc"); break;
            case "mayor": hql.append(" order by p.precio desc, p.id desc"); break;
            default:      hql.append(" order by p.id desc");
        }
        return HibernateUtil.ejecutar(s -> {
            Query<Producto> q = s.createQuery(hql.toString(), Producto.class);
            q.setParameter("max", BigDecimal.valueOf(precioMax));
            if (categoria != null) q.setParameter("cat", categoria);
            if (!categorias.isEmpty()) q.setParameterList("cats", categorias);
            if (buscar) q.setParameter("q", "%" + busqueda.trim().toLowerCase(Locale.ROOT) + "%");
            return q.list();
        });
    }

    @Override
    public List<Producto> promociones(String categoria) {
        String hql = "from Producto p where p.descuento > 0"
                + (categoria != null ? " and p.categoria.nombre = :cat" : "") + " order by p.id";
        return HibernateUtil.ejecutar(s -> {
            Query<Producto> q = s.createQuery(hql, Producto.class);
            if (categoria != null) q.setParameter("cat", categoria);
            return q.list();
        });
    }
}
