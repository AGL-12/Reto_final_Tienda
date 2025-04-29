drop database tienda_brico;
create database tienda_brico;
use tienda_brico;
create table cliente(
id_clien int primary key,
usuario varchar(50) unique,
contra varchar(50),
dni char(9) unique not null,
correo varchar(320) not null,
direccion varchar(200),
metodo_pago enum("visa","mastercard","paypal"),
num_cuenta char(16),
esAdmin boolean default false
);
create table pedido(
id_ped int primary key,
id_clien int,
total float default 0,
fecha_compra datetime,
foreign key (id_clien) references cliente (id_clien) on delete cascade
);
create table articulo(
id_art int primary key,
nombre varchar(200) not null,
descripcion varchar(200),
stock int default 0,
precio float default 0 check (precio>=0),
oferta float check (oferta between 0 and 100),
seccion enum("pintura","jardineria","fontaneria","soldadura","carpinteria")
);
create table compra(
id_art int,
id_ped int,
cantidad int default 1 check (cantidad>=1),
primary key(id_art,id_ped),
foreign key (id_art) references articulo (id_art) on delete cascade,
foreign key (id_ped) references pedido (id_ped)on delete cascade
);
INSERT INTO cliente VALUES 
(1,'admin', 'admin', '13245678k', 'adm@gmail.com', 'mi casa', 'visa', '1234567887654321', '1'),
(2,'usuario1', 'usuario1', '12345678A', 'usuario1@example.com', 'Calle Falsa 123', 'visa', '1111222233334444', false),
(3,'usuario2', 'usuario2', '23456789B', 'usuario2@example.com', 'Avenida Siempre Viva 742', 'mastercard', '5555666677778888', false),
(4,'usuario3', 'usuario3', '34567890C', 'usuario3@example.com', 'Plaza Mayor 10', 'paypal', '9999000011112222', false),
(5,'usuario4', 'usuario4', '45678901D', 'usuario4@example.com', 'Calle Luna 45', 'visa', '3333444455556666', false),
(6,'usuario5', 'usuario5', '45678491D', 'usuario5@example.com', 'Calle Sol 45', 'visa', '3333444455556633', false);

INSERT INTO articulo (id_art, nombre, descripcion, stock, precio, oferta, seccion)
VALUES
(10, 'Sierra Circular', 'Sierra eléctrica para cortes precisos', 5, 79.50, 15, 'jardineria'), 
(11, 'Lijadora Orbital', 'Lijadora para acabados suaves', 12, 35.75, 5, 'pintura'), 
(12, 'Juego de Destornilladores', 'Juego con 6 destornilladores de diferentes tamaños', 25, 19.99, 0, 'fontaneria'), 
(13, 'Martillo de Carpintero', 'Martillo con cabeza de acero y mango ergonómico', 18, 12.50, 0, 'jardineria'), 
(14, 'Cinta Métrica', 'Cinta métrica de 5 metros', 40, 6.99, 0, 'fontaneria'),
(15, 'Nivel de Burbuja', 'Nivel de burbuja de 60 cm', 15, 9.25, 0, 'fontaneria'), 
(16, 'Llave Inglesa', 'Llave ajustable de 200 mm', 8, 24.00, 10, 'fontaneria'),
(17, 'Alicates Universales', 'Alicates para cortar, sujetar y doblar', 20, 14.75, 0, 'fontaneria'),
(18, 'Brocas para Madera (Juego)', 'Juego de 10 brocas de diferentes diámetros', 10, 29.99, 20, 'jardineria'), 
(19, 'Brocas para Metal (Juego)', 'Juego de 10 brocas de acero de alta velocidad', 15, 34.50, 10, 'soldadura'), 
(20, 'Pintura Blanca (1 Litro)', 'Pintura plástica blanca para interiores/exteriores', 30, 15.99, 0, 'pintura'),
(21, 'Rodillo de Pintura', 'Rodillo de pintura con mango ergonómico', 22, 8.50, 0, 'pintura'),
(22, 'Pinceles (Juego)', 'Juego de 5 pinceles de diferentes tamaños', 18, 11.25, 0, 'pintura'),
(23, 'Cutter', 'Cúter con cuchillas reemplazables', 35, 4.50, 0, 'fontaneria'), 
(24, 'Guantes de Trabajo', 'Par de guantes de seguridad para trabajos manuales', 50, 9.75, 5, 'jardineria'), 
(25, 'Gafas de Seguridad', 'Gafas protectoras transparentes', 45, 6.25, 0, 'soldadura'), 
(26, 'Mascarilla Antipolvo', 'Mascarilla para proteger de partículas y polvo', 60, 3.50, 0, 'pintura'), 
(27, 'Flexómetro Láser', 'Medidor láser de distancias', 7, 49.99, 15, 'fontaneria'), 
(28, 'Atornillador Eléctrico', 'Atornillador eléctrico compacto', 10, 39.00, 10, 'fontaneria'),
(29, 'Sargento (Pequeño)', 'Sargento de apriete de 150 mm', 28, 7.50, 0, 'jardineria'), 
(30, 'Sargento (Mediano)', 'Sargento de apriete de 300 mm', 20, 11.00, 0, 'jardineria'),
(31, 'Lima (Juego)', 'Juego de 3 limas de diferentes formas', 14, 16.75, 5, 'soldadura'), 
(32, 'Mordaza de Banco', 'Mordaza de banco con base giratoria', 3, 65.00, 20, 'soldadura'), 
(33, 'Pistola de Pintura (Eléctrica)', 'Pistola de pintura eléctrica de baja presión', 6, 89.99, 10, 'pintura'),
(34, 'Cinta de Carrocero', 'Cinta adhesiva para delimitar áreas al pintar', 32, 5.25, 0, 'pintura'),
(35, 'Espátula', 'Espátula de acero inoxidable para emplastecer', 25, 3.99, 0, 'pintura'),
(36, 'Taladro de Percusión', 'Taladro con función de percusión para materiales duros', 8, 69.00, 15, 'fontaneria'), 
(37, 'Maza de Goma', 'Maza con cabeza de goma para golpear sin dañar', 16, 13.50, 0, 'fontaneria'), 
(38, 'Carretilla', 'Carretilla de obra de 60 litros', 2, 75.00, 5, 'jardineria'),
(39, 'Pala de Punta', 'Pala de punta para excavar', 10, 18.25, 0, 'jardineria'),
(40, 'Pintura Acrílica Azul', 'Pintura resistente al agua', 50, 15.99, 10, 'pintura'),
(41, 'Tijeras de Jardinería', 'Tijeras de acero inoxidable', 30, 8.50, 5, 'jardineria'),
(42, 'Llave Inglesa', 'Llave ajustable para tuberías', 20, 12.99, 15, 'fontaneria'),
(43, 'Electrodo de Soldadura', 'Electrodo de 2.5mm para soldadura', 100, 1.50, 0, 'soldadura'),
(45, 'Martillo de Carpintero', 'Martillo con mango de madera', 40, 9.99, 8, 'carpinteria'),
(46, 'Barniz Transparente', 'Barniz protector para madera', 25, 18.75, 12, 'pintura'),
(47, 'Manguera de Jardín', 'Manguera de 10 metros con boquilla', 15, 25.00, 20, 'jardineria');



