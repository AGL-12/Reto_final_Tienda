package vista;

import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;

import java.util.Map;

import javax.swing.*;

public class AdminListaCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tableClientes;

	/**
	 * Create the dialog.
	 */
	public AdminListaCliente(JDialog ventanaIntermedia) {
		super(ventanaIntermedia,"Lista de todos los clientes",true);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		// Creamos un JScrollPane para la tabla
		tableClientes = new JTable();

		// Crear un modelo de tabla vacío
		DefaultTableModel model = new DefaultTableModel();
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
		// Definir las columnas de la tabla
		// String[] columnNames = {"Matrícula", "Marca", "Modelo", "Antigüedad",
		// "Precio"};

		// Agregar los datos de los vehículos al modelo de la tabla
		for (Cliente cliens : listaClienteTod.values()) {
			model.addRow(new Object[] { cliens.getId_usu(), cliens.getUsuario(), cliens.getDni(), cliens.getCorreo(),
					cliens.getDireccion(), cliens.getListaCompra().size() });

		}
		// Establecer el modelo de la tabla con los datos
		tableClientes.setModel(model);

		JScrollPane scrollPane = new JScrollPane(tableClientes);
		scrollPane.setBounds(10, 46, 418, 179); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Añadir el JScrollPane al panel
	
	}

}
