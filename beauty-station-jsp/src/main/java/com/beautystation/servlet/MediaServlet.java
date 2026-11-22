package com.beautystation.servlet;

import com.beautystation.util.Archivos;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/media/*")
public class MediaServlet extends HttpServlet {

    private static final Pattern NOMBRE = Pattern.compile("^/([a-f0-9]{32}\\.(jpg|png|webp|gif))$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String info = req.getPathInfo();
        java.util.regex.Matcher m = info == null ? null : NOMBRE.matcher(info);
        if (m == null || !m.matches()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path archivo = Archivos.carpeta().resolve(m.group(1));
        if (!Files.isRegularFile(archivo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String tipo;
        switch (m.group(2)) {
            case "png":  tipo = "image/png"; break;
            case "webp": tipo = "image/webp"; break;
            case "gif":  tipo = "image/gif"; break;
            default:     tipo = "image/jpeg";
        }
        resp.setContentType(tipo);
        resp.setContentLengthLong(Files.size(archivo));
        resp.setHeader("Cache-Control", "public, max-age=86400");
        Files.copy(archivo, resp.getOutputStream());
    }
}
