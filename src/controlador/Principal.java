package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import modelo.Cliente;
import vista.VistaLogIn;

public class Principal {
	private static Dao dao = new DaoImplementMySQL();

	public static void main(String[] args) {
<<<<<<< HEAD
;
=======
		VistaLogIn inicio = new VistaLogIn();
		inicio.setVisible(true);
>>>>>>> branch 'main' of https://github.com/AGL-12/Reto_final_Tienda.git
	}

	public static void login(Cliente cli) throws LoginError {
		dao.login(cli);
	}

//	public static void Alta(Propietario propi) throws InsertError {
//		dao.altaPropietario(propi);
//	}
//
//	public static Map<String, Propietario> listarPropietarios() {
//		return dao.listarPropietarios();
//	}

}
