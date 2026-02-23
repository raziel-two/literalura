package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;

	private Integer fechaNacimiento;
	private Integer fechaFallecimiento;

	@OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Libro> libros = new ArrayList<>();

	// Constructor por defecto requerido por JPA
	public Autor() {}

	// Constructor con datos
	public Autor(String nombre, Integer fechaNacimiento, Integer fechaFallecimiento) {
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaFallecimiento = fechaFallecimiento;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Integer fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Integer getFechaFallecimiento() {
		return fechaFallecimiento;
	}

	public void setFechaFallecimiento(Integer fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
	}

	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

	// Método para formatear fechas (años negativos = Desconocido)
	private String formatearFecha(Integer fecha) {
		if (fecha == null || fecha < 0) {
			return "Desconocido";
		}
		return fecha.toString();
	}

	@Override
	public String toString() {
		// Obtener lista de títulos de libros
		String titulosLibros = libros.stream()
				.map(Libro::getTitulo)
				.collect(Collectors.joining(", "));

		return String.format("""
                👤 Autor: %s
                🎂 Nacimiento: %s
                ⚰️ Fallecimiento: %s
                📚 Libros: %s""",
				nombre,
				formatearFecha(fechaNacimiento),
				formatearFecha(fechaFallecimiento),
				titulosLibros.isEmpty() ? "Ninguno" : titulosLibros);
	}
}