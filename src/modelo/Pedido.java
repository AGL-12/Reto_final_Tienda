package modelo;

import java.time.LocalDateTime;

public class Pedido {
	private int id_ped;
	private int id_usu;
	private float total;
	private LocalDateTime fecha_compra;

	public Pedido(int id_ped, int id_usu, float total, LocalDateTime fecha_compra) {
		super();
		this.id_ped = id_ped;
		this.id_usu = id_usu;
		this.total = total;
		this.fecha_compra = fecha_compra;
	}

	public Pedido() {
	}

	public int getId_ped() {
		return id_ped;
	}

	public void setId_ped(int id_ped) {
		this.id_ped = id_ped;
	}

	public int getId_usu() {
		return id_usu;
	}

	public void setId_usu(int id_usu) {
		this.id_usu = id_usu;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public LocalDateTime getFecha_compra() {
		return fecha_compra;
	}

	public void setFecha_compra(LocalDateTime fecha_compra) {
		this.fecha_compra = fecha_compra;
	}

	@Override
	public String toString() {
		return "Pedido [id_ped=" + id_ped + ", id_usu=" + id_usu + ", total=" + total + ", fecha_compra=" + fecha_compra
				+ "]";
	}

}
