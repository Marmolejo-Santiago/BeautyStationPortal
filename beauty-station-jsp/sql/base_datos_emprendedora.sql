SET NAMES utf8mb4;

DROP DATABASE IF EXISTS emprendedora_db;
CREATE DATABASE emprendedora_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE emprendedora_db;

CREATE TABLE Usuario (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100),
    Genero VARCHAR(20),
    Edad INT,
    Email VARCHAR(100) UNIQUE,
    Telefono VARCHAR(20),
    Contrasena VARCHAR(255),
    FechaRegistro DATE,
    Frase VARCHAR(150),
    NotifPromos TINYINT(1) NOT NULL DEFAULT 1,
    NotifPedidos TINYINT(1) NOT NULL DEFAULT 1,
    NotifCitas TINYINT(1) NOT NULL DEFAULT 1,
    NotifNovedades TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE Emprendedora (
    Id_Emprendedora INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100),
    Rol VARCHAR(50),
    Servicios VARCHAR(255),
    Horario VARCHAR(100),
    Contrasena VARCHAR(255)
);

CREATE TABLE Categoria (
    Id_Categoria INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL UNIQUE,
    Emoji VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
);

CREATE TABLE Producto (
    Id_Prod INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(255),
    Stock INT,
    CostoProd DECIMAL(10,2),
    Proveedor VARCHAR(100),
    Precio DECIMAL(10,2) NOT NULL,
    Emoji VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    Etiqueta VARCHAR(30),
    Descuento INT NOT NULL DEFAULT 0,
    Rating DOUBLE NOT NULL DEFAULT 0,
    NumResenas INT NOT NULL DEFAULT 0,
    Id_Categoria INT,
    Id_Usuario INT,
    FOREIGN KEY (Id_Categoria) REFERENCES Categoria(Id_Categoria),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id)
);

CREATE TABLE Servicio (
    Id_Serv INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Emoji VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    Costo DECIMAL(10,2),
    EspecificacionServ VARCHAR(255),
    DurAprox INT
);

CREATE TABLE Promociones (
    ID_Promo INT AUTO_INCREMENT PRIMARY KEY,
    Concepto VARCHAR(100),
    Duracion VARCHAR(50),
    Especificaciones VARCHAR(255),
    Id_Emprendedora INT,
    FOREIGN KEY (Id_Emprendedora) REFERENCES Emprendedora(Id_Emprendedora)
);

CREATE TABLE Citas (
    Id_Cita INT AUTO_INCREMENT PRIMARY KEY,
    Fecha DATETIME,
    Especificacion VARCHAR(255),
    Costo DECIMAL(10,2),
    Duracion INT,
    Servicio VARCHAR(255),
    Area VARCHAR(100),
    Cliente VARCHAR(100),
    Estado VARCHAR(20) NOT NULL DEFAULT 'Confirmada',
    Id_Usuario INT,
    Id_Emprendedora INT,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id),
    FOREIGN KEY (Id_Emprendedora) REFERENCES Emprendedora(Id_Emprendedora)
);

CREATE TABLE Cita_Servicio (
    Id_Cita_Serv INT AUTO_INCREMENT PRIMARY KEY,
    Id_Cita INT NOT NULL,
    Id_Serv INT NOT NULL,
    UNIQUE (Id_Cita, Id_Serv),
    FOREIGN KEY (Id_Cita) REFERENCES Citas(Id_Cita) ON DELETE CASCADE,
    FOREIGN KEY (Id_Serv) REFERENCES Servicio(Id_Serv)
);

CREATE TABLE Venta (
    Id_venta INT AUTO_INCREMENT PRIMARY KEY,
    FechaVenta DATE,
    DetalleVenta VARCHAR(255),
    Monto DECIMAL(10,2),
    Beneficiario VARCHAR(100),
    Id_Prod INT,
    Id_Serv INT,
    FOREIGN KEY (Id_Prod) REFERENCES Producto(Id_Prod),
    FOREIGN KEY (Id_Serv) REFERENCES Servicio(Id_Serv)
);

