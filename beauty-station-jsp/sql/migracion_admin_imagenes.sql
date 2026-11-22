SET NAMES utf8mb4;
USE emprendedora_db;

-- Ejecutar UNA sola vez

ALTER TABLE Usuario  ADD COLUMN Rol VARCHAR(10) NOT NULL DEFAULT 'CLIENTE';
ALTER TABLE Producto ADD COLUMN Imagen VARCHAR(255), ADD COLUMN ImagenPromo VARCHAR(255),
                     ADD COLUMN Activo TINYINT(1) NOT NULL DEFAULT 1;
ALTER TABLE Servicio ADD COLUMN Imagen VARCHAR(255);


UPDATE Producto SET Imagen='img/productos/bolsa-tote-rosa.jpg', ImagenPromo='img/productos/promo-bolsa-tote.jpg' WHERE Id_Prod=1;
UPDATE Producto SET Imagen='img/productos/bolsa-hombro-negra.jpg' WHERE Id_Prod=2;
UPDATE Producto SET Imagen='img/productos/vestido-largo-elegante.jpg' WHERE Id_Prod=3;
UPDATE Producto SET Imagen='img/productos/termo-acero.jpg', ImagenPromo='img/productos/promo-termo.jpg' WHERE Id_Prod=4;
UPDATE Producto SET Imagen='img/productos/set-maquillaje.jpg', ImagenPromo='img/productos/promo-maquillaje.jpg' WHERE Id_Prod=5;
UPDATE Producto SET Imagen='img/productos/tenis-casuales.jpg' WHERE Id_Prod=6;
UPDATE Producto SET Imagen='img/productos/vestido-corto.jpg' WHERE Id_Prod=7;
UPDATE Producto SET Imagen='img/productos/bolsa-crossbody-beige.jpg' WHERE Id_Prod=8;
UPDATE Producto SET Imagen='img/productos/vestido-midi-rosa.jpg', ImagenPromo='img/productos/promo-vestido.jpg' WHERE Id_Prod=9;
UPDATE Producto SET Imagen='img/productos/lentes-sol.jpg' WHERE Id_Prod=10;
UPDATE Producto SET Imagen='img/productos/sueter-ligero.jpg' WHERE Id_Prod=11;
UPDATE Producto SET Imagen='img/productos/perfume-floral.jpg' WHERE Id_Prod=12;
UPDATE Servicio SET Imagen='img/servicios/corte-cabello.jpg' WHERE Id_Serv=1;
UPDATE Servicio SET Imagen='img/servicios/peinado-planchado.jpg' WHERE Id_Serv=2;
UPDATE Servicio SET Imagen='img/servicios/tinte-coloracion.jpg' WHERE Id_Serv=3;
UPDATE Servicio SET Imagen='img/servicios/tratamiento-capilar.jpg' WHERE Id_Serv=4;
UPDATE Servicio SET Imagen='img/servicios/manicure-pedicure.jpg' WHERE Id_Serv=5;

-- Cuenta de admin: admin@beautystation.com / admin123
INSERT INTO Usuario (Id, Nombre, Genero, Edad, Email, Telefono, Contrasena, FechaRegistro, Frase, Rol) VALUES
(3, 'Administradora BS', 'Femenino', 30, 'admin@beautystation.com', NULL,
 'pbkdf2$65536$AAECAwQFBgcICQoLDA0ODw==$KgCG32zsYeYL2ZUca8RqEbVHZL8GanxoDlSDXXmo+78=', '2023-01-01', 'Panel de administración', 'ADMIN');
