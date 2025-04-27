package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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
	final String INSERTAR_CLIENTE = "INSERT INTO cliente(id_clien,usuario, contra, dni, correo, direccion, metodo_pago, num_cuenta) VALUES (?,?,?,?,?,?,?,?)";
	final String ELIMINAR_CLIENTE = "DELETE from cliente where id_clien=?";
	final String MODIFICAR_CLIENTE = "UPDATE cliente set usuario=?, contra=?, dni=?, correo=?, direccion=?, metodo_pago=?, num_cuenta=? WHERE id_clien=?;";
	final String select_cliente = "select * from cliente";
	final String newIdCliente = "SELECT MAX(id_clien) FROM cliente";

	final String INTRODUCIR_PEDIDO = "INSERT INTO pedido (id_ped, id_clien, total, fecha_compra) VALUES (?, ?, ?, ?)";
	final String pedido_compra = "select * from compra where id_ped in (Select id_ped from pedido where id_clien=?)";
	final String CREAR_PEDIDO_CLIENTE = "insert into pedido (id_clien,fecha_compra) values (?,?)";
	final String newIdPedido = "SELECT MAX(id_ped) FROM pedido";
	final String insert_pedido = "insert into pedido (id_ped,id_clien,total,fecha_compra) values (?,?,?,?)";
	final String pedidos_cliente = "SELECT * FROM pedido where id_clien=?";
	final String totalGastadoPedidos = "SELECT TOTALGASTADO(?) as resultado";

	final String CANTIDAD_COMPRA = "UPDATE articulo SET stock = stock - ? WHERE id_art = ?";
	final String INTRODUCIR_COMPRA = "INSERT INTO compra(id_art, id_ped, cantidad) VALUES (?, ?, ?)";
	final String insert_listaCompra = "insert into compra (id_art, id_ped, cantidad) values (?,?,?)";

	final String MAX_ARTICULO = "SELECT MAX(ID_ART)FROM ARTICULO";
	final String OBTENER_ARTICULOS = "SELECT a.id_art, a.nombre, a.precio, a.oferta, c.cantidad FROM articulo a JOIN compra c ON a.id_art = c.id_art WHERE c.id_ped = ?";
	final String TODOS_ARTICULOS = "SELECT * FROM articulo";
	final String busca_articulo = "SELECT * FROM articulo where id_art=?";
	final String update_stockArticulo = "Update articulo set stock=? where id_art=?";

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
			JOptionPane.showMessageDialog(null, "Error al cargar la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
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

	private void closeResult(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Autentica a un cliente en la base de datos comparando usuario y contraseña.
	 *
	 * @param cli Un objeto Cliente que contiene el nombre de usuario y la contraseña
	 * a verificar.
	 * @return Un objeto Cliente con todos los datos si la autenticación es exitosa.
	 * @throws LoginError Si el usuario o la contraseña son incorrectos, o si ocurre
	 * un error de SQL durante la consulta.
	 */
	@Override
	public Cliente login(Cliente cli) throws LoginError {

		ResultSet rs = null;
		openConnection();
		Cliente usuarioAutenticado = null;

		try {
			stmt = con.prepareStatement(LOGIN);

			stmt.setString(1, cli.getUsuario());
			stmt.setString(2, cli.getContra());

			rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new LoginError("Usuario o password incorrecto");
			} else {

				usuarioAutenticado = new Cliente();
				usuarioAutenticado.setId_usu(rs.getInt("id_clien")); // Asegúrate de que el nombre de la columna sea
																		// "id_usu"
				usuarioAutenticado.setUsuario(rs.getString("usuario")); // Asegúrate de que el nombre de la columna sea
																		// "id_usu"
				usuarioAutenticado.setContra(rs.getString("contra")); // Asegúrate de que el nombre de la columna sea //
																		// "id_usu"
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
			closeResult(rs);
			closeConnection();
		}
		return usuarioAutenticado;
	}

	/**
	 * Modifica los datos de un artículo existente en la base de datos.
	 *
	 * @param art El objeto Articulo con los datos actualizados y el ID del artículo
	 * a modificar.
	 * @throws modifyError Si ocurre un error de SQL durante la actualización.
	 */
	@Override
	public void modificarArticulo(Articulo art) throws modifyError {
		System.out.println(art.toString());
		openConnection();
		try {
			stmt = con.prepareStatement(MODIFICAR_ARTICULO);
			stmt.setString(1, art.getNombre());
			stmt.setString(2, art.getDescripcion());
			stmt.setInt(3, art.getStock());
			stmt.setFloat(4, art.getPrecio());
			stmt.setFloat(5, art.getOferta());
			stmt.setString(6, art.getSeccion().name());
			stmt.setInt(7, art.getId_art());
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new modifyError("Error al modificar el perfil");
		} finally {
			closeConnection();
		}

	}
	/**
	 * Elimina un artículo de la base de datos utilizando su ID.
	 *
	 * @param art El objeto Articulo que contiene el ID del artículo a eliminar.
	 * @throws modifyError Si ocurre un error de SQL durante la eliminación.
	 */
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
			closeConnection();
		}
	}

	/**
	 * Añade un nuevo artículo a la base de datos.
	 * Obtiene automáticamente el siguiente ID disponible antes de la inserción.
	 *
	 * @param art El objeto Articulo con los datos del nuevo artículo (el ID se
	 * ignora y se genera automáticamente).
	 * @throws modifyError Si ocurre un error de SQL durante la inserción.
	 */
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
			closeConnection();
		}

	}

	/**
	 * Recupera todos los clientes registrados en la base de datos.
	 *
	 * @return Un Map donde la clave es el ID del cliente  y el valor es el
	 * objeto Cliente correspondiente. Retorna un mapa vacío si no hay
	 * clientes o si ocurre un error.
	 */
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
				String metodoPagoStr = rs.getString("metodo_pago");
				Metodo metodoPago = (metodoPagoStr != null) ? Metodo.valueOf(metodoPagoStr) : null;
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
			e.getMessage();
			e.printStackTrace();
		} finally {
			closeResult(rs);
			closeConnection();
		}

		// Retornar el Map con todos los clientes
		return todosClientes;
	}

	/**
	 * Carga todas las compras asociadas a un ID de cliente específico.
	 *
	 * @param idCliente El ID del cliente cuyas compras se van a cargar.
	 * @return Un Map donde la clave es el ID del pedido asociado a la
	 * compra y el valor es el objeto Compra. Retorna un mapa vacío si no
	 * hay compras o si ocurre un error.
	 */
	private Map<Integer, Compra> cargarMapaCom(int id) {
		Map<Integer, Compra> paraCargar = new HashMap<>();

		openConnection();
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(pedido_compra);
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
		} finally {
			closeConnection();
			closeResult(rs);
		}
		return paraCargar;
	}
	/**
	 * Registra un nuevo cliente en la base de datos.
	 *
	 * @param clien El objeto Cliente con los datos a insertar.
	 * @throws AltaError Si ocurre un error de SQL durante la inserción. 
	 */

	@Override
	public void altaCliente(Cliente clien) throws AltaError {
		openConnection();
		try {
			// Si el usuario no existe, procedemos con la inserción
			stmt = con.prepareStatement(INSERTAR_CLIENTE);
			stmt.setInt(1, clien.getId_usu());
			stmt.setString(2, clien.getUsuario());
			stmt.setString(3, clien.getContra());
			stmt.setString(4, clien.getDni());
			stmt.setString(5, clien.getCorreo());
			stmt.setString(6, clien.getDireccion());
			if (clien.getMetodo_pago() == null) {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(7, clien.getMetodo_pago().name());
			}
			stmt.setString(8, clien.getNum_cuenta());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			closeConnection();
		}
	}

	/*@Override
	public void modificarCliente(Cliente clien) throws modifyError {
		openConnection();

		try {
			stmt = con.prepareStatement(MODIFICAR_CLIENTE);

			stmt.setString(1, clien.getUsuario());
			stmt.setString(2, clien.getContra());
			stmt.setString(3, clien.getDni());
			stmt.setString(4, clien.getCorreo());
			stmt.setString(5, clien.getDireccion());
			if (clien.getMetodo_pago() == null) {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(7, clien.getMetodo_pago().name());
			}
			stmt.setString(7, clien.getNum_cuenta());
			stmt.setInt(8, clien.getId_usu()); // Falta agregar el ID para el WHERE

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new modifyError("Error al modificar el perfil");
		} finally {
			closeConnection();
		}
	}*/
	
	/**
	 * Modifica los datos de un cliente existente en la base de datos.
	 *
	 * @param clien El objeto Cliente con los datos actualizados y el ID del cliente
	 * a modificar.
	 * @throws modifyError Si ocurre un error de SQL durante la actualización.
	 */
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

	    
	        if (clien.getMetodo_pago() == null) {
	            stmt.setNull(6, java.sql.Types.VARCHAR);
	        } else {
	            stmt.setString(6, clien.getMetodo_pago().name());
	        }

	       
	        stmt.setString(7, clien.getNum_cuenta());

	        
	        stmt.setInt(8, clien.getId_usu());

	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        throw new modifyError("Error al modificar el perfil del cliente en la base de datos.");
	    } finally {
	        closeConnection();
	    }
	}
	
	/**
	 * Elimina un cliente de la base de datos utilizando su ID.
	 *
	 * @param clien El objeto Cliente que contiene el ID del cliente a eliminar.
	 * @throws DropError Si ocurre un error de SQL durante la eliminación.
	 */
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

	/**
	 * Recupera todos los artículos disponibles en la base de datos.
	 *
	 * @return Un Map donde la clave es el ID del artículo y el valor es
	 * el objeto Articulo correspondiente. Retorna un mapa vacío si no hay
	 * artículos o si ocurre un error.
	 */
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
			JOptionPane.showMessageDialog(null, "Error al cargar los articulos", "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			closeResult(rs);
			closeConnection();
		}

		return articulos;

	}
	
	/**
	 * Guarda un nuevo pedido (orden) en la base de datos.
	 *
	 * @param ped El objeto Pedido que contiene los detalles del pedido a guardar
	 * (ID del pedido, ID del cliente, total, fecha).
	 * @throws SQLException Si ocurre un error durante la inserción en la base de
	 * datos.
	 */

	public void guardarPedido(Pedido ped) throws SQLException {
		openConnection();
		try {
			stmt = con.prepareStatement(INTRODUCIR_PEDIDO);

			stmt.setInt(1, ped.getId_ped());
			stmt.setInt(2, ped.getId_usu());
			stmt.setFloat(3, ped.getTotal());
			stmt.setTimestamp(4, Timestamp.valueOf(ped.getFecha_compra()));

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("No se pudo insertar el pedido.", e);
		} finally {
			closeConnection();
		}

	}

	/**
	 * Guarda una lista de artículos comprados (objetos Compra) asociados a un
	 * pedido.
	 * Después de insertar las compras, actualiza el stock de cada artículo
	 * correspondiente.
	 *
	 * @param listaCompra La lista de objetos Compra a guardar en la base de datos.
	 * @throws SQLException Si ocurre un error durante la inserción de las compras o
	 * la actualización del stock.
	 */
	public void guardarCompra(List<Compra> listaCompra) throws SQLException {
		openConnection();
		try {
			stmt = con.prepareStatement(INTRODUCIR_COMPRA);
			for (Compra com : listaCompra) {
				stmt.setInt(1, com.getId_art());
				stmt.setInt(2, com.getId_ped());
				stmt.setInt(3, com.getCantidad());

				stmt.executeUpdate();
			}
			for (Compra comp : listaCompra) {
				updateStockArticulo(comp);
			}

		} catch (SQLException e) {
			throw new SQLException("Error al insertar los artículos en la compra", e);
		} finally {
			closeConnection();

		}
	}
	
	/**
	 * Actualiza (decrementa) el stock de un artículo específico en la base de
	 * datos.
	 *
	 * @param idArticulo       El ID del artículo cuyo stock se va a actualizar.
	 * @param cantidadComprada La cantidad que se restará del stock actual.
	 * @throws SQLException Si ocurre un error durante la actualización del stock.
	 */
	public void actualizarStock(int idArticulo, int cantidadComprada) throws SQLException {
		openConnection();
		try {
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

	/**
	 * Crea un registro de pedido en la base de datos para un usuario.
	 *
	 * @param nuevoBD El objeto Pedido con los datos iniciales (ID pedido, ID
	 * usuario, total, fecha) a insertar.
	 */
	@Override
	public void crearPedidoUsuario(Pedido nuevoBD) {
		openConnection();
		try {
			stmt = con.prepareStatement(CREAR_PEDIDO_CLIENTE);

			stmt.setInt(1, nuevoBD.getId_ped());
			stmt.setInt(2, nuevoBD.getId_usu());
			stmt.setFloat(3, nuevoBD.getTotal());
			// Convertir LocalDateTime a Timestamp correctamente
			stmt.setTimestamp(4, Timestamp.valueOf(nuevoBD.getFecha_compra()));

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	/**
	 * Obtiene el ID máximo actual de la tabla de pedidos y le suma 1.
	 * Útil para generar el ID del siguiente pedido a insertar.
	 *
	 * @return El siguiente ID de pedido potencial (max ID + 1). Retorna 1 si no hay
	 * pedidos.
	 */
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
			closeResult(rs);
			closeConnection();
		}
		return ultimoId + 1;
	}
	
	/**
	 * Busca y recupera un artículo específico de la base de datos por su ID.
	 *
	 * @param id_art El ID del artículo a buscar.
	 * @return El objeto Articulo encontrado, o  null si no se encuentra un
	 * artículo con ese ID o si ocurre un error.
	 */
	@Override
	public Articulo buscarArticulo(int id_art) {
		openConnection();
		ResultSet rs = null;
		Articulo busca = null;

		try {
			stmt = con.prepareStatement(busca_articulo);
			stmt.setInt(1, id_art);
			rs = stmt.executeQuery();
			if (rs.next()) {
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
		} finally {
			closeConnection();
			closeResult(rs);
		}

		return busca;
	}
	/**
	 * Obtiene el ID máximo actual de la tabla de clientes y le suma 1.
	 * Útil para generar el ID del siguiente cliente a insertar.
	 *
	 * @return El siguiente ID de cliente potencial (max ID + 1). Retorna 1 si no
	 * hay clientes.
	 */

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
			closeResult(rs);
			closeConnection();
		}
		return ultimoId + 1;
	}
	/**
	 * Obtiene una lista de artículos asociados a un pedido específico.
	 *
	 * @param id_ped El ID del **pedido** cuyos artículos se desean obtener.
	 * @return Una lista de objetos Articulo que pertenecen al pedido especificado.
	 * Incluye ID, nombre, precio y oferta del artículo (la cantidad viene
	 * de la tabla compra, pero no se mapea al objeto Articulo aquí).
	 * Retorna una lista vacía si el pedido no tiene artículos o si ocurre
	 * un error.
	 */
	@Override
	public List<Articulo> obtenerArticulosPedido(int id_clien) {
		List<Articulo> listaArticulo = new ArrayList<>();
		// TODO Auto-generated method stub
		openConnection();
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(OBTENER_ARTICULOS);
			stmt.setInt(1, id_clien);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Articulo art = new Articulo();
				art.setId_art(rs.getInt("id_art"));
				art.setNombre(rs.getString("nombre"));
				art.setPrecio(rs.getFloat("precio"));
				art.setOferta(rs.getFloat("oferta"));

				listaArticulo.add(art);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResult(rs);
			closeConnection();
		}

		return listaArticulo;
	}

	/**
	 * Obtiene una lista de todos los pedidos realizados por un cliente específico.
	 *
	 * @param id_usu El ID del cliente cuyos pedidos se desean obtener.
	 * @return Una lista de objetos Pedido asociados al cliente. Incluye ID del
	 * pedido, ID del cliente, total y fecha de compra. Retorna una lista
	 * vacía si el cliente no tiene pedidos o si ocurre un error.
	 */
	@Override
	public List<Pedido> obtenerPedidosCliente(int id_usu) {
		List<Pedido> listaPedidos = new ArrayList<>();
		// TODO Auto-generated method stub

		openConnection();
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(pedidos_cliente);
			stmt.setInt(1, id_usu);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Pedido ped = new Pedido();
				ped.setId_ped(rs.getInt("id_ped"));
				ped.setId_usu(id_usu);
				ped.setTotal(rs.getFloat("total"));
				// Obtener timestamp y convertirlo a LocalDateTime
				Timestamp timestamp = rs.getTimestamp("fecha_compra");
				if (timestamp != null) {
					ped.setFecha_compra(timestamp.toLocalDateTime());
				}
				listaPedidos.add(ped);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResult(rs);
			closeConnection();
		}
		return listaPedidos;
	}

	/**
	 * Inserta una lista de compras (artículos de un pedido) en la base de datos.
	 * Después de insertar, actualiza el stock de cada artículo comprado.
	 *
	 * @param localListaCompra La lista de objetos Compra a insertar.
	 */
	@Override
	public void insertListCompra(List<Compra> localListaCompra) {
		openConnection();

		try {
			stmt = con.prepareStatement(insert_listaCompra);

			for (Compra com : localListaCompra) {
				stmt.setInt(1, com.getId_art());
				stmt.setInt(2, com.getId_ped());
				stmt.setInt(3, com.getCantidad());

				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		for (Compra compra : localListaCompra) {
			updateStockArticulo(compra);
		}
	}
	/**
	 * Método auxiliar para actualizar el stock de un artículo después de
	 * una compra.
	 * Primero busca el artículo para obtener el stock actual, luego calcula el
	 * nuevo stock y lo actualiza en la base de datos.
	 *
	 * @param com El objeto Compra que contiene el ID del artículo y la cantidad
	 * comprada.
	 */
	private void updateStockArticulo(Compra com) {
		Articulo cambio = buscarArticulo(com.getId_art());
		openConnection();
		cambio.setStock(cambio.getStock() - com.getCantidad());
		try {
			stmt = con.prepareStatement(update_stockArticulo);

			stmt.setInt(1, cambio.getStock());
			stmt.setInt(2, cambio.getId_art());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	/**
	 * Inserta un registro completo de pedido en la base de datos.
	 *
	 * @param localPedido El objeto Pedido a insertar.
	 */
	@Override
	public void insertPedido(Pedido localPedido) {
		openConnection();

		try {
			stmt = con.prepareStatement(insert_pedido);

			stmt.setInt(1, localPedido.getId_ped());
			stmt.setInt(2, localPedido.getId_usu());
			stmt.setFloat(3, localPedido.getTotal());
			stmt.setTimestamp(4, Timestamp.valueOf(localPedido.getFecha_compra()));

			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	/**
	 * Obtiene la cantidad de un artículo específico dentro de un pedido
	 * determinado.
	 * Consulta directamente la tabla 'compra'.
	 *
	 * @param idPedido   El ID del pedido a consultar.
	 * @param idArticulo El ID del artículo a buscar dentro del pedido.
	 * @return La cantidad del artículo encontrado en ese pedido. Retorna 0 si el
	 * artículo no está en el pedido o si ocurre un error.
	 */
	public int obtenerCantidadArticuloEnPedido(int idPedido, int idArticulo) {
		int cantidad = 0;
		openConnection();
		String sql = "SELECT cantidad FROM compra WHERE id_ped = ? AND id_art = ?";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, idPedido);
			stmt.setInt(2, idArticulo);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				cantidad = rs.getInt("cantidad");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cantidad;
	}

	/**
	 * Verifica si ya existe un cliente con un nombre de usuario específico en la
	 * base de datos.
	 * Es útil para evitar nombres de usuario duplicados durante el registro.
	 *
	 * @param nombreUsuario El nombre de usuario a verificar.
	 * @return true si el nombre de usuario ya existe, false en caso
	 * contrario o si ocurre un error.
	 */
	public boolean existeUsuario(String nombreUsuario) {
		ResultSet rs = null;
		boolean usuarioExiste = false;
		openConnection();
		try {
			stmt = con.prepareStatement("SELECT 1 FROM cliente WHERE usuario = ?");
			stmt.setString(1, nombreUsuario);

			rs = stmt.executeQuery();

			if (rs.next()) {
				usuarioExiste = true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return usuarioExiste;
	}

	/**
	 * Obtiene el ID máximo actual de la tabla de artículos y le suma 1.
	 * Útil para generar el ID del siguiente artículo a insertar.
	 *
	 * @return El siguiente ID de artículo potencial (max ID + 1). Retorna 1 si no
	 * hay artículos.
	 */
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

	/**
	 * Calcula el gasto total de un cliente sumando el campo 'total' de todos sus
	 * pedidos.
	 * Utiliza una función SQL predefinida llamada  TOTALGASTADO.
	 *
	 * @param clien El objeto Cliente (se utiliza su ID) para el cual calcular el
	 * gasto total.
	 * @return El gasto total como un valor float. Retorna 0 si el cliente no
	 * tiene pedidos, la función no existe, o si ocurre un error.
	 */
	@Override
	public float totalGastado(Cliente clien) {
		float resultado = 0;
		ResultSet rs = null;
		try {
			openConnection();
			stmt = con.prepareStatement(totalGastadoPedidos);
			stmt.setInt(1, clien.getId_usu());
			rs = stmt.executeQuery();
			if (rs.next()) {
				resultado = rs.getFloat("resultado");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection();

		}
		return resultado;
	}
}