CREATE TABLE Realiza (
    Id_Emprendedora INT,
    Id_Serv INT,
    PRIMARY KEY (Id_Emprendedora, Id_Serv),
    FOREIGN KEY (Id_Emprendedora) REFERENCES Emprendedora(Id_Emprendedora),
    FOREIGN KEY (Id_Serv) REFERENCES Servicio(Id_Serv)
);

CREATE TABLE Alteran (
    Id_Prod INT,
    Id_Serv INT,
    ID_Promo INT,
    PRIMARY KEY (Id_Prod, Id_Serv, ID_Promo),
    FOREIGN KEY (Id_Prod) REFERENCES Producto(Id_Prod),
    FOREIGN KEY (Id_Serv) REFERENCES Servicio(Id_Serv),
    FOREIGN KEY (ID_Promo) REFERENCES Promociones(ID_Promo)
);

CREATE TABLE Direccion (
    Id_Direccion INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Etiqueta VARCHAR(50),
    Calle VARCHAR(150) NOT NULL,
    Colonia VARCHAR(100),
    Ciudad VARCHAR(100) NOT NULL,
    Estado VARCHAR(50) NOT NULL,
    CodigoPostal VARCHAR(10) NOT NULL,
    Predeterminada TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id)
);

CREATE TABLE MetodoPago (
    Id_Metodo INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Marca VARCHAR(20) NOT NULL,
    Titular VARCHAR(100),
    Ultimos4 CHAR(4) NOT NULL,
    Vence VARCHAR(5) NOT NULL,
    Predeterminado TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id)
);

CREATE TABLE Carrito (
    Id_Carrito INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Id_Prod INT NOT NULL,
    Cantidad INT NOT NULL DEFAULT 1,
    UNIQUE (Id_Usuario, Id_Prod),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id) ON DELETE CASCADE,
    FOREIGN KEY (Id_Prod) REFERENCES Producto(Id_Prod)
);

CREATE TABLE Pedido (
    Id_Pedido INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Fecha DATE NOT NULL,
    Estado VARCHAR(20) NOT NULL,
    Total DECIMAL(10,2) NOT NULL DEFAULT 0,
    FechaEntrega DATE NULL,
    Id_Direccion INT NULL,
    Id_Metodo INT NULL,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id),
    FOREIGN KEY (Id_Direccion) REFERENCES Direccion(Id_Direccion),
    FOREIGN KEY (Id_Metodo) REFERENCES MetodoPago(Id_Metodo)
);

CREATE TABLE DetallePedido (
    Id_Detalle INT AUTO_INCREMENT PRIMARY KEY,
    Id_Pedido INT NOT NULL,
    Id_Prod INT NOT NULL,
    Cantidad INT NOT NULL,
    PrecioUnit DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (Id_Pedido) REFERENCES Pedido(Id_Pedido) ON DELETE CASCADE,
    FOREIGN KEY (Id_Prod) REFERENCES Producto(Id_Prod)
);

CREATE TABLE Notificacion (
    Id_Notif INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Tipo VARCHAR(20) NOT NULL,
    Titulo VARCHAR(100) NOT NULL,
    Mensaje VARCHAR(255),
    Fecha DATETIME NOT NULL,
    Leida TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id) ON DELETE CASCADE
);

CREATE TABLE Resena (
    Id_Resena INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT NOT NULL,
    Id_Prod INT NOT NULL,
    Calificacion TINYINT NOT NULL,
    Comentario VARCHAR(500),
    Fecha DATETIME NOT NULL,
    UNIQUE (Id_Usuario, Id_Prod),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id),
    FOREIGN KEY (Id_Prod) REFERENCES Producto(Id_Prod)
);

#Ejemplos

