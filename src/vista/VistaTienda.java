package vista;

import java.awt.EventQueue;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Seccion;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controlador.Dao;
import controlador.DaoImplementMySQL;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class VistaTienda extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;
	private JButton btnUsuario;
	private Cliente cambio;
	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista, boolean modal) {
		super(vista);
		super.setModal(modal);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		this.cambio= clien;

	  
		JLabel lblTitulo = new JLabel("DYE TOOLS");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(157, 10, 106, 38);
		getContentPane().add(lblTitulo);
		
		/*tableProductos = new JTable();
		tableProductos.setBounds(30, 62, 360, 115);
		getContentPane().add(tableProductos);*/
		
		 btnUsuario = new JButton("USER");
		btnUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUsuario.setBounds(10, 232, 85, 21);
		getContentPane().add(btnUsuario);
		btnUsuario.addActionListener(this);
		
		JButton btnAdmin = new JButton("ADMIN");
		btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAdmin.setBounds(109, 233, 85, 21);
		getContentPane().add(btnAdmin);
		
		JButton btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		getContentPane().add(btnCompra);
			
		// Crear la tabla antes de usarla en JScrollPane
		tableArticulo = new JTable();

		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana

		// Crear un modelo de tabla vacío
		DefaultTableModel model = new DefaultTableModel(); 
        model.addColumn("");
        model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
	    model.addColumn("Cantidad");

		// Obtener los artículos del DAO
		Dao dao = new DaoImplementMySQL();
		Map<Integer, Articulo> articulos = dao.obtenerTodosArticulos();

		// Agregar los datos de los artículos al modelo de la tabla
		for (Articulo art : articulos.values()) {
		    model.addRow(new Object[]{
		    	false,
		        art.getNombre(),
		        art.getDescripcion(),
		        art.getPrecio(),
		        art.getOferta(),
		        art.getStock(),
		        0
		    });
		}

		// Establecer el modelo de la tabla con los datos
		tableArticulo.setModel(model);
		
/*
		   // Configurar la columna "Seleccionar" para mostrar checkboxes
        TableColumn selectColumn = tableArticulo.getColumnModel().getColumn(0);
        JCheckBox checkBox = new JCheckBox();
        selectColumn.setCellEditor(new DefaultCellEditor(checkBox));

        // Configurar la columna "Cantidad" para ser editable con un JTextField
        TableColumn quantityColumn = tableArticulo.getColumnModel().getColumn(6);
        quantityColumn.setCellEditor(new DefaultCellEditor(new JTextField()));
    }*/
		

		// Configurar la columna "Seleccionar" para mostrar checkboxes
		TableColumn selectColumn = tableArticulo.getColumnModel().getColumn(0);

		// Crear un JCheckBox como editor de la celda (para poder marcar/desmarcar)
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);  // Centrar el checkbox
		selectColumn.setCellEditor(new DefaultCellEditor(checkBox));

		// Configurar el renderizador para que se muestre un tick (✔) cuando está marcado
		selectColumn.setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
		    @Override
		    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        JCheckBox checkBox = new JCheckBox();
		        checkBox.setSelected(value != null && (Boolean) value); // Marca el checkbox si el valor es true
		        return checkBox;
		    }
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btnUsuario)) {
			
	        VistaUsuario vistaUsuario = new VistaUsuario(this.cambio, this, true); // "this" es el JFrame principal, "true" para modal
	        vistaUsuario.setVisible(true);
	        
	        this.dispose(); 
		}
	}
}
