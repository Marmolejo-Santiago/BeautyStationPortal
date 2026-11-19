package com.beautystation.modelo;

import javax.servlet.http.HttpSession;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class EstadoApp {

    public static final String ATRIBUTO = "app";

    private final Set<Integer> favoritos = new HashSet<>();

    private YearMonth cal = YearMonth.now();
    private YearMonth mini = YearMonth.now();
    private FormCita form = new FormCita();

    private int precioMax = 2000;
    private final Set<String> catChecks = new LinkedHashSet<>();
    private String orden = "recientes";
    private String busqueda = "";

    private String filtroCitas = "Todas";
    private String filtroPedidos = "Todos";

    public static EstadoApp de(HttpSession sesion) {
        EstadoApp e = (EstadoApp) sesion.getAttribute(ATRIBUTO);
        if (e == null) {
            e = new EstadoApp();
            sesion.setAttribute(ATRIBUTO, e);
        }
        return e;
    }

    public void limpiarFiltros() {
        catChecks.clear();
        precioMax = 2000;
    }

    public void reiniciarForm() { form = new FormCita(); }

    public Set<Integer> getFavoritos() { return favoritos; }
    public YearMonth getCal() { return cal; }
    public void setCal(YearMonth cal) { this.cal = cal; }
    public YearMonth getMini() { return mini; }
    public void setMini(YearMonth mini) { this.mini = mini; }
    public FormCita getForm() { return form; }
    public int getPrecioMax() { return precioMax; }
    public void setPrecioMax(int precioMax) { this.precioMax = precioMax; }
    public Set<String> getCatChecks() { return catChecks; }
    public String getOrden() { return orden; }
    public void setOrden(String orden) { this.orden = orden; }
    public String getBusqueda() { return busqueda; }
    public void setBusqueda(String busqueda) { this.busqueda = busqueda; }
    public String getFiltroCitas() { return filtroCitas; }
    public void setFiltroCitas(String filtroCitas) { this.filtroCitas = filtroCitas; }
    public String getFiltroPedidos() { return filtroPedidos; }
    public void setFiltroPedidos(String filtroPedidos) { this.filtroPedidos = filtroPedidos; }
}
