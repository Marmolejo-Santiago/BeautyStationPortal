package com.example.beautystation;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Versión móvil de Beauty Station: muestra el portal web (JSP en Tomcat) dentro de un WebView y le aplica,
 * solo desde la app, el diseño móvil del Look and Feel (barra inferior, tarjetas apiladas, etc.).
 * El proyecto web no se modifica: mobile.css y mobile.js (carpeta assets) se inyectan en cada página.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "beauty_station";
    private static final String CLAVE_URL = "url_servidor";
    private static final String PAGINA_ERROR = "file:///android_asset/error.html";

    private WebView webView;
    private SwipeRefreshLayout refrescar;
    private ProgressBar progreso;

    private String cssMovil = "";
    private String jsMovil = "";

    private ValueCallback<Uri[]> selectorArchivos;
    private ActivityResultLauncher<Intent> lanzadorArchivos;

    private boolean mostrandoError = false;
    private boolean limpiarHistorial = false;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webViewBeauty);
        refrescar = findViewById(R.id.refrescar);
        progreso = findViewById(R.id.progreso);

        cssMovil = leerAsset("mobile.css");
        jsMovil = leerAsset("mobile.js");

        // Selector de archivos: lo necesita el panel de administración para subir la foto de un producto
        lanzadorArchivos = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
            if (selectorArchivos == null) return;
            Uri[] uris = WebChromeClient.FileChooserParams.parseResult(resultado.getResultCode(), resultado.getData());
            selectorArchivos.onReceiveValue(uris);
            selectorArchivos = null;
        });

        webView.setBackgroundColor(Color.parseColor("#F9F9F9"));
        WebSettings ajustes = webView.getSettings();
        ajustes.setJavaScriptEnabled(true);
        ajustes.setDomStorageEnabled(true);
        ajustes.setUseWideViewPort(true);
        ajustes.setLoadWithOverviewMode(false);
        ajustes.setSupportZoom(false);
        ajustes.setBuiltInZoomControls(false);
        ajustes.setTextZoom(100);
        ajustes.setAllowFileAccess(false);          // las páginas de assets siguen abriendo con file:///android_asset
        ajustes.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        ajustes.setUserAgentString(ajustes.getUserAgentString() + " BeautyStationApp/1.0");

        CookieManager.getInstance().setAcceptCookie(true);   // mantiene la sesión (JSESSIONID) del servidor
        webView.addJavascriptInterface(new Puente(), "BSApp");

        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new ClienteWeb());
        webView.setWebChromeClient(new ClienteChrome());

        // Deslizar hacia abajo para recargar, solo cuando la página está arriba del todo
        refrescar.setColorSchemeResources(R.color.bs_rosa_intenso);
        refrescar.setOnRefreshListener(() -> webView.reload());
        refrescar.setOnChildScrollUpCallback((padre, hijo) -> webView.getScrollY() > 0);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!mostrandoError && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            cargarInicio();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle salida) {
        super.onSaveInstanceState(salida);
        webView.saveState(salida);
    }

    @Override
    protected void onPause() {
        CookieManager.getInstance().flush();
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (selectorArchivos != null) {
            selectorArchivos.onReceiveValue(null);
            selectorArchivos = null;
        }
        webView.destroy();
        super.onDestroy();
    }

    // ------------------------------------------------------------------ servidor

    private SharedPreferences prefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** Dirección del portal: la guardada por el usuario o, si no hay, la de strings.xml (emulador). */
    private String urlBase() {
        return prefs().getString(CLAVE_URL, getString(R.string.url_servidor));
    }

    private void cargarInicio() {
        mostrandoError = false;
        limpiarHistorial = true;
        webView.loadUrl(urlBase());
    }

    private boolean mismoServidor(Uri uri) {
        Uri base = Uri.parse(urlBase());
        return base.getHost() != null && base.getHost().equalsIgnoreCase(uri.getHost()) && base.getPort() == uri.getPort();
    }

    private String normalizarUrl(String texto) {
        String url = texto == null ? "" : texto.trim();
        if (url.isEmpty()) return null;
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
        while (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return Uri.parse(url).getHost() == null ? null : url;
    }

    private void dialogoServidor() {
        final EditText campo = new EditText(this);
        campo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        campo.setSingleLine(true);
        campo.setText(urlBase());
        campo.setSelectAllOnFocus(true);
        int margen = (int) (20 * getResources().getDisplayMetrics().density);
        FrameLayout contenedor = new FrameLayout(this);
        contenedor.setPadding(margen, margen / 2, margen, 0);
        contenedor.addView(campo);

        new AlertDialog.Builder(this)
                .setTitle(R.string.servidor_titulo)
                .setMessage(R.string.servidor_mensaje)
                .setView(contenedor)
                .setPositiveButton(R.string.guardar, (dialogo, boton) -> {
                    String url = normalizarUrl(campo.getText().toString());
                    if (url == null) {
                        Toast.makeText(this, R.string.direccion_invalida, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prefs().edit().putString(CLAVE_URL, url).apply();
                    cargarInicio();
                })
                .setNeutralButton(R.string.restablecer, (dialogo, boton) -> {
                    prefs().edit().remove(CLAVE_URL).apply();
                    cargarInicio();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    // ------------------------------------------------------------------ inyección del diseño móvil

    private String leerAsset(String nombre) {
        try (InputStream in = getAssets().open(nombre)) {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) salida.write(buffer, 0, n);
            return new String(salida.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    private void inyectar(WebView vista, String url) {
        if (url == null || url.startsWith("file:")) return;   // la página de error trae su propio estilo
        String js = "(function(){try{if(!document.getElementById('bs-mobile-css')){"
                + "var s=document.createElement('style');s.id='bs-mobile-css';s.textContent=" + JSONObject.quote(cssMovil) + ";"
                + "(document.head||document.documentElement).appendChild(s);}}catch(e){}})();"
                + jsMovil;
        vista.evaluateJavascript(js, null);
    }

    private void mostrarWeb() {
        webView.setVisibility(View.VISIBLE);
    }

    // ------------------------------------------------------------------ clientes del WebView

    private class ClienteWeb extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView vista, WebResourceRequest peticion) {
            Uri uri = peticion.getUrl();
            String esquema = uri.getScheme();
            if ("file".equals(esquema)) return false;
            if (("http".equals(esquema) || "https".equals(esquema)) && mismoServidor(uri)) return false;
            try {
                // Enlaces a otros sitios (Google Maps, teléfono, correo...) se abren fuera de la app
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, R.string.no_se_puede_abrir, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView vista, String url, Bitmap icono) {
            super.onPageStarted(vista, url, icono);
            if (url != null && url.startsWith("file:")) {
                mostrarWeb();
                return;
            }
            mostrandoError = false;
            // Se oculta un instante para no enseñar la versión de escritorio antes de aplicar el diseño móvil
            vista.setVisibility(View.INVISIBLE);
            vista.postDelayed(MainActivity.this::mostrarWeb, 4000);   // red de seguridad
        }

        @Override
        public void onPageCommitVisible(WebView vista, String url) {
            super.onPageCommitVisible(vista, url);
            inyectar(vista, url);
            vista.postDelayed(MainActivity.this::mostrarWeb, 60);
        }

        @Override
        public void onPageFinished(WebView vista, String url) {
            super.onPageFinished(vista, url);
            inyectar(vista, url);
            mostrarWeb();
            refrescar.setRefreshing(false);
            if (limpiarHistorial && url != null && !url.startsWith("file:")) {
                limpiarHistorial = false;
                vista.clearHistory();      // así "atrás" nunca regresa a la pantalla de error
            }
        }

        @Override
        public void onReceivedError(WebView vista, WebResourceRequest peticion, WebResourceError error) {
            super.onReceivedError(vista, peticion, error);
            if (peticion.isForMainFrame()) {
                mostrandoError = true;
                refrescar.setRefreshing(false);
                vista.loadUrl(PAGINA_ERROR);
            }
        }
    }

    private class ClienteChrome extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView vista, int avance) {
            progreso.setProgress(avance);
            progreso.setVisibility(avance < 100 ? View.VISIBLE : View.GONE);
        }

        @Override
        public boolean onShowFileChooser(WebView vista, ValueCallback<Uri[]> callback, FileChooserParams parametros) {
            if (selectorArchivos != null) selectorArchivos.onReceiveValue(null);
            selectorArchivos = callback;
            try {
                lanzadorArchivos.launch(parametros.createIntent());
            } catch (ActivityNotFoundException e) {
                selectorArchivos = null;
                return false;
            }
            return true;
        }
    }

    /** Funciones que la página local de error (assets/error.html) puede llamar. */
    private class Puente {
        @JavascriptInterface
        public void reintentar() {
            runOnUiThread(MainActivity.this::cargarInicio);
        }

        @JavascriptInterface
        public void cambiarServidor() {
            runOnUiThread(MainActivity.this::dialogoServidor);
        }

        @JavascriptInterface
        public String servidor() {
            return urlBase();
        }
    }
}
