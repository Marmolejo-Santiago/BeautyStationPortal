<t:layout ruta="inicio">
    <section class="banner hero">
      <div><small>NUEVA TEMPORADA</small><h1>Estilo que te<br>acompaña <b>siempre</b></h1>
      <p>Descubre las últimas tendencias en moda y luce increíble en cada ocasión.</p>
      <a class="btn btn-light" href="${ctx}/catalogo">Ver catálogo →</a></div>
      <div class="script">Moda<br>que inspira ♡</div>
    </section>
    <div class="section-head"><h2>Categorías</h2><a href="${ctx}/catalogo">Ver todas →</a></div>
    <div class="row row-cols-5 g-3">
      <c:forEach var="cat" items="${categoriasInicio}">
        <div class="col"><a class="cat" href="${ctx}/catalogo"><div class="thumb"><img src="${ctx}/${cat[1]}" alt="${cat[0]}" loading="lazy"></div><div class="n">${cat[0]} <t:ic n="chev" s="14"/></div></a></div>
      </c:forEach>
    </div>
    <div class="promo-strip"><div class="ico">%</div><div><small>PROMOCIONES ESPECIALES</small><h3>Hasta 30% de descuento</h3><p>en productos seleccionados</p></div>
      <a class="btn btn-light ms-auto" href="${ctx}/promociones">Ver promociones →</a></div>
    <div class="section-head"><h2>Productos destacados</h2><a href="${ctx}/catalogo">Ver todos los productos →</a></div>
    <div class="row row-cols-6 g-3">
      <c:forEach var="p" items="${destacados}"><t:producto p="${p}"/></c:forEach>
    </div>
</t:layout>
