package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Cliente;
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
//	public static void Alta(Propietario propi) throws InsertError {
//		dao.altaPropietario(propi);
//	}
//
//	public static Map<String, Propietario> listarPropietarios() {
//		return dao.listarPropietarios();
//	}

	public static void altaCliente(Cliente clien) throws AltaError {
		// TODO Auto-generated method stub
		dao.altaCliente(clien);
	}

	public static void modificarCliente(Cliente clien) throws modifyError {
		dao.modificarCliente(clien);
	}

	public static void bajaCliente(Cliente clien) throws DropError{
		// TODO Auto-generated method stub
		dao.bajaCliente(clien);
	}
	
	public void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException{
		dao.guardarCompra(idPedido, idArticulo, cantidad);
	}
	
	public void guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException {
		dao.guardarPedido(idUsuario, totalCompra, fechaCompra);
	}
	

}
