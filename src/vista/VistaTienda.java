package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;

import javax.swing.JButton;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;
	private JButton btnUsuario;
	private Cliente localClien;
	private JButton btnAdmin;
	private JButton btnCompra;
	private DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista) {
		super(vista, "Bienvendido", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		this.localClien = clien;

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

		// Crear la tabla antes de usarla en JScrollPane
		tableArticulo = new JTable();

		if (clien.isEsAdmin()) {
			btnAdmin.setVisible(true);
		}
		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0; // Solo la primera columna (checkbox) es editable
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 0 ? Boolean.class : String.class; // CheckBox en la primera columna
			}
		};
		model.addColumn("Seleccionar");
		model.addColumn("id_art");
		model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
		model.addColumn("Cantidad");

		// Obtener los artículos del DAO
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		// Agregar los datos de los artículos al modelo de la tabla
		for (Articulo art : articulos.values()) {
			if (art.getStock()!=0) {
				model.addRow(new Object[] { false, art.getId_art(), art.getNombre(), art.getDescripcion(), art.getPrecio(),
						art.getOferta(), art.getStock(), 0 });
			}
		}

		// Establecer el modelo de la tabla con los datos
		tableArticulo.setModel(model);
		// 🔹 OCULTAR LA COLUMNA ID_ART
		tableArticulo.removeColumn(tableArticulo.getColumnModel().getColumn(1));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnUsuario)) {

			this.setVisible(false);

			VistaUsuario vistaUsuario = new VistaUsuario(this.localClien, this); // "this" es el JFrame principal,
			vistaUsuario.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnAdmin)) {

			this.setVisible(false);

			VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
			menuAdmin.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnCompra)) {

			Pedido preSetCompra = new Pedido(Principal.obtenerUltimoIdPed() + 1, localClien.getId_usu(), 0,
					LocalDateTime.now());

			this.setVisible(false);

			VistaCarrito carritoNoCompra = new VistaCarrito(this, cargaPedCom(preSetCompra), preSetCompra);
			carritoNoCompra.setVisible(true);

			this.setVisible(true);
		}
	}

	private List<Compra> cargaPedCom(Pedido preSetCompra) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			boolean isChecked = (Boolean) model.getValueAt(i, 0);
			if (isChecked && (Integer) model.getValueAt(i, 7) != 0) {
				Compra palCarro = new Compra((Integer) model.getValueAt(i, 2), preSetCompra.getId_usu(), i);
				listaCompra.add(palCarro);
			}
		}
		return listaCompra;
	}

}
