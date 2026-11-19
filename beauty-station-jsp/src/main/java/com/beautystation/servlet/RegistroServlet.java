package com.beautystation.servlet;

import com.beautystation.modelo.Usuario;
import com.beautystation.util.Clave;
import com.beautystation.util.Validar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/registro")
public class RegistroServlet extends PaginaServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("generos", Validar.GENEROS);
        req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = texto(req, "nombre");
        String genero = texto(req, "genero");
        Integer edad = entero(req.getParameter("edad"));
        String correo = texto(req, "correo").toLowerCase();
        String telefono = texto(req, "telefono");
        String clave = req.getParameter("clave") == null ? "" : req.getParameter("clave");
        String confirmar = req.getParameter("confirmar") == null ? "" : req.getParameter("confirmar");

        String error = null;
        if (!Validar.nombre(nombre)) error = "Escribe tu nombre";
        else if (!Validar.GENEROS.contains(genero)) error = "Elige una opción de género";
        else if (edad == null || !Validar.edad(edad)) error = "Escribe una edad válida";
        else if (!Validar.email(correo)) error = "Escribe un correo válido";
        else if (!Validar.telefono(telefono)) error = "Escribe un teléfono válido";
        else if (clave.length() < 6) error = "La contraseña debe tener al menos 6 caracteres";
        else if (!clave.equals(confirmar)) error = "Las contraseñas no coinciden";
        else if (usuarioDAO.buscarPorEmail(correo) != null) error = "Ya existe una cuenta con ese correo";

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("generos", Validar.GENEROS);
            doGetConDatos(req, resp, nombre, genero, edad, correo, telefono);
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setGenero(genero);
        u.setEdad(edad);
        u.setEmail(correo);
        u.setTelefono(telefono.isEmpty() ? null : telefono);
        u.setContrasena(Clave.hash(clave));
        u.setFechaRegistro(LocalDate.now());
        u.setFrase("Siempre hermosa, siempre tú ♡");
        usuarioDAO.guardar(u);

        iniciarSesion(req, u.getId());
        aviso(req, "¡Cuenta creada! Bienvenida a Beauty Station ✨");
        irA(req, resp, "/inicio");
    }

    private void doGetConDatos(HttpServletRequest req, HttpServletResponse resp, String nombre, String genero,
                               Integer edad, String correo, String telefono) throws ServletException, IOException {
        req.setAttribute("v_nombre", nombre);
        req.setAttribute("v_genero", genero);
        req.setAttribute("v_edad", edad);
        req.setAttribute("v_correo", correo);
        req.setAttribute("v_telefono", telefono);
        req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
    }
}
