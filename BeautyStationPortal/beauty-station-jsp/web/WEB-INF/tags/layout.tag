<%@ tag pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bs" uri="https://beautystation.com/funciones" %>
<%@ attribute name="ruta" required="true" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>

  <div id="app" class="app">
    <aside class="sidebar">
      <div class="logo logo-md"><span>B</span><span>S</span></div>
      <nav id="nav" class="nav nav-pills flex-column gap-1">
        <c:forEach var="r" items="${applicationScope.rutas}">
          <c:if test="${not r.oculta}"><a class="nav-link ${ruta == r.clave ? 'active' : ''}" href="${ctx}/${r.clave}"><t:ic n="${r.icono}" s="19"/>${r.etiqueta}</a></c:if>
        </c:forEach>
      </nav>
      <div class="sidebar-user">
        <a href="${ctx}/usuario" class="user-pill ${ruta == 'usuario' ? 'active' : ''}"><span class="avatar"><t:ic n="user"/></span><b>Usuario</b></a>
        <button id="logout" class="logout" form="acciones" formaction="${ctx}/logout"><span><t:ic n="logout"/></span> Cerrar sesión</button>
      </div>
    </aside>

    <div class="content">
      <header class="topbar d-flex align-items-center gap-3 mb-4">
        <form class="search" action="${ctx}/catalogo" method="get" role="search">
          <span><t:ic n="search"/></span>
          <input id="search-input" name="q" type="text" placeholder="${applicationScope.rutasPorClave[ruta].placeholder}" value="<c:out value='${sessionScope.app.busqueda}'/>">
        </form>
        <a class="icon-btn" href="#notificaciones" aria-label="Notificaciones"><span><t:ic n="bell"/></span><c:if test="${noLeidas > 0}"><em class="dot"></em></c:if></a>
        <a class="icon-btn" href="#carrito" aria-label="Carrito"><span><t:ic n="cart"/></span><em class="count" id="cart-count">${carritoCantidad}</em></a>
      </header>
      <main id="view"><div class="page-in"><jsp:doBody/></div></main>
    </div>
  </div>

  <form id="acciones" method="post" hidden>
    <input type="hidden" name="volver" value="<c:out value='${volver}'/>">
  </form>

  <div class="modal fade" id="notificaciones" tabindex="-1">
    <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"><div class="modal-content">
      <div class="modal-header"><h5 class="modal-title fw-bold">Notificaciones</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
      <div class="modal-body">
        <c:choose>
          <c:when test="${not empty notificaciones}">
            <c:forEach var="n" items="${notificaciones}">
              <div class="notif ${n.leida ? '' : 'nueva'}"><span class="e">${n.emoji}</span>
                <div><b class="small d-block"><c:out value="${n.titulo}"/></b><span class="small"><c:out value="${n.mensaje}"/></span><small class="d-block text-body-secondary">${n.fechaTexto}</small></div></div>
            </c:forEach>
          </c:when>
          <c:otherwise><div class="empty">No tienes notificaciones.</div></c:otherwise>
        </c:choose>
      </div>
      <div class="modal-footer">
        <c:if test="${noLeidas > 0}"><button type="submit" class="btn btn-outline-primary" form="acciones" formaction="${ctx}/notificaciones" name="accion" value="leer">Marcar como leídas</button></c:if>
        <a class="btn btn-outline-primary" href="#cerrar">Cerrar</a>
      </div>
    </div></div>
  </div>

  <div class="modal fade" id="carrito" tabindex="-1">
    <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"><div class="modal-content">
      <div class="modal-header"><h5 class="modal-title fw-bold">Mi carrito</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
      <div class="modal-body">
        <c:choose>
          <c:when test="${not empty carritoItems}">
            <c:forEach var="i" items="${carritoItems}">
              <div class="cart-row">
                <span class="pic"><t:foto src="${i.producto.imagen}" emoji="${i.producto.emoji}" alt="${i.producto.nombre}"/></span>
                <div class="flex-fill"><b class="small d-block">${i.producto.nombre}</b><small class="text-body-secondary">${bs:money(i.producto.precioFinal)}</small></div>
                <div class="qty">
                  <button type="submit" class="btn btn-outline-primary btn-sm" form="acciones" formaction="${ctx}/carrito" name="accion" value="menos:${i.id}" aria-label="Quitar uno">−</button>
                  <span>${i.cantidad}</span>
                  <button type="submit" class="btn btn-outline-primary btn-sm" form="acciones" formaction="${ctx}/carrito" name="accion" value="mas:${i.id}" aria-label="Agregar uno">+</button>
                </div>
                <b class="small cart-sub">${bs:money(i.subtotal)}</b>
                <button type="submit" class="btn btn-outline-danger btn-sm" form="acciones" formaction="${ctx}/carrito" name="accion" value="quitar:${i.id}" aria-label="Quitar del carrito"><t:ic n="trash" s="14"/></button>
              </div>
            </c:forEach>
            <div class="d-flex justify-content-between mt-3 mb-2"><b>Total</b><b>${bs:money(carritoTotal)}</b></div>
            <c:choose>
              <c:when test="${not empty direcciones and not empty metodosPago}">
                <label class="form-label small fw-bold mb-1" for="cart-dir">Dirección de envío</label>
                <select class="form-select form-select-sm mb-2" id="cart-dir" name="direccion" form="acciones">
                  <c:forEach var="d" items="${direcciones}"><option value="${d.id}"><c:out value="${d.etiqueta}"/> — <c:out value="${d.texto}"/></option></c:forEach>
                </select>
                <label class="form-label small fw-bold mb-1" for="cart-pago">Método de pago</label>
                <select class="form-select form-select-sm" id="cart-pago" name="metodo" form="acciones">
                  <c:forEach var="m" items="${metodosPago}"><option value="${m.id}">${m.marca} **** ${m.ultimos4}</option></c:forEach>
                </select>
              </c:when>
              <c:otherwise><p class="small text-body-secondary mb-0">Agrega una dirección y un método de pago en <a class="link" href="${ctx}/usuario">Usuario</a> para finalizar tu compra.</p></c:otherwise>
            </c:choose>
          </c:when>
          <c:otherwise><div class="empty">Tu carrito está vacío.</div></c:otherwise>
        </c:choose>
      </div>
      <div class="modal-footer">
        <c:if test="${not empty carritoItems}">
          <button type="submit" class="btn btn-outline-primary" form="acciones" formaction="${ctx}/carrito" name="accion" value="vaciar">Vaciar</button>
          <c:if test="${not empty direcciones and not empty metodosPago}"><button type="submit" class="btn btn-primary" form="acciones" formaction="${ctx}/carrito" name="accion" value="comprar">Realizar pedido →</button></c:if>
        </c:if>
        <a class="btn btn-outline-primary" href="#cerrar">Cerrar</a>
      </div>
    </div></div>
  </div>

  <c:if test="${not empty sessionScope.aviso}">
    <div class="toast-container position-fixed bottom-0 start-50 translate-middle-x p-3">
      <div id="toast" class="toast show align-items-center text-bg-dark border-0 rounded-pill" role="status" aria-live="polite">
        <div class="d-flex"><div class="toast-body fw-semibold px-4"><c:out value="${sessionScope.aviso}"/></div></div>
      </div>
    </div>
    <c:remove var="aviso" scope="session"/>
  </c:if>
</body>
</html>
