package com.beautystation.impl;

import com.beautystation.dao.ProductoDAO;
import com.beautystation.modelo.Categoria;
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
        return HibernateUtil.ejecutar(s -> {
            Producto p = s.get(Producto.class, id);
            return p != null && p.isActivo() ? p : null;
        });
    }

    @Override
    public List<Producto> buscarPorIds(List<Integer> ids) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Producto p where p.activo = true and p.id in (:ids)", Producto.class)
                .setParameterList("ids", ids).list());
    }

    @Override
    public List<Producto> todos() {
        return HibernateUtil.ejecutar(s -> s.createQuery("from Producto p where p.activo = true order by p.nombre", Producto.class).list());
    }

    @Override
    public List<Producto> filtrar(int precioMax, String categoria, Set<String> categorias, String busqueda, String orden) {
        // El tope del filtro (2,000) significa "$2,000 o más": con el control al máximo no se limita el precio
        boolean limitar = precioMax < 2000;
        StringBuilder hql = new StringBuilder("from Producto p where p.activo = true");
        if (limitar) hql.append(" and p.precio <= :max");
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
            if (limitar) q.setParameter("max", BigDecimal.valueOf(precioMax));
            if (categoria != null) q.setParameter("cat", categoria);
            if (!categorias.isEmpty()) q.setParameterList("cats", categorias);
            if (buscar) q.setParameter("q", "%" + busqueda.trim().toLowerCase(Locale.ROOT) + "%");
            return q.list();
        });
    }

    @Override
    public List<Producto> promociones(String categoria) {
        String hql = "from Producto p where p.activo = true and p.descuento > 0"
                + (categoria != null ? " and p.categoria.nombre = :cat" : "") + " order by p.id";
        return HibernateUtil.ejecutar(s -> {
            Query<Producto> q = s.createQuery(hql, Producto.class);
            if (categoria != null) q.setParameter("cat", categoria);
            return q.list();
        });
    }

    @Override
    public List<Producto> listarParaAdmin() {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Producto p where p.activo = true order by p.id desc", Producto.class).list());
    }

    @Override
    public Producto crear(Producto producto, int idCategoria) {
        return HibernateUtil.ejecutar(s -> {
            Categoria c = s.get(Categoria.class, idCategoria);
            if (c == null) throw new IllegalStateException("Elige una categoría válida");
            producto.setCategoria(c);
            producto.setActivo(true);
            s.persist(producto);
            return producto;
        });
    }

    @Override
    public boolean eliminar(int id) {
        return HibernateUtil.ejecutar(s -> {
            Producto p = s.get(Producto.class, id);
            if (p == null || !p.isActivo()) return false;
            p.setActivo(false);
            s.createQuery("delete from CarritoItem i where i.producto.id = :p").setParameter("p", id).executeUpdate();
            return true;
        });
    }
}
