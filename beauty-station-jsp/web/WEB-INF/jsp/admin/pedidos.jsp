<t:admin ruta="admin/pedidos">
    <t:banner arte="art-pedidos" icono="box" titulo="Administración · Pedidos" sub="Consulta los pedidos de todas las clientas.">Gracias por<br>ser parte de BS ♡</t:banner>
    <div class="admin-stats">
      <div><b>${totalPedidos}</b><small>Pedidos en total</small></div>
      <div><b>${enCamino}</b><small>En camino</small></div>
      <div><b>${bs:money(vendido)}</b><small>Vendido (sin cancelados)</small></div>
    </div>
    <div class="card"><h3><span><t:ic n="box"/></span>Pedidos</h3>
      <div class="nav nav-pills pills pills-sm gap-1 mb-3">
        <c:forEach var="f" items="${filtros}"><a class="nav-link ${estadoActual == f[0] ? 'active' : ''}" href="${ctx}/admin/pedidos?estado=${f[0]}">${f[1]}</a></c:forEach>
      </div>
      <c:choose>
        <c:when test="${not empty pedidos}">
          <c:forEach var="o" items="${pedidos}">
            <div class="row g-3 align-items-center order-row">
              <div class="col-3"><h4 class="fs-6 fw-bold">Pedido #${o.codigo}</h4><small class="d-block text-body-secondary mb-2">Realizado el ${o.fecha}</small><t:badge txt="${o.estado}" k="${o.tipoBadge}"/></div>
              <div class="col-3"><small class="text-body-secondary">Clienta</small><div class="fw-semibold"><c:out value="${o.usuario.nombre}"/></div><small class="text-body-secondary"><c:out value="${o.usuario.email}"/></small></div>
              <div class="col-3"><small class="text-body-secondary">${o.cantidad} productos</small><div class="thumbs"><c:forEach var="em" items="${o.items}"><i><t:foto src="${em.imagen}" emoji="${em.emoji}" alt="${em.nombre}"/></i></c:forEach></div></div>
              <div class="col-3"><small class="text-body-secondary">Total</small><div class="fw-bold fs-5">${bs:money(o.total)}</div>
                <small class="d-block text-body-secondary"><c:if test="${not empty o.direccion}"><c:out value="${o.direccion.texto}"/></c:if></small></div>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise><div class="empty">No hay pedidos con este estado.</div></c:otherwise>
      </c:choose>
    </div>
</t:admin>
