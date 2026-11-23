package com.beautystation.util;

import java.util.List;
import java.util.regex.Pattern;

public final class Validar {

    public static final List<String> GENEROS = List.of("Femenino", "Masculino", "Otro", "Prefiero no decirlo");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9+()\\-\\s]{7,20}$");
    private static final Pattern CP = Pattern.compile("^[0-9]{5}$");

    private Validar() { }

    public static boolean email(String s) { return s.length() <= 100 && EMAIL.matcher(s).matches(); }

    public static boolean telefono(String s) { return s.isEmpty() || TELEFONO.matcher(s).matches(); }

    public static boolean codigoPostal(String s) { return CP.matcher(s).matches(); }

    public static boolean nombre(String s) { return !s.isBlank() && s.length() <= 100; }

    public static boolean edad(int e) { return e >= 12 && e <= 120; }

    public static boolean tarjeta(String digitos) {
        if (!digitos.matches("[0-9]{13,19}")) return false;
        int suma = 0;
        boolean doble = false;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int d = digitos.charAt(i) - '0';
            if (doble) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            suma += d;
            doble = !doble;
        }
        return suma % 10 == 0;
    }

    public static String marcaTarjeta(String digitos) {
        if (digitos.startsWith("4")) return "VISA";
        int dos = Integer.parseInt(digitos.substring(0, 2));
        int cuatro = Integer.parseInt(digitos.substring(0, 4));
        if ((dos >= 51 && dos <= 55) || (cuatro >= 2221 && cuatro <= 2720)) return "MASTERCARD";
        if (dos == 34 || dos == 37) return "AMEX";
        return "TARJETA";
    }
}
