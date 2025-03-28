package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controlador.Principal;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;
	private JButton btnUsuario, btnCompra, btnAdmin;
	private Cliente cambio;
	private Cliente localClien;
	private DefaultTableModel model;

	/**
	 * Create the dialog.
	 */
	public VistaTienda(Cliente clien, JFrame vista) {
		super(vista, "Bienvendido", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		this.localClien = clien;

		JLabel lblTitulo = new JLabel("DYE TOOLS");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(157, 10, 106, 38);
		getContentPane().add(lblTitulo);

		btnUsuario = new JButton("USER");
		btnUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUsuario.setBounds(10, 232, 85, 21);
		getContentPane().add(btnUsuario);
		btnUsuario.addActionListener(this);
		
		 btnAdmin = new JButton("ADMIN");

		btnAdmin = new JButton("ADMIN");
		btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAdmin.setBounds(109, 233, 85, 21);
		btnAdmin.addActionListener(this);
		btnAdmin.setVisible(false);
		getContentPane().add(btnAdmin);
		
		 btnCompra = new JButton("BUY");

		btnCompra = new JButton("BUY");
		btnCompra.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCompra.setBounds(341, 233, 85, 21);
		getContentPane().add(btnCompra);
		btnCompra.addActionListener(this);
			

		// Crear la tabla antes de usarla en JScrollPane
		tableArticulo = new JTable();

		if (clien.isEsAdmin()) {
			btnAdmin.setVisible(true);
		}
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
=======
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
		model.addColumn("Seleccionar");
		model.addColumn("id_art");
		model.addColumn("Nombre");
>>>>>>> refs/heads/develop
		model.addColumn("Descripción");
		model.addColumn("Precio");
		model.addColumn("Oferta");
		model.addColumn("Stock");
<<<<<<< HEAD
	    model.addColumn("Cantidad");*/
		model.addColumn("Cantidad");

		// Obtener los artículos del DAO
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		// Agregar los datos de los artículos al modelo de la tabla
		for (Articulo art : articulos.values()) {
			if (art.getStock()!=0) {
				model.addRow(new Object[] { false, art.getId_art(), art.getNombre(), art.getDescripcion(), art.getPrecio(),
						art.getOferta(), art.getStock(), 0 });
			}
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
		// 🔹 OCULTAR LA COLUMNA ID_ART
		tableArticulo.removeColumn(tableArticulo.getColumnModel().getColumn(1));
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource().equals(btnUsuario)) {
            VistaUsuario vistaUsuario = new VistaUsuario(this.cambio, this);
            vistaUsuario.setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnCompra)) {
            abrirCarrito();
        }
		if (e.getSource().equals(btnUsuario)) {

			this.setVisible(false);

			VistaUsuario vistaUsuario = new VistaUsuario(this.localClien, this); // "this" es el JFrame principal,
			vistaUsuario.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnAdmin)) {

			this.setVisible(false);

			VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
			menuAdmin.setVisible(true);

			this.setVisible(true);
		} else if (e.getSource().equals(btnCompra)) {

			Pedido preSetCompra = new Pedido(Principal.obtenerUltimoIdPed() + 1, localClien.getId_usu(), 0,
					LocalDateTime.now());

			this.setVisible(false);

			VistaCarrito carritoNoCompra = new VistaCarrito(this, cargaPedCom(preSetCompra), preSetCompra);
			carritoNoCompra.setVisible(true);

			this.setVisible(true);
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
		            float oferta = (Float) model.getValueAt(i, 4); //Oferta
		            int cantidad = (Integer) model.getValueAt(i, 6);  // Cantidad seleccionada

		            if (cantidad > 0) {
		                Articulo articulo = new Articulo(0, nombre, "", cantidad, precio,oferta, null);  // No es necesario pasar el ID
		                articulo.setStock(cantidad);  // Establecer la cantidad seleccionada
		                seleccionados.put(seleccionados.size() + 1, articulo);  // Usamos el tamaño como ID
		            }
		        }
		    }
		    return seleccionados;
		}

	private List<Compra> cargaPedCom(Pedido preSetCompra) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			boolean isChecked = (Boolean) model.getValueAt(i, 0);
			if (isChecked && (Integer) model.getValueAt(i, 7) != 0) {
				Compra palCarro = new Compra((Integer) model.getValueAt(i, 2), preSetCompra.getId_usu(), i);
				listaCompra.add(palCarro);
			}
		}
		return listaCompra;
	}

}
