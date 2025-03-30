package controlador;

import java.security.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.InsertError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;

public interface Dao {

	public Cliente login(Cliente usu) throws LoginError;
//	public void altaPropietario(Propietario prop) throws InsertError;
//	public Map<String, Propietario> listarPropietarios();

	public void altaCliente(Cliente clien) throws AltaError;


	public void modificarCliente(Cliente clien) throws modifyError;

	public void bajaCliente(Cliente clien) throws DropError;
	public Map<Integer, Articulo> obtenerTodosArticulos();
	public int guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException;
	public void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException;
}

