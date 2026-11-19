<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="texto" required="true" %>
<%@ attribute name="activo" required="false" type="java.lang.Boolean" %>
<%@ attribute name="nombre" required="false" %>
<div class="form-check form-switch d-flex justify-content-between align-items-center ps-0 py-1"><label class="form-check-label small">${texto}</label><input class="form-check-input ms-0" type="checkbox" role="switch" name="${nombre}" ${activo ? 'checked' : ''}></div>
