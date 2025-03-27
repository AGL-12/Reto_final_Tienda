package controlador;

import java.util.Map;

import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;

public interface Dao {

	public void login(Cliente usu) throws LoginError;
	public void modificarArticulo(Articulo art) throws modifyError;
	public void eliminarArticulo(Articulo art) throws modifyError;
	public void añadirArticulo(Articulo art) throws modifyError;
}
