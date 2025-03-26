package controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import excepciones.InsertError;
import excepciones.LoginError;
import modelo.Cliente;
import modelo.Metodo;
import modelo.Pedido;

public class DaoImplementMySQL implements Dao {
	// Atributo para conexion
	private ResourceBundle configFile;
	private String urlBD, userBD, passwordBD;
	// Atributos
	private Connection con;
	private PreparedStatement stmt;
	// Sentencias SQL
	final String LOGIN = "select * from cliente where usuario=? and contra=?";
	private static final String SELECT_Cliente = "select * from cliente";
	// Consulta SQL para obtener todos los clientes
	final String SELECT_Clientes = "SELECT * FROM propietario";
	final String CONTAR_PEDS = "SELECT * FROM propietario";

	public DaoImplementMySQL() {
		this.configFile = ResourceBundle.getBundle("modelo.configClase");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	private void openConnection() {
		try {
			con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);

			/*
			 * DriverManager.getConnection(
			 * "jdbc:mysql://localhost:3306/tienda_brico?serverTimezone=Europe/Madrid&useSSL=false",
			 * "root", "abcd*1234");
			 */
		} catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD");
		}
	}

	private void closeConnection() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (con != null)
			con.close();
	}

	@Override
	public Cliente login(Cliente cli) throws LoginError {
		// Tenemos que definie el ResusultSet para recoger el resultado de la consulta
		ResultSet rs = null;
		openConnection();
		try {
			stmt = con.prepareStatement(LOGIN);

			stmt.setString(1, cli.getUsuario());
			stmt.setString(2, cli.getContra());

			rs = stmt.executeQuery();
			// Leemos de uno en uno los propietarios devueltos en el ResultSet
			if (!rs.next()) {
				throw new LoginError("Usuario o password incorrecto");
			}
			cli.setId_usu(rs.getInt("id_clien"));
			cli.setUsuario(rs.getString("usuario"));
			cli.setContra(rs.getString("contra"));
			cli.setDni(rs.getString("dni"));
			cli.setCorreo(rs.getString("correo"));
			cli.setDireccion(rs.getString("direccion"));
			cli.setMetodo_pago(Metodo.valueOf(rs.getString("metodo_pago")));
			cli.setNum_cuenta(rs.getString("num_cuenta"));
			cli.setEsAdmin(rs.getBoolean("esAdmin"));
		} catch (SQLException e) {
			throw new LoginError("Error con el SQL");
		} finally {
			// Cerrar el ResultSet
			try {
				rs.close();
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cli;
	}

	@Override
	public Map<String, Cliente> listarPropietarios() {
		Map<String, Cliente> todosClientes = new HashMap<>();
		ResultSet rs = null;

		openConnection();

		try {
			// Preparar la sentencia SQL
			stmt = con.prepareStatement(SELECT_Cliente);

			// Ejecutar la consulta
			rs = stmt.executeQuery();

			// Iterar sobre el ResultSet y agregar los propietarios al Map
			while (rs.next()) {
				int id = rs.getInt("id");
				String usuario = rs.getString("nombre");
				String contra = rs.getString("nombre");
				String dni = rs.getString("nombre");
				String correo = rs.getString("nombre");
				String direccion = rs.getString("nombre");
				Metodo metodoPago = Metodo.valueOf(rs.getString("metodo_pago"));
				String num_cuenta = rs.getString("num_cuenta");
				boolean esAdmin = rs.getBoolean("esAdmin");
				Map<Integer, Pedido> listaPedido = cargarMapaPed();

				// Crear el objeto Propietario con los datos obtenidos
				Cliente prop = new Cliente(id, usuario, contra, dni, correo, direccion, metodoPago, num_cuenta, esAdmin, null);

				// Agregar al Map con el ID como clave
				todosClientes.put(String.valueOf(id), prop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Retornar el Map con todos los propietarios
		return todosClientes;
	}
	/*
	 * @Override public void altaPropietario(Propietario prop) throws InsertError {
	 * openConnection(); try { stmt = con.prepareStatement(ALTAPROP);
	 * stmt.setString(1, prop.getNombre()); stmt.setDate(2,
	 * Date.valueOf(prop.getFechaNace())); stmt.executeUpdate();
	 * 
	 * } catch (SQLException e) { throw new
	 * InsertError("El nombre excede el limite (30)"); } finally { // Cerrar el
	 * ResultSet try { closeConnection(); } catch (SQLException e) {
	 * e.printStackTrace(); } } }
	 * 
	 * public Map<String, Propietario> listarPropietarios() {
	 * 
	 * // Tenemos que definie el ResusultSet para recoger el resultado de la
	 * consulta Map<String, Propietario> prop = new HashMap<>(); ResultSet rs =
	 * null; openConnection();
	 * 
	 * final String selec_pro = "select * from propietario"; try { stmt =
	 * con.prepareStatement(selec_pro);
	 * 
	 * rs = stmt.executeQuery(); while (rs.next()) { int id = rs.getInt("ide");
	 * String nom = rs.getString("nombre"); Date nace = rs.getDate("fechaNace");
	 * 
	 * Propietario propie = new Propietario(id, nom, nace.toLocalDate());
	 * 
	 * prop.put(String.valueOf(id), propie); } } catch (SQLException e) {
	 * e.printStackTrace(); } finally { // Cerrar el ResultSet try { rs.close();
	 * closeConnection(); } catch (SQLException e) { e.printStackTrace(); } } return
	 * prop; }
	 */

	private Map<Integer, Pedido> cargarMapaPed() {
		Map<Integer, Pedido> paraCargar = new HashMap<>();
		
		return null;
	}
}
