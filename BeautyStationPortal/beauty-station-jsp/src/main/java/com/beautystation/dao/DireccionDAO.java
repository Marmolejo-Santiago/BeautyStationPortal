package com.beautystation.dao;

import com.beautystation.modelo.Direccion;
import java.util.List;

public interface DireccionDAO {
    List<Direccion> listarPorUsuario(int idUsuario);

    Direccion guardar(int idUsuario, Direccion direccion, boolean predeterminada);
}
