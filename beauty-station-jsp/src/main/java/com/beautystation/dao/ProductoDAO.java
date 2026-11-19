package com.beautystation.dao;

import com.beautystation.modelo.Producto;
import java.util.List;
import java.util.Set;

public interface ProductoDAO {
    Producto buscarPorId(int id);
    List<Producto> buscarPorIds(List<Integer> ids);
    List<Producto> todos();

    List<Producto> filtrar(int precioMax, String categoria, Set<String> categorias, String busqueda, String orden);

    List<Producto> promociones(String categoria);
}
