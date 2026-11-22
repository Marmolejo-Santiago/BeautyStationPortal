<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bs" uri="https://beautystation.com/funciones" %>
<%-- Tarjeta de producto. Los botones usan el formulario global #acciones (ver layout.tag). --%>
<%@ attribute name="p" required="true" type="com.beautystation.modelo.Producto" %>
<%@ attribute name="grande" required="false" type="java.lang.Boolean" %>
<%@ attribute name="etiqueta" required="false" type="java.lang.Boolean" %>
<%@ attribute name="valoracion" required="false" type="java.lang.Boolean" %>
<%@ attribute name="largo" required="false" type="java.lang.Boolean" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="col">
  <article class="product ${grande ? 'product-lg' : ''}" id="p-${p.id}">
    <c:if test="${etiqueta and not empty p.etiqueta}"><span class="badge rounded-pill product-tag ${p.etiqueta == 'Más vendido' ? 'bg-hot' : 'bg-primary'}">${p.etiqueta}</span></c:if>
    <button class="fav ${sessionScope.app.favoritos.contains(p.id) ? 'on' : ''}" form="acciones" formaction="${ctx}/favorito" name="id" value="${p.id}" aria-label="Favorito"><t:ic n="heart" s="15"/></button>
    <div class="pic"><t:foto src="${p.imagen}" emoji="${p.emoji}" alt="${p.nombre}"/></div>
    <div class="name">${p.nombre}</div>
    <c:if test="${valoracion}"><div class="stars">${bs:stars(p.rating)}<small>(${p.resenas})</small></div></c:if>
    <div class="price ${grande ? 'big' : ''}">${bs:money(p.precio)}</div>
    <button class="btn btn-primary btn-sm mt-auto" form="acciones" formaction="${ctx}/carrito" name="accion" value="agregar:${p.id}"><t:ic n="cart" s="14"/> ${largo ? 'Agregar al carrito' : 'Agregar'}</button>
  </article>
</div>
