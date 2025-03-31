package vista;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Articulo;
import modelo.Compra;
import modelo.Pedido;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;
	private DefaultTableModel model;
	private JTable tablaCarrito;

	/**
	 * Create the dialog.
	 * 
	 * @param preSetCompra
	 */
	public VistaCarrito(JDialog vista, List<Compra> listaCompra, Pedido preSetCompra) {
		super(vista);
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

		JLabel lblTitulo = new JLabel("CARRITO");
		lblTitulo.setBounds(632, 0, 205, 90);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(lblTitulo);

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnComprar)) {
			fullCompra();
		} else if (e.getSource().equals(btnVolver)) {
			this.dispose();
		}
	}

	private void fullCompra() {
		
	}
}
