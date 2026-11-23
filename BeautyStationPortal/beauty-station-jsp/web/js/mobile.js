/* Beauty Station · comportamiento móvil. Se carga desde head.jspf; solo actúa en pantallas de hasta 768 px (o dentro de la app Android). */
(function () {
  if (window.__bsMobileJs) return;
  window.__bsMobileJs = true;

  // En el navegador solo se activa en pantallas angostas; si se cruza el umbral (girar el teléfono) se recarga.
  var esApp = /BeautyStationApp/.test(navigator.userAgent);
  var mq = window.matchMedia('(max-width: 768px)');
  var activo = esApp || mq.matches;
  if (!esApp) {
    var cambio = function (e) { if (e.matches !== activo) location.reload(); };
    if (mq.addEventListener) mq.addEventListener('change', cambio); else mq.addListener(cambio);
  }
  if (!activo) return;
  document.documentElement.classList.add('bs-mobile');

  function ready(fn) {
    if (document.readyState !== 'loading') fn();
    else document.addEventListener('DOMContentLoaded', fn);
  }
  function svgDe(el) {
    var s = el && el.querySelector('svg');
    return s ? s.outerHTML : '';
  }
  function clave(a) {
    return (a.getAttribute('href') || '').split('?')[0].replace(/\/+$/, '').split('/').pop();
  }
  function crear(tag, clase, html) {
    var e = document.createElement(tag);
    if (clase) e.className = clase;
    if (html) e.innerHTML = html;
    return e;
  }

  ready(function () {
    var nav = document.getElementById('nav');
    if (!nav || document.getElementById('bs-tabbar')) { filtrosPlegables(); tirasHorizontales(); return; }

    var enlaces = [].slice.call(nav.querySelectorAll('a'));
    var usuario = document.querySelector('.sidebar .user-pill');
    var salir = document.getElementById('logout');
    var porClave = {};
    enlaces.forEach(function (a) { porClave[clave(a)] = a; });

    /* ----- pestañas de la barra inferior ----- */
    var pestanas = [];
    if (porClave['inicio']) {                       // clienta: Inicio · Citas · Catálogo · Promos · Usuario
      [['inicio', 'Inicio'], ['citas', 'Citas'], ['catalogo', 'Catálogo'], ['promociones', 'Promos']].forEach(function (p) {
        var a = porClave[p[0]];
        if (a) pestanas.push({ href: a.href, texto: p[1], icono: svgDe(a), activa: a.classList.contains('active') });
      });
      if (usuario) {
        var hayActiva = pestanas.some(function (t) { return t.activa; });
        pestanas.push({ href: usuario.href, texto: 'Usuario', icono: svgDe(usuario.querySelector('.avatar')),
                        activa: usuario.classList.contains('active') || !hayActiva });
      }
    } else {                                        // administración: sus secciones + Salir
      enlaces.forEach(function (a) {
        pestanas.push({ href: a.href, texto: a.textContent.trim(), icono: svgDe(a), activa: a.classList.contains('active') });
      });
      if (salir) pestanas.push({ texto: 'Salir', icono: svgDe(salir), accion: function () { salir.click(); } });
    }

    var barra = crear('nav');
    barra.id = 'bs-tabbar';
    pestanas.forEach(function (t) {
      var el = t.href ? crear('a', t.activa ? 'on' : '', t.icono + '<span></span>') : crear('button', '', t.icono + '<span></span>');
      el.querySelector('span').textContent = t.texto;
      if (t.href) el.href = t.href;
      if (t.accion) el.addEventListener('click', t.accion);
      barra.appendChild(el);
    });
    document.body.appendChild(barra);

    /* ----- menú con todas las secciones (se abre con el logo de la barra superior) ----- */
    var topbar = document.querySelector('.topbar');
    if (topbar) {
      var logo = crear('button', 'bs-logo', '<span class="logo"><span>B</span><span>S</span></span>');
      logo.type = 'button';
      logo.setAttribute('aria-label', 'Menú');
      topbar.insertBefore(logo, topbar.firstChild);

      var hoja = crear('div', 'bs-sheet');
      var fondo = crear('div', 'bs-bg');
      var panel = crear('div', 'bs-panel');
      panel.appendChild(crear('h2', '', 'BEAUTY STATION'));
      enlaces.forEach(function (a) {
        var item = crear('a', a.classList.contains('active') ? 'on' : '', svgDe(a) + '<span></span>');
        item.href = a.href;
        item.querySelector('span').textContent = a.textContent.trim();
        panel.appendChild(item);
      });
      if (usuario) {
        var yo = crear('a', usuario.classList.contains('active') ? 'on' : '', svgDe(usuario.querySelector('.avatar')) + '<span>Usuario</span>');
        yo.href = usuario.href;
        panel.appendChild(yo);
      }
      if (salir) {
        var cerrar = crear('button', 'bs-salir', svgDe(salir) + '<span>Cerrar sesión</span>');
        cerrar.type = 'button';
        cerrar.addEventListener('click', function () { salir.click(); });
        panel.appendChild(cerrar);
      }
      hoja.appendChild(fondo);
      hoja.appendChild(panel);
      document.body.appendChild(hoja);
      logo.addEventListener('click', function () { hoja.classList.add('open'); });
      fondo.addEventListener('click', function () { hoja.classList.remove('open'); });
    }

    filtrosPlegables();
    tirasHorizontales();
  });

  /* Catálogo: los filtros quedan plegados detrás de un botón "Filtrar y ordenar" */
  function filtrosPlegables() {
    var filtros = document.querySelector('.filters');
    if (!filtros || document.querySelector('.bs-filter-toggle')) return;
    var columna = filtros.closest('[class*="col-"]') || filtros;
    var boton = crear('button', 'bs-filter-toggle', '<span>Filtrar y ordenar</span><span>▾</span>');
    boton.type = 'button';
    columna.parentNode.insertBefore(boton, columna);
    columna.classList.add('bs-collapsed');
    boton.addEventListener('click', function () {
      var oculto = columna.classList.toggle('bs-collapsed');
      boton.lastChild.textContent = oculto ? '▾' : '▴';
    });
  }

  /* Tira de categorías del catálogo / promociones: se desliza en horizontal */
  function tirasHorizontales() {
    var tab = document.querySelector('.cat-tab');
    if (tab && tab.parentNode) tab.parentNode.classList.add('bs-hscroll');
  }
})();