INSERT INTO pedido (id_ped, id_clien, total, fecha_compra) 
VALUES 
(1, 1, 150.00, '2025-04-10 14:30:00'), 
(2, 1, 80.00, '2025-04-12 16:45:00'),   
(3, 1, 200.00, '2025-04-15 10:00:00'),  
(4, 6, 75.00, '2025-04-18 11:15:00'),  
(5, 6, 120.00, '2025-04-20 13:20:00'),  
(6, 1, 300.00, '2025-04-22 17:25:00'); 


-- Insertar compras para el pedido 1 (cliente 1)
INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(10, 1, 2),  
(11, 1, 1),  
(13, 1, 1); 


INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(15, 2, 2),  
(20, 2, 3),  
(23, 2, 2); 


INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(17, 3, 3), 
(42, 3, 1),  
(27, 3, 1);  


INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(24, 4, 2),
(29, 4, 1),  
(40, 4, 2);  


INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(18, 5, 1),  
(36, 5, 1),  
(45, 5, 2);  


INSERT INTO compra (id_art, id_ped, cantidad) 
VALUES 
(46, 6, 3),  
(47, 6, 1),  
(41, 6, 2);  




/*Crea un procedimiento llamado ReportePedidosCliente que, dado un id_cliente, muestre los pedidos realizados por el cliente y los 
artículos de cada pedido utilizando cursores que recorra todos los pedidos del cliente y los articulos que ha comprado*/

DELIMITER //

CREATE PROCEDURE ReportePedidosCliente(IN id_cliente INT)
BEGIN
    DECLARE id_pedido INT;
    DECLARE total_pedido FLOAT;
    DECLARE fecha_compra DATETIME;
    DECLARE fin INT DEFAULT 0;

    DECLARE cursor_pedidos CURSOR FOR
        SELECT p.id_ped, p.total, p.fecha_compra
        FROM pedido p
        WHERE p.id_clien = id_cliente;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET fin = 1;

    OPEN cursor_pedidos;

    FETCH cursor_pedidos INTO id_pedido, total_pedido, fecha_compra;

    WHILE fin = 0 DO
        SELECT CONCAT('Pedido ID: ', id_pedido, ', Fecha: ', fecha_compra, ', Total: ', total_pedido) AS PedidoInfo;

        SELECT a.nombre, a.descripcion, c.cantidad, a.precio, (c.cantidad * a.precio) AS Subtotal
        FROM compra c
        JOIN articulo a ON a.id_art = c.id_art
        WHERE c.id_ped = id_pedido;

        FETCH cursor_pedidos INTO id_pedido, total_pedido, fecha_compra;
    END WHILE;

    CLOSE cursor_pedidos;
