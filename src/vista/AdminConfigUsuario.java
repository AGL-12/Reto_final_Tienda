package vista;

import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class AdminConfigUsuario extends JDialog implements ActionListener {
	// JoptionPane Showconfig dialog mirar usos
	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private JTable tableListaUsuarios;
	JButton btnSeleccionarUsuario;
	private Map<Integer, Cliente> listaClienteTod;

	/**
	 * Create the dialog.
	 */
	public AdminConfigUsuario(JDialog ventanaIntermedia) {
		super(ventanaIntermedia, "Lista de todos los clientes", true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// cada vez que se vuelva a esta ventana, se realizara un refresh a la tabla
				// para que se puedan ver los cambios
				if (listaClienteTod != null) {

					refrescarTabla();
				}
			}

		});
		setBounds(100, 100, 450, 315);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(ventanaIntermedia);

		JLabel lblTitulo = new JLabel("SELECT USER");
		lblTitulo.setBounds(134, 10, 115, 25);
		getContentPane().add(lblTitulo);

		tableListaUsuarios = new JTable();

		btnSeleccionarUsuario = new JButton("SELECT USER");
		btnSeleccionarUsuario.setBounds(135, 235, 148, 33);
		getContentPane().add(btnSeleccionarUsuario);
		btnSeleccionarUsuario.addActionListener(this);

		// Llamamos al DAO para obtener los clientes
		listaClienteTod = Principal.listarCliente();

		// Configurar el modelo de la tabla
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

		// Definimos las columnas que habra en la tabla
		model.addColumn("Seleccionar");
		model.addColumn("id_clien");
		model.addColumn("usuario");
		model.addColumn("dni");
		model.addColumn("correo");
		model.addColumn("direccion");
		model.addColumn("Total de compras");

		tableListaUsuarios = new JTable(model);
		tableListaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		refrescarTabla();

		JScrollPane scrollPane = new JScrollPane(tableListaUsuarios);
		scrollPane.setBounds(10, 46, 418, 179);
		getContentPane().add(scrollPane);

	}
	
	/**
	 * Maneja los eventos de acción disparados por los componentes en este diálogo.
	 * Específicamente, escucha los clics en el botón "SELECT USER".
	 * Cuando se hace clic en el botón, identifica el usuario seleccionado en la tabla
	 * y abre un dialog VistaUsuario para ese usuario.
	 *
	 * @param e El ActionEvent que ocurrió.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Cliente usuarioSeleccionado = null;
		boolean usuarioEncontrado = false;
		if (e.getSource().equals(btnSeleccionarUsuario)) {

			for (int row = 0; row < model.getRowCount(); row++) {
				boolean isSelected = (boolean) model.getValueAt(row, 0);

				if (isSelected && !usuarioEncontrado) {
					int idCliente = (int) model.getValueAt(row, 1);
					usuarioSeleccionado = listaClienteTod.get(idCliente);
					usuarioEncontrado = true;
				}
			}

			if (usuarioSeleccionado != null) {
				VistaUsuario vista = new VistaUsuario(usuarioSeleccionado, this);
				vista.setVisible(true);
			}
		}
	}
	/**
	 * Actualiza los datos que se muestran en la tabla de usuarios.
	 * Limpia los datos actuales de la tabla, obtiene la lista más reciente de clientes
	 * de la base de datos y rellena el modelo de la tabla con la información actualizada.
	 */
	private void refrescarTabla() {

		model.setRowCount(0);

		// Llamamos al DAO para obtener los vehículos del propietario
		listaClienteTod = Principal.listarCliente();

		for (Cliente cliens : listaClienteTod.values()) {
			model.addRow(new Object[] { false, cliens.getId_usu(), cliens.getUsuario(), cliens.getDni(),
					cliens.getCorreo(), cliens.getDireccion(), cliens.getListaCompra().size() });
		}

	}
}
