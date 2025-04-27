package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics; // Necesario para pintar botón redondeado (opcional)
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import controlador.Dao;
import controlador.Principal;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;
import javax.swing.JTextField;

public class VerPedidosCliente extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable tablePedidos;
	private DefaultTableModel modelPedidos;
	private JTabbedPane tabbedPane;
	private BufferedImage backgroundImage;
	private float totalGastado;

	// Constantes que usaremos para el estilo
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final int PADDING_TABLA_CELDA_V = 5;
	private static final int PADDING_TABLA_CELDA_H = 10;
	private static final Color COLOR_FONDO_VIEWPORT = new Color(245, 222, 179);
	private static final Color COLOR_BORDE_SCROLLPANE = new Color(101, 67, 33);
	private static final Color COLOR_CELDA_FONDO = new Color(245, 245, 220, 180);
	private static final Color COLOR_SELECCION_FUENTE = Color.blue;

	// Constantes que usaremos para la cabecera
	private static final Color HEADER_BACKGROUND = new Color(210, 180, 140);
	private static final Color HEADER_FOREGROUND = new Color(101, 67, 33);
	private static final Color HEADER_BORDER_COLOR = new Color(139, 69, 19);
	private static final Font HEADER_FONT = FONT_TABLA_HEADER;
	private static final int HEADER_VPADDING = PADDING_TABLA_CELDA_V / 2;
	private static final int HEADER_HPADDING = PADDING_TABLA_CELDA_H;

	// Para las pestañas
	private static final Font FONT_PESTANA_TITULO = new Font("Segoe UI", Font.PLAIN, 12); // Fuente para el título de la
																							// pestaña
	private static final Color COLOR_BOTON_CERRAR_HOVER = new Color(255, 99, 71, 200); // Rojo tomate semi-transparente
																						// para hover
	private static final Color COLOR_BOTON_CERRAR_NORMAL = new Color(160, 160, 160); // Gris para la 'x'
	private JTextField txtTotalGastado;

	/**
	 * Create the dialog.
	 */
	public VerPedidosCliente(JDialog padre, Cliente clien) {
		super(padre, "Pedidos de " + (clien != null ? clien.getUsuario() : "Cliente Desconocido"), true);
		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().setLayout(new BorderLayout());
		setPreferredSize(new Dimension(700, 500));

		try {
			backgroundImage = ImageIO.read(getClass().getResource("/imagenes/fondoMadera.jpg"));
		} catch (IOException e) {
			e.getMessage();
			getContentPane().setBackground(new Color(240, 240, 240));
		}

		// Cambiamos el color al panel
		setContentPane(new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		});

		tabbedPane = new JTabbedPane();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		// Panel y la tabla de pedidos
		JPanel panelPedidos = new JPanel(new BorderLayout());
		panelPedidos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelPedidos.setOpaque(false);

		// Modelo de la tabla de los pedidos
		modelPedidos = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int i) {
				switch (i) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return Object.class;
				default:
					return Object.class;
				}
			}
		};
		modelPedidos.addColumn("Id Pedido");
		modelPedidos.addColumn("Precio Total (€)");
		modelPedidos.addColumn("Fecha de Compra");

		// Carga datos de los pedidos
		if (clien != null) {
			List<Pedido> pedidos = Principal.obtenerPedidosCliente(clien.getId_usu());
			if (pedidos != null && !pedidos.isEmpty()) {
				for (Pedido ped : pedidos) {
					String precioTotalFormateado = String.format(java.util.Locale.US, "%.2f", ped.getTotal());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					String fechaFormateada = ped.getFecha_compra().format(formatter);
					modelPedidos.addRow(new Object[] { ped.getId_ped(), precioTotalFormateado + "€", fechaFormateada });
				}
			} else {
				modelPedidos.addRow(new Object[] { null, "No hay pedidos", null });
			}
		} else {
			modelPedidos.addRow(new Object[] { null, "Cliente no válido", null });
		}

		// Crear la tabla
		tablePedidos = new JTable(modelPedidos) {
			private static final long serialVersionUID = 1L;
			
			/**
			 * Configura el renderizador para cada celda de la tabla de pedidos.
			 * Aplica estilos como opacidad, color de fondo/fuente, bordes y alineación.
			 */
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				c.setBackground(COLOR_CELDA_FONDO);
				if (!isRowSelected(row)) {
					c.setForeground(Color.BLACK);
				} else {
					c.setForeground(COLOR_SELECCION_FUENTE);
				}
				if (c instanceof JLabel) {
					JLabel label = (JLabel) c;
					label.setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H,
							PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
					if (column == 1) {
						label.setHorizontalAlignment(SwingConstants.RIGHT);
					} else if (column == 0 || column == 2) {
						label.setHorizontalAlignment(SwingConstants.CENTER);
					} else {
						label.setHorizontalAlignment(SwingConstants.LEFT);
					}
				}
				return c;
			}
		};
		// Aplicar estilos generales
		tablePedidos.setFont(FONT_TABLA_CELDA);
		tablePedidos.setRowHeight(tablePedidos.getRowHeight() + 10);
		tablePedidos.setShowGrid(false);
		tablePedidos.setShowHorizontalLines(true);
		tablePedidos.setShowVerticalLines(false);
		tablePedidos.setIntercellSpacing(new Dimension(0, 1));
		tablePedidos.setAutoCreateRowSorter(true);
		tablePedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePedidos.setOpaque(false);
		tablePedidos.setFillsViewportHeight(true);
		// Estilo de cabecera
		applyCustomHeaderRenderer(tablePedidos);
		// --- ScrollPane --- (Sin cambios)
		JScrollPane scrollPane = new JScrollPane(tablePedidos);
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(COLOR_FONDO_VIEWPORT);
		scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SCROLLPANE, 2));
		panelPedidos.add(scrollPane, BorderLayout.CENTER);
		tablePedidos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int selectedRow = tablePedidos.getSelectedRow();
					if (selectedRow != -1) {
						int modelRow = tablePedidos.convertRowIndexToModel(selectedRow);
						Object idObj = modelPedidos.getValueAt(modelRow, 0);
						if (idObj instanceof Integer) {
							int idPedido = (Integer) idObj;
							boolean pestanaExiste = false;
							String tituloBuscado = "Pedido " + idPedido;
							for (int i = 0; i < tabbedPane.getTabCount(); i++) {
								Component tabComp = tabbedPane.getTabComponentAt(i);
								String tituloActual = "";
								if (tabComp instanceof JPanel) {
									for (Component child : ((JPanel) tabComp).getComponents()) {
										if (child instanceof JLabel) {
											tituloActual = ((JLabel) child).getText();
											break;
										}
									}
								}
								if (tituloActual.isEmpty()) {
									tituloActual = tabbedPane.getTitleAt(i);
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
						}
					}
				}
			}
		});

		// Para la pestaña de los pedidos
		tabbedPane.addTab("Mis Pedidos", panelPedidos);
		adjustColumnWidths(tablePedidos);
		
		txtTotalGastado = new JTextField();
		panelPedidos.add(txtTotalGastado, BorderLayout.SOUTH);
		txtTotalGastado.setColumns(10);
		pack();
		setLocationRelativeTo(padre);
		totalGastado=actualizarTotalGastado(clien);
		txtTotalGastado.setText("Total Gastado: "+totalGastado);
	}
	/**
	 *Consigue el total gastado por el cliente al consultar el pedido 
	 */

	private float actualizarTotalGastado(Cliente clien) {
		float resultado=Principal.totalGastado(clien);
		resultado = Math.round(resultado * 100f) / 100f;
		return resultado;
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
		panelArticulos.setOpaque(false);

		// Modelo de la tabla
		DefaultTableModel modelDetalle = new DefaultTableModel() { /* ... sin cambios ... */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int i) {
				switch (i) {
				case 0:
					return String.class;
				case 1:
					return Integer.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				default:
					return Object.class;
				}
			}
		};
		modelDetalle.addColumn("Artículo");
		modelDetalle.addColumn("Cant.");
		modelDetalle.addColumn("P. Unit. (€)");
		modelDetalle.addColumn("Dto. (%)");
		modelDetalle.addColumn("P. Total (€)");

		// Carga datos artilos
		List<Articulo> articulos = Principal.obtenerArticulosPorPedido(idPedido);
		if (articulos != null && !articulos.isEmpty()) { /* ... sin cambios ... */
			for (Articulo art : articulos) {
				int cantidad = Principal.obtenerCantidadArticuloEnPedido(idPedido, art.getId_art());
				float precioOriginal = art.getPrecio();
				float descuento = art.getOferta();
				float precioConDescuento = precioOriginal * (1 - (descuento / 100.0f));
				float precioTotalArticulo = precioConDescuento * cantidad;
				String pUnitF = String.format(java.util.Locale.US, "%.2f", precioConDescuento);
				String dtoF = String.format(java.util.Locale.US, "%.0f", descuento);
				String pTotalF = String.format(java.util.Locale.US, "%.2f", precioTotalArticulo);
				modelDetalle.addRow(new Object[] { art.getNombre(), cantidad, pUnitF, dtoF, pTotalF });
			}
		} else {
			modelDetalle.addRow(new Object[] { "No hay artículos", null, null, null, null });
		}

		// Tabla para los detalles
		JTable tableDetalle = new JTable(modelDetalle) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component c = super.prepareRenderer(renderer, row, col);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				c.setBackground(COLOR_CELDA_FONDO);
				if (!isRowSelected(row)) {
					c.setForeground(Color.BLACK);
				} else {
					c.setForeground(COLOR_SELECCION_FUENTE);
				}
				if (c instanceof JLabel) {
					JLabel label = (JLabel) c;
					label.setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H,
							PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
					if (col >= 1) {
						label.setHorizontalAlignment(SwingConstants.RIGHT);
					} else {
						label.setHorizontalAlignment(SwingConstants.LEFT);
					}
				}
				return c;
			}
		};
		// --- Aplicar Estilos Generales de Tabla
		tableDetalle.setFont(FONT_TABLA_CELDA);
		tableDetalle.setRowHeight(tableDetalle.getRowHeight() + 10);
		tableDetalle.setShowGrid(false);
		tableDetalle.setShowHorizontalLines(true);
		tableDetalle.setShowVerticalLines(false);
		tableDetalle.setIntercellSpacing(new Dimension(0, 1));
		tableDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDetalle.setOpaque(false);
		tableDetalle.setFillsViewportHeight(true);
		// --- Estilo Cabecera
		applyCustomHeaderRenderer(tableDetalle);
		JScrollPane scrollPaneDetalle = new JScrollPane(tableDetalle);
		scrollPaneDetalle.setOpaque(true);
		scrollPaneDetalle.getViewport().setOpaque(true);
		scrollPaneDetalle.getViewport().setBackground(COLOR_FONDO_VIEWPORT);
		scrollPaneDetalle.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SCROLLPANE, 2));
		panelArticulos.add(scrollPaneDetalle, BorderLayout.CENTER);

		// Añadir la pestaña con la cabecera que hemos definido antes
		String tituloPestana = "Pedido " + idPedido;
		tabbedPane.addTab(null, panelArticulos);
		int index = tabbedPane.indexOfComponent(panelArticulos);

		// *** Crear Componente de Pestaña MEJORADO ***
		JPanel tabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		tabComponent.setOpaque(false);

		JLabel titleLabel = new JLabel(tituloPestana);
		titleLabel.setFont(FONT_PESTANA_TITULO);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		// Boton de cierre con mejoras
		JButton closeButton = new JButton("X");
		closeButton.setFont(new Font("Arial", Font.BOLD, 11));
		closeButton.setForeground(COLOR_BOTON_CERRAR_NORMAL);
		closeButton.setMargin(new Insets(1, 1, 1, 1));
		closeButton.setPreferredSize(new Dimension(18, 18));
		closeButton.setToolTipText("Cerrar Pestaña (Pedido " + idPedido + ")");
		closeButton.setVerticalAlignment(SwingConstants.CENTER);
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setFocusPainted(false);
		closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeButton.setOpaque(false);

		// Para el efecto Hover
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setContentAreaFilled(true);
				closeButton.setOpaque(true);
				closeButton.setBackground(COLOR_BOTON_CERRAR_HOVER);
				closeButton.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setContentAreaFilled(false);
				closeButton.setOpaque(false);
				closeButton.setForeground(COLOR_BOTON_CERRAR_NORMAL);
			}
		});

		// Para provocar el cierre
		closeButton.addActionListener(e -> {
			int tabIndex = tabbedPane.indexOfTabComponent(tabComponent);
			if (tabIndex != -1) {
				tabbedPane.remove(tabIndex);
			}
		});

		tabComponent.add(titleLabel);
		tabComponent.add(closeButton);
		tabbedPane.setTabComponentAt(index, tabComponent);
		tabbedPane.setToolTipTextAt(index, "Ver detalles del Pedido " + idPedido);
		adjustColumnWidths(tableDetalle);
		tabbedPane.setSelectedComponent(panelArticulos);
	}

	/**
	 * Aplica un renderizador de cabecera personalizado a la tabla dada.
	 * Configura la fuente, colores, bordes y alineación de la cabecera.
	 *
	 * @param table La JTable a la que se aplicará el renderizador de cabecera.
	 */
	private void applyCustomHeaderRenderer(JTable table) {
		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setResizingAllowed(true);
		header.setDefaultRenderer(new TableCellRenderer() {
			private final TableCellRenderer defaultRenderer = table.getTableHeader().getDefaultRenderer();
			
			/**
			 * Devuelve el componente utilizado para renderizar la cabecera de una celda.
			 * @param t La tabla.
			 * @param v El valor de la celda (texto de la cabecera).
			 * @param sel Si la celda está seleccionada (no aplica a cabeceras).
			 * @param foc Si la celda tiene el foco (no aplica a cabeceras).
			 * @param r La fila de la celda (siempre 0 para cabeceras).
			 * @param c La columna de la celda.
			 * @return El componente renderizado.
			 */
			@Override
			public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
				Component comp = defaultRenderer.getTableCellRendererComponent(t, v, sel, foc, r, c);
				if (comp instanceof JLabel) {
					JLabel label = (JLabel) comp;
					label.setFont(HEADER_FONT);
					label.setBackground(HEADER_BACKGROUND);
					label.setForeground(HEADER_FOREGROUND);
					label.setOpaque(true);
					Border pad = BorderFactory.createEmptyBorder(HEADER_VPADDING, HEADER_HPADDING, HEADER_VPADDING,
							HEADER_HPADDING);
					Border line = BorderFactory.createMatteBorder(0, 0, 2, 0, HEADER_BORDER_COLOR);
					label.setBorder(BorderFactory.createCompoundBorder(line, pad));
					label.setHorizontalAlignment(SwingConstants.CENTER);
				}
				return comp;
			}
		});
		header.repaint();
	}

	/**
	 * Ajusta automáticamente el ancho preferido de cada columna de la tabla
	 * para que se adapte al contenido más ancho (cabecera o celdas) más un padding.
	 * Este ajuste se realiza después de que la interfaz de usuario sea visible para asegurar
	 * que las dimensiones de los componentes se han calculado.
	 *
	 * @param table La JTable cuyas columnas se van a ajustar.
	 */
	private void adjustColumnWidths(JTable table) {
		SwingUtilities.invokeLater(() -> {
			TableColumnModel columnModel = table.getColumnModel();
			final int PADDING = 15;
			for (int column = 0; column < table.getColumnCount(); column++) {
				TableColumn tableColumn = columnModel.getColumn(column);
				int maxWidth = 0;
				TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
				if (headerRenderer == null) {
					headerRenderer = table.getTableHeader().getDefaultRenderer();
				}
				Component headerComp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(),
						false, false, 0, column);
				maxWidth = headerComp.getPreferredSize().width;
				for (int row = 0; row < table.getRowCount(); row++) {
					TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
					Component cellComp = table.prepareRenderer(cellRenderer, row, column);
					maxWidth = Math.max(maxWidth, cellComp.getPreferredSize().width);
				}
				tableColumn.setPreferredWidth(maxWidth + PADDING);
			}
		});
	}

}