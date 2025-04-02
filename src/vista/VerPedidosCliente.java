package vista;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
		                System.out.println("Doble clic en el pedido con ID: " + idPedido); // Agregar esta línea
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
	        modelDetalle.addColumn("ID Pedido");
	        modelDetalle.addColumn("Nombre Artículo");
	        modelDetalle.addColumn("Cantidad");
	        modelDetalle.addColumn("Precio Total");

	        
	        List<Articulo> articulos = Principal.obtenerArticulosPorPedido(idPedido);


	        for (Articulo art : articulos) {
	         
	            int cantidad = Principal.obtenerCantidadArticuloEnPedido(idPedido, art.getId_art()); 

	          
	            float precioOriginal = art.getPrecio();
	            float descuento = art.getOferta(); 
	      
	            float precioConDescuento = precioOriginal * (1 - (descuento / 100.0f));   // Si no tienes descuento, será igual al precioOriginal
	            System.out.println(descuento);
	      
	            float precioTotal = precioConDescuento * cantidad;
	          
	            modelDetalle.addRow(new Object[] { idPedido, art.getNombre(), cantidad, precioTotal });
	        }

	        JTable tableDetalle = new JTable(modelDetalle);
	        JScrollPane scrollPaneDetalle = new JScrollPane(tableDetalle);
	        panelArticulos.add(scrollPaneDetalle, BorderLayout.CENTER);

	        tabbedPane.addTab("Pedido " + idPedido, panelArticulos);

	 
	        tabbedPane.setSelectedComponent(panelArticulos);
	    }
}
