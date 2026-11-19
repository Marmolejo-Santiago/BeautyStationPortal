<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="txt" required="true" %>
<%@ attribute name="k" required="true" %>
<c:choose>
  <c:when test="${k == 'ok'}"><c:set var="cls" value="bg-success-subtle text-success-emphasis"/></c:when>
  <c:when test="${k == 'wait'}"><c:set var="cls" value="bg-warning-subtle text-warning-emphasis"/></c:when>
  <c:when test="${k == 'off'}"><c:set var="cls" value="bg-secondary-subtle text-secondary-emphasis"/></c:when>
  <c:when test="${k == 'cancel'}"><c:set var="cls" value="bg-danger-subtle text-danger-emphasis"/></c:when>
  <c:otherwise><c:set var="cls" value="bg-primary-subtle text-primary-emphasis"/></c:otherwise>
</c:choose>
<span class="badge rounded-pill ${cls}">${txt}</span>
