<!DOCTYPE html>
<html lang="es">
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>

  <section id="registro" class="screen login registro">
    <div class="blob blob-tl"></div><div class="blob blob-bl"></div>
    <form id="registro-form" class="login-card" method="post" action="${ctx}/registro" autocomplete="off">
      <div class="logo logo-lg"><span>B</span><span>S</span></div>
      <h1 class="h2 fw-extrabold mt-2">Crea tu cuenta</h1>
      <p class="login-sub">Regístrate para agendar citas y comprar<br>en Beauty Station.</p>

      <c:if test="${not empty error}"><div class="alert alert-danger py-2 small w-100" role="alert"><c:out value="${error}"/></div></c:if>

      <label class="field">
        <span><t:ic n="user"/></span>
        <input type="text" name="nombre" class="form-control" placeholder="Nombre completo" maxlength="100" value="<c:out value='${v_nombre}'/>" required>
      </label>

      <div class="d-flex gap-2 w-100">
        <label class="field">
          <select name="genero" class="form-control" required>
            <option value="" disabled ${empty v_genero ? 'selected' : ''}>Género</option>
            <c:forEach var="g" items="${generos}"><option value="${g}" ${v_genero == g ? 'selected' : ''}>${g}</option></c:forEach>
          </select>
        </label>
        <label class="field" style="max-width:110px">
          <input type="number" name="edad" class="form-control" placeholder="Edad" min="12" max="120" value="${v_edad}" required>
        </label>
      </div>

      <label class="field">
        <span><t:ic n="mail"/></span>
        <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" maxlength="100" value="<c:out value='${v_correo}'/>" required>
      </label>

      <label class="field">
        <span><t:ic n="phone"/></span>
        <input type="tel" name="telefono" class="form-control" placeholder="Teléfono (opcional)" maxlength="20" value="<c:out value='${v_telefono}'/>">
      </label>

      <input type="checkbox" id="ver-pass" class="visually-hidden">
      <div class="field" id="f-pass">
        <span><t:ic n="lock"/></span>
        <input type="text" name="clave" class="form-control clave" placeholder="Contraseña (mínimo 6 caracteres)" autocomplete="off" required>
        <label for="ver-pass" class="ojo" aria-label="Mostrar contraseña"><t:ic n="eye"/></label>
      </div>
      <div class="field" id="f-pass2">
        <span><t:ic n="lock"/></span>
        <input type="text" name="confirmar" class="form-control clave" placeholder="Confirmar contraseña" autocomplete="off" required>
      </div>

      <button class="btn btn-primary w-100" type="submit">Crear cuenta →</button>
      <p class="login-foot">¿Ya tienes una cuenta? <a href="${ctx}/login?salir=1" class="link strong">Inicia sesión</a></p>
    </form>
    <div class="login-photo"></div>
  </section>
</body>
</html>
