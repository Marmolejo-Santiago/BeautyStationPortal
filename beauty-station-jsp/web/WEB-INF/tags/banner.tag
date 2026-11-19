<%@ tag pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="icono" required="true" %>
<%@ attribute name="titulo" required="true" %>
<%@ attribute name="sub" required="true" %>
<section class="banner">
  <div class="banner-lead"><div class="banner-ico"><t:ic n="${icono}" s="28"/></div><div><h1>${titulo}</h1><p>${sub}</p></div></div>
  <div class="script"><jsp:doBody/></div><div class="art"></div></section>
