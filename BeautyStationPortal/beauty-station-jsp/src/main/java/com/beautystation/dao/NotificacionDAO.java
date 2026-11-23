package com.beautystation.dao;

import com.beautystation.modelo.Notificacion;
import java.util.List;

public interface NotificacionDAO {
    List<Notificacion> recientes(int idUsuario, int max);
    long contarNoLeidas(int idUsuario);
    void marcarLeidas(int idUsuario);
}
