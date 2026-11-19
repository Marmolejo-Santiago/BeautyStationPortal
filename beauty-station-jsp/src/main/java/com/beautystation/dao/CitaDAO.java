package com.beautystation.dao;

import com.beautystation.modelo.Cita;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface CitaDAO {
    List<Cita> listarPorUsuario(int idUsuario);

    List<Cita> delDia(LocalDate dia);

    Cita crear(int idUsuario, LocalDate dia, String hora, Collection<Integer> idsServicios, String nota);

    boolean cancelar(int idCita, int idUsuario);
}
