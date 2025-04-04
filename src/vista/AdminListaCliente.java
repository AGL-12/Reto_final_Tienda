package vista;

import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

public class AdminListaCliente extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableClientes;
	private JButton btnTest;
	private DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public AdminListaCliente(JDialog ventanaIntermedia) {
		super(ventanaIntermedia, "Lista de todos los clientes", true);
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

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
		// Definir las columnas de la tabla
		model.addColumn("Seleccionar");
		model.addColumn("id_clien");
		model.addColumn("usuario");
//		model.addColumn("contra");
		model.addColumn("dni");
		model.addColumn("correo");
		model.addColumn("direccion");
//		model.addColumn("metodo_pago");
//		model.addColumn("num_cuenta");
//		model.addColumn("esAdmin");
		model.addColumn("Total de compras");
		// Llamamos al DAO para obtener los vehículos del propietario
		Map<Integer, Cliente> listaClienteTod = Principal.listarCliente();

		// Agregar los datos de los vehículos al modelo de la tabla
		for (Cliente cliens : listaClienteTod.values()) {
			model.addRow(new Object[] { false, cliens.getId_usu(), cliens.getUsuario(), cliens.getDni(),
					cliens.getCorreo(), cliens.getDireccion(), cliens.getListaCompra().size() });

		}
		// Establecer el modelo de la tabla con los datos
		tableClientes = new JTable(model);
		tableClientes.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tableClientes);
		scrollPane.setBounds(10, 46, 418, 179); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Añadir el JScrollPane al panel

		btnTest = new JButton("test");
		btnTest.setBounds(173, 261, 89, 23);
		getContentPane().add(btnTest);
		btnTest.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnTest)) {
			mostrarFilasSeleccionadas();
		}
	}

	// Método para mostrar las filas seleccionadas
	private void mostrarFilasSeleccionadas() {
		Set<Integer> filasSeleccionadas = new HashSet<>();

		for (int i = 0; i < model.getRowCount(); i++) {
			boolean isChecked = (Boolean) model.getValueAt(i, 0);
			if (isChecked) {
				filasSeleccionadas.add(i);
			}
		}

		
	}
}
