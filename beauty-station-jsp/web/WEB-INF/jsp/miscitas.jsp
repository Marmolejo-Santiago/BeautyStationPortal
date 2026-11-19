<t:layout ruta="miscitas">
    <t:banner icono="calendar" titulo="Mis citas" sub="Aquí puedes ver y gestionar tus citas programadas.">Tu belleza,<br>nuestra prioridad ♡</t:banner>
    <div class="row g-3">
      <div class="col-3"><div class="card">
        <form method="post" action="${ctx}/miscitas">
          <t:calendario cal="${sessionScope.app.mini}" marcas="${fechasCitas}" pasado="${false}" prefijo="mini"/>
        </form>
        <h4 class="small fw-bold mt-3 mb-2">Filtrar por estado</h4>
        <div class="nav nav-pills pills pills-sm gap-1">
          <c:forEach var="s" items="${filtros}"><a class="nav-link ${sessionScope.app.filtroCitas == s ? 'active' : ''}" href="${ctx}/miscitas?estado=${s}">${s == 'Todas' ? s : s.concat('s')}</a></c:forEach>
        </div></div></div>
      <div class="col-6"><div class="card"><h3><span><t:ic n="calendar"/></span>Próximas citas</h3>
        <c:choose>
          <c:when test="${not empty citas}">
            <c:forEach var="a" items="${citas}">
              <div class="appt">
                <div class="date">${a.diaSemana}<b>${a.diaNumero}</b>${a.mesCorto} ${a.anio}</div>
                <div class="pic">${a.emoji}</div>
                <div><h4>${a.servicio}</h4><small><span><t:ic n="clock" s="13"/></span>${a.hora}</small><small><span><t:ic n="pin" s="13"/></span>${direccion}</small></div>
                <div class="side"><t:badge txt="${a.estado}" k="${a.tipoBadge}"/>
                  <div class="d-flex gap-2"><a class="btn btn-primary btn-sm" href="#detalle-${a.id}">Ver detalles →</a>
                    <c:choose>
                      <c:when test="${a.estado != 'Cancelada'}"><a class="btn btn-outline-primary btn-sm" href="#cancelar-${a.id}">Cancelar</a></c:when>
                      <c:otherwise><a class="btn btn-outline-primary btn-sm" href="${ctx}/citas">Reagendar</a></c:otherwise>
                    </c:choose></div></div></div>
            </c:forEach>
          </c:when>
          <c:otherwise><div class="empty">No tienes citas con este estado.</div></c:otherwise>
        </c:choose></div></div>
      <div class="col-3"><a class="btn btn-primary w-100 mb-3" href="${ctx}/citas"><t:ic n="calendar" s="16"/> Agendar nueva cita →</a>
        <div class="card p-2"><div class="stat"><span><t:ic n="calendar"/></span>Citas próximas<b>${proximas}</b></div>
          <div class="stat"><span><t:ic n="clock"/></span>Citas completadas<b>${completadas}</b></div><div class="stat mb-0"><span><t:ic n="x"/></span>Citas canceladas<b>${canceladas}</b></div></div>
        <div class="card mt-3"><h3><span><t:ic n="headset"/></span>¿Necesitas ayuda?</h3><p class="small text-body-secondary mb-3">Si tienes alguna duda o necesitas cambiar tu cita, contáctanos.</p>
          <a class="btn btn-outline-primary w-100" href="${ctx}/acerca">Contáctanos →</a></div>
        <div class="card thanks mt-3 py-4"><div class="script">Cada cita<br>es un paso más<br>hacia tu mejor versión ♡</div></div></div>
    </div>

    <c:forEach var="a" items="${citas}">
      <div class="modal fade" id="detalle-${a.id}" tabindex="-1">
        <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
        <div class="modal-dialog modal-dialog-centered"><div class="modal-content">
          <div class="modal-header"><h5 class="modal-title fw-bold">${a.servicio}</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
          <div class="modal-body"><p class="text-capitalize mb-1"><b>${a.fechaLarga}</b> · ${a.hora}</p><p class="mb-2"><t:badge txt="${a.estado}" k="${a.tipoBadge}"/></p><p class="small text-body-secondary mb-0">${direccion}</p></div>
          <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cerrar</a></div>
        </div></div>
      </div>
      <c:if test="${a.estado != 'Cancelada'}">
        <div class="modal fade" id="cancelar-${a.id}" tabindex="-1">
          <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
          <div class="modal-dialog modal-dialog-centered modal-sm"><div class="modal-content">
            <div class="modal-header"><h5 class="modal-title fw-bold">Cancelar cita</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
            <div class="modal-body">¿Seguro que deseas cancelar esta cita?</div>
            <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Volver</a><button type="submit" class="btn btn-primary" form="acciones" formaction="${ctx}/miscitas" name="accion" value="cancelar:${a.id}">Sí, cancelar</button></div>
          </div></div>
        </div>
      </c:if>
    </c:forEach>
</t:layout>
