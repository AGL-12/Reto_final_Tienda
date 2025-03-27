package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controlador.Dao;
import controlador.DaoImplementMySQL;
import modelo.Articulo;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;
	private Map<Integer, Articulo> carrito;
	 private JTable tableCarrito;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the dialog.
	 * @param seleccionados 
	 */
	public VistaCarrito(JDialog vista, boolean modal, Map<Integer, Articulo> seleccionados)  {
		super(vista);
		super.setModal(modal);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{113, 93, 48, 113, 0};
		gridBagLayout.rowHeights = new int[]{50, 198, 47, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		 carrito = seleccionados;
		
		JLabel lblTitulo = new JLabel("CARRITO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
		gbc_lblTitulo.fill = GridBagConstraints.BOTH;
		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitulo.gridx = 1;
		gbc_lblTitulo.gridy = 0;
		getContentPane().add(lblTitulo, gbc_lblTitulo);
		
	     /* JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(20, 50, 450, 150);
	        getContentPane().add(scrollPane);

	        tableCarrito = new JTable();
	        scrollPane.setViewportView(tableCarrito);
		*/
		 btnVolver = new JButton("BACK");
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnVolver = new GridBagConstraints();
		gbc_btnVolver.fill = GridBagConstraints.BOTH;
		gbc_btnVolver.insets = new Insets(0, 0, 0, 5);
		gbc_btnVolver.gridx = 0;
		gbc_btnVolver.gridy = 2;
		getContentPane().add(btnVolver, gbc_btnVolver);
		btnVolver.addActionListener(this);
		
		 btnComprar = new JButton("BUY");
		btnComprar.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_btnComprar = new GridBagConstraints();
		gbc_btnComprar.fill = GridBagConstraints.BOTH;
		gbc_btnComprar.gridx = 3;
		gbc_btnComprar.gridy = 2;
		getContentPane().add(btnComprar, gbc_btnComprar);
		btnComprar.addActionListener(this);
	
		
		// Crear la tabla antes de usarla en JScrollPane
				tableCarrito = new JTable();

				// Crear el JScrollPane con la tabla correctamente inicializada
				JScrollPane scrollPane = new JScrollPane(tableCarrito);
				scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
				getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana

				// Crear un modelo de tabla vacío
				DefaultTableModel model = new DefaultTableModel(); 
		        //model.addColumn("");
		        model.addColumn("Nombre");
				//model.addColumn("Descripción");
				model.addColumn("Precio");
				//model.addColumn("Oferta");
				//model.addColumn("Stock");
			    model.addColumn("Cantidad");

				/*
				Dao dao = new DaoImplementMySQL();
				Map<Integer, Articulo> articulos = dao.obtenerTodosArticulos();
				*/
				// Agregar los datos de los artículos al modelo de la tabla
				for (Articulo art : seleccionados.values()) {
				    model.addRow(new Object[]{
				    	false,
				        art.getNombre(),
				       // art.getDescripcion(),
				        art.getPrecio(),
				        //art.getOferta(),
				        art.getStock(),
				        0
				    });
				}

				// Establecer el modelo de la tabla con los datos
				tableCarrito.setModel(model);
	 
				cargarArticulos();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btnComprar)) {
		
		}else if (e.getSource().equals(btnVolver)) {
            this.dispose(); 
		}
	}
	private void cargarArticulos() {
	    // Crear el modelo de la tabla con las columnas necesarias
	    DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("Nombre");
	    model.addColumn("Cantidad");
	    model.addColumn("Precio");
	    model.addColumn("Precio Total");

	    float totalCompra = 0;  // Para calcular la suma total

	    // Recorrer los artículos seleccionados y agregarlos a la tabla
	    for (Articulo art : carrito.values()) {
	        float precioTotal = art.getPrecio() * art.getStock();  // Precio total de este artículo
	        totalCompra += precioTotal;  // Sumar al total de la compra

	        model.addRow(new Object[]{
	            art.getNombre(),  // Nombre del artículo
	            art.getStock(),   // Cantidad seleccionada
	            art.getPrecio(),  // Precio del artículo
	            precioTotal      // Precio total de este artículo
	        });
	    }
	 
	}  
	
}
