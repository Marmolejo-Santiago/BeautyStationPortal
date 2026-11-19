package com.beautystation.dao;

import com.beautystation.modelo.Resena;
import java.util.List;

public interface ResenaDAO {
    List<Resena> recientes(int max);

    void crear(int idUsuario, int idProducto, int calificacion, String comentario);
}
