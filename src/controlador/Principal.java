package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
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

	public static void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException {
		dao.guardarCompra(idPedido, idArticulo, cantidad);
	}

	public static int guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException {
		return dao.guardarPedido(idUsuario, totalCompra, fechaCompra);
	}

	public static Map<Integer, Articulo> obtenerTodosArticulos() {
		return dao.obtenerTodosArticulos();
	}

	public static void crearPedidoUsuario(Pedido id_usu) {
		dao.crearPedidoUsuario(id_usu);
	}

	public static Articulo buscarArticulo(int id_art) {
		return dao.buscarArticulo(id_art);
	}

	public static int obtenerNewIdCliente() {
		return dao.obtenerNewIdCliente();
	}

	public static List<Articulo> obtenerArticulosPedido(int i) {
		return dao.obtenerArticulosPedido(i);
	}

	public static List<Pedido> obtenerPedidosCliente(int id_usu) {
		return dao.obtenerPedidosCliente(id_usu);
	}

	public static int obtenerUltimoIdPed() {
		return 0;
	}
}
