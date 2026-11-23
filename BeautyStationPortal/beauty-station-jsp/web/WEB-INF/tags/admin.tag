<%@ tag pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bs" uri="https://beautystation.com/funciones" %>
<%-- Diseño del panel de administración: mismo look que el resto, con su propio menú. --%>
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
        <c:forEach var="r" items="${applicationScope.rutasAdmin}">
          <a class="nav-link ${ruta == r.clave ? 'active' : ''}" href="${ctx}/${r.clave}"><t:ic n="${r.icono}" s="19"/>${r.etiqueta}</a>
        </c:forEach>
      </nav>
      <div class="sidebar-user">
        <span class="user-pill"><span class="avatar"><t:ic n="user"/></span><b><c:out value="${empty sessionScope.nombreAdmin ? 'Administración' : sessionScope.nombreAdmin}"/></b></span>
        <button id="logout" class="logout" form="acciones" formaction="${ctx}/logout"><span><t:ic n="logout"/></span> Cerrar sesión</button>
      </div>
    </aside>

    <div class="content">
      <main id="view"><div class="page-in"><jsp:doBody/></div></main>
    </div>
  </div>

  <form id="acciones" method="post" hidden>
    <input type="hidden" name="estado" value="<c:out value='${estadoActual}'/>">
  </form>

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
