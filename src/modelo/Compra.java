package modelo;

public class Compra {
	private int id_art;
	private int id_ped;
	private int cantidad;
;
	public int getId_art() {
		return id_art;
	}

	public void setId_art(int id_art) {
		this.id_art = id_art;
	}

	public int getId_ped() {
		return id_ped;
	}

	public void setId_ped(int id_ped) {
		this.id_ped = id_ped;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Compra [id_art=" + id_art + ", id_ped=" + id_ped + ", cantidad=" + cantidad + "]";
	}

}
