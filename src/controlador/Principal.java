package controlador;

import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;
import vista.VistaLogIn;

public class Principal {
	private static Dao dao = new DaoImplementMySQL();

	public static void main(String[] args) {
		VistaLogIn inicio = new VistaLogIn();
		inicio.setVisible(true);

	}

	public static Cliente login(Cliente cli) throws LoginError {
		return dao.login(cli);

	}

	public static void modificarArticulo(Articulo art) throws modifyError {
		dao.modificarArticulo(art);
	}

	public static void eliminarArticulo(Articulo art) throws modifyError {
		dao.eliminarArticulo(art);
	}

	public static void añadirArticulo(Articulo art) throws modifyError {
		dao.añadirArticulo(art);
	}

	public static Map<Integer, Cliente> listarCliente() {
		return dao.listarClientesTod();
	}

	public static void altaCliente(Cliente clien) throws AltaError {
		dao.altaCliente(clien);
	}

	public static void modificarCliente(Cliente clien) throws modifyError {
		dao.modificarCliente(clien);
	}

	public static void bajaCliente(Cliente clien) throws DropError {
		dao.bajaCliente(clien);
	}

	public static Map<Integer, Articulo> obtenerTodosArticulos() {
		return dao.obtenerTodosArticulos();
	}

	public static Pedido crearPedidoUsuario(int id_usu) {
		return dao.crearPedidoUsuario(id_usu);
	}

	public static int obtenerUltimoIdPed() {
		return dao.obtenerUltimoIdPed();
	}
}
