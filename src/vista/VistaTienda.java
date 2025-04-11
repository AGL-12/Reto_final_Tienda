package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;
import controlador.Principal; // Asumiendo métodos de acceso a datos

import javax.swing.*;
import javax.swing.border.Border; // Para bordes
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer; // Necesario para transparencia
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor; // Necesario para transparencia
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL; // Necesario para cargar imagen de fondo
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Importar FlatLaf (RECUERDA CONFIGURARLO EN TU main)
// import com.formdev.flatlaf.FlatClientProperties;
// import com.formdev.flatlaf.extras.FlatSVGIcon;

// Opcional pero recomendado para layouts flexibles: MigLayout
// import net.miginfocom.swing.MigLayout;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	// --- Componentes UI ---
	private JTable tableArticulo;
	private JButton btnUsuario, btnCompra, btnAdmin;
	private JLabel lblTitulo, lblLogo;
	private DefaultTableModel model;
	private JTextField quantityEditorField;

	// --- Datos ---
	private Cliente localClien;
	private Image backgroundImage; // Imagen de fondo

	// --- Constantes de Estilo ---
	private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Color COLOR_VALIDATION_ERROR = Color.RED;

	// Padding y Espaciado tamaño de tabla
	private static final int PADDING_GENERAL = 10;
	private static final int PADDING_INTERNO = 5;
	private static final int PADDING_TABLA_CELDA_V = 3;
	private static final int PADDING_TABLA_CELDA_H = 5;

	/**
	 * Constructor
	 */
	public VistaTienda(Cliente clien, Frame owner) {
		super(owner, "DYE TOOLS - Tienda", true);
		this.localClien = clien;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// --- Cargar Imagen de Fondo ---
		cargarImagenFondo("/imagenes/fondoTienda.png");

		// --- Crear Panel Principal con Fondo ---
		JPanel panelPrincipal = new JPanel(new BorderLayout()) { // Usa BorderLayout
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		setContentPane(panelPrincipal);

		initComponents();
		configurarTabla();
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();

		pack();
		// Cambia tamaño de la ventana completagg
		setMinimumSize(new Dimension(900, 700));
		setLocationRelativeTo(owner);
	}

	/**
	 * Carga la imagen de fondo desde el path especificado.
	 */
	private void cargarImagenFondo(String path) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			backgroundImage = new ImageIcon(imgURL).getImage();
		} else {
			// System.err.println("No se pudo cargar la imagen de fondo: " + path); //
			// REMOVED
			backgroundImage = null;
		}
	}

	/**
	 * Inicializa componentes básicos (Título, Logo, Botones).
	 */
	private void initComponents() {
		// --- Panel Cabecera (NORTH) ---
		JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING_INTERNO * 2, 0));
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(PADDING_INTERNO, 0, PADDING_GENERAL, 0));
		panelCabecera.setOpaque(false);

		lblLogo = new JLabel(cargarIcono("/iconos/tienda_logo.png", 64, 64));
		panelCabecera.add(lblLogo);
		lblTitulo = new JLabel("DYE TOOLS - Catálogo");
		lblTitulo.setFont(FONT_TITULO);
		panelCabecera.add(lblTitulo);
		getContentPane().add(panelCabecera, BorderLayout.NORTH);

		JLabel lblAdvertencia = new JLabel("Para escoger un articulo introuduzca una cantidad en la tabla");
		lblAdvertencia.setForeground(new Color(255, 215, 0));
		lblAdvertencia.setFont(new Font("Calibri", Font.BOLD, 20));
		panelCabecera.add(lblAdvertencia);

		// --- Panel Botones (SOUTH) ---
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
		panelBotones.setBorder(
				BorderFactory.createEmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));
		panelBotones.setOpaque(false);

		btnUsuario = crearBoton("Mi Cuenta", "/iconos/user.png");
		btnAdmin = crearBoton("Admin Panel", "/iconos/admin.png");
		btnCompra = crearBoton("Ver Carrito", "/iconos/cart.png");
		btnAdmin.setVisible(localClien != null && localClien.isEsAdmin());

		panelBotones.add(btnUsuario);
		if (btnAdmin.isVisible()) {
			panelBotones.add(Box.createHorizontalStrut(PADDING_INTERNO));
			panelBotones.add(btnAdmin);
		}
		panelBotones.add(Box.createHorizontalGlue());
		panelBotones.add(btnCompra);
		getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}

	/**
	 * Crea y configura un JButton estándar.
	 */
	private JButton crearBoton(String texto, String iconoPath) {
		JButton btn = new JButton(texto);
		btn.setFont(FONT_BOTON);
		btn.setIcon(cargarIcono(iconoPath, 16, 16));
		btn.addActionListener(this);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setFocusPainted(false);
		btn.setMargin(new Insets(PADDING_INTERNO, PADDING_INTERNO * 2, PADDING_INTERNO, PADDING_INTERNO * 2));
		return btn;
	}

	/**
	 * Configura la JTable (modelo, columnas, renderers, editor, listener).
	 */
	private void configurarTabla() {
		// --- Modelo ---
		model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return Integer.class; // ID (Modelo col 0)
				case 3:
					return Double.class; // Precio (Modelo col 3)
				case 4:
					return Double.class; // Oferta (Modelo col 4)
				case 5:
					return Integer.class; // Stock (Modelo col 5)
				case 6:
					return Integer.class; // Cantidad (Modelo col 6, editable)
				default:
					return String.class; // Nombre (1), Desc (2)
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6; // Modelo col 6 = Cantidad
			}
		};
		model.addColumn("ID_ART"); // Modelo Col 0
		model.addColumn("Nombre"); // Modelo Col 1
		model.addColumn("Descripción"); // Modelo Col 2
		model.addColumn("Precio (€)"); // Modelo Col 3
		model.addColumn("Oferta (%)"); // Modelo Col 4
		model.addColumn("Stock"); // Modelo Col 5
		model.addColumn("Cantidad"); // Modelo Col 6

		// --- Creación Tabla ---
		tableArticulo = new JTable(model) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				Color alternateColor = new Color(230, 240, 255, 100);
				Color baseColor = new Color(255, 255, 255, 120);

				Color selectionColor = tableArticulo.getSelectionBackground();
				c.setBackground(
						new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 150));
				c.setForeground(Color.blue);

				if (c instanceof JComponent) {
					((JComponent) c).setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V,
							PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
				}
				if (c instanceof DefaultTableCellRenderer) {
					DefaultTableCellRenderer rendererComp = (DefaultTableCellRenderer) c;
					int modelColumn = tableArticulo.convertColumnIndexToModel(column);
					if (modelColumn >= 3 && modelColumn <= 6) {
						rendererComp.setHorizontalAlignment(SwingConstants.RIGHT);
					} else {
						rendererComp.setHorizontalAlignment(SwingConstants.LEFT);
					}
				}
				return c;
			}

			@Override
			public Component prepareEditor(TableCellEditor editor, int row, int column) {
				Component c = super.prepareEditor(editor, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				if (c instanceof JTextField) {
					((JTextField) c).setOpaque(true);
					((JTextField) c).setBackground(Color.WHITE);
				}
				return c;
			}
		};

		// --- Propiedades Tabla ---
		tableArticulo.setFont(FONT_TABLA_CELDA);
		tableArticulo.setRowHeight(tableArticulo.getRowHeight() + PADDING_INTERNO);
		tableArticulo.setGridColor(new Color(180, 180, 180, 100));
		tableArticulo.setShowGrid(true);
		tableArticulo.setShowHorizontalLines(true);
		tableArticulo.setShowVerticalLines(false);
		tableArticulo.setIntercellSpacing(new Dimension(0, 1));
		tableArticulo.setAutoCreateRowSorter(true);
		tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// --- Cabecera Tabla ---
		JTableHeader header = tableArticulo.getTableHeader();
		header.setFont(FONT_TABLA_HEADER);
		header.setReorderingAllowed(false);
		header.setResizingAllowed(true);
		header.setOpaque(false);
		if (header.getDefaultRenderer() instanceof JLabel) {
			((JLabel) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		}

		// --- Ocultar Columna ID_ART ---
		TableColumnModel columnModel = tableArticulo.getColumnModel();
		try {
			TableColumn columnToHide = tableArticulo.getColumn("ID_ART");
			columnModel.removeColumn(columnToHide);
		} catch (IllegalArgumentException e) {
			// System.err.println("No se pudo encontrar/ocultar la columna 'ID_ART'."); //
			// REMOVED
		}

		// --- Editor Personalizado para Cantidad ---
		quantityEditorField = new JTextField();
		quantityEditorField.setHorizontalAlignment(SwingConstants.RIGHT);
		quantityEditorField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		quantityEditorField.setOpaque(true);
		quantityEditorField.setBackground(Color.white);
		quantityEditorField.setForeground(UIManager.getColor("TextField.foreground"));

		quantityEditorField.getDocument().addDocumentListener(new DocumentListener() {
			private void verificarNumero() {
				String text = quantityEditorField.getText();
				boolean valido = text.matches("[0-9]*");
				Color foregroundColor = UIManager.getColor("TextField.foreground");
				if (text.isEmpty()) {
					// ok
				} else if (!valido) {
					foregroundColor = COLOR_VALIDATION_ERROR;
				} else {
					try {
						int filaEditada = tableArticulo.getEditingRow();
						if (filaEditada != -1) {
							int filaModelo = tableArticulo.convertRowIndexToModel(filaEditada);
							if (filaModelo != -1) {
								int cantidad = Integer.parseInt(text);
								Object stockObj = model.getValueAt(filaModelo, 5);
								if (stockObj instanceof Integer) {
									int stock = (Integer) stockObj;
									if (cantidad > stock || cantidad < 0) {
										foregroundColor = COLOR_VALIDATION_ERROR;
									}
								} else {
									foregroundColor = COLOR_VALIDATION_ERROR;
								}
							}
						}
					} catch (NumberFormatException | IndexOutOfBoundsException ignored) {
						foregroundColor = COLOR_VALIDATION_ERROR;
					}
				}
				quantityEditorField.setForeground(foregroundColor);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				verificarNumero();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				verificarNumero();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				/* No necesario */ }
		});

		// Índices VISTA tras ocultar ID: Nom(0), Desc(1), Precio(2), Oferta(3),
		// Stock(4), Cant(5)
		int cantidadViewIndex = 5;
		if (cantidadViewIndex < columnModel.getColumnCount()) {
			columnModel.getColumn(cantidadViewIndex).setCellEditor(new DefaultCellEditor(quantityEditorField));
		} else {
			// System.err.println("Error: Índice de columna de vista 'Cantidad' fuera de
			// rango."); // REMOVED
		}

		// --- Listener del Modelo ---
		model.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) { // Cantidad (Modelo)
				int filaModelo = e.getFirstRow();
				if (filaModelo < 0 || filaModelo >= model.getRowCount())
					return;

				Object cantidadObj = model.getValueAt(filaModelo, 6);
				Object stockObj = model.getValueAt(filaModelo, 5);

				if (cantidadObj == null || cantidadObj.toString().isEmpty()) {
					Object currentValue = model.getValueAt(filaModelo, e.getColumn());
					if (currentValue != null && !(currentValue instanceof Integer && (Integer) currentValue == 0)) {
						SwingUtilities.invokeLater(() -> model.setValueAt(0, filaModelo, 6));
					}
					return;
				}

				if (stockObj instanceof Integer) {
					int stockDisponible = (Integer) stockObj;
					try {
						int cantidadIngresada = Integer.parseInt(cantidadObj.toString());
						if (cantidadIngresada < 0) {
							SwingUtilities.invokeLater(() -> model.setValueAt(0, filaModelo, 6));
						} else if (cantidadIngresada > stockDisponible) {
							SwingUtilities.invokeLater(() -> {
								JOptionPane.showMessageDialog(VistaTienda.this,
										"La cantidad (" + cantidadIngresada + ") excede el stock disponible ("
												+ stockDisponible + "). Se ajustará al máximo.",
										"Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
								model.setValueAt(stockDisponible, filaModelo, 6);
							});
						}
					} catch (NumberFormatException ex) {
						SwingUtilities.invokeLater(() -> model.setValueAt(0, filaModelo, 6));
					}
				} else {
					SwingUtilities.invokeLater(() -> model.setValueAt(0, filaModelo, 6));
				}
			}
		});

		// --- ScrollPane ---
		JScrollPane scrollPane = new JScrollPane(tableArticulo);

		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, PADDING_GENERAL, 0, PADDING_GENERAL));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Carga (o recarga) los datos de los artículos desde la base de datos a la
	 * tabla.
	 */
	public void cargarDatosTabla() {
		if (tableArticulo.isEditing()) {
			TableCellEditor editor = tableArticulo.getCellEditor();
			if (editor != null) {
				editor.stopCellEditing();
			}
		}
		model.setRowCount(0);
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		if (articulos != null && !articulos.isEmpty()) {
			for (Articulo art : articulos.values()) {
				if (art.getStock() > 0) {
					model.addRow(new Object[] { art.getId_art(), art.getNombre(), art.getDescripcion(), art.getPrecio(),
							art.getOferta(), art.getStock(), 0 });
				}
			}
		} else {
			// System.out.println("No se encontraron artículos o la lista está vacía."); //
			// REMOVED
		}
	}

	/**
	 * Ajusta el ancho preferido de cada columna visible de la tabla.
	 */
	private void ajustarAnchosColumnaTabla() {
		SwingUtilities.invokeLater(() -> {
			if (tableArticulo.getWidth() == 0 || tableArticulo.getColumnCount() == 0) {
				Timer timer = new Timer(100, e -> ajustarAnchosColumnaTabla());
				timer.setRepeats(false);
				timer.start();
				return;
			}

			TableColumnModel columnModel = tableArticulo.getColumnModel();
			final int PADDING = PADDING_TABLA_CELDA_H * 2;
			int[] minWidths = { 150, 200, 75, 75, 65, 75 };
			int[] maxWidths = { 400, 600, 100, 100, 90, 110 };

			for (int columnView = 0; columnView < tableArticulo.getColumnCount(); columnView++) {
				TableColumn tableColumn = columnModel.getColumn(columnView);
				int headerWidth = getColumnHeaderWidth(tableArticulo, columnView);
				int contentWidth = getMaximumColumnContentWidth(tableArticulo, columnView);
				int preferredWidth = Math.max(headerWidth, contentWidth) + PADDING;

				if (columnView < minWidths.length) {
					tableColumn.setMinWidth(minWidths[columnView]);
					preferredWidth = Math.max(preferredWidth, minWidths[columnView]);
				}
				if (columnView < maxWidths.length && maxWidths[columnView] > 0) {
					tableColumn.setMaxWidth(maxWidths[columnView]);
					preferredWidth = Math.min(preferredWidth, maxWidths[columnView]);
				}
				tableColumn.setPreferredWidth(preferredWidth);
			}
			tableArticulo.revalidate();
			tableArticulo.repaint();
		});
	}

	// --- Métodos Auxiliares para Ancho de Columna ---
	private int getColumnHeaderWidth(JTable table, int columnIndexView) {
		TableColumn column = table.getColumnModel().getColumn(columnIndexView);
		TableCellRenderer renderer = column.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1,
				columnIndexView);
		return comp.getPreferredSize().width;
	}

	private int getMaximumColumnContentWidth(JTable table, int columnIndexView) {
		int maxWidth = 0;
		// TableColumn column = table.getColumnModel().getColumn(columnIndexView); //
		// Not needed?
		for (int row = 0; row < table.getRowCount(); row++) {
			TableCellRenderer renderer = table.getCellRenderer(row, columnIndexView);
			Component comp = table.prepareRenderer(renderer, row, columnIndexView);
			int width = comp.getPreferredSize().width;
			if (comp instanceof JComponent) {
				Insets insets = ((JComponent) comp).getInsets();
				width += insets.left + insets.right;
			}
			maxWidth = Math.max(maxWidth, width);
		}
		return maxWidth;
	}

	// --- Manejo de Acciones ---
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (tableArticulo.isEditing()) {
			TableCellEditor editor = tableArticulo.getCellEditor();
			if (editor != null && !editor.stopCellEditing()) {
				return;
			}
		}

		if (source == btnUsuario)
			mostrarVistaUsuario();
		else if (source == btnAdmin)
			mostrarVistaAdmin();
		else if (source == btnCompra)
			abrirCarrito();
	}

	// --- Métodos de Navegación/Acción ---
	private void mostrarVistaUsuario() {
		if (localClien != null) {
			VistaUsuario vistaUsuario = new VistaUsuario(localClien, this);
			vistaUsuario.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Cliente no identificado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostrarVistaAdmin() {
		VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
		menuAdmin.setVisible(true);
		// System.out.println("Volviendo de Admin Panel, recargando tabla..."); //
		// REMOVED
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();
	}

	private void abrirCarrito() {
		if (!validarCantidadesParaCarrito()) {
			JOptionPane.showMessageDialog(this,
					"Hay cantidades inválidas (negativas, no numéricas o superiores al stock).\n"
							+ "Por favor, corrígelas (pon 0 o un valor válido) antes de ir al carrito.",
					"Cantidades Inválidas", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Pedido pedidoActual = new Pedido(Principal.obtenerUltimoIdPed(), localClien.getId_usu(), 0,
				LocalDateTime.now());
		List<Compra> comprasParaCarrito = recopilarCompras(pedidoActual.getId_ped());

		if (comprasParaCarrito.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No has añadido ningún artículo al carrito (cantidad > 0).",
					"Carrito Vacío", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		VistaCarrito carritoDialog = new VistaCarrito(this, comprasParaCarrito, pedidoActual);
		carritoDialog.setVisible(true);

		// System.out.println("Volviendo del Carrito, recargando tabla..."); // REMOVED
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();
	}

	/**
	 * Verifica si todas las cantidades ingresadas en la tabla son válidas (>= 0 y
	 * <= stock).
	 */
	private boolean validarCantidadesParaCarrito() {
		if (tableArticulo.isEditing()) {
			TableCellEditor editor = tableArticulo.getCellEditor();
			if (editor != null && !editor.stopCellEditing()) {
				// System.err.println("Validación fallida: No se pudo detener la edición."); //
				// REMOVED
				return false;
			}
		}
		for (int i = 0; i < model.getRowCount(); i++) {
			Object cantidadObj = model.getValueAt(i, 6);
			Object stockObj = model.getValueAt(i, 5);

			if (cantidadObj == null || cantidadObj.toString().isEmpty()) {
				continue;
			}
			if (!(stockObj instanceof Integer)) {
				// System.err.println("Validación fallida: Stock no es Integer en fila " + i);
				// // REMOVED
				return false;
			}
			int stock = (Integer) stockObj;
			try {
				int cantidad = Integer.parseInt(cantidadObj.toString());
				if (cantidad < 0 || cantidad > stock) {
					// System.err.println("Validación fallida: Cantidad " + cantidad + " inválida
					// para stock " + stock + " en fila " + i); // REMOVED
					return false;
				}
			} catch (NumberFormatException e) {
				// System.err.println("Validación fallida: Cantidad no numérica '" + cantidadObj
				// + "' en fila " + i); // REMOVED
				return false;
			}
		}
		// System.out.println("Validación de cantidades para carrito: OK"); // REMOVED
		return true;
	}

	/**
	 * Recopila las compras basándose en el modelo de la tabla.
	 */
	private List<Compra> recopilarCompras(int idePed) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			Object valorId = model.getValueAt(i, 0);
			Object valorCantidad = model.getValueAt(i, 6);

			if (valorCantidad != null && valorId instanceof Integer) {
				try {
					int cantidadFinal = 0;
					if (!valorCantidad.toString().isEmpty()) {
						cantidadFinal = Integer.parseInt(valorCantidad.toString());
					}
					if (cantidadFinal > 0) {
						int idArt = (Integer) valorId;
						Compra palCarro = new Compra(idArt, idePed, cantidadFinal);
						listaCompra.add(palCarro);
					}
				} catch (NumberFormatException e) {
					// System.err.println("Error de formato numérico al recopilar compra en fila " +
					// i + ": '" + valorCantidad + "'. Se ignora."); // REMOVED
				}
			} else if (valorId == null) {
				// System.err.println("Error al recopilar: ID de artículo nulo en fila " + i);
				// // REMOVED
			}
		}
		// System.out.println("Recopiladas " + listaCompra.size() + " compras para el
		// carrito."); // REMOVED
		return listaCompra;
	}

	// --- Método Cargar Icono ---
	private ImageIcon cargarIcono(String path, int width, int height) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			if (originalIcon.getIconWidth() != width || originalIcon.getIconHeight() != height) {
				Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
				return new ImageIcon(image);
			} else {
				return originalIcon;
			}
		} else {
			// System.err.println("Icono no encontrado: " + path); // REMOVED
			BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = placeholder.createGraphics();
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(Color.RED);
			g2d.drawLine(0, 0, width, height);
			g2d.drawLine(0, height, width, 0);
			g2d.dispose();
			return new ImageIcon(placeholder);
		}
	}

	private ImageIcon cargarIcono(String path) {
		return cargarIcono(path, 16, 16);
	}

	// --- Main de Ejemplo (Comentado por defecto) ---
	/*
	 * public static void main(String[] args) { try { // Usar FlatLaf recomendado
	 * UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
	 * UIManager.put("Button.arc", 10); UIManager.put("Component.arc", 8);
	 * UIManager.put("Table.showHorizontalLines", true);
	 * UIManager.put("Table.showVerticalLines", true); // O false
	 * UIManager.put("Table.intercellSpacing", new Dimension(0,1)); } catch
	 * (Exception e) { e.printStackTrace(); } // <- Este no se quita porque es del
	 * try-catch del main de ejemplo
	 * 
	 * Cliente clientePrueba = new Cliente(); clientePrueba.setId_usu(1);
	 * clientePrueba.setUsuario("testuser"); clientePrueba.setEsAdmin(true);
	 * 
	 * SwingUtilities.invokeLater(() -> { JFrame framePadre = new JFrame();
	 * framePadre.setUndecorated(true); framePadre.setLocationRelativeTo(null);
	 * 
	 * VistaTienda dialog = new VistaTienda(clientePrueba, framePadre);
	 * dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	 * 
	 * dialog.addWindowListener(new java.awt.event.WindowAdapter() {
	 * 
	 * @Override public void windowClosed(java.awt.event.WindowEvent windowEvent) {
	 * framePadre.dispose(); System.exit(0); // <- Este tampoco, es para cerrar la
	 * app en el ejemplo } });
	 * 
	 * dialog.setVisible(true); }); }
	 */
}