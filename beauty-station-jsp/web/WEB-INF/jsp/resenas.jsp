<t:layout ruta="resenas">
    <t:banner icono="star" titulo="Reseñas" sub="Lee lo que opinan nuestras clientas y comparte tu experiencia.">Tu opinión<br>nos inspira ♡</t:banner>
    <div class="row g-3">
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="edit"/></span>Escribe tu reseña</h3>
        <form method="post" action="${ctx}/resenas">
          <label class="form-label small fw-bold" for="rs-producto">Producto</label>
          <select class="form-select mb-2" id="rs-producto" name="producto" required>
            <option value="" disabled ${empty seleccionado ? 'selected' : ''}>Selecciona un producto</option>
            <c:forEach var="p" items="${productos}"><option value="${p.id}" ${seleccionado == p.id ? 'selected' : ''}>${p.emoji} ${p.nombre}</option></c:forEach>
          </select>
          <label class="form-label small fw-bold" for="rs-cal">Calificación</label>
          <select class="form-select mb-2" id="rs-cal" name="calificacion" required>
            <option value="5">★★★★★ — Excelente</option>
            <option value="4">★★★★☆ — Muy bueno</option>
            <option value="3">★★★☆☆ — Bueno</option>
            <option value="2">★★☆☆☆ — Regular</option>
            <option value="1">★☆☆☆☆ — Malo</option>
          </select>
          <label class="form-label small fw-bold" for="rs-comentario">Comentario <small class="fw-normal text-body-secondary">(opcional)</small></label>
          <textarea class="form-control mb-3" id="rs-comentario" name="comentario" rows="4" maxlength="500" placeholder="Cuéntanos qué te pareció..."></textarea>
          <button class="btn btn-primary w-100" type="submit">Publicar reseña →</button>
        </form></div></div>
      <div class="col-8"><div class="card h-100"><h3><span><t:ic n="star"/></span>Reseñas de la comunidad</h3>
        <c:choose>
          <c:when test="${not empty resenas}">
            <c:forEach var="r" items="${resenas}">
              <div class="review">
                <div class="pic">${r.producto.emoji}</div>
                <div class="flex-fill">
                  <div class="d-flex justify-content-between align-items-start"><h4 class="fs-6 fw-bold mb-0">${r.producto.nombre}</h4><small class="text-body-secondary">${r.fechaTexto}</small></div>
                  <div class="stars">${bs:stars(r.calificacion)}</div>
                  <c:if test="${not empty r.comentario}"><p class="small mb-1"><c:out value="${r.comentario}"/></p></c:if>
                  <small class="text-body-secondary">— <c:out value="${r.usuario.nombre}"/></small>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise><div class="empty">Aún no hay reseñas. ¡Sé la primera en opinar!</div></c:otherwise>
        </c:choose></div></div>
    </div>
</t:layout>
