package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import vista.VistaLogIn;

public class Principal {
	private static Dao dao = new DaoImplementMySQL();

	public static void main(String[] args) {
		VistaLogIn inicio = new VistaLogIn();
		inicio.setVisible(true);
	}

	public static void login(Cliente cli) throws LoginError {
		dao.login(cli);
	}

	public static void modificarArticulo(Articulo art) throws modifyError {
		dao.modificarArticulo(art);
	}
	public static void eliminarArticulo(Articulo art) throws modifyError {
		dao.eliminarArticulo(art);
	}
	public static void añadirArticulo(Articulo art) throws modifyError {
		dao.añadirArticulo(art);
	}
}
