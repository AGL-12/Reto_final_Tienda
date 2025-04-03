package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.Principal;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;

public class VerPedidosCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablePedidos;
	private DefaultTableModel model;
    private JTabbedPane tabbedPane; // Contenedor de pestañas


	/**
	 * Create the dialog.
	 */
	public VerPedidosCliente(JDialog padre, Cliente clien) {
		setBounds(100, 100, 450, 300);
		
		setModal(true);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

  
        JPanel panelPedidos = new JPanel(new BorderLayout());
		model = new DefaultTableModel();
		model.addColumn("Id del pedido");
		model.addColumn("Precio Total");
		model.addColumn("Fecha de compra");

		
		List<Pedido> pedidos = Principal.obtenerPedidosCliente(clien.getId_usu());

		for (Pedido ped : pedidos) {
			model.addRow(new Object[] { ped.getId_ped(), ped.getTotal(), ped.getFecha_compra() });
		}

	
		tablePedidos = new JTable(model);
		tablePedidos.setDefaultEditor(Object.class, null);
		
	 
		tablePedidos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) { // Detecta doble clic
		            int selectedRow = tablePedidos.getSelectedRow();
		            if (selectedRow != -1) {
		                int idPedido = (Integer) model.getValueAt(selectedRow, 0);
		         
		                agregarPestañaArticulos(idPedido);
		            }
		        }
		    }
		});
	      JScrollPane scrollPane = new JScrollPane(tablePedidos);
	        panelPedidos.add(scrollPane, BorderLayout.CENTER);

	        tabbedPane.addTab("Pedidos", panelPedidos);
	}
	
	
	
	  private void agregarPestañaArticulos(int idPedido) {
	        JPanel panelArticulos = new JPanel(new BorderLayout());
	        
	        DefaultTableModel modelDetalle = new DefaultTableModel();
	      //  modelDetalle.addColumn("ID Pedido");
	        modelDetalle.addColumn("Nombre Artículo");
	        modelDetalle.addColumn("Cantidad");
	        modelDetalle.addColumn("Precio Unitario (€)"); 
	        modelDetalle.addColumn("Descuento (%)");     
	        modelDetalle.addColumn("Precio Total (€)");
	     

	        
	        List<Articulo> articulos = Principal.obtenerArticulosPorPedido(idPedido);


	        for (Articulo art : articulos) {
	         
	            int cantidad = Principal.obtenerCantidadArticuloEnPedido(idPedido, art.getId_art()); 

	          
	            float precioOriginal = art.getPrecio();
	            float descuento = art.getOferta(); 
	      
	            float precioConDescuento = precioOriginal * (1 - (descuento / 100.0f));  
	           
	      
	            float precioTotal = precioConDescuento * cantidad;
	          

	            // --- MODIFICACIÓN 1: Formatear el precio total del artículo ---
	            String precioTotalFormateado = String.format("%.2f", precioTotal);
	            String precioUnitarioFormateado = String.format("%.2f", precioConDescuento);
	            String descuentoFormateado = String.format("%.2f", descuento); 

	            modelDetalle.addRow(new Object[]{
	                    art.getNombre(),
	                    cantidad,
	                    precioUnitarioFormateado, // Añadido
	                    descuentoFormateado,      // Añadido
	                    precioTotalFormateado     // Usar la cadena formateada
	            });
	        }


	        JTable tableDetalle = new JTable(modelDetalle);
	        tableDetalle.setDefaultEditor(Object.class, null); // Evita la edición de la tabla
	        JScrollPane scrollPaneDetalle = new JScrollPane(tableDetalle);
	        panelArticulos.add(scrollPaneDetalle, BorderLayout.CENTER);

	        tabbedPane.addTab("Pedido " + idPedido, panelArticulos);

	 
	        tabbedPane.setSelectedComponent(panelArticulos);
	        

	        // --- MODIFICACIÓN 2: Añadir pestaña con botón de cierre ---
	        String tituloPestana = "Pedido " + idPedido;
	        tabbedPane.addTab(tituloPestana, panelArticulos);
	        int index = tabbedPane.indexOfComponent(panelArticulos); // Obtener índice de la nueva pestaña

	        // Crear el componente de cabecera de la pestaña (Panel con Título y Botón 'X')
	        JPanel tabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	        tabComponent.setOpaque(false); // Hacerlo transparente
	        JLabel titleLabel = new JLabel(tituloPestana);
	        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5)); // Espacio entre título y botón

	        JButton closeButton = new JButton("x");
	        // Estilo básico para el botón de cierre
	        closeButton.setMargin(new Insets(0, 1, 0, 1)); // Margen interior pequeño
	        closeButton.setVerticalAlignment(SwingConstants.CENTER); // Alinear verticalmente
	        closeButton.setContentAreaFilled(false); // Sin relleno de fondo
	        closeButton.setBorderPainted(false); // Sin borde visible (opcional)
	        closeButton.setFocusPainted(false); // Sin borde de foco al hacer clic

	        // Acción para cerrar la pestaña
	        closeButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Eliminar la pestaña asociada a este botón
	                int tabIndex = tabbedPane.indexOfTabComponent(tabComponent);
	                if (tabIndex != -1) {
	                    tabbedPane.remove(tabIndex);
	                }
	                // O alternativamente, si sabes que panelArticulos es único por pestaña:
	                // tabbedPane.remove(panelArticulos);
	            }
	        });

	        tabComponent.add(titleLabel);
	        tabComponent.add(closeButton);

	        // Asignar el componente personalizado a la cabecera de la pestaña
	        tabbedPane.setTabComponentAt(index, tabComponent);
	        // ----------------------------------------------------------

	        // Seleccionar la nueva pestaña añadida
	        tabbedPane.setSelectedComponent(panelArticulos);
	    }
	    
}
