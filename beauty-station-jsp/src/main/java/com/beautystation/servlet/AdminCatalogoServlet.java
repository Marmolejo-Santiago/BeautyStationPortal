package com.beautystation.servlet;

import com.beautystation.modelo.Categoria;
import com.beautystation.modelo.Producto;
import com.beautystation.util.Archivos;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/catalogo")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = Archivos.MAX_BYTES, maxRequestSize = Archivos.MAX_BYTES + 1024 * 1024)
public class AdminCatalogoServlet extends AdminServlet {

    private static final List<String> ETIQUETAS = List.of("", "Nuevo", "Más vendido");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("productos", productoDAO.listarParaAdmin());
        req.setAttribute("etiquetas", ETIQUETAS);
        req.setAttribute("rutaAdmin", "admin/catalogo");
        mostrarAdmin(req, resp, "catalogo");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String accion = req.getParameter("accion");
            if (accion == null) accion = "";
            if (accion.startsWith("eliminar:")) {
                Integer id = entero(accion.substring(9));
                if (id != null && productoDAO.eliminar(id)) aviso(req, "Producto eliminado del catálogo");
                else aviso(req, "No se pudo eliminar el producto");
            } else if (accion.equals("crear")) {
                crear(req);
            }
        } catch (IllegalStateException | ServletException e) {
            aviso(req, e.getMessage() != null && e.getMessage().length() < 120
                    ? e.getMessage() : "No se pudo guardar el producto (la foto debe pesar máximo 5 MB)");
        }
        irA(req, resp, "/admin/catalogo");
    }

    private void crear(HttpServletRequest req) throws IOException, ServletException {
        String nombre = texto(req, "nombre");
        String descripcion = texto(req, "descripcion");
        String etiqueta = texto(req, "etiqueta");
        Integer stock = entero(req.getParameter("stock"));
        Integer descuento = entero(req.getParameter("descuento"));
        Integer categoria = entero(req.getParameter("categoria"));
        BigDecimal precio = decimal(texto(req, "precio"));
        if (descuento == null) descuento = 0;

        String error = null;
        if (nombre.isEmpty() || nombre.length() > 100) error = "Escribe el nombre del producto (máximo 100 caracteres)";
        else if (descripcion.length() > 255) error = "La descripción es muy larga (máximo 255 caracteres)";
        else if (categoria == null || !categoriaExiste(categoria)) error = "Elige una categoría";
        else if (precio == null || precio.signum() <= 0 || precio.compareTo(new BigDecimal("999999")) > 0) error = "Escribe un precio válido";
        else if (stock == null || stock < 0 || stock > 99999) error = "Escribe un stock válido";
        else if (descuento < 0 || descuento > 90) error = "El descuento debe estar entre 0 y 90%";
        else if (!ETIQUETAS.contains(etiqueta)) error = "Etiqueta no válida";
        if (error != null) {
            aviso(req, error);
            return;
        }

        String imagen = Archivos.guardarImagen(req.getPart("imagen"));

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion.isEmpty() ? null : descripcion);
        p.setPrecio(precio.setScale(2, RoundingMode.HALF_UP));
        p.setStock(stock);
        p.setDescuento(descuento);
        p.setEtiqueta(etiqueta.isEmpty() ? null : etiqueta);
        p.setEmoji("🛍️");
        p.setRating(0.0);
        p.setResenas(0);
        p.setImagen(imagen);
        productoDAO.crear(p, categoria);
        aviso(req, "Producto agregado al catálogo ✨");
    }

    @SuppressWarnings("unchecked")
    private boolean categoriaExiste(int id) {
        for (Categoria c : (List<Categoria>) getServletContext().getAttribute("categorias")) {
            if (c.getId() == id) return true;
        }
        return false;
    }

    private static BigDecimal decimal(String texto) {
        try {
            return new BigDecimal(texto.replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
