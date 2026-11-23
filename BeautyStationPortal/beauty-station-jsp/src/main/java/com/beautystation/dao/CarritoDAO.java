package com.beautystation.dao;

import com.beautystation.modelo.CarritoItem;
import java.util.List;

public interface CarritoDAO {
    List<CarritoItem> listarPorUsuario(int idUsuario);
    void agregar(int idUsuario, int idProducto, int cantidad);

    void cambiarCantidad(int idUsuario, int idItem, int delta);
    void quitar(int idUsuario, int idItem);
    void vaciar(int idUsuario);

    void agregarPedido(int idUsuario, int idPedido);
}
