package com.beautystation.servlet;

import com.beautystation.datos.Datos;
import com.beautystation.modelo.Cita;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Administración: ver las citas asignadas, agendar nuevas y cancelarlas. */
@WebServlet("/admin/citas")
public class AdminCitasServlet extends AdminServlet {

    private static final List<String> FILTROS = List.of("Todas", "Confirmada", "Pendiente", "Cancelada");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String estado = req.getParameter("estado");
        if (estado == null || !FILTROS.contains(estado)) estado = "Todas";

        List<Cita> todas = citaDAO.listarTodas(null);
        List<Cita> lista = new ArrayList<>();
        int confirmadas = 0;
        int pendientes = 0;
        int canceladas = 0;
        for (Cita c : todas) {
            switch (c.getEstado()) {
                case "Confirmada": confirmadas++; break;
                case "Pendiente":  pendientes++; break;
                default:           canceladas++;
            }
            if (estado.equals("Todas") || c.getEstado().equals(estado)) lista.add(c);
        }

        req.setAttribute("citas", lista);
        req.setAttribute("filtros", FILTROS);
        req.setAttribute("estadoActual", estado);
        req.setAttribute("totalCitas", todas.size());
        req.setAttribute("confirmadas", confirmadas);
        req.setAttribute("pendientes", pendientes);
        req.setAttribute("canceladas", canceladas);
        req.setAttribute("clientes", usuarioDAO.listarClientes());
        req.setAttribute("hoy", LocalDate.now().toString());
        req.setAttribute("rutaAdmin", "admin/citas");
        mostrarAdmin(req, resp, "citas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String estado = req.getParameter("estado");
        if (estado == null || !FILTROS.contains(estado)) estado = "Todas";
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        if (accion.startsWith("cancelar:")) {
            Integer id = entero(accion.substring(9));
            if (id != null && citaDAO.cancelarPorAdmin(id)) aviso(req, "Cita cancelada");
            else aviso(req, "No se pudo cancelar la cita");
        } else if (accion.equals("crear")) {
            crear(req);
        }
        irA(req, resp, "/admin/citas?estado=" + estado);
    }

    private void crear(HttpServletRequest req) {
        Integer cliente = entero(req.getParameter("cliente"));      // vacío = clienta sin cuenta
        String nombre = texto(req, "nombre");
        String nota = texto(req, "nota");
        if (nota.length() > 255) nota = nota.substring(0, 255);

        LocalDate dia;
        try {
            dia = LocalDate.parse(texto(req, "fecha"));
        } catch (DateTimeParseException e) {
            aviso(req, "Selecciona una fecha");
            return;
        }
        if (dia.isBefore(LocalDate.now())) {
            aviso(req, "La fecha no puede ser anterior a hoy");
            return;
        }
        String hora = texto(req, "hora");
        if (!Datos.HORARIOS.contains(hora)) {
            aviso(req, "Selecciona un horario");
            return;
        }
        if (dia.equals(LocalDate.now()) && LocalTime.parse(hora).isBefore(LocalTime.now())) {
            aviso(req, "Ese horario de hoy ya pasó");
            return;
        }
        Set<Integer> servicios = new LinkedHashSet<>();
        String[] marcados = req.getParameterValues("svc");
        if (marcados != null) {
            for (String m : marcados) {
                Integer id = entero(m);
                if (id != null && servicio(id) != null) servicios.add(id);
            }
        }
        if (servicios.isEmpty()) {
            aviso(req, "Elige al menos un servicio");
            return;
        }
        try {
            citaDAO.crearPorAdmin(cliente, nombre, dia, hora, servicios, nota);
            aviso(req, "¡Cita agendada! ✨");
        } catch (IllegalStateException e) {
            aviso(req, e.getMessage());
        }
    }
}
