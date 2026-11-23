package com.beautystation.servlet;

import com.beautystation.modelo.Cita;
import com.beautystation.modelo.EstadoApp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/miscitas")
public class MisCitasServlet extends PaginaServlet {

    private static final List<String> FILTROS = List.of("Todas", "Confirmada", "Pendiente", "Cancelada");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EstadoApp e = estado(req);
        String filtro = req.getParameter("estado");
        if (filtro != null && FILTROS.contains(filtro)) e.setFiltroCitas(filtro);

        LocalDate hoy = LocalDate.now();
        List<Cita> todas = citaDAO.listarPorUsuario(usuarioId(req));
        List<Cita> lista = new ArrayList<>();
        List<String> fechas = new ArrayList<>();
        int proximas = 0;
        int completadas = 0;
        int canceladas = 0;
        for (Cita c : todas) {
            fechas.add(c.getFechaIso());
            if (e.getFiltroCitas().equals("Todas") || c.getEstado().equals(e.getFiltroCitas())) lista.add(c);
            if (c.getEstado().equals("Cancelada")) canceladas++;
            else if (c.getFechaDia().isBefore(hoy)) completadas++;
            else proximas++;
        }

        req.setAttribute("citas", lista);
        req.setAttribute("fechasCitas", fechas);
        req.setAttribute("filtros", FILTROS);
        req.setAttribute("proximas", proximas);
        req.setAttribute("completadas", completadas);
        req.setAttribute("canceladas", canceladas);
        mostrar(req, resp, "miscitas");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EstadoApp e = estado(req);
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        if (accion.equals("mini-prev")) {
            e.setMini(e.getMini().minusMonths(1));
        } else if (accion.equals("mini-next")) {
            e.setMini(e.getMini().plusMonths(1));
        } else if (accion.startsWith("dia:")) {
            try {
                String iso = accion.substring(4);
                if (!LocalDate.parse(iso).isBefore(LocalDate.now())) {
                    e.getForm().setFecha(iso);
                    e.getForm().setHora(null);
                }
            } catch (DateTimeParseException ignorada) {
            }
        } else if (accion.startsWith("cancelar:")) {
            Integer id = entero(accion.substring(9));
            if (id != null && citaDAO.cancelar(id, usuarioId(req))) aviso(req, "Cita cancelada");
        }
        irA(req, resp, "/miscitas");
    }
}