END //

DELIMITER ;

/*CALL ReportePedidosCliente(1);*/





 /*Crear una función que calcule el total gastado por un usuario en todos sus pedidos. */
DELIMITER //
 CREATE FUNCTION TOTALGASTADO(ID_CLIENTE INT)
 RETURNS FLOAT
 READS SQL DATA
 BEGIN 
	
	DECLARE CUANTO FLOAT;
    SELECT SUM(TOTAL) INTO CUANTO FROM PEDIDO WHERE ID_CLIEN =ID_CLIENTE ;
    RETURN CUANTO;
END //
DELIMITER ;
/*SELECT TOTALGASTADO(1);*/


 /*Crear un procedimiento que aplique un descuento a todos los artículos de una sección específica. */

 DELIMITER //
 CREATE PROCEDURE SECCION_DESCUENTO (  IN SECCION_PARAM VARCHAR(50),
    IN DESCUENTO FLOAT)
 BEGIN
	UPDATE ARTICULO SET OFERTA = DESCUENTO WHERE SECCION = SECCION_PARAM;
END //
DELIMITER ;

/*CALL SECCION_DESCUENTO('pintura', 10);*/

	

/* Crear una función que devuelva el número total de artículos en stock de una categoría dada. */
DELIMITER //
CREATE FUNCTION CUANTOSTOCK(TIPO VARCHAR(50)) 
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE CUANTO INT;
    SELECT sum(stock) INTO CUANTO from articulo where seccion=tipo;
    RETURN CUANTO;
END //
DELIMITER ;

/*SELECT CUANTOSTOCK('pintura');*/


/* Crear un procedimiento que elimine un pedido y todas sus compras asociadas, asegurando la integridad de la base de datos. */
DELIMITER //
CREATE PROCEDURE eliminarPedido(idPed int)
BEGIN
	delete from compra where id_ped=idPed;
	delete from pedido where id_ped=idPed;
END //
DELIMITER ;

/*CALL eliminarPedido(2);*/





/*Crear un procedimiento que reciba el ID de un usuario y devuelva en un parámetro el total de pedidos que ha realizado.  */
DELIMITER //
CREATE FUNCTION TOTALPEDIDOS(USUID INT) RETURNS int
READS SQL DATA
BEGIN
DECLARE CUANTO INT;
SELECT COUNT(*) INTO CUANTO FROM PEDIDO WHERE ID_CLIEN=USUID;
RETURN CUANTO;
END //
DELIMITER ;


/*SELECT TOTALPEDIDOS(1);*/

	
/*Crear un procedimiento llamado actualizarDireccion que reciba como parámetros el id_cliente y una nueva dirección . 
El procedimiento debe actualizar la dirección del cliente en la tabla cliente, asegurándose de que se modifique la dirección 
correspondiente al id_cliente proporcionado. */
DELIMITER //

CREATE PROCEDURE ACTUALIZARDIRECCION(cliente INT, direc VARCHAR(200))
BEGIN
    IF direc IS NOT NULL THEN
        UPDATE cliente SET direccion = direc WHERE id_clien = cliente;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Dirección no puede ser NULL';
    END IF;
END //

DELIMITER ;
/*
CALL ACTUALIZARDIRECCION(1, 'Calle Nueva 123');*/

/*Recorrer todos los articulos (con while) para contar cuantos articulos de una seccion tienen un precio mayor que a un valor dado*/

DELIMITER //

CREATE PROCEDURE CONTAR_ARTICULOS_CAROS(
    IN p_seccion VARCHAR(50),
    IN p_precio_minimo FLOAT,
    OUT p_total INT
)
BEGIN
    DECLARE v_id_actual INT;
    DECLARE v_precio_actual FLOAT;
    
    SET p_total = 0;

    SELECT MIN(id_art) INTO v_id_actual
    FROM articulo
    WHERE seccion = p_seccion;
    
    WHILE v_id_actual IS NOT NULL DO
        SELECT precio INTO v_precio_actual
        FROM articulo
        WHERE id_art = v_id_actual;

        IF v_precio_actual > p_precio_minimo THEN
            SET p_total = p_total + 1;
        END IF;
        
        SELECT MIN(id_art) INTO v_id_actual
        FROM articulo
        WHERE seccion = p_seccion
        AND id_art > v_id_actual;
    END WHILE;
END //

DELIMITER ;


/*
SET @resultado = 0;
CALL CONTAR_ARTICULOS_CAROS('pintura', 20, @resultado);
SELECT @resultado AS total_articulos_caros;
*/










