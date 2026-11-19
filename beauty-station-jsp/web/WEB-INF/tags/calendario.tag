<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bs" uri="https://beautystation.com/funciones" %>
<%-- Calendario del mes. Debe ir dentro de un <form method="post">: cada botón envía name="accion". --%>
<%@ attribute name="cal" required="true" type="java.time.YearMonth" %>
<%@ attribute name="seleccionado" required="false" %>
<%@ attribute name="marcas" required="false" type="java.util.Collection" %>
<%@ attribute name="pasado" required="false" type="java.lang.Boolean" %>
<%@ attribute name="prefijo" required="true" %>
<div class="cal-head"><button type="submit" name="accion" value="${prefijo}-prev"><t:ic n="chevl"/></button>
  <span>${bs:mesAnio(cal)}</span><button type="submit" name="accion" value="${prefijo}-next"><t:ic n="chev"/></button></div>
<div class="cal">
  <div class="dow">Lu</div><div class="dow">Ma</div><div class="dow">Mi</div><div class="dow">Ju</div><div class="dow">Vi</div><div class="dow">Sá</div><div class="dow">Do</div>
  <c:forEach var="d" items="${bs:celdas(cal, seleccionado, marcas, empty pasado ? true : pasado)}">
    <c:choose>
      <c:when test="${d.fuera}"><div class="d out">${d.numero}</div></c:when>
      <c:otherwise><button type="submit" class="${d.clases}" ${d.deshabilitado ? 'disabled' : ''} name="accion" value="dia:${d.clave}">${d.numero}</button></c:otherwise>
    </c:choose>
  </c:forEach>
</div>
