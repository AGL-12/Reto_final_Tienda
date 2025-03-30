package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import controlador.Dao;
import controlador.DaoImplementMySQL;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.Map;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Articulo> carrito;
	private JTable tableCarrito;
	private Pedido pedido;
	private JButton btnVolver, btnComprar;
	private Cliente clienteActual;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 * 
	 * @param seleccionados
	 */
	public VistaCarrito(Cliente clien, JDialog vista, boolean modal, Map<Integer, Articulo> seleccionados) {
		super(vista);
		super.setModal(modal);
		this.clienteActual = clien;
		setBounds(100, 100, 747, 335);

		carrito = seleccionados;
		pedido = new Pedido();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 400, 113, 0 };
		gridBagLayout.rowHeights = new int[] { 45, 150, 47, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Cantidad");
		//model.addColumn("Precio");
		//model.addColumn("Oferta (%)");
		model.addColumn("Precio Final"); //Con oferta aplicada
		model.addColumn("Precio Total"); //Total de los articulos


		float totalCompra = 0;

	
		for (Articulo art : carrito.values()) {
			int cantidadSeleccionada = art.getStock(); 
			float descuento = (art.getOferta() / 100) * art.getPrecio();
			float precioFinal = art.getPrecio() - descuento;
			float precioTotal = precioFinal * cantidadSeleccionada;
			totalCompra += precioTotal; 

			model.addRow(new Object[] { 
				art.getNombre(), 
				cantidadSeleccionada, 
				//art.getPrecio(), 
				//art.getOferta(), 
				precioFinal, 
				precioTotal 
			});
		}
		
		pedido.setTotal(totalCompra); 

		
		model.addRow(new Object[] { "Total Compra", 
				"", 
				//"", 
				//"",
				"",
				totalCompra 
		});

		JLabel lblTitulo = new JLabel("CARRITO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
		gbc_lblTitulo.gridwidth = 2;
		gbc_lblTitulo.fill = GridBagConstraints.VERTICAL;
		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitulo.gridx = 0;
		gbc_lblTitulo.gridy = 0;
		getContentPane().add(lblTitulo, gbc_lblTitulo);

		
		tableCarrito = new JTable(model); 
		JScrollPane scrollPane = new JScrollPane(tableCarrito);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		getContentPane().add(scrollPane, gbc_scrollPane); 

		btnVolver = new JButton("BACK");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnVolver = new GridBagConstraints();
		gbc_btnVolver.anchor = GridBagConstraints.WEST;
		gbc_btnVolver.fill = GridBagConstraints.VERTICAL;
		gbc_btnVolver.insets = new Insets(0, 0, 0, 5);
		gbc_btnVolver.gridx = 0;
		gbc_btnVolver.gridy = 2;
		getContentPane().add(btnVolver, gbc_btnVolver);
		btnVolver.addActionListener(this);
		btnComprar = new JButton("BUY");
		btnComprar.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnComprar = new GridBagConstraints();
		gbc_btnComprar.fill = GridBagConstraints.BOTH;
		gbc_btnComprar.gridx = 1;
		gbc_btnComprar.gridy = 2;
		getContentPane().add(btnComprar, gbc_btnComprar);
		btnComprar.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    if (e.getSource().equals(btnComprar)) {
	        // Obtener el total de la compra
	        float totalCompra = pedido.getTotal();
	        // Obtener el usuario actual (suponiendo que tienes el ID del usuario logueado)
	        int idUsuario = clienteActual.getId_usu();

	        // Obtener la fecha actual
	        LocalDateTime now = LocalDateTime.now();
	        // Crear una instancia de DaoImplementMySQL
	        Dao dao = new DaoImplementMySQL();
	        // Guardar el pedido en la base de datos
	        int idPedido = 0;
			try {
				idPedido = dao.guardarPedido(idUsuario, totalCompra, now);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        // Registrar los artículos en el pedido
	        for (Articulo art : carrito.values()) {
	        	int idArticulo = art.getId_art();
	            int cantidad = art.getStock();  // O la cantidad que se seleccionó en el carrito
	            // Depuración: Imprimir los valores de id_art y cantidad
	            System.out.println("Guardando artículo: ID = " + idArticulo + ", Cantidad = " + cantidad);

	            try {
					dao.guardarCompra(idPedido, art.getId_art(), cantidad);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }

	        // Mostrar mensaje de éxito o cerrar el carrito
	        JOptionPane.showMessageDialog(this, "¡Compra realizada con éxito!");
	        this.dispose(); // Cierra la ventana del carrito
	    } else if (e.getSource().equals(btnVolver)) {
	        this.dispose(); // Vuelve a la pantalla anterior
	    }
	}
    


	private void cargarArticulos() {

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Cantidad");
		model.addColumn("Precio");
		model.addColumn("Precio Total Compra");

		float totalCompra = 0;

		
		for (Articulo art : carrito.values()) {
			float precioTotal = art.getPrecio() * art.getStock();
			totalCompra += precioTotal; 

			model.addRow(new Object[] { art.getNombre(), 
					art.getStock(), 
					art.getPrecio(), 
					precioTotal
			});
		}

	}
	
	
}
