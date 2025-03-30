package controlador;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.sql.Statement;
import java.time.LocalDateTime;

//import com.mysql.cj.xdevapi.Statement;

import modelo.Articulo;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
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
	final String LOGIN = "SELECT * FROM cliente WHERE usuario = ? AND contra = ?";
	final String INSERTAR_CLIENTE = "INSERT INTO cliente(usuario, contra, dni, correo, direccion, metodo_pago, num_cuenta) VALUES (?,?,?,?,?,?,?)";
	final String ELIMINAR_CLIENTE = "DELETE from cliente where id_clien=?";
	final String MODIFICAR_CLIENTE = "UPDATE cliente set usuario=?, contra=?, dni=?, correo=?, direccion=?, metodo_pago=?, num_cuenta=? WHERE id_clien=?;";

	final String TODOS_ARTICULOS = "SELECT * FROM articulo";
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

	@Override
	public void altaCliente(Cliente clien) throws AltaError {
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
	public void modificarCliente(Cliente clien) throws modifyError {
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
			throw new modifyError("Error al modificar el perfil");
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
	public void bajaCliente(Cliente clien) throws DropError{
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
	
	public Map<Integer, Articulo> obtenerTodosArticulos() {
	    Map<Integer, Articulo> articulos = new HashMap<>();
	    ResultSet rs = null;

	    try {
	        openConnection();
	        
	        if (con == null) {
	            throw new SQLException("No se pudo establecer la conexión con la base de datos.");
	        }

	        stmt = con.prepareStatement(TODOS_ARTICULOS);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            Articulo articulo = new Articulo(
	                rs.getInt("id_art"),
	                rs.getString("nombre"),
	                rs.getString("descripcion"),
	                rs.getInt("stock"),
	                rs.getFloat("precio"),
	                rs.getFloat("oferta"),
	                Seccion.valueOf(rs.getString("seccion"))
	            );
	            articulos.put(articulo.getId_art(), articulo);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener los artículos: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            closeConnection();
	        } catch (SQLException e) {
	            System.err.println("Error al cerrar la conexión: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    return articulos; // Siempre devuelve un HashMap válido (vacío si hay errores)
	}
	
	
	@Override
	public int guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException {
	    int idPedido = -1;
	    String query = "INSERT INTO pedido(id_clien, total, fecha_compra) VALUES (?, ?, ?)";

	    try {
	        openConnection();
	        stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        stmt.setInt(1, idUsuario);
	        stmt.setFloat(2, totalCompra);
	        stmt.setObject(3, fechaCompra); // Usamos setObject para pasar LocalDateTime

	        int rowsAffected = stmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    idPedido = generatedKeys.getInt(1); // Obtener el ID generado
	                }
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error al insertar el pedido", e);
	    } finally {
	        closeConnection();
	    }
	    return idPedido;
	}

	
	public void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException {
	    String query = "INSERT INTO compra(id_art, id_ped, cantidad) VALUES (?, ?, ?)";
	    
	    try {
	        openConnection();
	        stmt = con.prepareStatement(query);
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
	        String query = "UPDATE articulo SET stock = stock - ? WHERE id_art = ?";
	        
	        try {
	            openConnection();
	            stmt = con.prepareStatement(query);
	            stmt.setInt(1, cantidadComprada);
	            stmt.setInt(2, idArticulo);
	            
	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            throw new SQLException("Error al actualizar el stock del artículo", e);
	        } finally {
	            closeConnection();
	        }
	    

	}


}
