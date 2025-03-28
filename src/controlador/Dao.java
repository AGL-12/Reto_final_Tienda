package controlador;

import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;

public interface Dao {


	public Cliente login(Cliente usu) throws LoginError;
	public void modificarArticulo(Articulo art) throws modifyError;
	public void eliminarArticulo(Articulo art) throws modifyError;
	public void añadirArticulo(Articulo art) throws modifyError;
//	public void altaPropietario(Propietario prop) throws InsertError;
//	public Map<String, Propietario> listarPropietarios();

	public void altaCliente(Cliente clien) throws AltaError;


	public void modificarCliente(Cliente clien) throws modifyError;

	public void bajaCliente(Cliente clien) throws DropError;
	public Map<Integer, Articulo> obtenerTodosArticulos();
}

