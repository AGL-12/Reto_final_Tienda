package vista;

import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class AdminConfigUsuario extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;

	private DefaultTableModel model;
	private JTable tableListaUsuarios;
	JButton btnSeleccionarUsuario;
	private Cliente localClien;
	private Map<Integer, Cliente> listaClientes;
	private Map<Integer, Cliente> listaClienteTod;
	/**
	 * Create the dialog.
	 */
	public AdminConfigUsuario( JDialog ventanaIntermedia) {
		super(ventanaIntermedia, "Lista de todos los clientes", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		//localClien = clien;
		// Llamamos al DAO para obtener los vehículos del propietario
		listaClienteTod = Principal.listarCliente();

		JLabel lblTitulo = new JLabel("SELECT USER");
		lblTitulo.setBounds(134, 10, 115, 25);
		getContentPane().add(lblTitulo);
		
		tableListaUsuarios = new JTable();
		//tableListaUsuarios.setBounds(48, 62, 285, 143);
		//getContentPane().add(tableListaUsuarios);
		
		 btnSeleccionarUsuario = new JButton("SELECT USER");
		btnSeleccionarUsuario.setBounds(124, 209, 148, 33);
		getContentPane().add(btnSeleccionarUsuario);
		btnSeleccionarUsuario.addActionListener(this);
		
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
		
		// Agregar los datos de los vehículos al modelo de la tabla
		for (Cliente cliens : listaClienteTod.values()) {
			model.addRow(new Object[] { false, cliens.getId_usu(), cliens.getUsuario(), cliens.getDni(),
					cliens.getCorreo(), cliens.getDireccion(), cliens.getListaCompra().size() });

		}
		// Establecer el modelo de la tabla con los datos
		tableListaUsuarios = new JTable(model);
		tableListaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		

		// Agregar un listener para detectar la selección del checkbox
		tableListaUsuarios.getModel().addTableModelListener(e -> {
			int row = e.getFirstRow();
			boolean isSelected = (boolean) model.getValueAt(row, 0);

			if (isSelected) {
				// Obtener los datos del cliente seleccionado
				int idCliente = (int) model.getValueAt(row, 1);
				String usuario = (String) model.getValueAt(row, 2);
				String dni = (String) model.getValueAt(row, 3);
				String correo = (String) model.getValueAt(row, 4);
				String direccion = (String) model.getValueAt(row, 5);
				int totalCompras = (int) model.getValueAt(row, 6);
				
				
				// Buscar el cliente en la lista original
				//localClien = listaClienteTod.getOrDefault(idCliente, null);	// Buscar el cliente en la lista original
			
				// Desmarcar otros checkboxes
				for (int i = 0; i < model.getRowCount(); i++) {
					if (i != row) {
						model.setValueAt(false, i, 0);
					}
				}
			} else {
				localClien = null;
			}
		});

		
		JScrollPane scrollPane = new JScrollPane(tableListaUsuarios);
		scrollPane.setBounds(10, 46, 418, 179); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Añadir el JScrollPane al panel
		
		

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
			  // Verificar si el botón "SELECT USER" fue presionado
		    if(e.getSource().equals(btnSeleccionarUsuario)) {
		        // Iterar sobre todas las filas para encontrar cuál tiene el checkbox marcado
		        for (int row = 0; row < model.getRowCount(); row++) {
		            boolean isSelected = (boolean) model.getValueAt(row, 0); // Obtenemos el valor del checkbox

		            if (isSelected) {
		                // Si el checkbox está marcado, obtener los datos de esa fila
		                int idCliente = (int) model.getValueAt(row, 1);
		                localClien = listaClienteTod.get(idCliente); // Buscar el cliente en la lista

		                // Si encontramos un cliente, mostrar la vista
		                if (localClien != null) {
		                    VistaUsuario vista = new VistaUsuario(localClien, this);
		                    vista.setVisible(true);
		                }
		                break; // Romper el ciclo después de encontrar el primer cliente seleccionado
		            }
		        
		    }
		    }
	}
}
