package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
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
	public static Map<String, Cliente> listarCliente() {
		return dao.listarPropietarios();
	}

}
