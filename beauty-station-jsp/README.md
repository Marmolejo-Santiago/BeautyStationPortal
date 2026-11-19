# Beauty Station Portal 

Ya quedo conectada a MySQL con Hibernate.
Requiere:
JDK 17
Maven 3
Tomcat 9


## 1. Ejecutar Base de datos

Abre my sql y ejecuta el scrip de sql, luego presiona Alt+F12 en Intelijent y copia y pega este comando en el bash
```bash
Get-Content "C:\Users\desk\Desktop\base_datos_emprendedora.sql" -Encoding utf8 -Raw | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" --default-character-set=utf8mb4 -u root -p
```
Una vez copiado si te pide tu contraseña pon la tuya y se correra la bd
La conexión está en `src/main/resources/hibernate.cfg.xml` y tiene por defecto `root` / `root` en `localhost:3306`).
Si quieres cambiar el user y la contraseña metete a `src/main/resources/hibernate.cfg.xml` y cambiala

## 2. Compilar y desplegar
Si tienes que hacerlo puedes hacerlo desde la pestaña de maven pero no sera necesario ya que subo el .war
```bash
mvn clean package
cp target/beauty-station.war $CATALINA_HOME/webapps/
```
Despues configura el tomcat con el deployment directory en la carpeta beauty-station de target y el mismo context path

Usuario de ejemplo: `sofia.martinez@email.com` / `sofia123`

## Cambios principales
- **Varios servicios en una cita**
- **Filtro de precio scrolleable**
- **Usuario**: 
  - editar perfil 
  - editar datos 
  - agregar dirección 
  - agregar método de pago 
  - guardar preferencias
- **Se añadieron notificaciones**
- **Se añadio el carrito**
- **Se añadio registrarse**
- **Reseñas**

## Falta añadir solamente imagenes en la carpeta que ya esta creada (Leeme.txt)