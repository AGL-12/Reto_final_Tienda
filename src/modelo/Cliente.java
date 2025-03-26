package modelo;

import java.util.HashMap;
import java.util.Map;
import modelo.Metodo;


public class Cliente {
	private int id_usu;
	private String usuario;
	private String contra;
	private String dni;
	private String correo;
	private String direccion;
	private Metodo metodo_pago;
	private String num_cuenta;
	private boolean esAdmin;
	private Map<Integer, Pedido> listaPedido;
		
	
	public Cliente(int id_usu, String usuario, String contra, String dni, String correo, String direccion,
			Metodo metodo_pago, String num_cuenta, boolean esAdmin, Map<Integer, Pedido> listaPedido) {
		super();
		this.id_usu = id_usu;
		this.usuario = usuario;
		this.contra = contra;
		this.dni = dni;
		this.correo = correo;
		this.direccion = direccion;
		this.metodo_pago = metodo_pago;
		this.num_cuenta = num_cuenta;
		this.esAdmin = esAdmin;
		this.listaPedido = listaPedido;
	}

	public Cliente() {
		this.listaPedido = new HashMap<>();
	}

	public int getId_usu() {
		return id_usu;
	}

	public void setId_usu(int id_usu) {
		this.id_usu = id_usu;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContra() {
		return contra;
	}

	public void setContra(String contra) {
		this.contra = contra;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Metodo getMetodo_pago() {
		return metodo_pago;
	}

	public void setMetodo_pago(Metodo metodo_pago) {
		this.metodo_pago = metodo_pago;
	}

	public String getNum_cuenta() {
		return num_cuenta;
	}

	public void setNum_cuenta(String num_cuenta) {
		this.num_cuenta = num_cuenta;
	}

	public boolean isEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public Map<Integer, Pedido> getListaPedido() {
		return listaPedido;
	}

	public void setListaPedido(Map<Integer, Pedido> listaPedido) {
		this.listaPedido = listaPedido;
	}

	@Override
	public String toString() {
		return "Cliente [id_usu=" + id_usu + ", usuario=" + usuario + ", contra=" + contra + ", dni=" + dni
				+ ", correo=" + correo + ", direccion=" + direccion + ", metodo_pago=" + metodo_pago + ", num_cuenta="
				+ num_cuenta + ", esAdmin=" + esAdmin + "]";
	}



}
