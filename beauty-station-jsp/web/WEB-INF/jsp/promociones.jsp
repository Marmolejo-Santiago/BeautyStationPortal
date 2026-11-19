<t:layout ruta="promociones">
    <section class="banner"><div><small class="script" style="font-size:1rem">PROMOCIONES ESPECIALES</small><h1>Belleza, estilo y<br><span class="script" style="font-size:2.3rem">ahorro en un solo lugar ♡</span></h1>
      <p>Descubre nuestras promociones y consiéntete con lo mejor de la moda y la belleza.</p></div><div class="script">Porque<br>te mereces<br>lo mejor ♡</div><div class="art"></div></section>
    <t:categorias base="/promociones" actual="${cat}" conTodas="${true}"/>
    <div class="section-head"><h2>🔥 Promociones actuales</h2><a href="${ctx}/promociones?limpiar=1">Ver todas las promociones →</a></div>
    <c:choose>
      <c:when test="${not empty promos}">
        <div class="row row-cols-4 g-3">
          <c:forEach var="p" items="${promos}">
            <div class="col"><article class="product product-lg promo-card" id="p-${p.id}"><span class="disc">-${p.descuento}%</span>
              <button class="fav ${sessionScope.app.favoritos.contains(p.id) ? 'on' : ''}" form="acciones" formaction="${ctx}/favorito" name="id" value="${p.id}" aria-label="Favorito"><t:ic n="heart" s="15"/></button><div class="pic">${p.emoji}</div>
              <div class="cat-l">${p.categoria.nombre}</div><div class="name big">${p.nombre}</div><p>${p.descripcion}</p>
              <div><span class="now">${bs:money(p.precioFinal)}</span><span class="old">${bs:money(p.precio)}</span></div>
              <button class="btn btn-primary btn-sm mt-auto" form="acciones" formaction="${ctx}/carrito" name="accion" value="agregar:${p.id}"><t:ic n="cart" s="14"/> Agregar al carrito</button></article></div>
          </c:forEach>
        </div>
      </c:when>
      <c:otherwise><div class="empty card">No hay promociones en esta categoría por ahora.</div></c:otherwise>
    </c:choose>
    <div class="promo-strip"><div class="ico">🏷️</div><div><small>ENVÍO GRATIS</small><h3>en compras mayores a $1,000</h3><p>Lleva tus productos favoritos sin costo de envío.</p></div><a class="btn btn-light ms-auto" href="${ctx}/catalogo">Ver productos →</a></div>
</t:layout>
