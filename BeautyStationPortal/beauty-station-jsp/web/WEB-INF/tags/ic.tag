<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="n" required="true" %>
<%@ attribute name="s" required="false" type="java.lang.Integer" %>
<svg width="${empty s ? 18 : s}" height="${empty s ? 18 : s}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">${applicationScope.iconos[n]}</svg>
