package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.Statement;

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
	// Sentencias SQL
	final String BAJA_ARTICULO = "DELETE FROM articulo  WHERE ID_ART= ?";
	final String ALTA_ARTICULO = "INSERT INTO articulo  VALUES (?, ?, ?, ?, ?, ?, ?)";
	final String MODIFICAR_ARTICULO = "UPDATE articulo SET nombre = ?, descripcion = ?, stock = ?, precio = ?, oferta = ?, seccion = ? WHERE id_art = ?";

	final String LOGIN = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";
	final String login = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";
	final String INSERTAR_CLIENTE = "INSERT INTO cliente(usuario, contra, dni, correo, direccion, metodo_pago, num_cuenta) VALUES (?,?,?,?,?,?,?)";
	final String ELIMINAR_CLIENTE = "DELETE from cliente where id_clien=?";
	final String MODIFICAR_CLIENTE = "UPDATE cliente set usuario=?, contra=?, dni=?, correo=?, direccion=?, metodo_pago=?, num_cuenta=? WHERE id_clien=?;";
	final String MAX_ARTICULO = "SELECT MAX(ID_ART)FROM ARTICULO";
	final String select_cliente = "select * from cliente";
	final String pedido_cliente = "select * from compra where id_ped in (Select id_ped from pedido where id_clien=?)";
	final String TODOS_ARTICULOS = "SELECT * FROM articulo";
	final String crear_pediod_cliente = "insert into pedido (id_clien,fecha_compra) values (?,?)";
	final String maxIdPedido = "SELECT MAX(id_ped) FROM pedido";

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
			stmt = con.prepareStatement(login);

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
				usuarioAutenticado.setContra(rs.getString("contra")); // Asegúrate de que el nombre de la columna sea														// "id_usu"
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
	public void modificarArticulo(Articulo art) throws modifyError {
		System.out.println(art.toString());
		openConnection();
		try {
			stmt = con.prepareStatement(MODIFICAR_ARTICULO);
			stmt.setString(1, art.getNombre());
			stmt.setString(2, art.getDescripcion());
			stmt.setInt(3,art.getStock());
			stmt.setFloat(4,art.getPrecio());
			stmt.setFloat(5,art.getOferta());
			stmt.setString(6,art.getSeccion().name());
			stmt.setInt(7, art.getId_art());
			stmt.executeUpdate();


		} catch (SQLException e) {
			throw new modifyError("Error al modificar el perfil");
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
	

	}

	@Override
	public void eliminarArticulo(Articulo art) throws modifyError {
		
		openConnection();
		try {
			stmt = con.prepareStatement(BAJA_ARTICULO);
			stmt.setInt(1, art.getId_art());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
	}

	@Override
	public void añadirArticulo(Articulo art) throws modifyError {
		int id;
		id = obtenerUltimoIdArt();
		openConnection();

		try {
			stmt = con.prepareStatement(ALTA_ARTICULO);
			stmt.setInt(1, id);
			stmt.setString(2, art.getNombre());
			stmt.setString(3, art.getDescripcion());
			stmt.setInt(4, art.getStock());
			stmt.setFloat(5, art.getPrecio());
			stmt.setFloat(6, art.getOferta());
			stmt.setString(7, art.getSeccion().name());
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}

	}

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

	@Override
	public void altaCliente(Cliente clien) throws AltaError {
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
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();

			}
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
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();

			}
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
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		try {
			openConnection();
			stmt = con.prepareStatement(maxIdPedido);
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
		return ultimoId;
	}

	@Override
	public int obtenerUltimoIdArt() {
		int ultimoIdArt = 0;
		ResultSet rs = null;
		try {
			openConnection();
			stmt = con.prepareStatement(MAX_ARTICULO);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ultimoIdArt = rs.getInt(1);
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
		return ultimoIdArt + 1;
	}
}