INSERT INTO Usuario (Id, Nombre, Genero, Edad, Email, Telefono, Contrasena, FechaRegistro, Frase) VALUES
(1, 'Sofía Martínez', 'Femenino', 25, 'sofia.martinez@email.com', '+52 55 1234 5678',
 'pbkdf2$65536$AAECAwQFBgcICQoLDA0ODw==$u1Zj5BtkH1wOZIkmKSonLJKpfKpnWU+aGWoshJ7Ewac=', '2023-03-15', 'Siempre hermosa, siempre tú ♡'),
(2, 'Valeria Ruiz', 'Femenino', 29, 'valeria.ruiz@email.com', '+52 33 9876 5432',
 'pbkdf2$65536$AAECAwQFBgcICQoLDA0ODw==$u1Zj5BtkH1wOZIkmKSonLJKpfKpnWU+aGWoshJ7Ewac=', '2024-01-10', 'Siempre hermosa, siempre tú ♡');

INSERT INTO Categoria (Id_Categoria, Nombre, Emoji) VALUES
(1, 'Bolsas', '👜'), (2, 'Vestidos', '👗'), (3, 'Ropa', '👚'), (4, 'Calzado', '👟'),
(5, 'Accesorios', '🕶️'), (6, 'Maquillaje', '💄'), (7, 'Termos', '🥤');

INSERT INTO Producto (Id_Prod, Nombre, Descripcion, Stock, Precio, Emoji, Etiqueta, Descuento, Rating, NumResenas, Id_Categoria) VALUES
(1, 'Bolsa tote rosa', 'Elegancia y estilo en un solo accesorio. Ideal para cualquier ocasión.', 50, 799, '👜', 'Nuevo', 30, 4.5, 12, 1),
(2, 'Bolsa de hombro negra', NULL, 50, 1299, '👜', 'Más vendido', 0, 5, 28, 1),
(3, 'Vestido largo elegante', NULL, 50, 1499, '👗', 'Nuevo', 0, 4.5, 17, 2),
(4, 'Termo de acero inoxidable', 'Mantiene tus bebidas frías o calientes por más tiempo.', 50, 649, '🥤', 'Más vendido', 15, 5, 34, 7),
(5, 'Set de maquillaje profesional', 'Todo lo que necesitas para un look perfecto, en un solo set.', 50, 1119, '💄', 'Nuevo', 20, 4.5, 22, 6),
(6, 'Tenis casuales', NULL, 50, 649, '👟', 'Más vendido', 0, 4.5, 18, 4),
(7, 'Vestido corto', NULL, 50, 999, '🖤', 'Nuevo', 0, 4, 9, 2),
(8, 'Bolsa crossbody beige', NULL, 50, 749, '👛', 'Nuevo', 0, 4.5, 9, 1),
(9, 'Vestido midi rosa', 'Perfecto para eventos especiales. Disponible en varias tallas y colores.', 50, 799, '👗', NULL, 25, 4.5, 14, 2),
(10, 'Lentes de sol', NULL, 50, 499, '🕶️', NULL, 0, 4, 11, 5),
(11, 'Suéter ligero', NULL, 50, 749, '🧥', NULL, 0, 4.5, 8, 3),
(12, 'Perfume floral', NULL, 50, 899, '🧴', NULL, 0, 5, 21, 6);

INSERT INTO Servicio (Id_Serv, Nombre, Emoji, Costo, DurAprox) VALUES
(1, 'Corte de cabello', '✂️', 250, 45),
(2, 'Peinado / Planchado', '💇‍', 300, 60),
(3, 'Tinte o coloración', '🎨', 650, 120),
(4, 'Tratamiento capilar', '🧖‍', 450, 90),
(5, 'Manicure y pedicure', '💅', 350, 60);

