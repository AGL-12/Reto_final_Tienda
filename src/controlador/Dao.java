package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Cliente;

public interface Dao {

	public Cliente login(Cliente usu) throws LoginError;
//	public void altaPropietario(Propietario prop) throws InsertError;
//	public Map<String, Propietario> listarPropietarios();

	public void altaCliente(Cliente clien);


	public void modificarCliente(Cliente clien) throws modifyError;

	public void bajaCliente(Cliente clien);
}

