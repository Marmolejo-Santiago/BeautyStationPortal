package com.beautystation.util;

import com.beautystation.modelo.Celda;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class Fmt {

    private static final Locale MX = Locale.forLanguageTag("es-MX");
    private static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String[] MESES_CORTO = {"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago",
            "sep", "oct", "nov", "dic"};
    private static final String[] DIAS = {"lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo"};
    private static final String[] DIAS_CORTO = {"LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"};

    private Fmt() { }

    public static String money(double n) {
        return String.format(MX, "$%,.2f", n);
    }

    public static String money0(double n) {
        return String.format(MX, "$%,d", (long) n);
    }

    public static String stars(double r) {
        int n = (int) Math.round(r);
        return "★".repeat(n) + "☆".repeat(5 - n);
    }

    public static String mesAnio(YearMonth ym) {
        return MESES[ym.getMonthValue() - 1] + " " + ym.getYear();
    }

    public static String fechaLarga(LocalDate d) {
        return DIAS[d.getDayOfWeek().getValue() - 1] + ", " + d.getDayOfMonth() + " de "
                + MESES[d.getMonthValue() - 1].toLowerCase(MX) + " de " + d.getYear();
    }

    public static String fechaLarga(String iso) {
        return fechaLarga(LocalDate.parse(iso));
    }

    public static String fechaCorta(LocalDate d) {
        return String.format("%02d %s. %d", d.getDayOfMonth(), MESES_CORTO[d.getMonthValue() - 1], d.getYear());
    }

    public static String diaSemana(LocalDate d) {
        return DIAS_CORTO[d.getDayOfWeek().getValue() - 1];
    }

    public static String mesCorto(LocalDate d) {
        return MESES_CORTO[d.getMonthValue() - 1];
    }

    public static List<Celda> celdas(YearMonth ym, String seleccionado, Collection<String> marcas,
                                     boolean deshabilitarPasado) {
        List<Celda> celdas = new ArrayList<>();
        int offset = ym.atDay(1).getDayOfWeek().getValue() - 1;
        int dias = ym.lengthOfMonth();
        int diasPrevios = ym.minusMonths(1).lengthOfMonth();
        String hoy = LocalDate.now().toString();

        for (int i = offset - 1; i >= 0; i--) {
            celdas.add(new Celda(true, diasPrevios - i, null, null, false));
        }
        for (int d = 1; d <= dias; d++) {
            LocalDate fecha = ym.atDay(d);
            String clave = fecha.toString();
            boolean finDeSemana = fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean pasado = deshabilitarPasado && clave.compareTo(hoy) < 0;
            StringBuilder clases = new StringBuilder("d");
            if (finDeSemana) clases.append(" wk");
            if (pasado) clases.append(" past");
            if (clave.equals(seleccionado)) clases.append(" sel");
            if (clave.equals(hoy)) clases.append(" today");
            if (marcas != null && marcas.contains(clave)) clases.append(" mark");
            celdas.add(new Celda(false, d, clave, clases.toString(), pasado));
        }
        int resto = (7 - ((offset + dias) % 7)) % 7;
        for (int i = 1; i <= resto; i++) {
            celdas.add(new Celda(true, i, null, null, false));
        }
        return celdas;
    }
}
