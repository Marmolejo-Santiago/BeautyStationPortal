<t:admin ruta="admin/citas">
    <t:banner arte="art-citas" icono="calendar" titulo="Administración · Citas" sub="Consulta las citas asignadas, agenda nuevas y cancela las que ya no se realizarán.">Panel de<br>administración ♡</t:banner>
    <div class="row g-3">
      <div class="col-8">
        <div class="admin-stats">
          <div><b>${totalCitas}</b><small>Citas en total</small></div>
          <div><b>${confirmadas}</b><small>Confirmadas</small></div>
          <div><b>${pendientes}</b><small>Pendientes</small></div>
          <div><b>${canceladas}</b><small>Canceladas</small></div>
        </div>
        <div class="card"><h3><span><t:ic n="calendar"/></span>Citas asignadas</h3>
          <div class="nav nav-pills pills pills-sm gap-1 mb-3">
            <c:forEach var="s" items="${filtros}"><a class="nav-link ${estadoActual == s ? 'active' : ''}" href="${ctx}/admin/citas?estado=${s}">${s == 'Todas' ? s : s.concat('s')}</a></c:forEach>
          </div>
          <c:choose>
            <c:when test="${not empty citas}">
              <c:forEach var="c" items="${citas}">
                <div class="appt">
                  <div class="date">${c.diaSemana}<b>${c.diaNumero}</b>${c.mesCorto} ${c.anio}</div>
                  <div class="pic"><t:foto src="${c.imagen}" emoji="${c.emoji}" alt="${c.servicio}"/></div>
                  <div><h4><c:out value="${c.servicio}"/></h4>
                    <small><span><t:ic n="user" s="13"/></span><c:out value="${c.cliente}"/><c:if test="${empty c.usuario}"> · sin cuenta</c:if></small>
                    <small><span><t:ic n="clock" s="13"/></span>${c.hora} · ${c.duracion} min · ${bs:money(c.costo)}</small>
                    <c:if test="${not empty c.especificacion}"><small><c:out value="${c.especificacion}"/></small></c:if></div>
                  <div class="side"><t:badge txt="${c.estado}" k="${c.tipoBadge}"/>
                    <c:if test="${c.estado != 'Cancelada'}"><a class="btn btn-outline-primary btn-sm" href="#cancelar-${c.id}">Cancelar</a></c:if></div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise><div class="empty">No hay citas con este estado.</div></c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="col-4">
        <div class="card"><h3><span><t:ic n="calendar"/></span>Agendar nueva cita</h3>
          <form method="post" action="${ctx}/admin/citas">
            <input type="hidden" name="accion" value="crear">
            <input type="hidden" name="estado" value="<c:out value='${estadoActual}'/>">
            <label class="form-label small fw-bold" for="ad-cliente">Clienta</label>
            <select class="form-select mb-2" id="ad-cliente" name="cliente">
              <option value="">— Clienta sin cuenta —</option>
              <c:forEach var="u" items="${clientes}"><option value="${u.id}"><c:out value="${u.nombre}"/> (<c:out value="${u.email}"/>)</option></c:forEach>
            </select>
            <label class="form-label small fw-bold" for="ad-nombre">Nombre <small class="fw-normal text-body-secondary">(solo si no tiene cuenta)</small></label>
            <input class="form-control mb-2" id="ad-nombre" name="nombre" maxlength="100">
            <div class="row g-2 mb-2">
              <div class="col-7"><label class="form-label small fw-bold" for="ad-fecha">Fecha</label><input class="form-control" type="date" id="ad-fecha" name="fecha" min="${hoy}" required></div>
              <div class="col-5"><label class="form-label small fw-bold" for="ad-hora">Hora</label>
                <select class="form-select" id="ad-hora" name="hora"><c:forEach var="h" items="${applicationScope.horarios}"><option value="${h}">${h}</option></c:forEach></select></div>
            </div>
            <div class="form-label small fw-bold">Servicios</div>
            <c:forEach var="s" items="${applicationScope.servicios}">
              <div class="form-check"><input class="form-check-input" type="checkbox" id="ad-svc-${s.id}" name="svc" value="${s.id}"><label class="form-check-label small" for="ad-svc-${s.id}"><c:out value="${s.nombre}"/> · ${s.min} min · &#36;${s.precio}</label></div>
            </c:forEach>
            <label class="form-label small fw-bold mt-2" for="ad-nota">Detalles <small class="fw-normal text-body-secondary">(opcional)</small></label>
            <textarea class="form-control mb-3" id="ad-nota" name="nota" rows="2" maxlength="255"></textarea>
            <button type="submit" class="btn btn-primary w-100"><t:ic n="calendar" s="16"/> Agendar cita →</button>
          </form>
        </div>
      </div>
    </div>

    <c:forEach var="c" items="${citas}">
      <c:if test="${c.estado != 'Cancelada'}">
        <div class="modal fade" id="cancelar-${c.id}" tabindex="-1">
          <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
          <div class="modal-dialog modal-dialog-centered modal-sm"><div class="modal-content">
            <div class="modal-header"><h5 class="modal-title fw-bold">Cancelar cita</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
            <div class="modal-body">¿Cancelar la cita de <b><c:out value="${c.cliente}"/></b> (${c.fechaLarga}, ${c.hora})?<c:if test="${not empty c.usuario}"> Se le enviará una notificación.</c:if></div>
            <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Volver</a><button type="submit" class="btn btn-primary" form="acciones" formaction="${ctx}/admin/citas" name="accion" value="cancelar:${c.id}">Sí, cancelar</button></div>
          </div></div>
        </div>
      </c:if>
    </c:forEach>
</t:admin>
