package vista;

import java.awt.EventQueue;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controlador.Dao;
import controlador.DaoImplementMySQL;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;
	private JButton btnUsuario, btnCompra, btnAdmin;
	private Cliente cambio;

	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista, boolean modal) {
		super(vista);
		super.setModal(modal);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		this.cambio = clien;

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
		getContentPane().add(btnAdmin);

		btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		getContentPane().add(btnCompra);
		btnCompra.addActionListener(this);

		tableArticulo = new JTable();

		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85);
		getContentPane().add(scrollPane);

		DefaultTableModel model = new DefaultTableModel(
				new Object[] { "Nombre", "Descripción", "Precio", "Oferta", "Stock", "Cantidad" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 2 || columnIndex == 3) {
					return Float.class;
				} else if (columnIndex == 4 || columnIndex == 5) {
					return Integer.class;
				}
				return String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Solo la columna de cantidad es editable
			}
		};

		Dao dao = new DaoImplementMySQL();
		Map<Integer, Articulo> articulos = dao.obtenerTodosArticulos();

		for (Articulo art : articulos.values()) {
			if (art.getStock() != 0) {
				model.addRow(new Object[] {
						// false,
						art.getNombre(), art.getDescripcion(), art.getPrecio(), art.getOferta(), art.getStock(), 0 });
			}
		}

		tableArticulo.setModel(model);

		TableColumn selectColumn = tableArticulo.getColumnModel().getColumn(0);
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);
		selectColumn.setCellEditor(new DefaultCellEditor(checkBox));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(btnUsuario)) {
			VistaUsuario vistaUsuario = new VistaUsuario(this.cambio, this, true);
			vistaUsuario.setVisible(true);
			this.dispose();
		} else if (e.getSource().equals(btnCompra)) {
			abrirCarrito();
		}
	}

	private void abrirCarrito() {
		Map<Integer, Articulo> seleccionados = obtenerArticulosSeleccionados();
		VistaCarrito carrito = new VistaCarrito(cambio, this, true, seleccionados);
		carrito.setVisible(true);
	}

	private Map<Integer, Articulo> obtenerArticulosSeleccionados() {
		Map<Integer, Articulo> seleccionados = new HashMap<>();
		DefaultTableModel model = (DefaultTableModel) tableArticulo.getModel();

		Dao dao = new DaoImplementMySQL();
		Map<Integer, Articulo> todosLosArticulos = dao.obtenerTodosArticulos(); // Obtener artículos originales desde BD

		for (int i = 0; i < model.getRowCount(); i++) {
			int cantidad = (Integer) model.getValueAt(i, 5); // Nueva posición de la columna cantidad
			if (cantidad > 0) {
				String nombre = (String) model.getValueAt(i, 0);

				for (Articulo art : todosLosArticulos.values()) {
					if (art.getNombre().equals(nombre)) {
						Articulo articulo = new Articulo(art.getId_art(), art.getNombre(), art.getDescripcion(),
								cantidad, art.getPrecio(), art.getOferta(), art.getSeccion());

						seleccionados.put(art.getId_art(), articulo);
						break;
					}
				}
			}
		}
		return seleccionados;
	}
}
