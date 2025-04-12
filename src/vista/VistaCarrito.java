package vista;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager; // Para colores/bordes del LaF
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // Para configurar cabecera
import javax.swing.table.TableCellRenderer; // Para renderers
import javax.swing.table.TableColumn; // Para ancho de columna
import javax.swing.table.TableColumnModel; // Para ancho de columna

// Imports necesarios para el controlador y modelo (sin cambios)
import controlador.Principal;
import modelo.Articulo;
import modelo.Compra;
import modelo.Pedido;

// Imports para Layout y Componentes Gráficos
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor; // Para cursor de mano
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets; // Para márgenes de botón
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Imports para iconos (copiados de otras vistas)
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import javax.swing.ImageIcon; // Para iconos

// Imports para formateo y excepciones (sin cambios)
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;
	private DefaultTableModel model;
	private JTable tablaCarrito;
	private JLabel lblTotalCompra; // Label para mostrar el total fuera de la tabla

	// Datos del pedido y la compra
	private Pedido localPedido;
	private List<Compra> localListaCompra;

	// --- Constantes de Estilo ---
	private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20);
	private static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final int PADDING_GENERAL = 15;
	private static final int PADDING_INTERNO = 10;
	private static final int PADDING_BOTONES = 8;
	private static final int PADDING_TABLA_CELDA_V = 5;
	private static final int PADDING_TABLA_CELDA_H = 8;

	// Formateador de moneda
	private static final Locale userLocale = new Locale("es", "ES"); // Español/España para Euros (€)
	private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(userLocale);

	/**
	 * Constructor principal.
	 */
	public VistaCarrito(JDialog vistaTienda, List<Compra> listaCompra, Pedido preSetCompra) {
		super(vistaTienda, "Carrito de Compra", true);

		this.localListaCompra = listaCompra;
		this.localPedido = preSetCompra;

		// --- Configuración Ventana ---
		getContentPane().setLayout(new BorderLayout(PADDING_INTERNO, PADDING_INTERNO));
		((JPanel) getContentPane())
				.setBorder(new EmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));

		// --- Inicializar Componentes ---
		initComponents(); // Crear título, botones, label total
		configurarTabla(); // Configurar tabla y modelo
		cargarYCalcular(); // Cargar datos en tabla y calcular total

		// --- Ajustar Ancho Columnas (después de cargar datos) ---
		ajustarAnchosColumnaTabla();

		// --- Configuración Final Ventana ---
		pack();
		setMinimumSize(new Dimension(680, 420)); // Tamaño mínimo
		setLocationRelativeTo(vistaTienda);

		// Botón por defecto (Enter)
		SwingUtilities.invokeLater(() -> getRootPane().setDefaultButton(btnComprar));
	}

	/**
	 * Inicializa los componentes principales (Título, Panel Sur con Total y
	 * Botones).
	 */
	private void initComponents() {
		// --- Título (NORTH) ---
		JLabel lblTitulo = new JLabel("CARRITO DE COMPRA");
		lblTitulo.setFont(FONT_TITULO);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(0, 0, PADDING_INTERNO, 0));
		getContentPane().add(lblTitulo, BorderLayout.NORTH);

		// --- Panel Inferior (SOUTH) ---
		JPanel southPanel = new JPanel(new BorderLayout(PADDING_INTERNO, 0));
		southPanel.setBorder(new EmptyBorder(PADDING_INTERNO, 0, 0, 0)); // Espacio superior

		// Label Total (Oeste del Panel Sur)
		lblTotalCompra = new JLabel("Total: Calculando...");
		lblTotalCompra.setFont(FONT_TOTAL);
		lblTotalCompra.setBorder(new EmptyBorder(5, PADDING_INTERNO, 5, PADDING_INTERNO));
		southPanel.add(lblTotalCompra, BorderLayout.WEST);

		// Panel de Botones (Este del Panel Sur)
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, PADDING_INTERNO, 0));

		// Crear botones usando el helper
		btnComprar = crearBoton("COMPRAR", "/iconos/comprar.png"); // Cambia ruta icono si es necesario
		btnVolver = crearBoton("VOLVER", "/iconos/volver.png"); // Cambia ruta icono si es necesario

		// Marcar Comprar como botón primario para que FlatLaf lo estilice
		btnComprar.putClientProperty("JButton.buttonType", "primary");

		panelBotones.add(btnVolver); // Volver primero a la derecha
		panelBotones.add(btnComprar); // Comprar a la izquierda de Volver
		southPanel.add(panelBotones, BorderLayout.EAST);

		getContentPane().add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * Crea y configura un JButton estándar con opción de icono.
	 */
	private JButton crearBoton(String texto, String iconoPath) {
		JButton btn = new JButton(texto);
		btn.setFont(FONT_BOTON);
		// Cargar icono opcionalmente
		if (iconoPath != null && !iconoPath.isEmpty()) {
			ImageIcon icon = cargarIcono(iconoPath, 16, 16); // Iconos pequeños 16x16
			if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
				btn.setIcon(icon);
			}
		}
		btn.addActionListener(this);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setFocusPainted(false);
		// Padding interno del botón
		btn.setMargin(new Insets(PADDING_BOTONES, PADDING_BOTONES * 2, PADDING_BOTONES, PADDING_BOTONES * 2));
		return btn;
	}

	/**
	 * Configura la JTable: modelo, renderers, propiedades, cabecera.
	 */
	private void configurarTabla() {
		String[] columnNames = { "Nombre", "Cantidad", "Descuento Ud.", "Precio Final Ud.", "Precio Total" };
		model = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 1:
					return Integer.class; // Cantidad
				case 2:
				case 3:
				case 4:
					return Float.class; // Precios/Descuentos
				default:
					return String.class; // Nombre
				}
			}
		};

		tablaCarrito = new JTable(model);
		tablaCarrito.setFillsViewportHeight(true);
		tablaCarrito.setRowHeight(tablaCarrito.getRowHeight() + PADDING_INTERNO); // Mayor altura de fila
		tablaCarrito.setFont(FONT_TABLA_CELDA); // Fuente para celdas
		tablaCarrito.setAutoCreateRowSorter(true); // Permitir ordenar

		// Configurar Cabecera
		JTableHeader header = tablaCarrito.getTableHeader();
		header.setFont(FONT_TABLA_HEADER);
		header.setReorderingAllowed(false); // No reordenar
		header.setResizingAllowed(false); // *** CAMBIO: No redimensionar ***

		// Configurar Renderers para formato y alineación
		configurarRenderersTabla();

		// Añadir Tabla al ScrollPane (se hace en el constructor ahora)
		JScrollPane scrollPane = new JScrollPane(tablaCarrito);
		// Usar borde estándar del LaF para el scroll pane
		scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Configura los renderers para las celdas de la tabla.
	 */
	private void configurarRenderersTabla() {
		// Renderer genérico para alinear a la derecha y añadir padding
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		rightRenderer.setBorder(new EmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V,
				PADDING_TABLA_CELDA_H));

		// Renderer para formato de moneda, alineación derecha, padding y filas alternas
		DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L; // Añadido

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof Number && c instanceof JLabel) {
					((JLabel) c).setText(currencyFormat.format(((Number) value).doubleValue()));
					((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
				}
				// Padding
				((JLabel) c).setBorder(new EmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H,
						PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));

				// Colores alternos usando UIManager para compatibilidad con temas
				if (!isSelected) {
					Color bg = UIManager.getColor(row % 2 == 0 ? "Table.background" : "Table.alternateRowColor");
					if (bg == null) { // Fallback si los colores no están definidos en el LaF
						bg = (row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
					}
					c.setBackground(bg);
				}
				return c;
			}
		};

		// Renderer para descuento (similar a currency, pero puede mostrar '-')
		DefaultTableCellRenderer discountRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L; // Añadido

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof Number && c instanceof JLabel) {
					double val = ((Number) value).doubleValue();
					// Mostrar "-" si el descuento es 0 o muy cercano
					((JLabel) c).setText(Math.abs(val) < 0.001 ? "-" : currencyFormat.format(val));
					((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
				}
				// Padding
				((JLabel) c).setBorder(new EmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H,
						PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));

				// Colores alternos
				if (!isSelected) {
					Color bg = UIManager.getColor(row % 2 == 0 ? "Table.background" : "Table.alternateRowColor");
					if (bg == null) {
						bg = (row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
					}
					c.setBackground(bg);
				}
				return c;
			}
		};

		// Renderer para texto alineado a la izquierda con padding
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		leftRenderer.setBorder(new EmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V,
				PADDING_TABLA_CELDA_H));

		// Asignar renderers a las columnas por índice
		tablaCarrito.getColumnModel().getColumn(0).setCellRenderer(leftRenderer); // Nombre
		tablaCarrito.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // Cantidad
		tablaCarrito.getColumnModel().getColumn(2).setCellRenderer(discountRenderer); // Descuento Ud.
		tablaCarrito.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer); // Precio Final Ud.
		tablaCarrito.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer); // Precio Total
	}

	/**
	 * Carga los datos en la tabla y calcula/muestra el total.
	 */
	private void cargarYCalcular() {
		float totalCompraCalculado = 0;
		model.setRowCount(0); // Limpiar tabla

		for (Compra com : localListaCompra) {
			Articulo arti = Principal.buscarArticulo(com.getId_art());
			if (arti != null) {
				float precioOriginal = arti.getPrecio();
				float porcentajeOferta = arti.getOferta();
				float descuentoUnidad = (porcentajeOferta / 100.0f) * precioOriginal;
				float precioFinalUnidad = precioOriginal - descuentoUnidad;
				float precioTotalArticulo = precioFinalUnidad * com.getCantidad();

				model.addRow(new Object[] { arti.getNombre(), com.getCantidad(), descuentoUnidad, precioFinalUnidad,
						precioTotalArticulo });
				totalCompraCalculado += precioTotalArticulo;
			} else {
				System.err.println("Advertencia: Artículo no encontrado ID " + com.getId_art());
				// Considerar añadir fila de error o simplemente omitir
			}
		}

		if (localPedido != null) {
			localPedido.setTotal(totalCompraCalculado);
		}
		lblTotalCompra.setText("Total: " + currencyFormat.format(totalCompraCalculado));
	}

	/**
	 * Ajusta el ancho preferido de cada columna visible de la tabla.
	 */
	private void ajustarAnchosColumnaTabla() {
		// Asegurarse de que la tabla tenga un tamaño antes de calcular
		tablaCarrito.revalidate();
		if (tablaCarrito.getWidth() == 0) {
			SwingUtilities.invokeLater(this::ajustarAnchosColumnaTabla);
			return;
		}
		TableColumnModel columnModel = tablaCarrito.getColumnModel();
		final int PADDING = PADDING_TABLA_CELDA_H * 2;

		for (int columnView = 0; columnView < tablaCarrito.getColumnCount(); columnView++) {
			TableColumn tableColumn = columnModel.getColumn(columnView);
			int headerWidth = getColumnHeaderWidth(tablaCarrito, columnView);
			int contentWidth = getMaximumColumnContentWidth(tablaCarrito, columnView);
			int preferredWidth = Math.max(headerWidth, contentWidth) + PADDING;

			// Definir anchos mínimos y máximos para controlar mejor
			if (columnView == 0) { // Nombre
				tableColumn.setMinWidth(180);
			} else if (columnView == 1) { // Cantidad
				tableColumn.setMinWidth(70);
				tableColumn.setMaxWidth(100);
			} else if (columnView == 2) { // Descuento Ud.
				tableColumn.setMinWidth(110);
				tableColumn.setMaxWidth(150);
			} else if (columnView == 3) { // Precio Final Ud.
				tableColumn.setMinWidth(110);
				tableColumn.setMaxWidth(160);
			} else if (columnView == 4) { // Precio Total
				tableColumn.setMinWidth(120);
				tableColumn.setMaxWidth(170);
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}
		tablaCarrito.getTableHeader().resizeAndRepaint(); // Aplicar cambios en cabecera
	}

	// --- Métodos Auxiliares para Ancho de Columna (Sin cambios respecto a versión
	// anterior) ---
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
		for (int row = 0; row < table.getRowCount(); row++) {
			TableCellRenderer renderer = table.getCellRenderer(row, columnIndexView);
			Component comp = table.prepareRenderer(renderer, row, columnIndexView);
			maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
		}
		return maxWidth;
	}

	// --- Manejo de Acciones Botones ---
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnComprar)) {
			int confirm = JOptionPane.showConfirmDialog(this,
					"¿Finalizar la compra por " + currencyFormat.format(localPedido.getTotal()) + "?",
					"Confirmar Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (confirm == JOptionPane.YES_OPTION) {
				fullCompra();
			}
		} else if (e.getSource().equals(btnVolver)) {
			this.dispose(); // Cerrar la ventana del carrito
		}
	}

	/**
	 * Lógica para procesar la compra final.
	 */
	private void fullCompra() {
		if (localPedido == null || localListaCompra == null || localListaCompra.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error interno: Datos de pedido o carrito inválidos.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			Principal.guardarPedido(localPedido);
			Principal.guardarCompra(localListaCompra);

			JOptionPane.showMessageDialog(this,
					"¡Compra realizada con éxito!\nID de Pedido: " + localPedido.getId_ped(), "Compra Finalizada",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose(); // Cerrar carrito

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error al guardar en Base de Datos:\n" + ex.getMessage(), "Error BD",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error inesperado:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	// --- Método Cargar Icono (copiado de VistaLogIn/VistaTienda) ---
	private ImageIcon cargarIcono(String path, int width, int height) {
		InputStream imgStream = getClass().getResourceAsStream(path);
		if (imgStream != null) {
			try {
				BufferedImage originalImage = ImageIO.read(imgStream);
				imgStream.close();
				Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				return new ImageIcon(scaledImage);
			} catch (java.io.IOException e) {
				System.err.println("Error al leer la imagen: " + path + " - " + e.getMessage());
			}
		} else {
			System.err.println("No se pudo encontrar el recurso: " + path);
		}
		// Crear placeholder si falla
		BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = placeholder.createGraphics();
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, width - 1, height - 1);
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(1));
			g2d.setFont(new Font("SansSerif", Font.BOLD, Math.min(width, height) * 3 / 4));
			g2d.drawString("?", width / 4, height * 3 / 4);
		} finally {
			g2d.dispose();
		}
		return new ImageIcon(placeholder);
	}

}