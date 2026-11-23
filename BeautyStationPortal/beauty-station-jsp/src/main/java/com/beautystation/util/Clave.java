package com.beautystation.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Clave {

    private static final int ITERACIONES = 65536;

    private Clave() { }

    public static String hash(String clave) {
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        byte[] h = derivar(clave, sal, ITERACIONES);
        return "pbkdf2$" + ITERACIONES + "$" + Base64.getEncoder().encodeToString(sal)
                + "$" + Base64.getEncoder().encodeToString(h);
    }

    public static boolean verificar(String clave, String guardada) {
        if (clave == null || guardada == null) return false;
        String[] p = guardada.split("\\$");
        if (p.length != 4 || !p[0].equals("pbkdf2")) return false;
        try {
            byte[] esperado = Base64.getDecoder().decode(p[3]);
            byte[] calculado = derivar(clave, Base64.getDecoder().decode(p[2]), Integer.parseInt(p[1]));
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(String clave, byte[] sal, int iteraciones) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return f.generateSecret(new PBEKeySpec(clave.toCharArray(), sal, iteraciones, 256)).getEncoded();
        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}
