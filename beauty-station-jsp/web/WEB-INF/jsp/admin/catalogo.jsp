<t:admin ruta="admin/catalogo">
    <t:banner arte="art-catalogo" icono="tag" titulo="Administración · Catálogo" sub="Agrega productos nuevos con su foto o elimina los que ya no se venden.">Tu estilo,<br>tu esencia ♡</t:banner>
    <div class="row g-3">
      <div class="col-4">
        <div class="card"><h3><span><t:ic n="tag"/></span>Agregar producto</h3>
          <form method="post" action="${ctx}/admin/catalogo" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="crear">
            <label class="form-label small fw-bold" for="pr-nombre">Nombre</label>
            <input class="form-control mb-2" id="pr-nombre" name="nombre" maxlength="100" required>
            <label class="form-label small fw-bold" for="pr-desc">Descripción <small class="fw-normal text-body-secondary">(opcional)</small></label>
            <textarea class="form-control mb-2" id="pr-desc" name="descripcion" rows="2" maxlength="255"></textarea>
            <label class="form-label small fw-bold" for="pr-cat">Categoría</label>
            <select class="form-select mb-2" id="pr-cat" name="categoria" required>
              <c:forEach var="c" items="${applicationScope.categorias}"><option value="${c.id}">${c.emoji} <c:out value="${c.nombre}"/></option></c:forEach>
            </select>
            <div class="row g-2 mb-2">
              <div class="col-4"><label class="form-label small fw-bold" for="pr-precio">Precio</label><input class="form-control" id="pr-precio" name="precio" inputmode="decimal" required></div>
              <div class="col-4"><label class="form-label small fw-bold" for="pr-stock">Stock</label><input class="form-control" id="pr-stock" name="stock" type="number" min="0" max="99999" value="10" required></div>
              <div class="col-4"><label class="form-label small fw-bold" for="pr-desc-pct">Desc. %</label><input class="form-control" id="pr-desc-pct" name="descuento" type="number" min="0" max="90" value="0"></div>
            </div>
            <label class="form-label small fw-bold" for="pr-etiqueta">Etiqueta</label>
            <select class="form-select mb-2" id="pr-etiqueta" name="etiqueta"><c:forEach var="e" items="${etiquetas}"><option value="${e}">${empty e ? 'Sin etiqueta' : e}</option></c:forEach></select>
            <label class="form-label small fw-bold" for="pr-foto">Foto <small class="fw-normal text-body-secondary">(JPG, PNG, WEBP o GIF, máx. 5 MB)</small></label>
            <input class="form-control mb-3" id="pr-foto" type="file" name="imagen" accept="image/jpeg,image/png,image/webp,image/gif">
            <button type="submit" class="btn btn-primary w-100">Agregar al catálogo →</button>
          </form>
        </div>
      </div>

      <div class="col-8">
        <div class="card"><h3><span><t:ic n="tag"/></span>Productos del catálogo (${productos.size()})</h3>
          <c:choose>
            <c:when test="${not empty productos}">
              <div class="row row-cols-3 g-3">
                <c:forEach var="p" items="${productos}">
                  <div class="col"><article class="product admin-product">
                    <c:if test="${not empty p.etiqueta}"><span class="tag-badge"><c:out value="${p.etiqueta}"/></span></c:if>
                    <div class="pic"><t:foto src="${p.imagen}" emoji="${p.emoji}" alt="${p.nombre}"/></div>
                    <div class="name"><c:out value="${p.nombre}"/></div>
                    <div class="price">${bs:money(p.precioFinal)}<c:if test="${p.descuento > 0}"> <small class="text-body-secondary">-${p.descuento}%</small></c:if></div>
                    <small class="d-block text-body-secondary mb-2"><c:out value="${p.categoria.nombre}"/> · Stock ${p.stock}</small>
                    <a class="btn btn-outline-danger btn-sm w-100" href="#eliminar-${p.id}"><t:ic n="trash" s="14"/> Eliminar</a>
                  </article></div>
                </c:forEach>
              </div>
            </c:when>
            <c:otherwise><div class="empty">El catálogo está vacío.</div></c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>

    <c:forEach var="p" items="${productos}">
      <div class="modal fade" id="eliminar-${p.id}" tabindex="-1">
        <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
        <div class="modal-dialog modal-dialog-centered modal-sm"><div class="modal-content">
          <div class="modal-header"><h5 class="modal-title fw-bold">Eliminar producto</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
          <div class="modal-body">¿Eliminar <b><c:out value="${p.nombre}"/></b> del catálogo? Dejará de mostrarse a las clientas y se quitará de sus carritos; los pedidos anteriores se conservan.</div>
          <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Volver</a><button type="submit" class="btn btn-primary" form="acciones" formaction="${ctx}/admin/catalogo" name="accion" value="eliminar:${p.id}">Sí, eliminar</button></div>
        </div></div>
      </div>
    </c:forEach>
</t:admin>
