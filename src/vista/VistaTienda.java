package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;

import javax.swing.DefaultCellEditor;
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
		this.localClien = clien;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		// Crear la tabla antes de usarla en JScrollPane
		tableArticulo = new JTable();
		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// La columna 6 es editable, el resto no
				return column == 6;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 6 ? Integer.class : String.class;
			}
		};
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// cada vez que se vuelva a esta ventana, se realizara un refresh a la tabla
				// para que se puedan ver los cambios
				if (tableArticulo != null) {
					cargaConteTabla();
				}
			}
		});

		model.addColumn("id_art");
		model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
		model.addColumn("Cantidad");

		// Establecer el modelo de la tabla con los datos
		tableArticulo.setModel(model);
		// 🔹 OCULTAR LA COLUMNA ID_ART
		tableArticulo.removeColumn(tableArticulo.getColumnModel().getColumn(0));
		// Aplicar el comportamiento del clic en la celda
		tableArticulo.setCellSelectionEnabled(true); // Permitir selección de celdas
		tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Asignar un DocumentListener al editor de la celda para la columna de
		// "Cantidad"
		tableArticulo.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				JTextField field = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row,
						column);

				// Crear un DocumentListener para detectar cambios mientras se escribe
				field.getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					// Verificar si el valor es numérico
					private void verificarNumero(JTextField field) {
						String text = field.getText();

						// Si el texto no es un número, deshabilitar el botón
						if (!text.matches("[0-9]*")) {
							field.setForeground(Color.RED);
							btnCompra.setEnabled(false); // Deshabilitar el botón
						} else {
							field.setForeground(Color.BLACK);
							btnCompra.setEnabled(true); // Habilitar el botón
						}
					}
				});

				return field; // Devolver el JTextField para que funcione como editor de la celda
			}
		});

		// Agregar el listener después de inicializar la tabla
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) { // columna real en el modelo
					int fila = e.getFirstRow();

					Object cantidadObj = model.getValueAt(fila, 6);
					Object stockObj = model.getValueAt(fila, 5); // columna stock

					if (cantidadObj != null && stockObj != null) {
						try {
							int cantidadIngresada = Integer.parseInt(cantidadObj.toString());
							int stockDisponible = Integer.parseInt(stockObj.toString());

							if (cantidadIngresada > stockDisponible) {
								// Solo corregimos, sin parar edición
								model.setValueAt(stockDisponible, fila, 6);
							}
						} catch (NumberFormatException ex) {
							model.setValueAt(null, fila, 6);
						}
					}
				}
			}
		});

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

		if (clien.isEsAdmin()) {
			btnAdmin.setVisible(true);
		}
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
			// Forzar que la celda en edición se guarde antes de continuar
			if (tableArticulo.isEditing()) {
				tableArticulo.getCellEditor().stopCellEditing();
			}

			Pedido preSetPedido = new Pedido(Principal.obtenerUltimoIdPed(), localClien.getId_usu(), 0,
					LocalDateTime.now());

			this.setVisible(false);

			VistaCarrito carritoNoCompra = new VistaCarrito(this, cargaPedCom(preSetPedido), preSetPedido);

			carritoNoCompra.setVisible(true);

			this.setVisible(true);

		}
	}

	private List<Compra> cargaPedCom(Pedido preSetPedido) {
		List<Compra> listaCompra = new ArrayList<>();

		for (int i = 0; i < model.getRowCount(); i++) {
			Object valor = model.getValueAt(i, 6);

			// Si la celda está en edición, obtener el valor del editor en lugar del modelo
			if (tableArticulo.isEditing() && tableArticulo.getEditingRow() == i) {
				Component editor = tableArticulo.getEditorComponent();
				if (editor instanceof JTextField textField) {
					valor = textField.getText();
				}
			}
			if (valor != null) {
				// Verificar si el valor es válido antes de hacer el cast
				try {
					int seleccionado = Integer.parseInt(valor.toString());
					if (seleccionado > 0) {
						Compra palCarro = new Compra((Integer) model.getValueAt(i, 0), preSetPedido.getId_ped(),
								seleccionado);
						listaCompra.add(palCarro);
					}
				} catch (NumberFormatException e) {
					model.setValueAt(null, i, 6);
				}
			}

		}
//		for (int i = 0; i < model.getRowCount(); i++) {
//			if (model.getValueAt(i, 6) != null) {
//				int selecionado = (Integer) model.getValueAt(i, 6);
//				if (selecionado != 0) {
//					Compra palCarro = new Compra((Integer) model.getValueAt(i, 0), preSetPedido.getId_ped(),
//							(Integer) model.getValueAt(i, 6));
//					listaCompra.add(palCarro);
//				}
//			}
//		}
		return listaCompra;

	}

	private void cargaConteTabla() {
		model.setRowCount(0);
		// Obtener los artículos del DAO
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();
		// Agregar los datos de los artículos al modelo de la tabla
		for (Articulo art : articulos.values()) {
			if (art.getStock() != 0) {
				model.addRow(new Object[] { art.getId_art(), art.getNombre(), art.getDescripcion(), art.getPrecio(),
						art.getOferta(), art.getStock(), null });
			}
		}
	}

}
