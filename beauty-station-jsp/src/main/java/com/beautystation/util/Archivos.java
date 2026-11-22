package com.beautystation.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.Part;

/** Guarda las fotos que sube la administradora (fuera de la carpeta del .war para que sobrevivan a cada despliegue). */
public final class Archivos {

    public static final long MAX_BYTES = 5L * 1024 * 1024;
    private static final Set<String> EXTENSIONES = Set.of("jpg", "jpeg", "png", "webp", "gif");

    private Archivos() { }

    /** Carpeta de fotos subidas: variable BS_UPLOADS o ~/beauty-station-uploads. */
    public static Path carpeta() {
        String v = System.getenv("BS_UPLOADS");
        Path p = (v != null && !v.isEmpty()) ? Paths.get(v) : Paths.get(System.getProperty("user.home"), "beauty-station-uploads");
        return p.toAbsolutePath().normalize();
    }

    /**
     * Guarda la imagen y devuelve su ruta relativa a la aplicación ("media/xxxx.jpg").
     * Devuelve null si no se eligió ningún archivo; lanza IllegalStateException si no es una imagen válida.
     */
    public static String guardarImagen(Part parte) throws IOException {
        if (parte == null || parte.getSize() == 0) return null;
        if (parte.getSize() > MAX_BYTES) throw new IllegalStateException("La imagen pesa más de 5 MB");

        String original = parte.getSubmittedFileName() == null ? "" : parte.getSubmittedFileName();
        int punto = original.lastIndexOf('.');
        String ext = punto < 0 ? "" : original.substring(punto + 1).toLowerCase(Locale.ROOT);
        if (!EXTENSIONES.contains(ext)) throw new IllegalStateException("Sube una imagen JPG, PNG, WEBP o GIF");

        byte[] cabecera = new byte[12];
        try (InputStream in = parte.getInputStream()) {
            in.readNBytes(cabecera, 0, cabecera.length);
        }
        if (!esImagen(cabecera)) throw new IllegalStateException("El archivo no es una imagen válida");

        Files.createDirectories(carpeta());
        String archivo = UUID.randomUUID().toString().replace("-", "") + "." + (ext.equals("jpeg") ? "jpg" : ext);
        try (InputStream in = parte.getInputStream()) {
            Files.copy(in, carpeta().resolve(archivo));
        }
        return "media/" + archivo;
    }

    private static boolean esImagen(byte[] b) {
        boolean jpg = (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
        boolean png = (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G';
        boolean gif = b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8';
        boolean webp = b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F'
                && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P';
        return jpg || png || gif || webp;
    }
}
