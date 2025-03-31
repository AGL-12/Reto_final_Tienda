package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;

import javax.swing.JButton;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;

	private JButton btnUsuario, btnCompra, btnAdmin;
	private Cliente cambio;

	private Cliente localClien;

	private DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista) {
		super(vista, "Bienvendido", true);
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
		btnAdmin.addActionListener(this);
		btnAdmin.setVisible(false);
		getContentPane().add(btnAdmin);

		btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		btnCompra.addActionListener(this);
		getContentPane().add(btnCompra);

		btnCompra.addActionListener(this);

		tableArticulo = new JTable();

		if (clien.isEsAdmin()) {
			btnAdmin.setVisible(true);
		}
		// Crear el JScrollPane con la tabla correctamente inicializada

		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85);
		getContentPane().add(scrollPane);

		/*
		 * DefaultTableModel model = new DefaultTableModel( new Object[] { "Nombre",
		 * "Descripción", "Precio", "Oferta", "Stock", "Cantidad" }, 0) {
		 * 
		 * @Override public Class<?> getColumnClass(int columnIndex) { if (columnIndex
		 * == 2 || columnIndex == 3) { return Float.class; } else if (columnIndex == 4
		 * || columnIndex == 5) { return Integer.class; } return String.class; }
		 */
		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 6 ? Integer.class : String.class; // CheckBox en la primera columna
			}
		};
		model.addColumn("id_art");
		model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
		model.addColumn("Cantidad");

		// Obtener los artículos del DAO
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		for (Articulo art : articulos.values()) {
			if (art.getStock() != 0) {
				model.addRow(new Object[] { art.getId_art(), art.getNombre(), art.getDescripcion(), art.getPrecio(),
						art.getOferta(), art.getStock(), 0 });
			}
		}

		tableArticulo.setModel(model);
		// 🔹 OCULTAR LA COLUMNA ID_ART
		tableArticulo.removeColumn(tableArticulo.getColumnModel().getColumn(0));
		// Agregar el listener después de inicializar la tabla
		model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				// Verifica que el cambio sea en la columna de "Cantidad" (última columna)
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
					int fila = e.getFirstRow(); // Obtener la fila modificada
					int cantidadIngresada = (Integer) model.getValueAt(fila, 6);
					int stockDisponible = (Integer) model.getValueAt(fila, 5); // Columna de Stock
					// Verifica si la cantidad excede el stock
					if (cantidadIngresada > stockDisponible) {
						// Mostrar mensaje de advertencia
						JOptionPane.showMessageDialog(null, "La cantidad ingresada supera el stock disponible.",
								"Error", JOptionPane.WARNING_MESSAGE);

						// Restablecer la cantidad al valor máximo permitido (el stock)
						model.setValueAt(stockDisponible, fila, 6);
					}
				}
			}

		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnUsuario)) {

			this.setVisible(false);

			VistaUsuario vistaUsuario = new VistaUsuario(this.localClien, this);
			vistaUsuario.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnAdmin)) {

			this.setVisible(false);

			VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
			menuAdmin.setVisible(true);

			this.setVisible(true);
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

		Map<Integer, Articulo> todosLosArticulos = Principal.obtenerTodosArticulos(); // Obtener artículos originales
																						// desde BD

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

	private List<Compra> cargaPedCom(Pedido preSetCompra) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			int selecionado = (Integer) model.getValueAt(i, 6);
			if (selecionado != 0) {
				Compra palCarro = new Compra((Integer) model.getValueAt(i, 0), preSetCompra.getId_usu(),
						(Integer) model.getValueAt(i, 6));
				listaCompra.add(palCarro);
			}
		}
		return listaCompra;
	}

}
