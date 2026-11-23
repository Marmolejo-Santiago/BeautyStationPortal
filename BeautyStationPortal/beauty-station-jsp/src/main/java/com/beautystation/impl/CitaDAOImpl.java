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
            if (u == null) throw new IllegalStateException("Elige al menos un servicio");
            return registrar(s, u, u.getNombre(), dia, hora, idsServicios, nota,
                    "Ese horario ya no está disponible para la duración de tus servicios");
        });
    }

    @Override
    public Cita crearPorAdmin(Integer idUsuario, String nombreCliente, LocalDate dia, String hora,
                              Collection<Integer> idsServicios, String nota) {
        return HibernateUtil.ejecutar(s -> {
            Usuario u = idUsuario == null ? null : s.get(Usuario.class, idUsuario);
            if (idUsuario != null && u == null) throw new IllegalStateException("No encontramos a esa clienta");
            String cliente = u != null ? u.getNombre() : (nombreCliente == null ? "" : nombreCliente.trim());
            if (cliente.isEmpty()) throw new IllegalStateException("Escribe el nombre de la clienta");
            return registrar(s, u, cliente, dia, hora, idsServicios, nota,
                    "Ese horario no está disponible para la duración de los servicios elegidos");
        });
    }

    /** Crea la cita dentro de la transacción en curso. u puede ser null (clienta sin cuenta). */
    private static Cita registrar(Session s, Usuario u, String cliente, LocalDate dia, String hora,
                                  Collection<Integer> idsServicios, String nota, String mensajeOcupado) {
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
        if (servicios.isEmpty()) throw new IllegalStateException("Elige al menos un servicio");
        if (Agenda.noDisponibles(delDia(s, dia), minutos).contains(hora)) {
            throw new IllegalStateException(mensajeOcupado);
        }

        Cita c = new Cita();
        c.setUsuario(u);
        c.setCliente(cliente);
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

        if (u != null) {
            NotificacionDAOImpl.crear(s, u, "cita", "Cita confirmada",
                    "Tu cita de " + resumen + " es el " + Fmt.fechaLarga(dia) + " a las " + hora + ".");
        }
        return c;
    }

    @Override
    public boolean cancelar(int idCita, int idUsuario) {
        return HibernateUtil.ejecutar(s -> {
            Cita c = s.get(Cita.class, idCita);
            if (c == null || c.getUsuario() == null || !c.getUsuario().getId().equals(idUsuario)
                    || "Cancelada".equals(c.getEstado())) {
                return false;
            }
            c.setEstado("Cancelada");
            NotificacionDAOImpl.crear(s, c.getUsuario(), "cita", "Cita cancelada",
                    "Cancelaste tu cita de " + c.getServicio() + " del " + c.getFechaLarga() + ".");
            return true;
        });
    }

    @Override
    public List<Cita> listarTodas(String estado) {
        boolean filtrar = estado != null && !estado.isEmpty() && !estado.equals("Todas");
        return HibernateUtil.ejecutar(s -> {
            org.hibernate.query.Query<Cita> q = s.createQuery(
                    "from Cita c" + (filtrar ? " where c.estado = :e" : "") + " order by c.fecha desc, c.id desc", Cita.class);
            if (filtrar) q.setParameter("e", estado);
            return q.list();
        });
    }

    @Override
    public boolean cancelarPorAdmin(int idCita) {
        return HibernateUtil.ejecutar(s -> {
            Cita c = s.get(Cita.class, idCita);
            if (c == null || "Cancelada".equals(c.getEstado())) return false;
            c.setEstado("Cancelada");
            if (c.getUsuario() != null) {
                NotificacionDAOImpl.crear(s, c.getUsuario(), "cita", "Cita cancelada",
                        "Beauty Station canceló tu cita de " + c.getServicio() + " del " + c.getFechaLarga()
                                + ". Puedes agendar otra cuando quieras.");
            }
            return true;
        });
    }
}
