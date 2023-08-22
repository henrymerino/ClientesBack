# ClientesBack
registro de clientes comoHogar

# DESPLIEGUE

- Clonar el proyecto 
- Configurar el archivo application.properties
- base de datos HSQLDB
- Tabla

create table cliente 
(id bigint not null, beneficio varchar(255), 
cod_mensaje varchar(255), email varchar(255), 
grupo varchar(255), 
mensaje varchar(255), name varchar(255), 
phone varchar(255), store varchar(255), primary key (id)) 

# URL Añadir cliente
  http://localhost:8080/cliente/add
# TRAMA

{
	"name":"Lore",
	"email":"M@gmail.com",
	"phone":"0999999997",
	"store":"Quicentro",
	"grupo":"SK"
} 

-URL Listar clientes all

# Funcionamiento
- Metodo add: añade un cliente, valida como requeridos name, email, phone, store, grupo (TH, SK), valida por nombre que sea unico,
  dependiendo si el usuario elije SK o TH, lee el archivo  sk_formato.json o th_formato.xml, que contienen los beneficios y asigna   	beneficio disponible, si los beneficios se terminar presenta mensaje
- Metodo all: lista todos los clientes




