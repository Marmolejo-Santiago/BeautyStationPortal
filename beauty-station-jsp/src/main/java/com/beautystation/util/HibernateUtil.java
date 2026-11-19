package com.beautystation.util;

import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory FABRICA = construir();

    private HibernateUtil() { }

    private static SessionFactory construir() {
        Configuration cfg = new Configuration().configure();
        propiedad(cfg, "BS_DB_URL", "hibernate.connection.url");
        propiedad(cfg, "BS_DB_USER", "hibernate.connection.username");
        propiedad(cfg, "BS_DB_PASS", "hibernate.connection.password");
        return cfg.buildSessionFactory();
    }

    private static void propiedad(Configuration cfg, String variable, String clave) {
        String valor = System.getenv(variable);
        if (valor != null && !valor.isEmpty()) cfg.setProperty(clave, valor);
    }

    public static SessionFactory getSessionFactory() {
        return FABRICA;
    }

    public static <R> R ejecutar(Function<Session, R> trabajo) {
        Session sesion = FABRICA.openSession();
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            R resultado = trabajo.apply(sesion);
            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            sesion.close();
        }
    }

    public static void ejecutarSinResultado(Consumer<Session> trabajo) {
        ejecutar(sesion -> {
            trabajo.accept(sesion);
            return null;
        });
    }

    public static void cerrar() {
        if (FABRICA != null && !FABRICA.isClosed()) FABRICA.close();
    }
}
