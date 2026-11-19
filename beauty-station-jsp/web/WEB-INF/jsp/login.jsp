<!DOCTYPE html>
<html lang="es">
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>

  <section id="login" class="screen login">
    <div class="blob blob-tl"></div><div class="blob blob-bl"></div>
    <form id="login-form" class="login-card" method="post" action="${ctx}/login" autocomplete="off">
      <div class="logo logo-lg"><span>B</span><span>S</span></div>
      <h1 class="h2 fw-extrabold mt-2">¡Bienvenido!</h1>
      <p class="login-sub">Inicia sesión para continuar y disfrutar<br>de nuestros servicios.</p>

      <c:if test="${not empty error}"><div class="alert alert-danger py-2 small w-100" role="alert"><c:out value="${error}"/></div></c:if>

      <label class="field" id="f-mail">
        <span><t:ic n="user"/></span>
        <input type="email" name="correo" class="form-control" placeholder="Correo electrónico" value="<c:out value='${param.correo}'/>" required>
      </label>

      <input type="checkbox" id="ver-pass" class="visually-hidden">
      <div class="field" id="f-pass">
        <span><t:ic n="lock"/></span>
        <input type="text" name="clave" class="form-control clave" placeholder="Contraseña" autocomplete="off" required>
        <label for="ver-pass" class="ojo" aria-label="Mostrar contraseña"><t:ic n="eye"/></label>
      </div>

      <div class="d-flex justify-content-between w-100 small mb-3">
        <div class="form-check">
          <input class="form-check-input" type="checkbox" id="remember" checked>
          <label class="form-check-label" for="remember">Recordar mi sesión</label>
        </div>
        <a href="#" class="link">¿Olvidaste tu contraseña?</a>
      </div>

      <button class="btn btn-primary w-100" type="submit">Iniciar sesión →</button>
      <div class="divider"><span>o continúa con</span></div>
      <button type="submit" class="btn btn-google" id="google-btn" name="google" value="1" formnovalidate><b>G</b> Google</button>
      <p class="login-foot">¿No tienes una cuenta? <a href="${ctx}/registro" class="link strong">Regístrate</a></p>
    </form>
    <div class="login-photo"></div>
  </section>

  <c:if test="${empty param.salir and empty error}">
    <section id="splash" class="screen splash">
      <div class="blob blob-tl"></div><div class="blob blob-br"></div>
      <div class="splash-content">
        <div class="logo logo-xl"><span>B</span><span>S</span></div>
        <p class="splash-text">Un momento mas...</p>
        <div class="spinner-border" role="status" style="width:56px;height:56px;border-width:6px;color:var(--pink-hot)"></div>
      </div>
    </section>
  </c:if>
</body>
</html>
