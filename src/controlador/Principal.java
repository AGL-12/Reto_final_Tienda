package controlador;

import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
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

	public static Cliente login(Cliente cli) throws LoginError {
		return dao.login(cli);

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

}
