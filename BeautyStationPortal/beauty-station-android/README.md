# Beauty Station · App Android

Versión móvil del portal. Es un proyecto de **Android Studio** independiente que abre el portal web (`beauty-station-jsp`) dentro de un `WebView` y le aplica el diseño móvil del Look and Feel **solo desde la app**: el proyecto web no se modificó.

Requiere: Android Studio (Hedgehog o más nuevo, trae JDK 17) y Android 8.0+ (API 26).

## Cómo correrla
1. Levanta el portal web (Tomcat 9 con `beauty-station`, y MySQL con `emprendedora_db`) como siempre.
2. En Android Studio: **File → Open** y elige la carpeta `beauty-station-android`. Espera la sincronización de Gradle.
3. Crea un emulador (o conecta un teléfono) y presiona **Run**.

El emulador ve tu PC como `10.0.2.2`, así que por defecto abre `http://10.0.2.2:8080/beauty-station`. Esa dirección está en `app/src/main/res/values/strings.xml` (`url_servidor`).

### En un teléfono real
`10.0.2.2` no funciona fuera del emulador. Opciones:
- Con la app abierta y sin conexión aparece una pantalla con **Cambiar servidor**: escribe `http://IP-de-tu-PC:8080/beauty-station` (celular y PC en el mismo Wi-Fi, y permite el puerto 8080 en el firewall). Queda guardada.
- O por USB: `adb reverse tcp:8080 tcp:8080` y usa `http://localhost:8080/beauty-station` en **Cambiar servidor**.

## Qué hace la app
- **Diseño móvil**: barra inferior (Inicio · Citas · Catálogo · Promos · Usuario), tarjetas y productos apilados (productos de 2 en 2), categorías deslizables, filtros del catálogo plegables, inicio de sesión con la foto abajo. Tocar el logo **BS** de la barra superior abre el menú con todas las secciones (Acerca de, Mis citas, Mis pedidos, Cerrar sesión).
- **Cuenta de administración**: al entrar con la cuenta admin la barra inferior muestra Citas · Pedidos · Catálogo · Salir, y el panel funciona igual (incluida la subida de la foto de un producto desde la galería).
- Mantiene la sesión, botón **atrás** navega por el historial, deslizar hacia abajo recarga, los enlaces a otros sitios (Google Maps, etc.) se abren fuera de la app.

## Cómo está armado
| Archivo | Para qué |
|---|---|
| `MainActivity.java` | WebView, sesión, selector de archivos, errores de conexión, inyección del diseño |
| `assets/mobile.css` | Estilos móviles (colores y tipografía vienen de la web; aquí solo layout) |
| `assets/mobile.js` | Barra inferior, menú del logo, filtros plegables |
| `assets/error.html` | Pantalla cuando no hay conexión con el servidor |

Para ajustar el diseño móvil edita `mobile.css` / `mobile.js`; no hace falta tocar la web.

> El tráfico http (sin certificado) está permitido en `res/xml/network_security_config.xml` porque Tomcat corre en desarrollo sin https. Si lo publicas, usa https y quita ese permiso.

## Solución de problemas
**"Incompatible Gradle JVM version"**: el proyecto usa Gradle 9.3.1 y el plugin de Android 9.1.1, que funcionan con JDK 17 a 25. Si aun así aparece el aviso, en *Settings → Build, Execution, Deployment → Build Tools → Gradle* elige en **Gradle JDK** el **jbr-21** que trae Android Studio, aplica y sincroniza (*File → Sync Project with Gradle Files*). Si Studio pide instalar el SDK de Android 36 o las Build Tools 36, acepta la instalación.
