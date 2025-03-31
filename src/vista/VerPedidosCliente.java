package vista;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Cliente;
import modelo.Pedido;

public class VerPedidosCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablePedidos;
	private DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public VerPedidosCliente(JDialog padre, Cliente clien) {
		setBounds(100, 100, 450, 300);
		
		setModal(true);

		model = new DefaultTableModel();
		model.addColumn("Id del pedido");
		model.addColumn("Precio Total");
		model.addColumn("Fecha de compra");

		// Obtener los artículos del DAO
		List<Pedido> pedidos = Principal.obtenerPedidosCliente(clien.getId_usu());

		// Agregar los datos de los artículos al modelo de la tabla
		for (Pedido ped : pedidos) {
			model.addRow(new Object[] { ped.getId_ped(), ped.getTotal(), ped.getFecha_compra() });
		}

		// Establecer el modelo de la tabla con los datos
		tablePedidos = new JTable(model);
		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tablePedidos);
		scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana
	}

}
