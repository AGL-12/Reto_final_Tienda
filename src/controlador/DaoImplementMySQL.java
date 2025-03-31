package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.sql.Statement;
import java.sql.Timestamp;

//import com.mysql.cj.xdevapi.Statement;

import modelo.Articulo;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Metodo;
import modelo.Pedido;
import modelo.Seccion;

public class DaoImplementMySQL implements Dao {
	// Atributo para conexion
	private ResourceBundle configFile;
	private String urlBD, userBD, passwordBD;
	// Atributos
	private Connection con;
	private PreparedStatement stmt;

	// Sentencias SQL CLIENTE
	final String LOGIN = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";
	final String INSERTAR_CLIENTE = "INSERT INTO cliente(id_clien, usuario, contra, dni, correo, direccion, metodo_pago, num_cuenta) VALUES (?,?,?,?,?,?,?,?)";

	// Sentencias SQL
	final String login = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";

	final String ELIMINAR_CLIENTE = "DELETE from cliente where id_clien=?";

	final String MODIFICAR_CLIENTE = "UPDATE cliente set usuario=?, contra=?, dni=?, correo=?, direccion=?, metodo_pago=?, num_cuenta=? WHERE id_clien=?;";
	// final String newIdCliente = "SELECT MAX(id_clien) FROM cliente";
	// SENTENCIAS SQL PEDIDO Y COMPRA
	final String INTRODUCIR_PEDIDO = "INSERT INTO pedido (id_ped, id_clien, total, fecha_compra) VALUES (?, ?, ?, ?)";
	final String INTRODUCIR_COMPRA = "INSERT INTO compra(id_art, id_ped, cantidad) VALUES (?, ?, ?)";

	final String select_cliente = "select * from cliente";
	final String pedido_cliente = "select * from compra where id_ped in (Select id_ped from pedido where id_clien=?)";

	final String TODOS_ARTICULOS = "SELECT * FROM articulo";

	final String newIdPedido = "SELECT MAX(id_ped) FROM pedido";
	final String CREAR_PEDIDO_CLIENTE = "insert into pedido (id_clien,fecha_compra) values (?,?)";
	final String CANTIDAD_COMPRA = "UPDATE articulo SET stock = stock - ? WHERE id_art = ?";

	final String crear_pediod_cliente = "insert into pedido (id_clien,fecha_compra) values (?,?)";

