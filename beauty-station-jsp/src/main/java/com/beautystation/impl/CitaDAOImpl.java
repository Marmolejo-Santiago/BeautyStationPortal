package com.beautystation.impl;

import com.beautystation.dao.CitaDAO;
import com.beautystation.modelo.Cita;
import com.beautystation.modelo.CitaServicio;
import com.beautystation.modelo.Servicio;
import com.beautystation.modelo.Usuario;
import com.beautystation.util.Agenda;
import com.beautystation.util.Fmt;
import com.beautystation.util.HibernateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public List<Cita> listarPorUsuario(int idUsuario) {
        return HibernateUtil.ejecutar(s -> s.createQuery(
                "from Cita c where c.usuario.id = :u order by c.fecha", Cita.class)
                .setParameter("u", idUsuario).list());
    }

    @Override
    public List<Cita> delDia(LocalDate dia) {
        return HibernateUtil.ejecutar(s -> delDia(s, dia));
    }

    private static List<Cita> delDia(Session s, LocalDate dia) {
        return s.createQuery(
                "from Cita c where c.fecha >= :ini and c.fecha < :fin and c.estado <> 'Cancelada'", Cita.class)
                .setParameter("ini", dia.atStartOfDay())
                .setParameter("fin", dia.plusDays(1).atStartOfDay())
                .list();
    }

    @Override
    public Cita crear(int idUsuario, LocalDate dia, String hora, Collection<Integer> idsServicios, String nota) {
        return HibernateUtil.ejecutar(s -> {
            Usuario u = s.get(Usuario.class, idUsuario);
            List<Servicio> servicios = new ArrayList<>();
            int minutos = 0;
            BigDecimal costo = BigDecimal.ZERO;
            for (Integer id : idsServicios) {
                Servicio sv = s.get(Servicio.class, id);
                if (sv == null) continue;
                servicios.add(sv);
                minutos += sv.getMin();
                costo = costo.add(sv.getCosto());
            }
            if (u == null || servicios.isEmpty()) throw new IllegalStateException("Elige al menos un servicio");
            if (Agenda.noDisponibles(delDia(s, dia), minutos).contains(hora)) {
                throw new IllegalStateException("Ese horario ya no está disponible para la duración de tus servicios");
            }

            Cita c = new Cita();
            c.setUsuario(u);
            c.setCliente(u.getNombre());
            c.setFecha(LocalDateTime.of(dia, LocalTime.parse(hora)));
            c.setEspecificacion(nota == null || nota.isBlank() ? null : nota.trim());
            c.setCosto(costo);
            c.setDuracion(minutos);
            c.setArea("Showroom");
            c.setEstado("Confirmada");
            StringBuilder resumen = new StringBuilder();
            for (Servicio sv : servicios) {
                if (resumen.length() > 0) resumen.append(" + ");
                resumen.append(sv.getNombre());
                c.getServicios().add(new CitaServicio(c, sv));
            }
            c.setServicio(resumen.toString());
            s.persist(c);

            NotificacionDAOImpl.crear(s, u, "cita", "Cita confirmada",
                    "Tu cita de " + resumen + " es el " + Fmt.fechaLarga(dia) + " a las " + hora + ".");
            return c;
        });
    }

    @Override
    public boolean cancelar(int idCita, int idUsuario) {
        return HibernateUtil.ejecutar(s -> {
            Cita c = s.get(Cita.class, idCita);
            if (c == null || !c.getUsuario().getId().equals(idUsuario) || "Cancelada".equals(c.getEstado())) {
                return false;
            }
            c.setEstado("Cancelada");
            NotificacionDAOImpl.crear(s, c.getUsuario(), "cita", "Cita cancelada",
                    "Cancelaste tu cita de " + c.getServicio() + " del " + c.getFechaLarga() + ".");
            return true;
        });
    }
}
