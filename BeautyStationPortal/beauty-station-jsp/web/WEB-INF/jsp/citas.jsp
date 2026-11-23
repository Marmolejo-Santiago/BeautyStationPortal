<t:layout ruta="citas">
    <c:set var="f" value="${sessionScope.app.form}"/>
    <t:banner arte="art-citas" icono="calendar" titulo="Agendar Citas" sub="Reserva tu cita y disfruta de una experiencia única.">Tu belleza<br>también es<br>una cita ♡</t:banner>
    <form method="post" action="${ctx}/citas">
    <div class="row g-3">
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="calendar"/></span>Selecciona la fecha</h3>
        <t:calendario cal="${sessionScope.app.cal}" seleccionado="${f.fecha}" prefijo="cal"/>
        <div class="info-bar"><span><t:ic n="calendar"/></span><div>
          <c:choose>
            <c:when test="${not empty f.fecha}"><b class="text-capitalize">${bs:fechaLarga(f.fecha)}</b></c:when>
            <c:otherwise><b>Sin fecha seleccionada</b></c:otherwise>
          </c:choose>
          <small>Selecciona una fecha para ver los horarios disponibles.</small></div></div></div></div>
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="clock"/></span>1. Elige tus servicios</h3>
        <select class="form-select mb-2" id="svc-select" name="svc"><option value="">Selecciona un servicio</option>
          <c:forEach var="s" items="${applicationScope.servicios}"><option value="${s.id}">${s.nombre}</option></c:forEach></select>
        <c:forEach var="s" items="${applicationScope.servicios}">
          <button type="submit" name="accion" value="svc:${s.id}" class="service w-100 text-start ${f.servicios.contains(s.id) ? 'on' : ''}">
            <span class="pic"><t:foto src="${s.imagen}" emoji="${s.emoji}" alt="${s.nombre}"/></span><span class="t">${s.nombre}<small>${s.min} min</small></span><b>&#36;${s.precio}</b><i class="radio"></i></button>
        </c:forEach></div></div>
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="clock"/></span>2. Selecciona un horario</h3>
        <c:choose>
          <c:when test="${empty f.fecha}"><p class="empty py-3">Elige primero una fecha.</p></c:when>
          <c:otherwise>
            <c:if test="${lleno}"><div class="alert alert-warning py-2 small">No hay espacio disponible en el showroom para este día. Prueba con otra fecha u horario.</div></c:if>
            <div class="slots">
              <c:forEach var="h" items="${applicationScope.horarios}"><button type="submit" name="accion" value="hora:${h}" class="slot ${f.hora == h ? 'on' : ''}" ${ocupados.contains(h) ? 'disabled' : ''}>${h}</button></c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
        <h3 class="mt-4"><span><t:ic n="edit"/></span>3. Detalles adicionales <small class="fw-normal text-body-secondary">(opcional)</small></h3>
        <textarea class="form-control mb-3" rows="3" id="note" name="nota" placeholder="Comentarios o preferencias..."><c:out value="${f.nota}"/></textarea>
        <button class="btn btn-primary w-100 mt-auto" type="submit" name="accion" value="confirmar"><t:ic n="calendar" s="16"/> Confirmar cita →</button></div></div>
    </div>
    </form>
</t:layout>
