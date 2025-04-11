package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import excepciones.AltaError;
import excepciones.DropError;
import excepciones.LoginError;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

public interface Dao {

	public Cliente login(Cliente usu) throws LoginError;

	public void modificarArticulo(Articulo art) throws modifyError;

	public void eliminarArticulo(Articulo art) throws modifyError;

	public void añadirArticulo(Articulo art) throws modifyError;

	public Map<Integer, Cliente> listarClientesTod();

	public void altaCliente(Cliente clien) throws AltaError;

	public void modificarCliente(Cliente clien) throws modifyError;

	public void bajaCliente(Cliente clien) throws DropError;

	public int obtenerUltimoIdPed();

	public Map<Integer, Articulo> obtenerTodosArticulos();

	public void crearPedidoUsuario(Pedido nuenoBD);

	public Articulo buscarArticulo(int id_art);

	public int obtenerNewIdCliente();

	public List<Articulo> obtenerArticulosPedido(int i);

	public List<Pedido> obtenerPedidosCliente(int id_usu);

	public void insertListCompra(List<Compra> localListaCompra);

	public void insertPedido(Pedido localPedido);

	public void guardarPedido(Pedido ped) throws SQLException;

	public void guardarCompra(List<Compra> listaCompra) throws SQLException;

	public int obtenerCantidadArticuloEnPedido(int idPedido, int idArticulo);

	public int obtenerUltimoIdArt();

	public float totalGastado(Cliente clien);
}
