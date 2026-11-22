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

    // ----- administración -----

    /** Todas las citas (de cualquier clienta). estado = null o "Todas" para no filtrar. */
    List<Cita> listarTodas(String estado);

    /** idUsuario = null agenda para una clienta sin cuenta (solo con su nombre). */
    Cita crearPorAdmin(Integer idUsuario, String nombreCliente, LocalDate dia, String hora,
                       Collection<Integer> idsServicios, String nota);

    boolean cancelarPorAdmin(int idCita);
}
