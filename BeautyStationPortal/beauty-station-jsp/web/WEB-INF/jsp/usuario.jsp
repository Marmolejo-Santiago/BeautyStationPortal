<t:layout ruta="usuario">
    <t:banner arte="art-usuario" icono="user" titulo="Mi usuario" sub="Aquí puedes gestionar tu información, direcciones y preferencias.">Tu belleza,<br>también está<br>en tus manos ♡</t:banner>
    <div class="row g-3">
      <div class="col-8"><div class="card profile h-100"><div class="big-av"><t:ic n="user" s="44"/></div><div><h2 class="fs-5 fw-bold"><c:out value="${usuario.nombre}"/></h2><small class="d-block text-body-secondary"><c:out value="${usuario.email}"/></small><span class="badge rounded-pill bg-primary-subtle text-primary-emphasis my-1">Cliente desde ${usuario.anioRegistro}</span><small class="d-block text-body-secondary"><c:out value="${usuario.frase}"/></small></div>
        <a class="btn btn-outline-primary btn-sm ms-auto" href="#editar-perfil"><t:ic n="edit" s="14"/> Editar perfil</a></div></div>
      <div class="col-4"><div class="card rows h-100"><a class="r" href="${ctx}/miscitas"><div><span><t:ic n="calendar" s="16"/></span>Mis citas</div><em>${proximas} próximas ›</em></a>
        <a class="r" href="${ctx}/pedidos"><div><span><t:ic n="box" s="16"/></span>Mis pedidos</div><em>${enCamino} en camino ›</em></a>
        <a class="r mb-0" href="${ctx}/promociones"><div><span><t:ic n="heart" s="16"/></span>Promociones</div><em>Ver ofertas ›</em></a></div></div>

      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="user"/></span>Datos personales</h3>
        <div class="kv"><small>Nombre completo</small><c:out value="${usuario.nombre}"/></div><div class="kv"><small>Correo electrónico</small><c:out value="${usuario.email}"/></div><div class="kv"><small>Teléfono</small><c:out value="${empty usuario.telefono ? '—' : usuario.telefono}"/></div>
        <a class="btn btn-outline-primary btn-sm" href="#editar-datos"><t:ic n="edit" s="13"/> Editar datos</a></div></div>
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="pin"/></span>Direcciones de envío</h3>
        <c:forEach var="d" items="${direcciones}">
          <div class="pay d-block"><b class="small"><c:out value="${d.etiqueta}"/></b> <c:if test="${d.predeterminada}"><t:badge txt="Predeterminada" k="way"/></c:if><small><c:out value="${d.texto}"/></small></div>
        </c:forEach>
        <a class="btn-dash" href="#nueva-direccion">+ Agregar nueva dirección</a></div></div>
      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="card"/></span>Métodos de pago</h3>
        <c:forEach var="m" items="${metodosPago}">
          <div class="pay"><b class="v">${m.marca}</b><div>**** ${m.ultimos4}<small>Vence ${m.vence}</small></div></div>
        </c:forEach>
        <a class="btn-dash" href="#nuevo-pago">+ Agregar método de pago</a></div></div>

      <div class="col-4"><div class="card h-100"><h3><span><t:ic n="bell"/></span>Notificaciones</h3>
        <form method="post" action="${ctx}/usuario">
          <input type="hidden" name="accion" value="preferencias">
          <t:interruptor nombre="promos" texto="Promociones y descuentos" activo="${usuario.notifPromos}"/><t:interruptor nombre="pedidos" texto="Estado de tus pedidos" activo="${usuario.notifPedidos}"/><t:interruptor nombre="citas" texto="Recordatorios de citas" activo="${usuario.notifCitas}"/><t:interruptor nombre="novedades" texto="Novedades de la tienda" activo="${usuario.notifNovedades}"/>
          <button class="btn btn-outline-primary btn-sm mt-2" type="submit">Guardar preferencias</button>
        </form></div></div>
      <div class="col-4"><div class="card rows h-100"><h3><span><t:ic n="shield"/></span>Seguridad</h3>
        <button class="r w-100 text-start" form="acciones" formaction="${ctx}/aviso" name="msg" value="pronto"><div><span><t:ic n="lock" s="16"/></span>Cambiar contraseña</div><em>›</em></button>
        <button class="r mb-0 w-100 text-start" form="acciones" formaction="${ctx}/aviso" name="msg" value="pronto"><div><span><t:ic n="shield" s="16"/></span>Autenticación en dos pasos</div><em>Desactivada</em></button></div></div>
      <div class="col-4"><div class="card h-100"><h3 class="text-danger"><span class="text-danger"><t:ic n="trash"/></span>Eliminar cuenta</h3><p class="small text-body-secondary mb-3">Si ya no deseas usar tu cuenta, puedes eliminarla de forma permanente.</p>
        <a class="btn btn-outline-danger btn-sm w-100 mt-auto" href="#eliminar"><t:ic n="trash" s="14"/> Eliminar mi cuenta</a></div></div>
    </div>

    <div class="modal fade" id="editar-perfil" tabindex="-1">
      <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
      <div class="modal-dialog modal-dialog-centered"><form class="modal-content" method="post" action="${ctx}/usuario">
        <input type="hidden" name="accion" value="perfil">
        <div class="modal-header"><h5 class="modal-title fw-bold">Editar perfil</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
        <div class="modal-body">
          <label class="form-label small fw-bold" for="pf-nombre">Nombre</label>
          <input class="form-control mb-2" id="pf-nombre" name="nombre" maxlength="100" value="<c:out value='${usuario.nombre}'/>" required>
          <label class="form-label small fw-bold" for="pf-frase">Frase de tu perfil</label>
          <input class="form-control mb-2" id="pf-frase" name="frase" maxlength="150" value="<c:out value='${usuario.frase}'/>">
          <div class="row g-2">
            <div class="col-8"><label class="form-label small fw-bold" for="pf-genero">Género</label>
              <select class="form-select" id="pf-genero" name="genero"><c:forEach var="g" items="${generos}"><option value="${g}" ${usuario.genero == g ? 'selected' : ''}>${g}</option></c:forEach></select></div>
            <div class="col-4"><label class="form-label small fw-bold" for="pf-edad">Edad</label>
              <input class="form-control" type="number" id="pf-edad" name="edad" min="12" max="120" value="${usuario.edad}" required></div>
          </div>
        </div>
        <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cancelar</a><button type="submit" class="btn btn-primary">Guardar</button></div>
      </form></div>
    </div>

    <div class="modal fade" id="editar-datos" tabindex="-1">
      <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
      <div class="modal-dialog modal-dialog-centered"><form class="modal-content" method="post" action="${ctx}/usuario">
        <input type="hidden" name="accion" value="datos">
        <div class="modal-header"><h5 class="modal-title fw-bold">Editar datos personales</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
        <div class="modal-body">
          <label class="form-label small fw-bold" for="dt-nombre">Nombre completo</label>
          <input class="form-control mb-2" id="dt-nombre" name="nombre" maxlength="100" value="<c:out value='${usuario.nombre}'/>" required>
          <label class="form-label small fw-bold" for="dt-correo">Correo electrónico</label>
          <input class="form-control mb-2" type="email" id="dt-correo" name="correo" maxlength="100" value="<c:out value='${usuario.email}'/>" required>
          <label class="form-label small fw-bold" for="dt-tel">Teléfono</label>
          <input class="form-control" type="tel" id="dt-tel" name="telefono" maxlength="20" value="<c:out value='${usuario.telefono}'/>">
        </div>
        <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cancelar</a><button type="submit" class="btn btn-primary">Guardar</button></div>
      </form></div>
    </div>

    <div class="modal fade" id="nueva-direccion" tabindex="-1">
      <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
      <div class="modal-dialog modal-dialog-centered"><form class="modal-content" method="post" action="${ctx}/usuario">
        <input type="hidden" name="accion" value="direccion">
        <div class="modal-header"><h5 class="modal-title fw-bold">Agregar nueva dirección</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
        <div class="modal-body">
          <label class="form-label small fw-bold" for="dr-etiqueta">Nombre de la dirección</label>
          <input class="form-control mb-2" id="dr-etiqueta" name="etiqueta" maxlength="50" placeholder="Casa, Trabajo...">
          <label class="form-label small fw-bold" for="dr-calle">Calle y número</label>
          <input class="form-control mb-2" id="dr-calle" name="calle" maxlength="150" required>
          <label class="form-label small fw-bold" for="dr-colonia">Colonia</label>
          <input class="form-control mb-2" id="dr-colonia" name="colonia" maxlength="100">
          <div class="row g-2 mb-2">
            <div class="col-6"><label class="form-label small fw-bold" for="dr-ciudad">Ciudad</label><input class="form-control" id="dr-ciudad" name="ciudad" maxlength="100" required></div>
            <div class="col-3"><label class="form-label small fw-bold" for="dr-estado">Estado</label><input class="form-control" id="dr-estado" name="estado" maxlength="50" required></div>
            <div class="col-3"><label class="form-label small fw-bold" for="dr-cp">C.P.</label><input class="form-control" id="dr-cp" name="cp" inputmode="numeric" pattern="[0-9]{5}" maxlength="5" required></div>
          </div>
          <div class="form-check"><input class="form-check-input" type="checkbox" id="dr-pred" name="predeterminada"><label class="form-check-label small" for="dr-pred">Usar como dirección predeterminada</label></div>
        </div>
        <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cancelar</a><button type="submit" class="btn btn-primary">Guardar dirección</button></div>
      </form></div>
    </div>

    <div class="modal fade" id="nuevo-pago" tabindex="-1">
      <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
      <div class="modal-dialog modal-dialog-centered"><form class="modal-content" method="post" action="${ctx}/usuario" autocomplete="off">
        <input type="hidden" name="accion" value="pago">
        <div class="modal-header"><h5 class="modal-title fw-bold">Agregar método de pago</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
        <div class="modal-body">
          <label class="form-label small fw-bold" for="pg-titular">Nombre del titular</label>
          <input class="form-control mb-2" id="pg-titular" name="titular" maxlength="100" required>
          <label class="form-label small fw-bold" for="pg-numero">Número de tarjeta</label>
          <input class="form-control mb-1" id="pg-numero" name="numero" inputmode="numeric" maxlength="23" placeholder="0000 0000 0000 0000" required>
          <small class="d-block text-body-secondary mb-2">Solo guardamos los últimos 4 dígitos.</small>
          <div class="row g-2 mb-2">
            <div class="col-6"><label class="form-label small fw-bold" for="pg-mes">Mes de vencimiento</label>
              <select class="form-select" id="pg-mes" name="mes"><c:forEach var="m" begin="1" end="12"><option value="${m}">${m < 10 ? '0' : ''}${m}</option></c:forEach></select></div>
            <div class="col-6"><label class="form-label small fw-bold" for="pg-anio">Año</label>
              <select class="form-select" id="pg-anio" name="anio"><c:forEach var="y" begin="${anioActual}" end="${anioActual + 12}"><option value="${y}">${y}</option></c:forEach></select></div>
          </div>
          <div class="form-check"><input class="form-check-input" type="checkbox" id="pg-pred" name="predeterminado"><label class="form-check-label small" for="pg-pred">Usar como método predeterminado</label></div>
        </div>
        <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Cancelar</a><button type="submit" class="btn btn-primary">Guardar tarjeta</button></div>
      </form></div>
    </div>

    <div class="modal fade" id="eliminar" tabindex="-1">
      <a class="modal-bg" href="#cerrar" aria-label="Cerrar"></a>
      <div class="modal-dialog modal-dialog-centered modal-sm"><div class="modal-content">
        <div class="modal-header"><h5 class="modal-title fw-bold">Eliminar cuenta</h5><a class="btn-close" href="#cerrar" aria-label="Cerrar"></a></div>
        <div class="modal-body">Esta acción es permanente. ¿Deseas continuar?</div>
        <div class="modal-footer"><a class="btn btn-outline-primary" href="#cerrar">Volver</a><button type="submit" class="btn btn-primary" form="acciones" formaction="${ctx}/aviso" name="msg" value="solicitud">Eliminar</button></div>
      </div></div>
    </div>
</t:layout>
