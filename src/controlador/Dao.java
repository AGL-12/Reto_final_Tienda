package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import modelo.Cliente;
import modelo.Pedido;

public interface Dao {

	public Cliente login(Cliente usu) throws LoginError;

	public Map<Integer, Cliente> listarClientesTod();
//	public Map<String, Pedido> listarPedidoCli();
	
	public void altaCliente(Cliente clien);

	public void modificarCliente(Cliente clien);

	public void bajaCliente(Cliente clien);
}
