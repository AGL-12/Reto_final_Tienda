package controlador;

import java.util.Map;

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
	public static Map<Integer, Cliente> listarCliente() {
		return dao.listarClientesTod();
	}

	public static void altaCliente(Cliente clien) {
		dao.altaCliente(clien);
	}

	public static void modificarCliente(Cliente clien) {
		dao.modificarCliente(clien);
	}

	public static void bajaCliente(Cliente clien) {
		dao.bajaCliente(clien);
	}

}
