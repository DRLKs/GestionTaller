-- Crear tablas

CREATE TABLE IF NOT EXISTS tRol (
    rolName VARCHAR(50) PRIMARY KEY,
    rolDes VARCHAR(255),
    admin BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS tTipoPieza (
    ID_TIPO VARCHAR(4) PRIMARY KEY,
    NOMBRE VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS tPiezas (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    NOMBRE VARCHAR(255) NOT NULL,
    FABRICANTE VARCHAR(255) NOT NULL,
    ID_TIPO VARCHAR(4) NOT NULL,
    FOREIGN KEY(ID_TIPO) REFERENCES tTipoPieza(ID_TIPO) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tUsuario (
    nombre VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    rolName VARCHAR(50) NOT NULL,
    FOREIGN KEY(rolName) REFERENCES tRol(rolName) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tPermiso (
    rolName VARCHAR(50) NOT NULL,
    pantalla VARCHAR(50) NOT NULL,
    acceso BIT NOT NULL,
    modificacion BIT NOT NULL,
    PRIMARY KEY(rolName, pantalla),
    FOREIGN KEY(rolName) REFERENCES tRol(rolName)
);

-- Insertar datos

INSERT INTO tRol (rolName, rolDes, admin) VALUES
    ('administrador', 'administrador', 1),
    ('usuario', 'usuario', 0),
    ('invitado', 'invitado', 0);

INSERT INTO tTipoPieza (ID_TIPO, NOMBRE) VALUES
    ('A', 'Chapa'),
    ('B', 'Motor'),
    ('C', 'Iluminación'),
    ('D', 'Sensores'),
    ('E', 'Cristales'),
    ('F', 'Pintura'),
    ('G', 'Otros');

INSERT INTO tPiezas (NOMBRE, FABRICANTE, ID_TIPO) VALUES
    ('PARAGOLPES DELANTERO NEGRO-LISO A IMPRIMAR', 'MAZDA', 'A'),
    ('PARAGOLPES TRASERO-IMPRIMADO', 'MAZDA', 'A'),
    ('REJILLA NEGRA', 'MAZDA', 'A'),
    ('ALETA DELANTERA DCH CON AUJERO PARA PILOTO CX3 16', 'MAZDA', 'A'),
    ('ALETA DELANTERA IZQ CON AUJERO PARA PILOTO CX3 16', 'MAZDA', 'A'),
    ('Bombillas luz delantera', 'RENAULT', 'C'),
    ('Bombillas señalización delantera', 'RENAULT', 'C'),
    ('Bombillas luz trasera', 'RENAULT', 'C'),
    ('Bombillas señalización trasera', 'RENAULT', 'C'),
    ('Estuches de bombillas', 'RENAULT', 'C'),
    ('Iluminación LED', 'RENAULT', 'C'),
    ('Bombillas interior', 'RENAULT', 'C'),
    ('Bombillas Xenon', 'RENAULT', 'C'),
    ('Juntas y otras piezas del motor', 'FORD', 'B'),
    ('Alimentación', 'FORD', 'B'),
    ('Kits de distribución', 'FORD', 'B'),
    ('Correas', 'FORD', 'B'),
    ('Poleas', 'FORD', 'B'),
    ('Kits', 'FORD', 'B'),
    ('Válvulas EGR', 'FORD', 'B'),
    ('Herramienta específica', 'FORD', 'B'),
    ('Turbocompresores', 'FORD', 'B'),
    ('Sensores electrónicos y medidores de flujo', 'FORD', 'B'),
    ('Cable de acelerador y starter', 'FORD', 'B');

INSERT INTO tUsuario (nombre, password, rolName) VALUES
    ('admin', 'admin', 'administrador'),
    ('user', 'user', 'usuario'),
    ('inv', 'inv', 'invitado');

INSERT INTO tPermiso (rolName, pantalla, acceso, modificacion) VALUES
    ('administrador', 'LOGIN', 1, 1),
    ('administrador', 'TIPOPIEZA', 1, 1),
    ('administrador', 'PIEZAS', 1, 1),
    ('usuario', 'LOGIN', 1, 1),
    ('usuario', 'TIPOPIEZA', 1, 0),
    ('usuario', 'PIEZAS', 1, 0),
    ('invitado', 'LOGIN', 1, 1),
    ('invitado', 'TIPOPIEZA', 0, 0),
    ('invitado', 'PIEZAS', 0, 0);
