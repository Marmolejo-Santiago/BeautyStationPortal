<t:layout ruta="pedidos">
    <t:banner arte="art-pedidos" icono="box" titulo="Mis pedidos" sub="Aquí puedes ver el historial y estado de tus compras.">Gracias por<br>ser parte de BS ♡</t:banner>
    <div class="nav nav-pills pills gap-2 mb-3">
      <c:forEach var="op" items="${filtros}">
        <c:url var="u" value="/pedidos"><c:param name="estado" value="${op[0]}"/></c:url>
        <a class="nav-link ${sessionScope.app.filtroPedidos == op[0] ? 'active' : ''}" href="${u}">${op[1]}</a>
      </c:forEach>
    </div>
    <div class="row g-3">
      <div class="col-9"><div class="card p-0">
        <c:choose>
          <c:when test="${not empty pedidos}">
            <c:forEach var="o" items="${pedidos}">
              <div class="order-item p-3 border-bottom" id="o-${o.codigo}"><div class="row g-3 align-items-center">
                <div class="col-3"><h4 class="fs-6 fw-bold">Pedido #${o.codigo}</h4><small class="d-block text-body-secondary mb-2">Realizado el ${o.fecha}</small><t:badge txt="${o.estado}" k="${o.tipoBadge}"/><br><a class="btn btn-outline-primary btn-sm mt-3" href="#detalle-${o.codigo}">Ver detalles →</a></div>
                <div class="col-3"><small class="text-body-secondary">${o.cantidad} productos</small><div class="thumbs"><c:forEach var="em" items="${o.items}"><i><t:foto src="${em.imagen}" emoji="${em.emoji}" alt="${em.nombre}"/></i></c:forEach></div></div>
                <div class="col-2"><small class="text-body-secondary d-block">Total</small><b class="fs-6">${bs:money(o.total)}</b>
                  <c:choose>
                    <c:when test="${o.estado == 'En camino'}"><a class="d-block link small mt-1" href="${ctx}/pedidos?rastreo=${o.codigo}">Rastrear pedido →</a></c:when>
                    <c:otherwise><button class="d-block link small mt-1" form="acciones" formaction="${ctx}/carrito" name="accion" value="recomprar:${o.codigo}">Volver a comprar →</button></c:otherwise>
                  </c:choose></div>
                <div class="col-4">
                  <c:choose>
                    <c:when test="${o.estado == 'En camino'}">
                      <div class="status-box"><b><span><t:ic n="truck"/></span>Tu pedido está en camino</b><small class="d-block mb-2">Llegará el ${o.eta}</small>
                        <div class="progress" style="height:6px"><div class="progress-bar" style="width:66%"></div></div>
                        <div class="d-flex justify-content-between track-l"><span>Pedido realizado</span><span>En proceso</span><span>En camino</span><span>Entregado</span></div></div>
                    </c:when>
                    <c:when test="${o.estado == 'Entregado'}">
                      <div class="status-box done"><b><span><t:ic n="check"/></span>Entregado</b><small class="d-block mb-2">Entregado el ${o.eta}</small><a class="btn btn-outline-primary btn-sm w-100" href="${ctx}/resenas">Dejar reseña ☆</a></div>
                    </c:when>
                    <c:otherwise>
                      <div class="status-box"><b><span class="text-danger"><t:ic n="x"/></span>Pedido cancelado</b><small class="d-block mb-2">El pago fue reembolsado</small><a class="btn btn-outline-primary btn-sm w-100" href="#detalle-${o.codigo}">Más información →</a></div>
                    </c:otherwise>
                  </c:choose></div>
              </div></div>
            </c:forEach>
          </c:when>
          <c:otherwise><div class="empty">No hay pedidos con este estado.</div></c:otherwise>
        </c:choose></div></div>
      <div class="col-3"><div class="card"><h3><span><t:ic n="box"/></span>Estado de tu pedido</h3><p class="small text-body-secondary">Ingresa tu número de pedido para ver el estado actual.</p>
        <form method="get" action="${ctx}/pedidos">
          <input class="form-control mb-2" id="track-input" name="rastreo" placeholder="Ej. BS-001245" value="<c:out value='${param.rastreo}'/>"><button class="btn btn-primary w-100" type="submit">Consultar</button>
        </form>
        <div id="track-result">
          <c:if test="${param.rastreo != null}">
            <div class="alert alert-light border small mt-3 mb-0">
              <c:choose>
                <c:when test="${not empty rastreoPedido}"><b>#${rastreoPedido.codigo}</b> · ${rastreoPedido.estado}<c:if test="${not empty rastreoPedido.eta and rastreoPedido.estado == 'En camino'}"> — llega el ${rastreoPedido.eta}</c:if></c:when>
                <c:otherwise>No encontramos ese número de pedido.</c:otherwise>
              </c:choose>
            </div>
          </c:if>
        </div></div>
        <div class="card mt-3"><h3><span><t:ic n="gift"/></span>Disfruta más</h3><p class="small text-body-secondary">¡Tus compras también te dan acceso a promociones exclusivas!</p><a class="btn btn-outline-primary w-100" href="${ctx}/promociones">Ver promociones →</a></div>
        <div class="card thanks mt-3 py-4"><div class="script">Gracias<br>por confiar<br>en nosotras ♡</div></div></div>
    </div>

    <c:forEach var="o" items="${pedidos}">
      <div class="modal fade" id="detalle-${o.codigo}" tabindex="-1">
        <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
        <div class="modal-dialog modal-dialog-centered"><div class="modal-content">
          <div class="modal-header"><h5 class="modal-title fw-bold">Pedido #${o.codigo}</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
          <div class="modal-body"><p class="mb-1">Realizado el ${o.fecha}</p><div class="thumbs mb-2"><c:forEach var="em" items="${o.items}"><i><t:foto src="${em.imagen}" emoji="${em.emoji}" alt="${em.nombre}"/></i></c:forEach></div><p class="mb-0"><b>${o.cantidad} productos</b> · ${bs:money(o.total)} · ${o.estado}</p></div>
          <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cerrar</a></div>
        </div></div>
      </div>
    </c:forEach>
</t:layout>
