package com.beautystation.dao;

import com.beautystation.modelo.Servicio;
import java.util.List;

public interface ServicioDAO {
    List<Servicio> listar();
    Servicio buscarPorId(int id);
}
