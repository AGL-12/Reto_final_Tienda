package vista;

import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;

import java.awt.Container;
import java.util.Map;

import javax.swing.*;

public class AdminListaCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	Container contentPanel;
	JTable tableCoches;

	/**
	 * Create the dialog.
	 */
	public AdminListaCliente() {
		setBounds(100, 100, 450, 300);
		// Creamos un JScrollPane para la tabla
		JScrollPane scrollPane = new JScrollPane(tableCoches);
		scrollPane.setBounds(67, 179, 327, 85); // Ubicación y tamaño del JScrollPane

		contentPanel.add(scrollPane); // Añadir el JScrollPane al panel
		// Crear un modelo de tabla vacío
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("id_clien");
		model.addColumn("usuario");
		model.addColumn("contra");
		model.addColumn("dni");
		model.addColumn("correo");
		model.addColumn("direccion");
		model.addColumn("metodo_pago");
		model.addColumn("num_cuenta");
		model.addColumn("esAdmin");
		// Llamamos al DAO para obtener los vehículos del propietario
		Map<String, Cliente> vehiculos = Principal.listarCliente();
		// Definir las columnas de la tabla
		// String[] columnNames = {"Matrícula", "Marca", "Modelo", "Antigüedad",
		// "Precio"};

		// Agregar los datos de los vehículos al modelo de la tabla
		for (Cliente cliens : vehiculos.values()) {
			model.addRow(new Object[] {
//	            		cliens.getMatricula(),
//	            		cliens.getMarca(),
//	            		cliens.getModelo(),
//	            		cliens.getAntiguedad(),
//	            		cliens.getPrecio()
			});

		}

		// Establecer el modelo de la tabla con los datos
		tableCoches.setModel(model);
	}

}
