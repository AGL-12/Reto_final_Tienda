package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
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

	public static void guardarPedido(Pedido ped) throws SQLException {
		dao.guardarPedido(ped);
	}

	public static void guardarPedido(int idUsuario, float totalCompra, LocalDateTime now, Pedido ped) {
		// TODO Auto-generated method stub
		try {
			dao.guardarPedido(ped);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void actualizarStock(int id_art, int cantidad) {
		// TODO Auto-generated method stub
		try {
			dao.actualizarStock(id_art, cantidad);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void guardarCompra(List<Compra> localListaCompra) {
		// TODO Auto-generated method stub
		try {
			dao.guardarCompra(localListaCompra);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Articulo> obtenerArticulosPorPedido(int idPedido) {
		// TODO Auto-generated method stub
		return dao.obtenerArticulosPedido(idPedido);
	}

	public static int obtenerCantidadArticuloEnPedido(int idPedido, int id_art) {
		// TODO Auto-generated method stub
		return dao.obtenerCantidadArticuloEnPedido(idPedido, id_art);
	}
}
