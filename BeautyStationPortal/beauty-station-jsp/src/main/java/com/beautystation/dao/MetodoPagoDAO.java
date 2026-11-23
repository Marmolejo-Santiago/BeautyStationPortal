package com.beautystation.dao;

import com.beautystation.modelo.MetodoPago;
import java.util.List;

public interface MetodoPagoDAO {
    List<MetodoPago> listarPorUsuario(int idUsuario);

    MetodoPago guardar(int idUsuario, MetodoPago metodo, boolean predeterminado);
}
