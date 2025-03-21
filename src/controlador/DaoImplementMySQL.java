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

public class DaoImplementMySQL implements Dao {
	// Atributo para conexion
	private ResourceBundle configFile;
	private String urlBD, userBD, passwordBD;
	// Atributos
	private Connection con;
	private PreparedStatement stmt;
	// Sentencias SQL
	final String LOGIN = "select * from usuario where usuario=? and contra=?";
	final String ALTAPROP = "insert into propietario (nombre,fechaNace) values (?,?)";

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
}
