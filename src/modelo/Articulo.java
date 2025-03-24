package modelo;

public class Articulo {
	private int id_art;
	private String nombre;
	private String descripcion;
	private int stock;
	private float precio;
	private float oferta;
	private Seccion seccion;
	
	public int getId_art() {
		return id_art;
	}

	public void setId_art(int id_art) {
		this.id_art = id_art;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public float getOferta() {
		return oferta;
	}

	public void setOferta(float oferta) {
		this.oferta = oferta;
	}

	public Seccion getSeccion() {
		return seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	@Override
	public String toString() {
		return "Articulo [id_art=" + id_art + ", nombre=" + nombre + ", descripcion=" + descripcion + ", stock=" + stock
				+ ", precio=" + precio + ", oferta=" + oferta + ", seccion=" + seccion + "]";
	}

}
