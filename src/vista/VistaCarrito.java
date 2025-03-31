package vista;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Articulo;
import modelo.Compra;
import modelo.Pedido;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;

	/**
	 * Create the dialog.
	 * 
	 * @param preSetCompra
	 */
	public VistaCarrito(JDialog vista, List<Compra> listaCompra, Pedido preSetCompra) {
		super(vista);
		super.setModal(true);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 113, 93, 48, 113, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 198, 47, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		// Crear el modelo de la tabla con las columnas necesarias
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Cantidad");
		model.addColumn("Precio");
		model.addColumn("Oferta (%)");
		model.addColumn("Precio Final");
		model.addColumn("Precio Total");

		for (Compra com : listaCompra) {
			Articulo arti = Principal.buscarArticulo(com.getId_art());
			model.addRow(new Object[] {
					arti.getNombre(),
					com.getCantidad(),
					arti.getPrecio(),
			});
		}

		JLabel lblTitulo = new JLabel("CARRITO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
		gbc_lblTitulo.fill = GridBagConstraints.BOTH;
		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitulo.gridx = 1;
		gbc_lblTitulo.gridy = 0;
		getContentPane().add(lblTitulo, gbc_lblTitulo);

		btnVolver = new JButton("BACK");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnVolver = new GridBagConstraints();
		gbc_btnVolver.fill = GridBagConstraints.BOTH;
		gbc_btnVolver.insets = new Insets(0, 0, 0, 5);
		gbc_btnVolver.gridx = 0;
		gbc_btnVolver.gridy = 2;
		getContentPane().add(btnVolver, gbc_btnVolver);
		btnVolver.addActionListener(this);

		btnComprar = new JButton("BUY");
		btnComprar.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnComprar = new GridBagConstraints();
		gbc_btnComprar.fill = GridBagConstraints.BOTH;
		gbc_btnComprar.gridx = 3;
		gbc_btnComprar.gridy = 2;
		getContentPane().add(btnComprar, gbc_btnComprar);
		btnComprar.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(btnComprar)) {

		} else if (e.getSource().equals(btnVolver)) {

		}
	}
}
