package com.alura.literalura.model;

import jakarta.persistence.*;

//creaacion de entidad
@Entity
@Table(name = "libros")
public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String titulo;

	private String idioma;
	private Double numeroDescargas;

	@ManyToOne
	@JoinColumn(name = "autor_id")
	private Autor autor;

	//constructor por defecto requerido por JPA)
	public Libro() {}

	// constructor con datos
	public Libro(String titulo, String idioma, Double numeroDescargas, Autor autor) {
		this.titulo = titulo;
		this.idioma = idioma;
		this.numeroDescargas = numeroDescargas;
		this.autor = autor;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public Double getNumeroDescargas() {
		return numeroDescargas;
	}
	public void setNumeroDescargas(Double numeroDescargas) {
		this.numeroDescargas = numeroDescargas;
	}

	public Autor getAutor() { return autor; }
	public void setAutor(Autor autor) { this.autor = autor; }

	@Override
	public String toString() {
		return String.format("""
            
            📚 Título: %s
            👤 Autor: %s
            🌐 Idioma: %s
            ⬇️ Descargas: %.0f
            %s""",
				titulo,
				autor != null ? autor.getNombre() : "Desconocido",
				idioma,
				numeroDescargas,
				"-".repeat(50));
	}
}