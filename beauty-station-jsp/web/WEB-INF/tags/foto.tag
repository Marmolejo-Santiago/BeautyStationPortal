<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Foto de un producto/servicio. Si aún no tiene imagen se muestra el emoji de siempre. --%>
<%@ attribute name="src" required="false" %>
<%@ attribute name="emoji" required="false" %>
<%@ attribute name="alt" required="false" %>
<c:choose>
  <c:when test="${not empty src}"><img src="${pageContext.request.contextPath}/<c:out value='${src}'/>" alt="<c:out value='${alt}'/>" loading="lazy"></c:when>
  <c:otherwise>${emoji}</c:otherwise>
</c:choose>
