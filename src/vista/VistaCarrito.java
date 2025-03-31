package vista;

import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import controlador.Dao;
import controlador.DaoImplementMySQL;
import controlador.Principal;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

import java.awt.BorderLayout;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.Timestamp;
import java.sql.SQLException;
import java.util.Map;

import java.util.List;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;
	private DefaultTableModel model;
	private JTable tablaCarrito;

	/**
	 * @param seleccionados
	 * @param preSetCompra
	 */

	public VistaCarrito(JDialog vistaTienda, List<Compra> listaCompra, Pedido preSetCompra) {

		super(vistaTienda);
		super.setModal(true);
		setBounds(100, 100, 849, 608);
		getContentPane().setLayout(null);

		model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Cantidad");
		model.addColumn("Descuento oferta");
		model.addColumn("Precio Final");
		model.addColumn("Precio Total");

		float totalCompra = 0;

		for (Compra com : listaCompra) {
			Articulo arti = Principal.buscarArticulo(com.getId_art());
			float descontado = (arti.getOferta() / 100) * arti.getPrecio();
			float precioFinal = arti.getPrecio() - descontado;
			float precioTotal = precioFinal * com.getCantidad();
			totalCompra += precioFinal;

			model.addRow(new Object[] { arti.getNombre(), com.getCantidad(), descontado, precioFinal, precioTotal });
		}
		preSetCompra.setTotal(totalCompra);

		tablaCarrito = new JTable(model);
		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tablaCarrito);
		scrollPane.setBounds(0, 0, 536, 335); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana

		model.addRow(new Object[] { "Total Compra", "", "", "", totalCompra });

		JLabel lblTitulo = new JLabel("CARRITO");
		lblTitulo.setBounds(632, 0, 205, 90);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(lblTitulo);

		tablaCarrito = new JTable(model);
		getContentPane().add(scrollPane);

		btnVolver = new JButton("BACK");
		btnVolver.setBounds(315, 407, 181, 103);
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(btnVolver);
		btnVolver.addActionListener(this);

		btnComprar = new JButton("BUY");
		btnComprar.setBounds(40, 407, 198, 103);
		btnComprar.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(btnComprar);
		btnComprar.addActionListener(this);

	}

	public VistaCarrito() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnComprar)) {
			fullCompra();
		} else if (e.getSource().equals(btnVolver)) {
			this.dispose();

		}
	}

	private void fullCompra() {
		int idPedido = 0;
		float totalCompra = pedido.getTotal();
		int idUsuario = clienteActual.getId_usu();
		LocalDateTime now = LocalDateTime.now();
		try {
			idPedido = Principal.guardarPedido(idUsuario, totalCompra, now);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Articulo art : carrito.values()) {
			int idArticulo = art.getId_art();
			int cantidad = art.getStock();

			System.out.println("Guardando artículo: ID = " + idArticulo + ", Cantidad = " + cantidad);

			try {
				Principal.guardarCompra(idPedido, art.getId_art(), cantidad);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		JOptionPane.showMessageDialog(this, "¡Compra realizada con éxito!");
		this.dispose();
	}
}