INSERT INTO Citas (Id_Cita, Fecha, Costo, Duracion, Servicio, Area, Cliente, Estado, Id_Usuario) VALUES
(1, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 3 DAY),  '09:00:00'), 250, 45,  'Corte de cabello', 'Showroom', 'Sofía Martínez', 'Confirmada', 1),
(2, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 5 DAY),  '12:00:00'), 350, 60,  'Manicure y pedicure', 'Showroom', 'Sofía Martínez', 'Confirmada', 1),
(3, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 8 DAY),  '16:00:00'), 750, 150, 'Tratamiento capilar + Peinado / Planchado', 'Showroom', 'Sofía Martínez', 'Pendiente', 1),
(4, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 13 DAY), '11:00:00'), 650, 120, 'Tinte o coloración', 'Showroom', 'Sofía Martínez', 'Cancelada', 1);
INSERT INTO Cita_Servicio (Id_Cita, Id_Serv) VALUES (1,1), (2,5), (3,4), (3,2), (4,3);

INSERT INTO Direccion (Id_Direccion, Id_Usuario, Etiqueta, Calle, Colonia, Ciudad, Estado, CodigoPostal, Predeterminada) VALUES
(1, 1, 'Dirección principal', 'Av. Benito Juárez 123', 'Centro', 'Guadalajara', 'Jal.', '44100', 1);

INSERT INTO MetodoPago (Id_Metodo, Id_Usuario, Marca, Titular, Ultimos4, Vence, Predeterminado) VALUES
(1, 1, 'VISA', 'Sofía Martínez', '4242', '12/28', 1);

INSERT INTO Pedido (Id_Pedido, Id_Usuario, Fecha, Estado, FechaEntrega, Id_Direccion, Id_Metodo) VALUES
(1245, 1, DATE_SUB(CURDATE(), INTERVAL 3 DAY),  'En camino', DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1, 1),
(1230, 1, DATE_SUB(CURDATE(), INTERVAL 25 DAY), 'Entregado', DATE_SUB(CURDATE(), INTERVAL 20 DAY), 1, 1),
(1218, 1, DATE_SUB(CURDATE(), INTERVAL 40 DAY), 'Entregado', DATE_SUB(CURDATE(), INTERVAL 35 DAY), 1, 1),
(1205, 1, DATE_SUB(CURDATE(), INTERVAL 50 DAY), 'Cancelado', NULL, 1, 1);
INSERT INTO DetallePedido (Id_Pedido, Id_Prod, Cantidad, PrecioUnit) VALUES
(1245, 1, 1, 559), (1245, 3, 1, 1499), (1245, 4, 1, 552),
(1230, 5, 2, 895), (1230, 12, 1, 899),
(1218, 8, 1, 749), (1218, 4, 1, 552),
(1205, 2, 1, 1299);
UPDATE Pedido p SET p.Total = (SELECT SUM(d.Cantidad * d.PrecioUnit) FROM DetallePedido d WHERE d.Id_Pedido = p.Id_Pedido);

INSERT INTO Notificacion (Id_Usuario, Tipo, Titulo, Mensaje, Fecha, Leida) VALUES
(1, 'pedido', 'Tu pedido está en camino', 'El pedido BS-001245 llegará pronto.', DATE_SUB(NOW(), INTERVAL 2 DAY), 0),
(1, 'cita', 'Recordatorio de cita', 'Tienes una cita próxima: Corte de cabello.', DATE_SUB(NOW(), INTERVAL 1 DAY), 0),
(1, 'promo', 'Promociones de temporada', 'Hasta 30% de descuento en productos seleccionados.', NOW(), 0);

INSERT INTO Resena (Id_Usuario, Id_Prod, Calificacion, Comentario, Fecha) VALUES
(2, 4, 5, 'Mantiene mi bebida caliente todo el día, me encantó.', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(2, 6, 4, 'Muy cómodos para el diario.', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2, 12, 5, 'Un aroma delicado y dura bastante.', DATE_SUB(NOW(), INTERVAL 2 DAY));
