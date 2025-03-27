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
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;

public class DaoImplementMySQL implements Dao {
	// Atributo para conexion
	private ResourceBundle configFile;
	private String urlBD, userBD, passwordBD;
	// Atributos
	private Connection con;
	private PreparedStatement stmt;
	// Sentencias SQL
	final String LOGIN = "select * from cliente where usuario=? and contra=?";
	
	
	final String ALTA_ARTICULO = "INSERT INTO articulo  VALUES (?, ?, ?, ?, ?, ?, ?)";
	final String MODIFICAR_ARTICULO = "UPDATE articulo SET nombre = ?, descripcion = ?, stock = ?, precio = ?, oferta = ?, seccion = ? WHERE id_art = ?";
	
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
	public void login(Cliente cli) throws LoginError {
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
	}

	@Override
	public void modificarArticulo(Articulo art) throws modifyError {
		
		
	}

	@Override
	public void eliminarArticulo(Articulo art) throws modifyError {
		
		
	}

	@Override
	public void añadirArticulo(Articulo art) throws modifyError {
		openConnection();

		try {
			stmt = con.prepareStatement(ALTA_ARTICULO);
			stmt.setString(1, art.getUsuario());
			stmt.setString(2, art.getContra());
			stmt.setString(3, art.getDni());
			stmt.setString(4, art.getCorreo());
			stmt.setString(5, art.getDireccion());
			stmt.setString(6, art.getMetodo_pago().name());
			stmt.setString(7, art.getNum_cuenta());
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

	
}
