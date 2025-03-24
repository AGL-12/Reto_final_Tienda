package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import vista.VistaLogIn;

public class Principal {
	private static Dao dao = new DaoImplementMySQL();

	public static void main(String[] args) {
;
	}

//	public static void login(Usuario usu) throws LoginError {
//		dao.login(usu);
//	}
//
//	public static void Alta(Propietario propi) throws InsertError {
//		dao.altaPropietario(propi);
//	}
//
//	public static Map<String, Propietario> listarPropietarios() {
//		return dao.listarPropietarios();
//	}

}
