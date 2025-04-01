Para usar este programa necesitas poner lo siguiente en la base de datos;

create database tienda_brico;<br>
use tienda_brico;<br>
create table cliente(<br>
id_clien int primary key,<br>
usuario varchar(50),<br>
contra varchar(50),<br>
dni char(9) not null,<br>
correo varchar(320) not null,<br>
direccion varchar(200),<br>
metodo_pago enum("visa","mastercard","paypal"),<br>
num_cuenta char(16),<br>
esAdmin boolean default false<br>
);<br>
create table pedido(
id_ped int primary key,
id_clien int,
total float default 0,
fecha_compra datetime,
foreign key (id_clien) references cliente (id_clien)
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
foreign key (id_art) references articulo (id_art),
foreign key (id_ped) references pedido (id_ped)
);
INSERT INTO cliente VALUES 
(1,'admin', 'admin', '12345678k', 'adm@gmail.com', 'mi casa', 'visa', '1234567887654321', '1'),
(2,'usuario1', 'usuario1', '12345678A', 'usuario1@example.com', 'Calle Falsa 123', 'visa', '1111222233334444', false),
(3,'usuario2', 'usuario2', '23456789B', 'usuario2@example.com', 'Avenida Siempre Viva 742', 'mastercard', '5555666677778888', false),
(4,'usuario3', 'usuario3', '34567890C', 'usuario3@example.com', 'Plaza Mayor 10', 'paypal', '9999000011112222', false),
(5,'usuario4', 'usuario4', '45678901D', 'usuario4@example.com', 'Calle Luna 45', 'visa', '3333444455556666', false),
(6,'alex', 'alex', '12345678k', 'alex@example.com', 'Calle Luna 45', 'visa', '3333222455556666', false);

INSERT INTO articulo (id_art, nombre, descripcion, stock, precio, oferta, seccion) 
VALUES 
(1, 'Pintura Acrílica Azul', 'Pintura resistente al agua', 50, 15.99, 10, 'pintura'),
(2, 'Tijeras de Jardinería', 'Tijeras de acero inoxidable', 30, 8.50, 5, 'jardineria'),
(3, 'Llave Inglesa', 'Llave ajustable para tuberías', 20, 12.99, 15, 'fontaneria'),
(4, 'Electrodo de Soldadura', 'Electrodo de 2.5mm para soldadura', 100, 1.50, 0, 'soldadura'),
(5, 'Martillo de Carpintero', 'Martillo con mango de madera', 40, 9.99, 8, 'carpinteria'),
(6, 'Barniz Transparente', 'Barniz protector para madera', 25, 18.75, 12, 'pintura'),
(7, 'Manguera de Jardín', 'Manguera de 10 metros con boquilla', 15, 25.00, 20, 'jardineria');



