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
import java.util.HashMap;
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
	private JButton btnUsuario, btnCompra, btnAdmin;
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
		
		 btnAdmin = new JButton("ADMIN");
		btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAdmin.setBounds(109, 233, 85, 21);
		getContentPane().add(btnAdmin);
		
		 btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		getContentPane().add(btnCompra);
		btnCompra.addActionListener(this);
			
		// Crear la tabla antes de usarla en JScrollPane
		tableArticulo = new JTable();

		// Crear el JScrollPane con la tabla correctamente inicializada
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBounds(51, 88, 327, 85); // Ubicación y tamaño del JScrollPane
		getContentPane().add(scrollPane); // Agregar el JScrollPane a la ventana
		
		
		DefaultTableModel model = new DefaultTableModel(new Object[]{"Seleccionar", "Nombre", "Descripción", "Precio", "Oferta", "Stock", "Cantidad"}, 0) {
		    @Override
		    public Class<?> getColumnClass(int columnIndex) {
		        if (columnIndex == 0) { // La primera columna es de checkboxes
		            return Boolean.class;
		        } else if (columnIndex == 3 || columnIndex == 4) { // Precio y Oferta deben ser Float
		            return Float.class;
		        } else if (columnIndex == 5 || columnIndex == 6) { // Stock y Cantidad deben ser Integer
		            return Integer.class;
		        }
		        return String.class; // Las demás son texto
		    }

		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 0 || column == 6; // Solo permitir editar el checkbox y la cantidad
		    }
		};
		/*// Crear un modelo de tabla vacío
		DefaultTableModel model = new DefaultTableModel(); 
        model.addColumn("");
        model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
	    model.addColumn("Cantidad");*/

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

        // Configurar la columna "Seleccionar" con checkboxes
        TableColumn selectColumn = tableArticulo.getColumnModel().getColumn(0);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        selectColumn.setCellEditor(new DefaultCellEditor(checkBox));
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
		//TableColumn selectColumn = tableArticulo.getColumnModel().getColumn(0);

		/*// Crear un JCheckBox como editor de la celda (para poder marcar/desmarcar)
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);  // Centrar el checkbox
		selectColumn.setCellEditor(new DefaultCellEditor(checkBox));
*/
		// Configurar el renderizador para que se muestre un tick (✔) cuando está marcado
		/*selectColumn.setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
		    @Override
		    public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        JCheckBox checkBox = new JCheckBox();
		        checkBox.setSelected(value != null && (Boolean) value); // Marca el checkbox si el valor es true
		        return checkBox;
		    }
		});*/
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	    if (e.getSource().equals(btnUsuario)) {
            VistaUsuario vistaUsuario = new VistaUsuario(this.cambio, this, true);
            vistaUsuario.setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnCompra)) {
            abrirCarrito();
        }
	}
	
	  private void abrirCarrito() {
	        Map<Integer, Articulo> seleccionados = obtenerArticulosSeleccionados();
	        VistaCarrito carrito = new VistaCarrito(this, true, seleccionados);
	        carrito.setVisible(true);
	    }
	
	
	  private Map<Integer, Articulo> obtenerArticulosSeleccionados() {
		    Map<Integer, Articulo> seleccionados = new HashMap<>();
		    DefaultTableModel model = (DefaultTableModel) tableArticulo.getModel();

		    for (int i = 0; i < model.getRowCount(); i++) {
		        Boolean seleccionado = (Boolean) model.getValueAt(i, 0);  // Columna de checkbox
		        if (seleccionado != null && seleccionado) {
		            // Obtener los valores de las celdas correspondientes
		            String nombre = (String) model.getValueAt(i, 1);  // Nombre
		            float precio = (Float) model.getValueAt(i, 3);  // Precio
		            int cantidad = (Integer) model.getValueAt(i, 6);  // Cantidad seleccionada

		            if (cantidad > 0) {
		                Articulo articulo = new Articulo(0, nombre, "", cantidad, precio, 0, null);  // No es necesario pasar el ID
		                articulo.setStock(cantidad);  // Establecer la cantidad seleccionada
		                seleccionados.put(seleccionados.size() + 1, articulo);  // Usamos el tamaño como ID
		            }
		        }
		    }
		    return seleccionados;
		}
}
