package com.alura.literalura.principal;

import com.alura.literalura.dto.DatosLibro;
import com.alura.literalura.dto.DatosRespuesta;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

	private final ConsumoAPI consumoAPI = new ConsumoAPI();
	private final ConvierteDatos conversor = new ConvierteDatos();
	private final Scanner teclado = new Scanner(System.in);
	private final AutorRepository autorRepository;
	private final LibroRepository libroRepository;

	public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
		this.autorRepository = autorRepository;
		this.libroRepository = libroRepository;
	}

	public void muestraElMenu() {
		var opcion = -1;
		while (opcion != 0) {
			var menu = """
                    \n*** LiterAlura - Catálogo de Libros ***
                    
                    Elija la opción a través de su número:
                    1- Buscar libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Top 10 libros más descargados
                    
                    0- Salir
                    """;
			System.out.println(menu);

			while (!teclado.hasNextInt()) {
				System.out.println("Por favor, ingrese un número válido");
				teclado.next();
			}
			opcion = teclado.nextInt();
			teclado.nextLine();

			switch (opcion) {
				case 1:
					buscarLibroPorTitulo();
					break;
				case 2:
					listarLibrosRegistrados();
					break;
				case 3:
					listarAutoresRegistrados();
					break;
				case 4:
					listarAutoresVivosEnAnio();
					break;
				case 5:
					listarLibrosPorIdioma();
					break;
				case 6:
					top10Libros();
					break;
				case 0:
					System.out.println("Gracias por usar LiterAlura, hasta pronto.");
					break;
				default:
					System.out.println("Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void buscarLibroPorTitulo() {
		System.out.println("Ingrese el título del libro que desea buscar:");
		String titulo = teclado.nextLine();

		if (titulo == null || titulo.trim().isEmpty()) {
			System.out.println("El título no puede estar vacío.");
			return;
		}

		try {
			// Primero buscar en la BD
			Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(titulo.trim());
			if (libroExistente.isPresent()) {
				System.out.println("\nLIBRO ENCONTRADO EN LA BASE DE DATOS:");
				System.out.println(libroExistente.get());
				return;
			}

			// si no existe en la BD, buscar en la API
			String tituloCodificado = URLEncoder.encode(titulo.trim(), StandardCharsets.UTF_8);
			String url = "https://gutendex.com/books/?search=" + tituloCodificado;

			System.out.println("Buscando Libro: \"" + titulo);
			String json = consumoAPI.obtenerDatos(url);
			DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

			if (datos.resultados().isEmpty()) {
				System.out.println("No se encontraron libros con el título \"" + titulo + "\".");
				return;
			}

			// tomamos el primer resultado
			DatosLibro datosLibro = datos.resultados().get(0);

			// verificar si el libro ya existe (por si acaso)
			if (libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo()).isPresent()) {
				System.out.println("\nLIBRO YA EXISTE EN LA BASE DE DATOS:");
				System.out.println(libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo()).get());
				return;
			}

			// buscar o crear autor
			Autor autor;
			if (!datosLibro.autor().isEmpty()) {
				var datosAutor = datosLibro.autor().get(0);
				autor = autorRepository.findByNombre(datosAutor.nombre())
						.orElseGet(() -> {
							Autor nuevoAutor = new Autor(
									datosAutor.nombre(),
									datosAutor.fechaNacimiento(),
									datosAutor.fechaFallecimiento()
							);
							return autorRepository.save(nuevoAutor);
						});
			} else {
				autor = autorRepository.findByNombre("Desconocido")
						.orElseGet(() -> autorRepository.save(new Autor("Desconocido", null, null)));
			}

			// crear y guardar libro
			Libro libro = new Libro(
					datosLibro.titulo(),
					datosLibro.idiomas().isEmpty() ? "es" : datosLibro.idiomas().get(0),
					datosLibro.numeroDescargas(),
					autor
			);

			libroRepository.save(libro);

			System.out.println("\nLIBRO");
			System.out.println(libro);

		} catch (Exception e) {
			System.out.println("Error al buscar el libro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void listarLibrosRegistrados() {
		List<Libro> libros = libroRepository.findAll();
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados en la base de datos.");
			return;
		}
		System.out.println("\nLIBROS REGISTRADOS:");
		libros.forEach(System.out::println);
	}

	private void listarAutoresRegistrados() {
		List<Autor> autores = autorRepository.findAll();
		if (autores.isEmpty()) {
			System.out.println("No hay autores registrados en la base de datos.");
			return;
		}

		System.out.println("\nAUTORES REGISTRADOS:");
		for (Autor autor : autores) {
			System.out.println("👤 Autor: " + autor.getNombre());

			// Formatear fechas correctamente (años negativos = Desconocido)
			String nacimiento = formatearFecha(autor.getFechaNacimiento());
			String fallecimiento = formatearFecha(autor.getFechaFallecimiento());

			System.out.println("🎂 Nacimiento: " + nacimiento);
			System.out.println("⚰️ Fallecimiento: " + fallecimiento);

			// mostrar los títulos de los libros
			if (!autor.getLibros().isEmpty()) {
				System.out.println("📚 Libros:");
				for (Libro libro : autor.getLibros()) {
					System.out.println("   - " + libro.getTitulo());
				}
			} else {
				System.out.println("📚 Libros: Ninguno");
			}
			System.out.println("-".repeat(50));
		}
	}

	private void listarAutoresVivosEnAnio() {
		System.out.println("Ingrese el año para buscar autores vivos:");
		while (!teclado.hasNextInt()) {
			System.out.println("Por favor, ingrese un año válido:");
			teclado.next();
		}
		Integer anio = teclado.nextInt();
		teclado.nextLine();

		List<Autor> autoresVivos = autorRepository.autoresVivosEnAnio(anio);
		if (autoresVivos.isEmpty()) {
			System.out.println("No se encontraron autores vivos en el año " + anio);
			return;
		}

		System.out.println("\nAUTORES VIVOS EN " + anio + ":");
		for (Autor autor : autoresVivos) {
			System.out.println("👤 Autor: " + autor.getNombre());

			// Formatear fechas correctamente (años negativos = Desconocido)
			String nacimiento = formatearFecha(autor.getFechaNacimiento());
			String fallecimiento = formatearFecha(autor.getFechaFallecimiento());

			System.out.println("🎂 Nacimiento: " + nacimiento);
			System.out.println("⚰️ Fallecimiento: " + fallecimiento);

			// mostrar los títulos de los libros
			if (!autor.getLibros().isEmpty()) {
				System.out.println("📚 Libros:");
				for (Libro libro : autor.getLibros()) {
					System.out.println("   - " + libro.getTitulo());
				}
			} else {
				System.out.println("📚 Libros: Ninguno");
			}
			System.out.println("-".repeat(50));
		}
	}

	private void listarLibrosPorIdioma() {
		String menuIdiomas = """
                \n Idiomas disponibles:
                es - Español
                en - Inglés
                
                Ingrese el código del idioma (es/en):
                """;
		System.out.println(menuIdiomas);
		String idioma = teclado.nextLine().toLowerCase().trim();

		// Validar que el idioma sea válido
		while (!idioma.equals("es") && !idioma.equals("en")) {
			System.out.println("Idioma no válido. Por favor ingrese 'es' para Español o 'en' para Inglés:");
			idioma = teclado.nextLine().toLowerCase().trim();
		}

		List<Libro> libros = libroRepository.findByIdioma(idioma);
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados en el idioma: " + (idioma.equals("es") ? "Español" : "Inglés"));
			return;
		}

		System.out.println("\nLIBROS EN " + (idioma.equals("es") ? "ESPAÑOL" : "INGLÉS") + ":");
		libros.forEach(System.out::println);
	}

	private void top10Libros() {
		List<Libro> top10 = libroRepository.top10LibrosMasDescargados();
		if (top10.isEmpty()) {
			System.out.println("No hay libros registrados en la base de datos.");
			return;
		}

		System.out.println("\nTOP 10 LIBROS MÁS DESCARGADOS:");
		for (int i = 0; i < top10.size(); i++) {
			Libro libro = top10.get(i);
			System.out.printf("%d. %s - %.0f descargas%n",
					i + 1, libro.getTitulo(), libro.getNumeroDescargas());
		}
	}

	// Método auxiliar para formatear fechas (años negativos = Desconocido)
	private String formatearFecha(Integer fecha) {
		if (fecha == null || fecha < 0) {
			return "Desconocido";
		}
		return fecha.toString();
	}
}