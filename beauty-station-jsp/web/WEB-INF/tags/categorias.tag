<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%-- Tira de categorías (catStrip del original). base = /catalogo o /promociones --%>
<%@ attribute name="base" required="true" %>
<%@ attribute name="actual" required="true" %>
<%@ attribute name="conTodas" required="false" type="java.lang.Boolean" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="d-flex gap-2 mb-3">
  <c:if test="${conTodas}"><a class="cat-tab flex-fill ${actual == 'Todas' ? 'on' : ''}" href="${ctx}${base}?cat=Todas"><span class="e"><t:ic n="grid" s="22"/></span>Todas</a></c:if>
  <c:forEach var="c" items="${applicationScope.categorias}"><a class="cat-tab flex-fill ${actual == c.nombre ? 'on' : ''}" href="${ctx}${base}?cat=${c.nombre}"><span class="e">${c.emoji}</span>${c.nombre}</a></c:forEach></div>
