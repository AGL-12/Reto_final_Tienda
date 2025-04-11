package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

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

	/*
	 * public static void main(String[] args) { VistaLogIn inicio = new
	 * VistaLogIn(); inicio.setVisible(true); }
	 */

	public static void main(String[] args) {

		// --- Configuración del Look and Feel (FlatLaf) ---
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			// UIManager.setLookAndFeel( new com.formdev.flatlaf.FlatDarkLaf() );
			// UIManager.setLookAndFeel( new com.formdev.flatlaf.FlatIntelliJLaf() );
			// UIManager.setLookAndFeel( new com.formdev.flatlaf.FlatDarculaLaf() );

			// Opcional: Puedes poner propiedades globales de UIManager aquí
			// para afectar a toda la aplicación, DESPUÉS de setLookAndFeel
			// Ejemplos:
			// UIManager.put( "Button.arc", 10 ); // Botones redondeados
			// UIManager.put( "Component.arc", 8 ); // Componentes (campos de texto, etc.)
			// redondeados
			// UIManager.put( "ProgressBar.arc", 8 );
			// UIManager.put( "TextComponent.arc", 8 );
			// UIManager.put( "Table.showHorizontalLines", true );
			// UIManager.put( "Table.showVerticalLines", false );
			// UIManager.put( "Table.intercellSpacing", new java.awt.Dimension(0,1) );

		} catch (UnsupportedLookAndFeelException e) {
			// Manejo del error si FlatLaf no se puede aplicar
			System.err.println("Error al inicializar FlatLaf Look and Feel: " + e.getMessage());
			e.printStackTrace();
			// Podrías intentar usar el L&F del sistema como fallback si falla
			// try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
			// catch (Exception ex) {}
		} catch (Exception e) {
			// Captura otras posibles excepciones (raro)
			System.err.println("Error inesperado al configurar Look and Feel: " + e.getMessage());
			e.printStackTrace();
		}
		// --- Fin de la configuración del Look and Feel ---

		// Es una buena práctica lanzar la interfaz gráfica en el Event Dispatch Thread
		// (EDT)
		SwingUtilities.invokeLater(() -> {
			VistaLogIn inicio = new VistaLogIn();
			inicio.setVisible(true);
		});
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

	public static void insertPedido(Pedido localPedido) {
		dao.insertPedido(localPedido);
	}

	public static void insertListCompra(List<Compra> localListaCompra) {
		dao.insertListCompra(localListaCompra);
	}

	public static int obtenerUltimoIdPed() {
		return dao.obtenerUltimoIdPed();
	}

	public static void guardarPedido(Pedido ped) throws SQLException {
		dao.guardarPedido(ped);
	}

	public static void guardarPedido(int idUsuario, float totalCompra, LocalDateTime now, Pedido ped)
			throws SQLException {
		dao.guardarPedido(ped);
	}

	public static void actualizarStock(int id_art, int cantidad) {
//		dao.actualizarStock(id_art, cantidad);
	}

	public static void guardarCompra(List<Compra> localListaCompra) throws SQLException {
		dao.guardarCompra(localListaCompra);
	}

	public static List<Articulo> obtenerArticulosPorPedido(int idPedido) {
		return dao.obtenerArticulosPedido(idPedido);
	}

	public static int obtenerCantidadArticuloEnPedido(int idPedido, int id_art) {
		return dao.obtenerCantidadArticuloEnPedido(idPedido, id_art);
	}

	public static float totalGastado(Cliente clien) {
		return dao.totalGastado(clien);

	}
}
