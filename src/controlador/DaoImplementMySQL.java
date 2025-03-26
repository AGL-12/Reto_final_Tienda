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
	final String LOGIN = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";
	final String INSERTAR_CLIENTE = "INSERT INTO cliente(usuario, contra, dni, correo, direccion, metodo_pago, num_cuenta) VALUES (?,?,?,?,?,?,?)";
	final String ELIMINAR_CLIENTE = "DELETE from cliente where id_clien=?";
	final String MODIFICAR_CLIENTE = "UPDATE cliente set usuario=?, contra=?, dni=?, correo=?, direccion=?, metodo_pago=?, num_cuenta=? WHERE id_clien=?;";
	static final String SELECT_Cliente = "select * from cliente";

	public DaoImplementMySQL() {
		this.configFile = ResourceBundle.getBundle("modelo.configClase");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	private void openConnection() {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/tienda_brico?serverTimezone=Europe/Madrid&useSSL=false", "root",
					"abcd*1234");
			/*
			 * DriverManager.getConnection(
			 * "jdbc:mysql://localhost:3306/tienda_brico?serverTimezone=Europe/Madrid&useSSL=false",
			 * "root", "abcd*1234");
			 */
		} catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD" + e.getMessage());
		}
	}

	private void closeConnection() throws SQLException {
		try {
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Cliente login(Cliente cli) throws LoginError {
		// Tenemos que definie el ResusultSet para recoger el resultado de la consulta
		ResultSet rs = null;
		openConnection();
		Cliente usuarioAutenticado = null;

		try {
			stmt = con.prepareStatement(LOGIN);

			stmt.setString(1, cli.getUsuario());
			stmt.setString(2, cli.getContra());

			rs = stmt.executeQuery();
			// Leemos de uno en uno los propietarios devueltos en el ResultSet
			if (!rs.next()) {
				throw new LoginError("Usuario o password incorrecto");
			} else {
				// Recuperamos los datos del usuario autenticado
				usuarioAutenticado = new Cliente();
				usuarioAutenticado.setId_usu(rs.getInt("id_clien")); // Asegúrate de que el nombre de la columna sea
																		// "id_usu"
				usuarioAutenticado.setUsuario(rs.getString("usuario")); // Asegúrate de que el nombre de la columna sea
																		// "id_usu"
				usuarioAutenticado.setContra(rs.getString("contra")); // Asegúrate de que el nombre de la columna sea
																		// "id_usu"
				usuarioAutenticado.setDni(rs.getString("dni"));
				usuarioAutenticado.setCorreo(rs.getString("correo"));
				usuarioAutenticado.setDireccion(rs.getString("direccion"));
				usuarioAutenticado.setMetodo_pago(Metodo.valueOf(rs.getString("metodo_pago"))); // Asegúrate de que
																								// coincida con la
																								// estructura de la base
																								// de datos
				usuarioAutenticado.setNum_cuenta(rs.getString("num_cuenta"));
				usuarioAutenticado.setEsAdmin(rs.getBoolean("esAdmin")); // Suponiendo que hay una columna "rol"
			}
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
		return usuarioAutenticado;
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
				Cliente prop = new Cliente(id, usuario, contra, dni, correo, direccion, metodoPago, num_cuenta, esAdmin,
						null);

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

		// Retornar el Map con todos los clientes
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

	@Override
	public void altaCliente(Cliente clien) {
		// TODO Auto-generated method stub
		openConnection();

		try {
			stmt = con.prepareStatement(INSERTAR_CLIENTE);
			stmt.setString(1, clien.getUsuario());
			stmt.setString(2, clien.getContra());
			stmt.setString(3, clien.getDni());
			stmt.setString(4, clien.getCorreo());
			stmt.setString(5, clien.getDireccion());
			stmt.setString(6, clien.getMetodo_pago().name());
			stmt.setString(7, clien.getNum_cuenta());
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	@Override
	public void modificarCliente(Cliente clien) {
		// TODO Auto-generated method stub
		openConnection();

		try {
			stmt = con.prepareStatement(MODIFICAR_CLIENTE);

			stmt.setString(1, clien.getUsuario());
			stmt.setString(2, clien.getContra());
			stmt.setString(3, clien.getDni());
			stmt.setString(4, clien.getCorreo());
			stmt.setString(5, clien.getDireccion());
			stmt.setString(6, clien.getMetodo_pago().name());
			stmt.setString(7, clien.getNum_cuenta());
			stmt.setInt(8, clien.getId_usu()); // Falta agregar el ID para el WHERE

			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	@Override
	public void bajaCliente(Cliente clien) {
		// TODO Auto-generated method stub
		openConnection();

		try {
			stmt = con.prepareStatement(ELIMINAR_CLIENTE);
			stmt.setInt(1, clien.getId_usu());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
