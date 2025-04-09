package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import controlador.Principal;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;

public class VerPedidosCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablePedidos;
	private DefaultTableModel modelPedidos;
	private JTabbedPane tabbedPane;

	// Renderers para alineación
	private DefaultTableCellRenderer rightRenderer;
	private DefaultTableCellRenderer centerRenderer; // Nuevo para centrar

	/**
	 * Create the dialog.
	 */
	public VerPedidosCliente(JDialog padre, Cliente clien) {
		// --- Título y Modalidad ---
		// Usa super() para establecer título y modalidad correctamente.
		// Usa getUsuario() como en el main de ejemplo, o getNombre() si prefieres.
		super(padre, "Pedidos de " + (clien != null ? clien.getUsuario() : "Cliente Desconocido"), true);

		// --- Renderers ---
		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		centerRenderer = new DefaultTableCellRenderer(); // Nuevo
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Nuevo

		// --- Padding general y Layout ---
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// --- Tamaño preferido inicial ---
		setPreferredSize(new Dimension(650, 450)); // Sugerir un tamaño inicial

		// --- TabbedPane ---
		tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);

		// --- Panel y Tabla de Pedidos ---
		JPanel panelPedidos = new JPanel(new BorderLayout());
		panelPedidos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// --- Modelo Tabla Pedidos ---
		modelPedidos = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			// Evitar que las celdas sean editables (alternativa a setDefaultEditor)
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelPedidos.addColumn("Id Pedido");
		modelPedidos.addColumn("Precio Total (€)");
		modelPedidos.addColumn("Fecha de Compra");

		// --- Carga Datos Pedidos ---
		List<Pedido> pedidos = Principal.obtenerPedidosCliente(clien.getId_usu());
		if (clien != null && pedidos != null) {
			for (Pedido ped : pedidos) {
				String precioTotalFormateado = String.format("%.2f", ped.getTotal());
				modelPedidos.addRow(new Object[] { ped.getId_ped(), precioTotalFormateado, ped.getFecha_compra() });
			}
		}
		// --- Creación Tabla Pedidos con Mejoras ---
		tablePedidos = new JTable(modelPedidos) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					Color alternateColor = UIManager.getColor("Table.alternateRowColor");
					Color defaultColor = UIManager.getColor("Table.background");
					if (alternateColor == null)
						alternateColor = new Color(240, 245, 250); // Fallback suave
					if (defaultColor == null)
						defaultColor = Color.WHITE;
					c.setBackground(row % 2 == 0 ? defaultColor : alternateColor);
				}
				// Añadir un pequeño padding vertical a las celdas si se desea
				if (renderer instanceof JLabel) {
					((JLabel) renderer).setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // top, left, bottom,
																								// right
				}

				return c;
			}
		};
		tablePedidos.setFillsViewportHeight(true);
		tablePedidos.setRowHeight(tablePedidos.getRowHeight() + 4); // Aumentar ligeramente altura de fila para padding

		// --- Estilo Cabecera ---
		tablePedidos.getTableHeader().setFont(tablePedidos.getTableHeader().getFont().deriveFont(Font.BOLD));

		// --- Alineación Columnas Pedidos ---
		tablePedidos.getColumnModel().getColumn(0).setMinWidth(80); // Id Pedido un poco más ancho
		// *** Cambio: Usar centerRenderer para Precio Total ***
		tablePedidos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tablePedidos.getColumnModel().getColumn(2).setMinWidth(120); // Fecha compra

		// --- Estilo Rejilla y ScrollPane ---
		tablePedidos.setShowGrid(false);
		tablePedidos.setIntercellSpacing(new Dimension(0, 0)); // Sin espacio entre celdas

		JScrollPane scrollPane = new JScrollPane(tablePedidos);
		// *** Mejora: Usar borde estándar del LaF para el scroll pane ***
		scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));

		panelPedidos.add(scrollPane, BorderLayout.CENTER);

		// --- Listener Doble Click ---
		tablePedidos.addMouseListener(new MouseAdapter() {
			private int selectedRow;
			private Object idObj;
			private int idPedido;
			private boolean pestanaExiste;
			private String tituloBuscado;

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2) { // Detecta doble clic
					selectedRow = tablePedidos.getSelectedRow();
					if (selectedRow != -1) {
						idObj = modelPedidos.getValueAt(selectedRow, 0);
						if (idObj instanceof Integer) {
							idPedido = (Integer) idObj;
							pestanaExiste = false;
							tituloBuscado = "Pedido " + idPedido;
							for (int i = 0; i < tabbedPane.getTabCount(); i++) {
								Component tabComp = tabbedPane.getTabComponentAt(i);
								String tituloActual = tabbedPane.getTitleAt(i);
								if (tabComp instanceof JPanel) {
									for (Component child : ((JPanel) tabComp).getComponents()) {
										if (child instanceof JLabel) {
											tituloActual = ((JLabel) child).getText();
											break;
										}
									}
								}
								if (tituloBuscado.equals(tituloActual)) {
									pestanaExiste = true;
									tabbedPane.setSelectedIndex(i);
									break;
								}
							}
							if (!pestanaExiste) {
								agregarPestañaArticulos(idPedido);
							}
						} else {
							System.err.println("ID del pedido no es un entero en la fila seleccionada: " + idObj);
						}
					}
				}
			}
		});

		// --- Añadir Pestaña Pedidos ---
		tabbedPane.addTab("Pedidos", panelPedidos);

		// *** Mejora: Ajustar anchos de columna DESPUÉS de añadir datos y al scrollpane
		// ***
		adjustColumnWidths(tablePedidos);

		// --- Configuración Final Diálogo ---
		pack(); // Ajusta tamaño basado en preferredSize y contenido
		setLocationRelativeTo(padre); // Centrar
	}

	/**
	 * Crea y añade una nueva pestaña mostrando los artículos de un pedido
	 * específico.
	 * 
	 * @param idPedido El ID del pedido a mostrar.
	 */
	private void agregarPestañaArticulos(int idPedido) {
		JPanel panelArticulos = new JPanel(new BorderLayout());
		panelArticulos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// --- Modelo Tabla Detalles ---
		DefaultTableModel modelDetalle = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelDetalle.addColumn("Artículo");
		modelDetalle.addColumn("Cant."); // Abreviado
		modelDetalle.addColumn("P. Unit. (€)"); // Abreviado
		modelDetalle.addColumn("Dto. (%)"); // Abreviado
		modelDetalle.addColumn("P. Total (€)"); // Abreviado

//		List<Compra> listaCompraPediso = Principal.obtenerCompraDePedido();
		// --- Carga Datos Artículos ---
		List<Articulo> articulos = Principal.obtenerArticulosPorPedido(idPedido);
		if (articulos != null) {
			for (Articulo art : articulos) {
				int cantidad = Principal.obtenerCantidadArticuloEnPedido(idPedido, art.getId_art());
				float precioOriginal = art.getPrecio();
				float descuento = art.getOferta();
				float precioConDescuento = precioOriginal * (1 - (descuento / 100.0f));
				float precioTotalArticulo = precioConDescuento * cantidad;

				String precioUnitarioFormateado = String.format("%.2f", precioConDescuento);
				String descuentoFormateado = String.format("%.0f", descuento);
				String precioTotalFormateado = String.format("%.2f", precioTotalArticulo);

				modelDetalle.addRow(new Object[] { art.getNombre(), cantidad, precioUnitarioFormateado,
						descuentoFormateado, precioTotalFormateado });
			}
		} else {
			System.err.println("Lista de artículos nula para el pedido " + idPedido);
		}

		// --- Creación Tabla Detalles con Mejoras ---
		JTable tableDetalle = new JTable(modelDetalle) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					Color alternateColor = UIManager.getColor("Table.alternateRowColor");
					Color defaultColor = UIManager.getColor("Table.background");
					if (alternateColor == null)
						alternateColor = new Color(240, 245, 250);
					if (defaultColor == null)
						defaultColor = Color.WHITE;
					c.setBackground(row % 2 == 0 ? defaultColor : alternateColor);
				}
				// Padding
				if (renderer instanceof JLabel) {
					((JLabel) renderer).setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
				}
				return c;
			}
		};
		// tableDetalle.setDefaultEditor(Object.class, null); // No necesario
		tableDetalle.setFillsViewportHeight(true);
		tableDetalle.setRowHeight(tableDetalle.getRowHeight() + 4); // Aumentar altura fila

		// --- Estilo Cabecera ---
		tableDetalle.getTableHeader().setFont(tableDetalle.getTableHeader().getFont().deriveFont(Font.BOLD));

		// --- Alineación Columnas Detalles ---
		// Columna 0 (Artículo) se queda a la izquierda por defecto.
		tableDetalle.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // Cant.
		tableDetalle.getColumnModel().getColumn(2).setCellRenderer(rightRenderer); // P. Unit.
		tableDetalle.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Dto.
		tableDetalle.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // P. Total

		// --- Estilo Rejilla y ScrollPane ---
		tableDetalle.setShowGrid(false);
		tableDetalle.setIntercellSpacing(new Dimension(0, 0));

		JScrollPane scrollPaneDetalle = new JScrollPane(tableDetalle);
		// *** Mejora: Usar borde estándar del LaF ***
		scrollPaneDetalle.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));

		panelArticulos.add(scrollPaneDetalle, BorderLayout.CENTER);

		// --- Añadir Pestaña con Cabecera Personalizada ---
		String tituloPestana = "Pedido " + idPedido;
		tabbedPane.addTab(tituloPestana, panelArticulos);
		int index = tabbedPane.indexOfComponent(panelArticulos);

		JPanel tabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		tabComponent.setOpaque(false);
		JLabel titleLabel = new JLabel(tituloPestana);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		// --- Botón de Cierre Mejorado ---
		ImageIcon closeIcon = null;
		try {
			// ¡¡¡REEMPLAZA CON TU RUTA AL ICONO!!!
			java.net.URL imgURL = getClass().getResource("iconos/icono_cerrar_16.png");
			if (imgURL != null) {
				closeIcon = new ImageIcon(imgURL);
			} else {
				System.err.println("Icono de cierre no encontrado.");
			}
		} catch (Exception e) {
			System.err.println("Error cargando icono: " + e.getMessage());
		}

		JButton closeButton = (closeIcon != null) ? new JButton(closeIcon) : new JButton("x");
		// Estilos botón cierre... (igual que antes)
		closeButton.setToolTipText("Cerrar esta pestaña");
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setVerticalAlignment(SwingConstants.CENTER);
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setOpaque(false);
		Color hoverColor = UIManager.getColor("TabbedPane.hoverColor");
		if (hoverColor == null)
			hoverColor = new Color(210, 210, 210);
		Color finalHoverColor = hoverColor;
		closeButton.addActionListener(e -> {
			int tabIndex = tabbedPane.indexOfTabComponent(tabComponent);
			if (tabIndex != -1) {
				tabbedPane.remove(tabIndex);
			}
		});
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setContentAreaFilled(true);
				closeButton.setOpaque(true);
				closeButton.setBackground(finalHoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setContentAreaFilled(false);
				closeButton.setOpaque(false);
			}
		});

		tabComponent.add(titleLabel);
		tabComponent.add(closeButton);
		tabbedPane.setTabComponentAt(index, tabComponent);

		// *** Mejora: Ajustar anchos de columna DESPUÉS de añadir datos y al scrollpane
		// ***
		adjustColumnWidths(tableDetalle);

		// Seleccionar la nueva pestaña
		tabbedPane.setSelectedComponent(panelArticulos);
	}

	/**
	 * Ajusta el ancho preferido de cada columna de la tabla para que se ajuste al
	 * contenido más ancho de la columna (celdas y cabecera). Llamar DESPUÉS de que
	 * la tabla tenga datos y esté en un JScrollPane.
	 * 
	 * @param table La tabla a ajustar.
	 */
	private void adjustColumnWidths(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		// Padding extra añadido al ancho calculado
		final int PADDING = 15;

		for (int column = 0; column < table.getColumnCount(); column++) {
			// Ancho de la cabecera
			TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
			Component headerComp = headerRenderer.getTableCellRendererComponent(table,
					columnModel.getColumn(column).getHeaderValue(), false, false, 0, column);
			int headerWidth = headerComp.getPreferredSize().width;

			// Ancho máximo de las celdas en esta columna
			int maxWidth = headerWidth;
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
				Component cellComp = table.prepareRenderer(cellRenderer, row, column);
				int cellWidth = cellComp.getPreferredSize().width;
				maxWidth = Math.max(maxWidth, cellWidth);
			}

			// Establecer el ancho preferido de la columna
			TableColumn tableColumn = columnModel.getColumn(column);
			tableColumn.setPreferredWidth(maxWidth + PADDING);
		}
		// Opcional: Forzar un redibujo si es necesario (normalmente no lo es)
		// table.getTableHeader().resizeAndRepaint();
		// table.revalidate();
		// table.repaint();
	}

}