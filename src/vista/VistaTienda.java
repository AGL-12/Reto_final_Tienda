package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Seccion;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableProductos;
	private JButton btnUsuario;
	private Cliente cambio;
	private JButton btnAdmin;
	private JButton btnCompra;

	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista) {
		super(vista, "Bienvendido", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		this.cambio = clien;

		// Datos de ejemplo para la tabla
		List<Articulo> articulos = new ArrayList<>();
		articulos.add(new Articulo(1, "Laptop", "Laptop description", 5, 1000f, 900f, Seccion.pintura));
		articulos.add(new Articulo(2, "Smartphone", "Smartphone description", 10, 500f, 450f, Seccion.pintura));
		articulos.add(new Articulo(3, "Tablet", "Tablet description", 7, 300f, 250f, Seccion.pintura));
		articulos.add(new Articulo(4, "Headphones", "Headphones description", 15, 100f, 90f, Seccion.pintura));

		// Crear el modelo de tabla con datos
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Hacer que todas las celdas no sean editables
			}
		};
		model.setColumnIdentifiers(
				new Object[] { "ID", "Nombre", "Descripción", "Stock", "Precio", "Oferta", "Sección" });

		for (Articulo articulo : articulos) {
			model.addRow(new Object[] { articulo.getId_art(), articulo.getNombre(), articulo.getDescripcion(),
					articulo.getStock(), articulo.getPrecio(), articulo.getOferta(), articulo.getSeccion() });
		}

		tableProductos = new JTable(model);

		// Añadir la tabla a un JScrollPane
		JScrollPane scrollPane = new JScrollPane(tableProductos);
		scrollPane.setBounds(30, 62, 720, 150); // Ajusta el tamaño y la posición según sea necesario
		getContentPane().add(scrollPane);

		JLabel lblTitulo = new JLabel("DYE TOOLS");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(157, 10, 106, 38);
		getContentPane().add(lblTitulo);

		btnUsuario = new JButton("USER");
		btnUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUsuario.setBounds(10, 232, 85, 21);
		getContentPane().add(btnUsuario);
		btnUsuario.addActionListener(this);

		btnAdmin = new JButton("ADMIN");
		btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAdmin.setBounds(109, 233, 85, 21);
		btnAdmin.addActionListener(this);
		btnAdmin.setVisible(false);
		getContentPane().add(btnAdmin);

		btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		getContentPane().add(btnCompra);

		if (clien.isEsAdmin()) {
			btnAdmin.setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnUsuario)) {

			this.setVisible(false);

			VistaUsuario vistaUsuario = new VistaUsuario(this.cambio, this); // "this" es el JFrame principal,
			vistaUsuario.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnAdmin)) {

			this.setVisible(false);

			VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
			menuAdmin.setVisible(true);

			this.setVisible(true);
		}
	}
}