	final String newIdCliente = "SELECT MAX(id_clien) FROM cliente";
	final String busca_articulo = "SELECT * FROM articulo where id_art=?";

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
			 * con = DriverManager.getConnection(
			 * "jdbc:mysql://localhost:3306/tienda_brico?serverTimezone=Europe/Madrid&useSSL=false",
			 * "root", "abcd*1234");
			 */
		} catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD" + e.getMessage());
		}
	}

	private void closeConnection() {
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

		ResultSet rs = null;
		openConnection();
		Cliente usuarioAutenticado = null;

		try {
			stmt = con.prepareStatement(login);

			stmt.setString(1, cli.getUsuario());
			stmt.setString(2, cli.getContra());

			rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new LoginError("Usuario o password incorrecto");
			} else {

				usuarioAutenticado = new Cliente();
				usuarioAutenticado.setId_usu(rs.getInt("id_clien"));
				usuarioAutenticado.setUsuario(rs.getString("usuario"));
				usuarioAutenticado.setContra(rs.getString("contra"));
				usuarioAutenticado.setDni(rs.getString("dni"));
				usuarioAutenticado.setCorreo(rs.getString("correo"));
				usuarioAutenticado.setDireccion(rs.getString("direccion"));
				usuarioAutenticado.setMetodo_pago(Metodo.valueOf(rs.getString("metodo_pago")));
				usuarioAutenticado.setNum_cuenta(rs.getString("num_cuenta"));
				usuarioAutenticado.setEsAdmin(rs.getBoolean("esAdmin"));
			}
		} catch (SQLException e) {
			throw new LoginError("Error con el SQL");
		} finally {

			closeConnection();
		}
		return usuarioAutenticado;
	}

	@Override
	public Map<Integer, Cliente> listarClientesTod() {
		Map<Integer, Cliente> todosClientes = new HashMap<>();
		ResultSet rs = null;

		openConnection();

		try {
			// Preparar la sentencia SQL
			stmt = con.prepareStatement(select_cliente);

			// Ejecutar la consulta
			rs = stmt.executeQuery();

			// Iterar sobre el ResultSet y agregar los propietarios al Map
			while (rs.next()) {
				int id = rs.getInt("id_clien");
				String usuario = rs.getString("usuario");
				String contra = rs.getString("contra");
				String dni = rs.getString("dni");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				Metodo metodoPago = Metodo.valueOf(rs.getString("metodo_pago"));
				String num_cuenta = rs.getString("num_cuenta");
				boolean esAdmin = rs.getBoolean("esAdmin");
				Map<Integer, Compra> listaCompra = cargarMapaCom(id);

				// Crear el objeto Propietario con los datos obtenidos
				Cliente prop = new Cliente(id, usuario, contra, dni, correo, direccion, metodoPago, num_cuenta, esAdmin,
						listaCompra);

				// Agregar al Map con el ID como clave
				todosClientes.put(id, prop);
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

	private Map<Integer, Compra> cargarMapaCom(int id) {
		Map<Integer, Compra> paraCargar = new HashMap<>();

		openConnection();
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(pedido_cliente);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int id_art = rs.getInt("id_art");
				int id_ped = rs.getInt("id_ped");
				int cantidad = rs.getInt("cantidad");

				Compra comp = new Compra(id_art, id_ped, cantidad);
				paraCargar.put(comp.getId_ped(), comp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return paraCargar;
	}

//	@Override
//	public Map<String, Pedido> listarPedidoCli() {
//		
//		return null;
//	}

	@Override
	public void altaCliente(Cliente clien) throws AltaError {
		openConnection();

		try {
			stmt = con.prepareStatement(INSERTAR_CLIENTE);

			stmt.setInt(1, clien.getId_usu());
			stmt.setString(2, clien.getUsuario());
			stmt.setString(3, clien.getContra());
			stmt.setString(4, clien.getDni());
			stmt.setString(5, clien.getCorreo());
			stmt.setString(6, clien.getDireccion());
			stmt.setString(7, clien.getMetodo_pago().name());
			stmt.setString(8, clien.getNum_cuenta());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	@Override
	public void modificarCliente(Cliente clien) throws modifyError {
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
			throw new modifyError("Error al modificar el perfil");
		} finally {
			closeConnection();
		}
	}

	@Override
	public void bajaCliente(Cliente clien) throws DropError {

		openConnection();

		try {
			stmt = con.prepareStatement(ELIMINAR_CLIENTE);
			stmt.setInt(1, clien.getId_usu());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public Map<Integer, Articulo> obtenerTodosArticulos() {
		Map<Integer, Articulo> articulos = new HashMap<>();
		ResultSet rs = null;
		openConnection();

		try {

			stmt = con.prepareStatement(TODOS_ARTICULOS);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Articulo articulo = new Articulo(rs.getInt("id_art"), rs.getString("nombre"),
						rs.getString("descripcion"), rs.getInt("stock"), rs.getFloat("precio"), rs.getFloat("oferta"),
						Seccion.valueOf(rs.getString("seccion")));
				articulos.put(articulo.getId_art(), articulo);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener los artículos: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
				e.printStackTrace();
			}
		}

		return articulos; // Siempre devuelve un HashMap válido (vacío si hay errores)

	}

	/*
	 * @Override public int guardarPedido(int idUsuario, float totalCompra,
	 * LocalDateTime fechaCompra) throws SQLException { int idPedido = -1; //String
	 * query = "INSERT INTO pedido(id_clien, total, fecha_compra) VALUES (?, ?, ?)";
	 * 
	 * try { openConnection(); //stmt = con.prepareStatement(INTRODUCIR_PEDIDO);
	 * stmt = con.prepareStatement(INTRODUCIR_PEDIDO,
	 * Statement.RETURN_GENERATED_KEYS); stmt.setInt(1, idUsuario); stmt.setFloat(2,
	 * totalCompra); stmt.setObject(3, fechaCompra); // Usamos setObject para pasar
	 * LocalDateTime
	 * 
	 * int rowsAffected = stmt.executeUpdate();
	 * 
	 * if (rowsAffected > 0) { try (ResultSet generatedKeys =
	 * stmt.getGeneratedKeys()) { if (generatedKeys.next()) { idPedido =
	 * generatedKeys.getInt(1); // Obtener el ID generado } } } } catch
	 * (SQLException e) { throw new SQLException("Error al insertar el pedido", e);
	 * } finally { closeConnection(); } return idPedido; }
	 */

	public int guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException {
		int nuevoIdPedido = obtenerUltimoIdPed();

		try {
			openConnection();

			stmt = con.prepareStatement(INTRODUCIR_PEDIDO);
			stmt.setInt(1, nuevoIdPedido);
			stmt.setInt(2, idUsuario);
			stmt.setFloat(3, totalCompra);
			stmt.setObject(4, fechaCompra);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException("No se pudo insertar el pedido.", e);
		} finally {
			closeConnection();
		}

		return nuevoIdPedido;
	}

	public void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException {

		try {
			openConnection();
			stmt = con.prepareStatement(INTRODUCIR_COMPRA);
			stmt.setInt(1, idArticulo);
			stmt.setInt(2, idPedido);
			stmt.setInt(3, cantidad);

			stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error SQL: " + e.getMessage());
			System.err.println("Código de error SQL: " + e.getErrorCode());
			System.err.println("Estado SQL: " + e.getSQLState());
			throw new SQLException("Error al insertar los artículos en la compra", e);
		} finally {
			closeConnection();
		}
	}

	public void actualizarStock(int idArticulo, int cantidadComprada) throws SQLException {

		try {
			openConnection();
			stmt = con.prepareStatement(CANTIDAD_COMPRA);

			stmt.setInt(1, cantidadComprada);
			stmt.setInt(2, idArticulo);

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Error al actualizar el stock del artículo", e);
		} finally {
			closeConnection();
		}

	}

	@Override
	public Pedido crearPedidoUsuario(int id_usu) {
		openConnection();
		try {
			stmt = con.prepareStatement(crear_pediod_cliente);

			stmt.setInt(1, id_usu);
			// Convertir LocalDateTime a Timestamp
			Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
			stmt.setTimestamp(2, timestamp);

			stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int obtenerUltimoIdPed() {
		int ultimoId = 0;
		ResultSet rs = null;
		openConnection();
		try {
			stmt = con.prepareStatement(newIdPedido);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ultimoId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return ultimoId + 1;
	}

	@Override
	public Articulo buscarArticulo(int id_art) {
		openConnection();
		ResultSet rs;
		Articulo busca = null;

		try {
			stmt = con.prepareStatement(busca_articulo);
			stmt.setInt(1, id_art);
			rs = stmt.executeQuery();
			if (rs.next()) {
				// Recuperamos los datos del usuario autenticado
				busca = new Articulo();
				busca.setId_art(id_art);
				busca.setNombre(rs.getString("nombre"));
				busca.setDescripcion(rs.getString("descripcion"));
				busca.setStock(rs.getInt("stock"));
				busca.setPrecio(rs.getFloat("precio"));
				busca.setOferta(rs.getFloat("oferta"));
				busca.setSeccion(Seccion.valueOf(rs.getString("seccion")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return busca;
	}

	@Override
	public int obtenerNewIdCliente() {
		int ultimoId = 0;
		ResultSet rs = null;
		openConnection();
		try {
			stmt = con.prepareStatement(newIdCliente);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ultimoId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return ultimoId + 1;
	}
}