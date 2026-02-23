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


