package com.beautystation.dao;

import com.beautystation.modelo.Pedido;
import java.util.List;

public interface PedidoDAO {
    List<Pedido> listarPorUsuario(int idUsuario);

    Pedido buscarPorCodigo(int idUsuario, String codigo);

    Pedido crearDesdeCarrito(int idUsuario, int idDireccion, int idMetodo);

    /** Todos los pedidos de todas las clientas (administración). estado = null o "Todos" para no filtrar. */
    List<Pedido> listarTodos(String estado);
}
