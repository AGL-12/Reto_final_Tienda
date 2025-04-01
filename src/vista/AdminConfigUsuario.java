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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class AdminConfigUsuario extends JDialog implements ActionListener{
	//JoptionPane Showconfig dialog mirar usos
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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				//cada vez que se vuelva a esta ventana, se realizara un refresh a la tabla para que se puedan ver los cambios
				if(listaClienteTod!=null) {
					System.out.println("Refrescando tabla");
					refrescarTabla();
				}
			}


		});
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
	
		

		JLabel lblTitulo = new JLabel("SELECT USER");
		lblTitulo.setBounds(134, 10, 115, 25);
		getContentPane().add(lblTitulo);
		
		tableListaUsuarios = new JTable();
		
		
		 btnSeleccionarUsuario = new JButton("SELECT USER");
		btnSeleccionarUsuario.setBounds(124, 209, 148, 33);
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

	    // Definir las columnas de la tabla
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
		                localClien = listaClienteTod.get(idCliente); 

		               
		                if (localClien != null) {
		                    VistaUsuario vista = new VistaUsuario(localClien, this);
		                    vista.setVisible(true);
		                }
		                break; 
		            }
		        
		    }
		    }
	}
	

	
	private void refrescarTabla() {
	  
	    model.setRowCount(0);
	    
	 // Llamamos al DAO para obtener los vehículos del propietario
	 		listaClienteTod = Principal.listarCliente();
	
	    for (Cliente cliens : listaClienteTod.values()) {
	        model.addRow(new Object[] { 
	            false, 
	            cliens.getId_usu(), 
	            cliens.getUsuario(), 
	            cliens.getDni(),
	            cliens.getCorreo(), 
	            cliens.getDireccion(), 
	            cliens.getListaCompra().size() 
	        });
	    }
	
	}
}
