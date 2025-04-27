package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;
import controlador.Principal;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTable tableArticulo;
	private JButton btnUsuario, btnCompra, btnAdmin;
	private JLabel lblTitulo, lblLogo;
	private DefaultTableModel model;
	private JTextField quantityEditorField;
	private BufferedImage backgroundImage;
	private JLabel lblMartilloAnime;
	private Cliente localClien;
	private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Color COLOR_VALIDATION_ERROR = Color.RED;
	private static final int PADDING_GENERAL = 15;
	private static final int PADDING_INTERNO = 8;
	private static final int PADDING_TABLA_CELDA_V = 5;
	private static final int PADDING_TABLA_CELDA_H = 10;

	public VistaTienda(Cliente clien, JFrame owner) {
		super(owner, "DYE TOOLS - Store", true);
		this.localClien = clien;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		try {
			backgroundImage = ImageIO.read(getClass().getResource("/imagenes/fondoMadera.jpg"));
		} catch (IOException e) {
			e.getMessage();
			getContentPane().setBackground(new Color(240, 240, 240));
		}
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
		getContentPane().setLayout(new BorderLayout(PADDING_GENERAL, PADDING_GENERAL));
		((JPanel) getContentPane()).setBorder(
				BorderFactory.createEmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));
		initComponents();
		configurarTabla();
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();
		pack();
		setMinimumSize(new Dimension(900, 550));
		setLocationRelativeTo(owner);
	}

	private void initComponents() {
		JPanel panelCabecera = new JPanel();
		// Establecer BoxLayout para apilar elementos verticalmente
		panelCabecera.setLayout(new BoxLayout(panelCabecera, BoxLayout.Y_AXIS));
		panelCabecera.setBackground(Color.WHITE); // Color base (se hará transparente)
		panelCabecera.setOpaque(false); // Hacer el panel principal transparente
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(PADDING_INTERNO, 0, PADDING_GENERAL, 0));

		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING_INTERNO * 2, 0));
		panelSuperior.setOpaque(false);
		// Añadir elementos al panel superior
		lblLogo = new JLabel(cargarIcono("/iconos/tienda_logo.png", 64, 64));
		panelSuperior.add(lblLogo);

		lblTitulo = new JLabel("DYE TOOLS - Catalog");
		lblTitulo.setFont(FONT_TITULO);
		panelSuperior.add(lblTitulo);

		lblMartilloAnime = new JLabel(cargarIcono("/imagenes/martilloanime.jpg", 100, 100));
		panelSuperior.add(lblMartilloAnime);

		JLabel lblCantidadInstruccion = new JLabel("Enter the desired item quantities to start your order");
		lblCantidadInstruccion.setFont(new Font("Tahoma", Font.BOLD, 14));
		// Centrar la etiqueta horizontalmente dentro del BoxLayout
		lblCantidadInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Asegurar que el panel superior también se centre
		panelSuperior.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelCabecera.add(panelSuperior); // Añadir el grupo superior primero
		panelCabecera.add(Box.createVerticalStrut(PADDING_INTERNO)); // Añadir un pequeño espacio vertical
		panelCabecera.add(lblCantidadInstruccion); // Añadir la etiqueta de instrucciones debajo

		// Añadir la cabecera completa al layout principal (BorderLayout.NORTH)
		getContentPane().add(panelCabecera, BorderLayout.NORTH);

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(PADDING_GENERAL, 0, 0, 0));
		panelBotones.setOpaque(false);
		btnUsuario = crearBoton("My Account", "/iconos/user.png");
		btnAdmin = crearBoton("Admin Panel", "/iconos/admin.png");
		btnCompra = crearBoton("Checkout", "/iconos/cart.png");
		btnCompra.putClientProperty("JButton.buttonType", "primary");
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

	private JButton crearBoton(String texto, String iconoPath) {
		JButton btn = new JButton(texto);
		btn.setFont(FONT_BOTON);
		ImageIcon originalIcon = cargarIcono(iconoPath, 32, 32);
		ImageIcon transparentIcon = makeTransparent(originalIcon, new Color(255, 255, 255));
		btn.setIcon(transparentIcon);
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setBackground(new Color(255, 140, 0));
		btn.setForeground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setMargin(new Insets(PADDING_INTERNO, PADDING_INTERNO * 2, PADDING_INTERNO, PADDING_INTERNO * 2));
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

	private ImageIcon makeTransparent(ImageIcon imageIcon, final Color color) {
		Image image = imageIcon.getImage();
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		for (int i = 0; i < bufferedImage.getWidth(); i++) {
			for (int j = 0; j < bufferedImage.getHeight(); j++) {
				if (bufferedImage.getRGB(i, j) == color.getRGB()) {
					bufferedImage.setRGB(i, j, 0x00FFFFFF);
				}
			}
		}
		return new ImageIcon(bufferedImage);
	}

	private void configurarTabla() {
		model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return Integer.class;
				case 3:
					return Double.class;
				case 4:
					return Double.class;
				case 5:
					return Integer.class;
				case 6:
					return Integer.class;
				default:
					return String.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6;
			}
		};
		model.addColumn("ID_ART");
		model.addColumn("Nombre");
		model.addColumn("Descripción");
		model.addColumn("Precio (€)");
		model.addColumn("Oferta (%)");
		model.addColumn("Stock");
		model.addColumn("Cantidad");
		tableArticulo = new JTable(model) {
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
				if (c instanceof JComponent) {
					((JComponent) c).setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V,
							PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
				}
				if (column >= 3 && column <= 6 && c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
				} else if (c instanceof JLabel) {
					((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
				}
				return c;
			}
		};
		tableArticulo.setFont(FONT_TABLA_CELDA);
		tableArticulo.setRowHeight(tableArticulo.getRowHeight() + PADDING_INTERNO * 2);
		tableArticulo.setGridColor(UIManager.getColor("Table.gridColor"));
		tableArticulo.setShowGrid(false);
		tableArticulo.setShowHorizontalLines(true);
		tableArticulo.setShowVerticalLines(false);
		tableArticulo.setIntercellSpacing(new Dimension(0, 1));
		tableArticulo.setAutoCreateRowSorter(true);
		tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = tableArticulo.getTableHeader();
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
			private final TableCellRenderer defaultRenderer = tableArticulo.getTableHeader().getDefaultRenderer();

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
		TableColumnModel columnModel = tableArticulo.getColumnModel();
		try {
			TableColumn columnToHide = tableArticulo.getColumn("ID_ART");
			columnModel.removeColumn(columnToHide);
		} catch (IllegalArgumentException e) {
			e.getMessage();
			/*
			 * try { if (columnModel.getColumnCount() > 0) { System.err
			 * .println("Aviso: Se intentó remover la primera columna visible como fallback para ID_ART."
			 * ); } } catch (Exception ex) {
			 * System.err.println("Fallo al remover columna por índice."); }
			 */
		}
		int cantidadViewIndex = 5;
		quantityEditorField = new JTextField();
		quantityEditorField.setHorizontalAlignment(SwingConstants.RIGHT);
		quantityEditorField.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
		quantityEditorField.getDocument().addDocumentListener(new DocumentListener() {
			private void verificarNumero() {
				String text = quantityEditorField.getText();
				boolean valido = text.matches("[0-9]*");
				if (!valido && !text.isEmpty()) {
					quantityEditorField.setForeground(COLOR_VALIDATION_ERROR);
				} else {
					quantityEditorField.setForeground(UIManager.getColor("TextField.foreground"));
					if (valido && !text.isEmpty()) {
						try {
							int filaEditada = tableArticulo.getEditingRow();
							int filaModelo = tableArticulo.convertRowIndexToModel(filaEditada);
							if (filaModelo != -1) {
								int cantidad = Integer.parseInt(text);
								int stock = (Integer) model.getValueAt(filaModelo, 5);
								if (cantidad > stock || cantidad < 0) {
									quantityEditorField.setForeground(COLOR_VALIDATION_ERROR);
								}
							}
						} catch (Exception ignored) {
							quantityEditorField.setForeground(COLOR_VALIDATION_ERROR);
						}
					}
				}
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
				verificarNumero();
			}
		});
		columnModel.getColumn(cantidadViewIndex).setCellEditor(new DefaultCellEditor(quantityEditorField));
		model.addTableModelListener(e -> {
			if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
				int filaModelo = e.getFirstRow();
				if (filaModelo < 0 || filaModelo >= model.getRowCount())
					return;
				Object cantidadObj = model.getValueAt(filaModelo, 6);
				Object stockObj = model.getValueAt(filaModelo, 5);
				if (cantidadObj == null || cantidadObj.toString().isEmpty()) {
					Object currentValue = model.getValueAt(filaModelo, 6);
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
								JOptionPane
										.showMessageDialog(VistaTienda.this,
												"La cantidad excede el stock disponible (" + stockDisponible
														+ "). Se ajustará.",
												"Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
								model.setValueAt(stockDisponible, filaModelo, 6);
							});
						}
					} catch (NumberFormatException ex) {
						SwingUtilities.invokeLater(() -> model.setValueAt(0, filaModelo, 6));
					}
				}
			}
		});
		tableArticulo.setOpaque(false);
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		scrollPane.setOpaque(false);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2));
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(new Color(245, 222, 179));
		tableArticulo.setOpaque(false);
	}

	public void cargarDatosTabla() {
		if (tableArticulo.isEditing()) {
			tableArticulo.getCellEditor().stopCellEditing();
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
		}
	}

	private void ajustarAnchosColumnaTabla() {
		tableArticulo.revalidate();
		if (tableArticulo.getWidth() == 0) {
			SwingUtilities.invokeLater(this::ajustarAnchosColumnaTabla);
			return;
		}
		TableColumnModel columnModel = tableArticulo.getColumnModel();
		final int PADDING = PADDING_TABLA_CELDA_H * 2;
		for (int columnView = 0; columnView < tableArticulo.getColumnCount(); columnView++) {
			TableColumn tableColumn = columnModel.getColumn(columnView);
			int headerWidth = getColumnHeaderWidth(tableArticulo, columnView);
			int contentWidth = getMaximumColumnContentWidth(tableArticulo, columnView);
			int preferredWidth = Math.max(headerWidth, contentWidth) + PADDING;
			if (columnView == 0)
				tableColumn.setMinWidth(150);
			if (columnView == 1)
				tableColumn.setMinWidth(200);
			if (columnView == 2)
				tableColumn.setMinWidth(75);
			if (columnView == 3)
				tableColumn.setMinWidth(75);
			if (columnView == 4)
				tableColumn.setMinWidth(65);
			if (columnView == 5) {
				tableColumn.setMinWidth(75);
				tableColumn.setMaxWidth(110);
			}
			tableColumn.setPreferredWidth(preferredWidth);
		}
		tableArticulo.getTableHeader().resizeAndRepaint();
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnUsuario)
			mostrarVistaUsuario();
		else if (source == btnAdmin)
			mostrarVistaAdmin();
		else if (source == btnCompra)
			abrirCarrito();
	}

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
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();
	}

	private void abrirCarrito() {
		if (localClien.getMetodo_pago() == null && localClien.getNum_cuenta() == null) {
			JOptionPane.showMessageDialog(this, "No tiene los credenciales necesarios para realizar una compra",
					"No se puede realizar una compra", JOptionPane.WARNING_MESSAGE);
		}
		if (tableArticulo.isEditing()) {
			if (!tableArticulo.getCellEditor().stopCellEditing()) {
				return;
			}
		}
		if (!validarCantidadesParaCarrito()) {
			JOptionPane.showMessageDialog(this, "Corrige cantidades inválidas antes de continuar.",
					"Cantidades Inválidas", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Pedido pedidoActual = new Pedido(Principal.obtenerUltimoIdPed(), localClien.getId_usu(), 0,
				LocalDateTime.now());
		List<Compra> comprasParaCarrito = recopilarCompras(pedidoActual.getId_ped());
		if (comprasParaCarrito.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Añade artículos (cantidad > 0).", "Pedido vacio",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		VistaCarrito carritoDialog = new VistaCarrito(this, comprasParaCarrito, pedidoActual);
		carritoDialog.setVisible(true);
		cargarDatosTabla();
		ajustarAnchosColumnaTabla();
	}

	private boolean validarCantidadesParaCarrito() {
		if (tableArticulo.isEditing()) {
			if (!tableArticulo.getCellEditor().stopCellEditing()) {
				return false;
			}
		}
		for (int i = 0; i < model.getRowCount(); i++) {
			Object cantidadObj = model.getValueAt(i, 6);
			if (cantidadObj != null && !cantidadObj.toString().isEmpty()) {
				try {
					int cantidad = Integer.parseInt(cantidadObj.toString());
					int stock = (Integer) model.getValueAt(i, 5);
					if (cantidad < 0 || cantidad > stock) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}

	private List<Compra> recopilarCompras(int idePed) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			Object valorId = model.getValueAt(i, 0);
			Object valorCantidad = model.getValueAt(i, 6);
			if (valorCantidad != null && valorId instanceof Integer) {
				try {
					int cantidadFinal = Integer.parseInt(valorCantidad.toString());
					if (cantidadFinal > 0) {
						int idArt = (Integer) valorId;
						Compra palCarro = new Compra(idArt, idePed, cantidadFinal);
						listaCompra.add(palCarro);
					}
				} catch (NumberFormatException e) {
					e.getMessage();
				}
			}
		}
		return listaCompra;
	}

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
			BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = placeholder.createGraphics();
			try {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fillRect(0, 0, width, height);
				g2d.setColor(Color.DARK_GRAY);
				g2d.drawRect(0, 0, width - 1, height - 1);
				g2d.setColor(Color.RED);
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(width / 4, height / 4, 3 * width / 4, 3 * height / 4);
				g2d.drawLine(width / 4, 3 * height / 4, 3 * width / 4, height / 4);
			} finally {
				g2d.dispose();
			}
			return new ImageIcon(placeholder);
		}
	}

	private ImageIcon cargarIcono(String path) {
		return cargarIcono(path, 16, 16);
	}

}