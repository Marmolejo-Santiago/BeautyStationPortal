package com.beautystation.util;

import com.beautystation.datos.Datos;
import com.beautystation.modelo.Cita;
import java.util.ArrayList;
import java.util.List;

public final class Agenda {

    private Agenda() { }

    public static int bloques(int minutos) {
        return Math.max(1, (int) Math.ceil(minutos / 60.0));
    }

    public static List<String> noDisponibles(List<Cita> citasDelDia, int minutos) {
        List<String> horarios = Datos.HORARIOS;
        int total = horarios.size();
        boolean[] ocupado = new boolean[total];
        for (Cita c : citasDelDia) {
            int inicio = horarios.indexOf(c.getHora());
            if (inicio < 0) continue;
            for (int k = 0; k < bloques(c.getDuracion()) && inicio + k < total; k++) ocupado[inicio + k] = true;
        }
        int n = bloques(minutos);
        List<String> no = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            boolean libre = i + n <= total;
            for (int k = 0; libre && k < n; k++) {
                if (ocupado[i + k]) libre = false;
            }
            if (!libre) no.add(horarios.get(i));
        }
        return no;
    }
}
