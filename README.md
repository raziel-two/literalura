<h1>📚 LiterAlura - Catálogo de Libros</h1>

¡Bienvenido a **LiterAlura**! Este proyecto es una aplicación de consola desarrollada en **Java con Spring Boot** que permite consultar un catálogo de libros a través de la API de Gutendex, almacenar la información en una base de datos PostgreSQL y realizar diversas consultas sobre los libros y autores registrados.

Este desafío forma parte del programa **Oracle Next Education (ONE)** y pone en práctica conceptos fundamentales como consumo de APIs, persistencia de datos, consultas personalizadas y programación orientada a objetos.


## 🚀 Funcionalidades

- **Búsqueda de libros por título:** Consulta la API de Gutendex y guarda los resultados en la base de datos local.
- **Listado de libros registrados:** Muestra todos los libros almacenados en la base de datos.
- **Listado de autores registrados:** Muestra todos los autores con sus respectivos libros.
- **Consulta de autores vivos en un año específico:** Filtra autores que estaban vivos en un año determinado.
- **Filtrado de libros por idioma:** Permite listar libros en español (`es`) o inglés (`en`).
- **Top 10 libros más descargados:** Muestra los libros con mayor número de descargas.


## 🛡️ Control de Errores

- **Validación de entradas:** Manejo de entradas vacías, números inválidos y opciones fuera de rango.
- **Manejo de años negativos:** Para autores antiguos (ej. Homero), las fechas se muestran como "Desconocido".
- **Protección contra duplicados:** Evita guardar el mismo libro o autor más de una vez.
- **Manejo de excepciones en API:** Captura errores de conexión y formato JSON.

## 🕹️ ¿Cómo funciona?

1. **Configura la base de datos:**
    - Crea una base de datos en PostgreSQL llamada `literalura`.
    - Configura tus credenciales en el archivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   spring.jpa.hibernate.ddl-auto=update

2. **Ejecuta la aplicación:**
   - Inicia la aplicación desde tu IDE o terminal.
   - Elige la opción deseada (1-6) o la opción (0) para salir del programa.


3. **Resultado:** 
- El programa consultará la API o la BD en caso de consultar un libro que ya este guardado. Ejemplo de busqueda:

```
Ingrese el título del libro que desea buscar:
Don Quijote

LIBRO GUARDADO EN LA BASE DE DATOS:
📚 Título: Don Quijote
👤 Autor: Cervantes Saavedra, Miguel de
🌐 Idioma: es
⬇️ Descargas: 10926
```

## 🧠 Conceptos Aplicados
- **Spring Boot:** Inyección de dependencias, JPA, repositorios y configuración automatizada.
- **Consumo de API:** Uso de `HttpClient`, `HttpRequest` y `HttpResponse` para consumir **Gutendex**.
- **Manipulación de JSON:** Mapeo de respuestas JSON a objetos Java mediante la librería **Jackson**.
- **Persistencia con JPA:** Entidades, repositorios y consultas personalizadas.
- **Derived Queries:** Métodos de búsqueda personalizados en repositorios.
- **JPQL:** Consultas avanzadas con `@Query`
- **Modelado de Datos:** Separación clara entre DTOs (para la API) y Entidades (para la BD).
- **Manejo de excepciones:** Captura y tratamiento de errores de entrada y conexión.

## 🛠️ Tecnologías usadas
- **Java JDK 21**.
- **Spring Boot 3.5.11**
- **PostgreSQL** 
- **Jackson** (para procesamiento JSON)
- **Maven** 
- **Gutendex API** 

## 👩‍💻 Autor
Challenge de Alura Latam, Creado por **Raziel** 💙


## 📂 Estado del proyecto
✅ **Completado**