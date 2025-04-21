package vista;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

// Imports necesarios para el controlador y modelo (sin cambios)
import controlador.Principal;
import modelo.Articulo;
import modelo.Compra;
import modelo.Pedido;

// Imports para Layout y Componentes Gráficos
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
// Imports para formateo y excepciones (sin cambios)
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
// Opcional: Iconos
// import javax.swing.ImageIcon;

public class VistaCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnComprar, btnVolver;
	private DefaultTableModel model;
	private JTable tablaCarrito;
	private JLabel lblTotalCompra;
	private BufferedImage backgroundImage;
	// Datos del pedido y la compra
	private Pedido localPedido;
	private List<Compra> localListaCompra;
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);

	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final int PADDING_TABLA_CELDA_V = 5;
	private static final int PADDING_TABLA_CELDA_H = 10;

	// Usa la moneda local
	private static final Locale userLocale = new Locale("es", "ES"); // Español/España para Euros (€)
	private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(userLocale);

	/**
	 * Constructor principal de la vista del carrito.
	 *
	 * @param vistaTienda  La ventana padre (JDialog de la tienda).
	 * @param listaCompra  La lista de objetos Compra que representan los artículos
	 *                     en el carrito.
	 * @param preSetCompra El objeto Pedido preconfigurado (con id_usu, fecha, etc.,
	 *                     pero sin ID de pedido aún).
	 */
	public VistaCarrito(JDialog vistaTienda, List<Compra> listaCompra, Pedido preSetCompra) {
		super(vistaTienda, "Carrito de Compra", true);
		this.localListaCompra = listaCompra;
		this.localPedido = preSetCompra;

		try {
			backgroundImage = ImageIO.read(getClass().getResource("/imagenes/fondoMadera.jpg"));
		} catch (IOException e) {
			e.getMessage();
			getContentPane().setBackground(new Color(240, 240, 240));
		}

		setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		});

		// Configuracion de la ventana
		getContentPane().setLayout(new BorderLayout(10, 10)); // Layout principal con márgenes H/V
		((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding general

		// Titulo
		JLabel lblTitulo = new JLabel("CARRITO DE COMPRA");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0)); // Espacio inferior
		getContentPane().add(lblTitulo, BorderLayout.NORTH);

		// Tabla de precompra
		inicializarTabla();
		JScrollPane scrollPane = new JScrollPane(tablaCarrito);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(new Color(245, 222, 179));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2));

		// Panel inferior
		JPanel southPanel = new JPanel(new BorderLayout(10, 0));
		southPanel.setBackground(new Color(245, 222, 179));
		southPanel.setOpaque(true);
		lblTotalCompra = new JLabel("Total: Calculando...");
		lblTotalCompra.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalCompra.setBorder(new EmptyBorder(5, 10, 5, 10));
		southPanel.add(lblTotalCompra, BorderLayout.WEST);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
		panelBotones.setOpaque(false);

		btnComprar = crearBoton("COMPRAR");
		btnVolver = crearBoton("VOLVER");

		panelBotones.add(btnComprar);
		panelBotones.add(btnVolver);
		southPanel.add(panelBotones, BorderLayout.EAST);

		getContentPane().add(southPanel, BorderLayout.SOUTH);

		pack();
		setMinimumSize(new Dimension(650, 400));
		setLocationRelativeTo(vistaTienda);

		SwingUtilities.invokeLater(() -> getRootPane().setDefaultButton(btnComprar));

		calcularYMostrarTotal();
	}

	/**
	 * Crea y configura un JButton estándar con el estilo de VistaTienda.
	 */
	private JButton crearBoton(String texto) {
		JButton btn = new JButton(texto);
		btn.setFont(FONT_BOTON);
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setBackground(new Color(255, 140, 0));
		btn.setForeground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setMargin(new Insets(8, 16, 8, 16));

		btn.addActionListener(this);

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(Color.blue);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(new Color(255, 140, 0));
			}
		});

		return btn;
	}

	/**
	 * Método para inicializar y configurar la JTable del carrito.
	 */
	private void inicializarTabla() {
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
				case 0:
					return String.class;
				case 1:
					return Integer.class;
				case 2:
				case 3:
				case 4:
					return Float.class;
				default:
					return Object.class;
				}
			}
		};

		tablaCarrito = new JTable(model) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
					c.setBackground(new Color(245, 245, 220, 180));
				}
				if (!isRowSelected(row)) {
					c.setForeground(Color.BLACK);
				} else {
					c.setForeground(Color.blue);
				}
				// Padding en celdas
				if (c instanceof JComponent) {
					((JComponent) c).setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V,
							PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
				}
				// Alinear números a la derecha
				if (column >= 1 && c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
				} else if (c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
				}
				return c;
			}
		};

		tablaCarrito.setFont(FONT_TABLA_CELDA);
		tablaCarrito.setRowHeight(tablaCarrito.getRowHeight() + 10);
		tablaCarrito.setGridColor(UIManager.getColor("Table.gridColor"));
		tablaCarrito.setShowGrid(false);
		tablaCarrito.setShowHorizontalLines(true);
		tablaCarrito.setShowVerticalLines(false);
		tablaCarrito.setIntercellSpacing(new Dimension(0, 1));
		tablaCarrito.setAutoCreateRowSorter(true);
		tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaCarrito.setOpaque(false);

		// Cabecera de la tabla
		JTableHeader header = tablaCarrito.getTableHeader();
		header.setFont(FONT_TABLA_HEADER);
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);

		final Color HEADER_BACKGROUND = new Color(210, 180, 140);
		final Color HEADER_FOREGROUND = new Color(101, 67, 33);
		final Color HEADER_BORDER_COLOR = new Color(139, 69, 19);
		final Font HEADER_FONT = FONT_TABLA_HEADER;
		final int HEADER_VPADDING = PADDING_TABLA_CELDA_V / 2;
		final int HEADER_HPADDING = PADDING_TABLA_CELDA_H;

		header.setDefaultRenderer(new TableCellRenderer() {
			private final TableCellRenderer defaultRenderer = tablaCarrito.getTableHeader().getDefaultRenderer();

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (c instanceof JLabel) {
					JLabel label = (JLabel) c;
					label.setFont(HEADER_FONT);
					label.setBackground(HEADER_BACKGROUND);
					label.setForeground(HEADER_FOREGROUND);
					label.setOpaque(true);

					Border paddingBorder = BorderFactory.createEmptyBorder(HEADER_VPADDING, HEADER_HPADDING,
							HEADER_VPADDING, HEADER_HPADDING);
					Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, HEADER_BORDER_COLOR);
					label.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

					label.setHorizontalAlignment(SwingConstants.CENTER);
				}
				return c;
			}
		});

		TableColumnModel tcm = tablaCarrito.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(250);
		tcm.getColumn(1).setPreferredWidth(80);
		tcm.getColumn(1).setMaxWidth(100);
		tcm.getColumn(2).setPreferredWidth(120);
		tcm.getColumn(3).setPreferredWidth(120);
		tcm.getColumn(4).setPreferredWidth(130);
	}

	/**
	 * Configura los renderers para las celdas de la tabla (formato moneda,
	 * alineación).
	 */
	private void configurarRenderersTabla() {
		// Este método ya no es necesario ya que la personalización de las celdas
		// se realiza en el prepareRenderer() del JTable anónimo en inicializarTabla().
	}

	/**
	 * Calcula el total de la compra y llena la tabla con los datos. Actualiza el
	 * label del total.
	 */
	private void calcularYMostrarTotal() {
		float totalCompraCalculado = 0;
		model.setRowCount(0);

		if (localListaCompra == null || localListaCompra.isEmpty()) {
			lblTotalCompra.setText("Total: " + currencyFormat.format(0));
			return;
		}

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
				System.err.println("Advertencia: No se encontró el artículo con ID " + com.getId_art());
			}
		}

		if (localPedido != null) {
			localPedido.setTotal(totalCompraCalculado);
		}

		lblTotalCompra.setText("Total: " + currencyFormat.format(totalCompraCalculado));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnComprar)) {
			int confirm = JOptionPane.showConfirmDialog(this,
					"¿Estás seguro de que quieres finalizar la compra por un total de "
							+ currencyFormat.format(localPedido.getTotal()) + "?",
					"Confirmar Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (confirm == JOptionPane.YES_OPTION) {
				fullCompra();
			}
		} else if (e.getSource().equals(btnVolver)) {
			this.dispose();
		}
	}

	/**
	 * Lógica para procesar la compra final. Guarda el pedido y las compras
	 * asociadas en la base de datos.
	 */
	private void fullCompra() {
		if (localPedido == null || localListaCompra == null || localListaCompra.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error: No hay datos de pedido o el carrito está vacío.",
					"Error Interno", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Principal.guardarPedido(localPedido);
			Principal.guardarCompra(localListaCompra);

			JOptionPane.showMessageDialog(this,
					"¡Compra realizada con éxito!\nID de Pedido: " + localPedido.getId_ped(), "Compra Finalizada",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this,
					"Error al procesar la compra:\n" + ex.getMessage()
							+ "\nPor favor, inténtalo de nuevo o contacta con soporte.",
					"Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error inesperado durante la compra:\n" + ex.getMessage(),
					"Error Inesperado", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

}