<t:layout ruta="catalogo">
    <c:set var="e" value="${sessionScope.app}"/>
    <section class="banner"><div><small class="script" style="font-size:1rem">DESCUBRE NUESTRO</small><h1>Catálogo de productos</h1><p>Moda, belleza y accesorios en un solo lugar.<br>¡Haz que cada detalle cuente!</p>
      <a class="btn btn-primary btn-sm mt-3" href="${ctx}/catalogo?limpiar=1">Ver todos los productos →</a></div><div class="script">Tu estilo,<br>tu esencia ♡</div><div class="art art-catalogo"></div></section>
    <t:categorias base="/catalogo" actual="${cat}" conTodas="${false}"/>
    <div class="row g-3">
      <div class="col-3"><aside class="card filters">
        <form method="get" action="${ctx}/catalogo">
          <c:if test="${cat != 'Todas'}"><input type="hidden" name="cat" value="${cat}"></c:if>
          <h4>Filtrar por</h4><h4>Categoría</h4>
          <c:forEach var="c" items="${applicationScope.categorias}">
            <div class="form-check"><input class="form-check-input" type="checkbox" id="chk-${c.id}" name="chk" value="${c.nombre}" ${e.catChecks.contains(c.nombre) ? 'checked' : ''}><label class="form-check-label small" for="chk-${c.id}">${c.nombre}</label></div>
          </c:forEach>
          <h4>Precio</h4><input type="range" class="form-range" id="price" name="price" min="300" max="2000" step="50" value="${e.precioMax}">
          <div class="d-flex justify-content-between small text-body-secondary"><span>$0</span><span id="price-label">hasta ${bs:money0(e.precioMax)}</span></div>
          <h4>Ordenar por</h4>
          <select class="form-select form-select-sm" id="sort" name="sort"><option value="recientes" ${e.orden == 'recientes' ? 'selected' : ''}>Más recientes</option>
            <option value="menor" ${e.orden == 'menor' ? 'selected' : ''}>Precio: menor a mayor</option>
            <option value="mayor" ${e.orden == 'mayor' ? 'selected' : ''}>Precio: mayor a menor</option></select>
          <button class="btn btn-primary btn-sm w-100 mt-3" type="submit" name="aplicar" value="1">Aplicar</button>
        </form></aside></div>
      <div class="col-9"><div class="text-end small text-body-secondary mb-2">Mostrando ${productos.size()} productos</div>
        <c:choose>
          <c:when test="${not empty productos}">
            <div class="row row-cols-4 g-3">
              <c:forEach var="p" items="${productos}"><t:producto p="${p}" grande="${true}" etiqueta="${true}" valoracion="${true}" largo="${true}"/></c:forEach>
            </div>
          </c:when>
          <c:otherwise><div class="empty card">No encontramos productos con esos filtros.</div></c:otherwise>
        </c:choose></div>
    </div>
    <script>
      (function () {
        var r = document.getElementById('price'), l = document.getElementById('price-label');
        if (!r) return;
        function pintar() { if (l) l.textContent = 'hasta $' + Number(r.value).toLocaleString('es-MX'); }
        r.addEventListener('input', pintar);
        r.addEventListener('wheel', function (ev) {
          ev.preventDefault();
          var paso = Number(r.step) || 50, v = Number(r.value) + (ev.deltaY < 0 ? paso : -paso);
          r.value = Math.max(Number(r.min), Math.min(Number(r.max), v));
          pintar();
        }, { passive: false });
      })();
    </script>
</t:layout>
