package com.beautystation.servlet;

import com.beautystation.datos.Datos;
import com.beautystation.modelo.EstadoApp;
import com.beautystation.modelo.FormCita;
import com.beautystation.modelo.Servicio;
import com.beautystation.util.Agenda;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/citas")
public class CitasServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormCita f = estado(req).getForm();
        List<String> ocupados = noDisponibles(f);
        req.setAttribute("ocupados", ocupados);
        req.setAttribute("lleno", f.getFecha() != null && ocupados.size() >= Datos.HORARIOS.size());
        mostrar(req, resp, "citas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstadoApp e = estado(req);
        FormCita f = e.getForm();

        String nota = req.getParameter("nota");
        if (nota != null) f.setNota(nota.length() > 255 ? nota.substring(0, 255) : nota);

        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        Integer deLista = entero(req.getParameter("svc"));
        Integer alternar = accion.startsWith("svc:") ? entero(accion.substring(4)) : null;
        if (deLista != null && !deLista.equals(alternar) && servicio(deLista) != null) f.getServicios().add(deLista);

        if (accion.equals("cal-prev")) {
            e.setCal(e.getCal().minusMonths(1));
        } else if (accion.equals("cal-next")) {
            e.setCal(e.getCal().plusMonths(1));
        } else if (accion.startsWith("dia:")) {
            elegirDia(f, accion.substring(4));
        } else if (alternar != null) {
            if (servicio(alternar) != null && !f.getServicios().remove(alternar)) f.getServicios().add(alternar);
        } else if (accion.startsWith("hora:")) {
            String hora = accion.substring(5);
            if (Datos.HORARIOS.contains(hora) && !noDisponibles(f).contains(hora)) f.setHora(hora);
        } else if (accion.equals("confirmar")) {
            confirmar(req, resp, e);
            return;
        }
        if (f.getHora() != null && noDisponibles(f).contains(f.getHora())) f.setHora(null);
        irA(req, resp, "/citas");
    }

    private List<String> noDisponibles(FormCita f) {
        if (f.getFecha() == null) return new ArrayList<>();
        int minutos = 0;
        for (int id : f.getServicios()) {
            Servicio s = servicio(id);
            if (s != null) minutos += s.getMin();
        }
        return Agenda.noDisponibles(citaDAO.delDia(LocalDate.parse(f.getFecha())), minutos);
    }

    private void elegirDia(FormCita f, String iso) {
        try {
            if (!LocalDate.parse(iso).isBefore(LocalDate.now())) {
                f.setFecha(iso);
                f.setHora(null);
            }
        } catch (DateTimeParseException ignorada) {
            // fecha inválida: se ignora
        }
    }

    private void confirmar(HttpServletRequest req, HttpServletResponse resp, EstadoApp e) throws IOException {
        FormCita f = e.getForm();
        if (f.getFecha() == null) {
            aviso(req, "Selecciona una fecha");
        } else if (f.getServicios().isEmpty()) {
            aviso(req, "Elige al menos un servicio");
        } else if (f.getHora() == null) {
            aviso(req, "Selecciona un horario");
        } else {
            try {
                citaDAO.crear(usuarioId(req), LocalDate.parse(f.getFecha()), f.getHora(), f.getServicios(), f.getNota());
                e.reiniciarForm();
                aviso(req, "¡Cita confirmada! ✨");
                irA(req, resp, "/miscitas");
                return;
            } catch (IllegalStateException ex) {
                f.setHora(null);
                aviso(req, ex.getMessage());
            }
        }
        irA(req, resp, "/citas");
    }
}
