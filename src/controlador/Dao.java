package controlador;

import java.security.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;

public interface Dao {

	public Cliente login(Cliente usu) throws LoginError;

	public Map<Integer, Cliente> listarClientesTod();
//	public Map<String, Pedido> listarPedidoCli();

	public void altaCliente(Cliente clien) throws AltaError;

	public void modificarCliente(Cliente clien) throws modifyError;

	public void bajaCliente(Cliente clien) throws DropError;

	public int guardarPedido(int idUsuario, float totalCompra, LocalDateTime fechaCompra) throws SQLException;

	public void guardarCompra(int idPedido, int idArticulo, int cantidad) throws SQLException;

	public int obtenerNewIdCliente();

	public int obtenerUltimoIdPed() throws SQLException;

	public Map<Integer, Articulo> obtenerTodosArticulos();

	public Pedido crearPedidoUsuario(int id_usu);

	public Articulo buscarArticulo(int id_art);
}