package com.beautystation.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Iconos {

    private static final Map<String, String> PATHS;

    static {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("home", "<path d=\"M3 11l9-8 9 8v10a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1z\"/>");
        m.put("calendar", "<rect x=\"3\" y=\"5\" width=\"18\" height=\"16\" rx=\"2\"/><path d=\"M16 3v4M8 3v4M3 10h18\"/>");
        m.put("tag", "<path d=\"M20 12l-8 8-9-9V3h8z\"/><circle cx=\"7.5\" cy=\"7.5\" r=\"1.5\"/>");
        m.put("info", "<circle cx=\"12\" cy=\"12\" r=\"9\"/><path d=\"M12 11v6M12 7.5v.01\"/>");
        m.put("percent", "<path d=\"M19 5L5 19\"/><circle cx=\"7\" cy=\"7\" r=\"2\"/><circle cx=\"17\" cy=\"17\" r=\"2\"/>");
        m.put("box", "<path d=\"M21 8l-9-5-9 5 9 5z\"/><path d=\"M3 8v8l9 5 9-5V8M12 13v8\"/>");
        m.put("search", "<circle cx=\"11\" cy=\"11\" r=\"7\"/><path d=\"M21 21l-4.3-4.3\"/>");
        m.put("bell", "<path d=\"M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9M13.7 21a2 2 0 0 1-3.4 0\"/>");
        m.put("cart", "<circle cx=\"9\" cy=\"21\" r=\"1\"/><circle cx=\"20\" cy=\"21\" r=\"1\"/><path d=\"M1 1h4l2.7 13.4a2 2 0 0 0 2 1.6h9.7a2 2 0 0 0 2-1.6L23 6H6\"/>");
        m.put("heart", "<path d=\"M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1-1.1a5.5 5.5 0 0 0-7.8 7.8l1 1.1L12 21l7.8-7.5 1-1.1a5.5 5.5 0 0 0 0-7.8z\"/>");
        m.put("user", "<circle cx=\"12\" cy=\"8\" r=\"4\"/><path d=\"M4 21a8 8 0 0 1 16 0\"/>");
        m.put("logout", "<path d=\"M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9\"/>");
        m.put("chev", "<path d=\"M9 6l6 6-6 6\"/>");
        m.put("chevl", "<path d=\"M15 6l-6 6 6 6\"/>");
        m.put("check", "<circle cx=\"12\" cy=\"12\" r=\"9\"/><path d=\"M8 12l3 3 5-6\"/>");
        m.put("clock", "<circle cx=\"12\" cy=\"12\" r=\"9\"/><path d=\"M12 7v5l3 3\"/>");
        m.put("pin", "<path d=\"M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0 1 18 0z\"/><circle cx=\"12\" cy=\"10\" r=\"3\"/>");
        m.put("truck", "<rect x=\"1\" y=\"3\" width=\"15\" height=\"13\"/><path d=\"M16 8h4l3 3v5h-7z\"/><circle cx=\"5.5\" cy=\"18.5\" r=\"2.5\"/><circle cx=\"18.5\" cy=\"18.5\" r=\"2.5\"/>");
        m.put("x", "<circle cx=\"12\" cy=\"12\" r=\"9\"/><path d=\"M15 9l-6 6M9 9l6 6\"/>");
        m.put("shield", "<path d=\"M12 3l8 3v6c0 5-3.5 8-8 9-4.5-1-8-4-8-9V6z\"/><path d=\"M9 12l2 2 4-4\"/>");
        m.put("star", "<path d=\"M12 3l2.7 5.6 6.1.9-4.4 4.3 1 6.1L12 17l-5.4 2.9 1-6.1L3.2 9.5l6.1-.9z\"/>");
        m.put("phone", "<path d=\"M22 17v3a2 2 0 0 1-2.2 2A19.8 19.8 0 0 1 2 4.2 2 2 0 0 1 4 2h3a2 2 0 0 1 2 1.7c.1 1 .4 1.9.7 2.8a2 2 0 0 1-.5 2.1L8 9.9a16 16 0 0 0 6 6l1.3-1.3a2 2 0 0 1 2.1-.4c.9.3 1.8.6 2.8.7A2 2 0 0 1 22 17z\"/>");
        m.put("mail", "<rect x=\"3\" y=\"5\" width=\"18\" height=\"14\" rx=\"2\"/><path d=\"M3 7l9 6 9-6\"/>");
        m.put("insta", "<rect x=\"3\" y=\"3\" width=\"18\" height=\"18\" rx=\"5\"/><circle cx=\"12\" cy=\"12\" r=\"4\"/><path d=\"M17.5 6.5v.01\"/>");
        m.put("lock", "<rect x=\"4\" y=\"11\" width=\"16\" height=\"10\" rx=\"2\"/><path d=\"M8 11V7a4 4 0 0 1 8 0v4\"/>");
        m.put("eye", "<path d=\"M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z\"/><circle cx=\"12\" cy=\"12\" r=\"3\"/>");
        m.put("grid", "<rect x=\"3\" y=\"3\" width=\"7\" height=\"7\" rx=\"1\"/><rect x=\"14\" y=\"3\" width=\"7\" height=\"7\" rx=\"1\"/><rect x=\"3\" y=\"14\" width=\"7\" height=\"7\" rx=\"1\"/><rect x=\"14\" y=\"14\" width=\"7\" height=\"7\" rx=\"1\"/>");
        m.put("edit", "<path d=\"M12 20h9M16.5 3.5a2.1 2.1 0 0 1 3 3L7 19l-4 1 1-4z\"/>");
        m.put("trash", "<path d=\"M3 6h18M8 6V4h8v2M19 6l-1 14H6L5 6\"/>");
        m.put("headset", "<path d=\"M3 14v-2a9 9 0 0 1 18 0v2\"/><rect x=\"2\" y=\"14\" width=\"5\" height=\"7\" rx=\"2\"/><rect x=\"17\" y=\"14\" width=\"5\" height=\"7\" rx=\"2\"/>");
        m.put("card", "<rect x=\"2\" y=\"5\" width=\"20\" height=\"14\" rx=\"2\"/><path d=\"M2 10h20\"/>");
        m.put("gift", "<rect x=\"3\" y=\"8\" width=\"18\" height=\"4\"/><path d=\"M12 8v13M5 12v9h14v-9M12 8c-2-4-6-3-5 0 .5 1.5 5 0 5 0zm0 0c2-4 6-3 5 0-.5 1.5-5 0-5 0z\"/>");
        PATHS = Collections.unmodifiableMap(m);
    }

    private Iconos() { }

    public static Map<String, String> todos() {
        return PATHS;
    }
}
