package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import modelo.Cliente;

public interface Dao {

	public void login(Cliente usu) throws LoginError;
//	public void altaPropietario(Propietario prop) throws InsertError;
//	public Map<String, Propietario> listarPropietarios();
	System.out.println("Prueba");
}
