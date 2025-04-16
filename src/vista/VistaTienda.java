package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;
import controlador.Principal; // Asumiendo métodos de acceso a datos

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
import java.awt.image.BufferedImage; // Importado para el icono placeholder
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Importar FlatLaf (RECUERDA CONFIGURARLO EN TU main)
// import com.formdev.flatlaf.FlatClientProperties; // Para propiedades específicas
// import com.formdev.flatlaf.extras.FlatSVGIcon; // Si usas iconos SVG

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
    private BufferedImage backgroundImage;


	// --- Datos ---
	private Cliente localClien;

	// --- Constantes de Estilo ---
	private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24); // Ligeramente más grande
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);

	// Color de acento (se usará implícitamente por FlatLaf para botón primario)
	// private static final Color COLOR_ACCENT = new Color(0x005A9C); // Ya no
	// necesario si se usa buttonType=primary
	private static final Color COLOR_VALIDATION_ERROR = Color.RED;

	// Padding y Espaciado
	private static final int PADDING_GENERAL = 15;
	private static final int PADDING_INTERNO = 8;
	private static final int PADDING_TABLA_CELDA_V = 5; // Más padding vertical en celdas
	private static final int PADDING_TABLA_CELDA_H = 10; // Más padding horizontal en celdas

	/**
	 * Constructor
	 */
	public VistaTienda(Cliente clien, Frame owner) {
		super(owner, "DYE TOOLS - Tienda", true);
		this.localClien = clien;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	    try {
            // *** CAMBIA "ruta/a/tu/imagen.jpg" a la ruta real de tu imagen ***
            backgroundImage = ImageIO.read(getClass().getResource("/imagenes/fondoMadera.jpg"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            // Puedes establecer un color de fondo alternativo si la imagen no carga
            getContentPane().setBackground(new Color(240, 240, 240));
        }

        // *** Usar un JPanel con pintura personalizada como ContentPane ***
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Dibujar la imagen de fondo para que cubra todo el panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });

		// --- Layout Principal y Padding ---
		getContentPane().setLayout(new BorderLayout(PADDING_GENERAL, PADDING_GENERAL));
		((JPanel) getContentPane()).setBorder(
				BorderFactory.createEmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));

		// --- Inicializar Componentes ---
		initComponents();
		configurarTabla();
		cargarDatosTabla();
		// Ajustar anchos DESPUÉS de cargar datos
		ajustarAnchosColumnaTabla();

		// --- Configuración Final Ventana ---
		pack(); // Ajustar al contenido
		setMinimumSize(new Dimension(750, 550)); // Ajustar tamaño mínimo si es necesario
		setLocationRelativeTo(owner); // Centrar
	}

	/**
	 * Inicializa componentes básicos (Título, Logo, Botones).
	 */
	private void initComponents() {
		// --- Panel Cabecera (NORTH) ---
		JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING_INTERNO * 2, 0)); // Más espacio
		panelCabecera.setBackground(Color.WHITE);
		panelCabecera.setOpaque(false);
																										// entre logo y
																										// título
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(PADDING_INTERNO, 0, PADDING_GENERAL, 0)); // Espacio
																											// arriba/abajo

		// Logo (Placeholder)
		// *** CAMBIO: Aumentar tamaño del logo aún más (ej. a 64x64) ***
		lblLogo = new JLabel(cargarIcono("/iconos/tienda_logo.png", 64, 64)); // Ajusta tamaño según necesidad
		panelCabecera.add(lblLogo);

		// Título
		lblTitulo = new JLabel("DYE TOOLS - Catálogo");
		lblTitulo.setFont(FONT_TITULO);
		panelCabecera.add(lblTitulo);

		getContentPane().add(panelCabecera, BorderLayout.NORTH);

		// --- Panel Botones (SOUTH) ---
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(PADDING_GENERAL, 0, 0, 0)); // Espacio arriba
		panelBotones.setOpaque(false);
		// Crear botones
		btnUsuario = crearBoton("Mi Cuenta", "/iconos/user.png");
		btnAdmin = crearBoton("Admin Panel", "/iconos/admin.png");
		btnCompra = crearBoton("Ver Carrito", "/iconos/cart.png");
		
		

		// *** CAMBIO: Estilo Botón Carrito usando Propiedad FlatLaf ***
		// Indicar que es el botón de acción principal, el tema (FlatLaf) se encargará
		// del estilo
		// para asegurar buena visibilidad y coherencia.
		btnCompra.putClientProperty("JButton.buttonType", "primary");
		// Quitar estilos manuales de color para no interferir con el tema
		// btnCompra.setBackground(COLOR_ACCENT); // <- Quitar
		// btnCompra.setForeground(Color.WHITE); // <- Quitar

		// Visibilidad Admin
		btnAdmin.setVisible(localClien != null && localClien.isEsAdmin());

		// Añadir botones al panel (Alternativa BoxLayout)
		panelBotones.add(btnUsuario);
		if (btnAdmin.isVisible()) {
			panelBotones.add(Box.createHorizontalStrut(PADDING_INTERNO));
			panelBotones.add(btnAdmin);
		}
		panelBotones.add(Box.createHorizontalGlue()); // Empujar a la derecha
		panelBotones.add(btnCompra);

		getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}

	/**
	 * Crea y configura un JButton estándar.
	 */
	private JButton crearBoton(String texto, String iconoPath) {
	    JButton btn = new JButton(texto);
	    btn.setFont(FONT_BOTON);
	   // btn.setIcon(cargarIcono(iconoPath, 16, 16));
	    ImageIcon originalIcon = cargarIcono(iconoPath, 32, 32);
        // Crear un ImageIcon con fondo transparente
        ImageIcon transparentIcon = makeTransparent(originalIcon, new Color(255, 255, 255)); // Asume blanco como fondo original
        btn.setIcon(transparentIcon);

	    
	    // Configuración para asegurar que los colores se muestren
	    btn.setOpaque(true);
	    btn.setContentAreaFilled(true); // Necesario para FlatLaf
	    btn.setBackground(new Color(255, 140, 0)); // COLOR_BOTON_PRIMARIO de VistaLogIn
	    btn.setForeground(Color.WHITE); // COLOR_TEXTO_BOTON_PRIMARIO
	    btn.setBorderPainted(false); // Sin borde por defecto, como btnLogIn
	    // Alternativa: Usa un borde como btnSignIn
	    // btn.setBorder(new LineBorder(new Color(52, 152, 219), 1));
	    
	    btn.setFocusPainted(false);
	    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    btn.setMargin(new Insets(PADDING_INTERNO, PADDING_INTERNO * 2, PADDING_INTERNO, PADDING_INTERNO * 2));
	    btn.addActionListener(this);
	    
	    // Añadir efecto hover, como en VistaLogIn
	    btn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btn.setBackground(Color.blue); // COLOR_BOTON_PRIMARIO_HOVER
	        }
	        
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btn.setBackground(new Color(255, 140, 0)); // COLOR_BOTON_PRIMARIO
	        }
	    });
	    
	    return btn;
	}
	
	
	   // --- Método para hacer un color transparente en un ImageIcon ---
    private ImageIcon makeTransparent(ImageIcon imageIcon, final Color color) {
        Image image = imageIcon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                if (bufferedImage.getRGB(i, j) == color.getRGB()) {
                    bufferedImage.setRGB(i, j, 0x00FFFFFF); // Hacer el píxel transparente
                }
            }
        }

        return new ImageIcon(bufferedImage);
    }
	/**
	 * Configura la JTable (modelo, columnas, renderers, editor, listener).
	 */
	private void configurarTabla() {
		// --- Modelo ---
		model = new DefaultTableModel() {
			// *** CAMBIO: Actualizar índices por eliminación de ID_ART de la vista ***
			// Indices del MODELO: ID(0, oculto), Nombre(1), Desc(2), Precio(3), Oferta(4),
			// Stock(5), Cant(6)
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
				// La editabilidad se basa en el índice del MODELO
				return column == 6; // Modelo col 6 = Cantidad
			}
		};
		// *** CAMBIO: Mantener ID_ART en el MODELO (índice 0) pero se OCULTARÁ de la
		// vista ***
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
				// 'column' aquí es el índice del MODELO
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
				// Alinear números a la derecha (basado en índice del MODELO)
				if (column >= 3 && column <= 6 && c instanceof JLabel) { // Precio, Oferta, Stock, Cantidad
					((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
				} else if (c instanceof JLabel) { // Nombre, Descripción
					((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
				}
				return c;
			}
		};
		
		

		// --- Propiedades Tabla ---
		tableArticulo.setFont(FONT_TABLA_CELDA);
		tableArticulo.setRowHeight(tableArticulo.getRowHeight() + PADDING_INTERNO * 2); // Mayor altura
		tableArticulo.setGridColor(UIManager.getColor("Table.gridColor"));
		tableArticulo.setShowGrid(false);
		tableArticulo.setShowHorizontalLines(true);
		tableArticulo.setShowVerticalLines(false);
		tableArticulo.setIntercellSpacing(new Dimension(0, 1));
		tableArticulo.setAutoCreateRowSorter(true);
		tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// --- Cabecera Tabla ---
		JTableHeader header = tableArticulo.getTableHeader();
		header.setFont(FONT_TABLA_HEADER);
		header.setReorderingAllowed(false); // No permitir reordenar columnas manualmente
		// *** CAMBIO: No permitir redimensionar columnas manualmente ***
		header.setResizingAllowed(false);
		
		// *** ESTILO CABECERA CON CLASE ANÓNIMA *** quitar esto para el header
		// Definir colores y fuente para la cabecera (pueden ser constantes de VistaTienda)
		final Color HEADER_BACKGROUND = new Color(210, 180, 140); // Tan (marrón claro)
		final Color HEADER_FOREGROUND = new Color(101, 67, 33);  // Marrón oscuro para texto
		final Color HEADER_BORDER_COLOR = new Color(139, 69, 19); // Marrón silla de montar
		final Font HEADER_FONT = FONT_TABLA_HEADER; // Usar tu constante de fuente
		final int HEADER_VPADDING = PADDING_TABLA_CELDA_V / 2;
		final int HEADER_HPADDING = PADDING_TABLA_CELDA_H;
		
		// Aplicar un Renderer Personalizado usando una Clase Anónima
		header.setDefaultRenderer(new TableCellRenderer() {
		    // Obtener el renderer por defecto para usarlo como base (usualmente un JLabel)
		    private final TableCellRenderer defaultRenderer = tableArticulo.getTableHeader().getDefaultRenderer();

		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		                                                   boolean isSelected, boolean hasFocus,
		                                                   int row, int column) {
		        // Usar el componente del renderer por defecto
		        Component c = defaultRenderer.getTableCellRendererComponent(table, value,
		                                                                  isSelected, hasFocus,
		                                                                  row, column);
		        if (c instanceof JLabel) {
		            JLabel label = (JLabel) c;
		            label.setFont(HEADER_FONT);
		            label.setBackground(HEADER_BACKGROUND);
		            label.setForeground(HEADER_FOREGROUND);
		            label.setOpaque(true); // ¡Importante para que se vea el fondo!

		            // Crear borde compuesto: línea inferior + padding
		            Border paddingBorder = BorderFactory.createEmptyBorder(HEADER_VPADDING, HEADER_HPADDING, HEADER_VPADDING, HEADER_HPADDING);
		            Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, HEADER_BORDER_COLOR); // Línea solo abajo
		            label.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder)); // Línea exterior, padding interior

		            label.setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
		        }
		        return c;
		    }
		});
		
		// --- Configuración Columnas (OCULTAR ID_ART de la VISTA) ---
		TableColumnModel columnModel = tableArticulo.getColumnModel();
		// Obtener la columna que corresponde al índice 0 del MODELO (ID_ART)
		TableColumn idColumn = columnModel.getColumn(model.getColumnCount() - 1); // Esto es propenso a errores si se
																					// reordena el modelo
																					// Mejorar obteniendo por
																					// identificador si es posible, o
																					// asegurarse que sea la primera
		// *** CAMBIO: Ocultar la columna ID_ART de la VISTA ***
		// Es crucial hacer esto ANTES de referirse a otros índices de vista
		// OJO: Si se permite reordenar columnas, obtener la columna por identificador
		// es más robusto
		try {
			TableColumn columnToHide = tableArticulo.getColumn("ID_ART"); // Obtener por nombre del modelo
			// Removerla del modelo de columnas de la vista
			columnModel.removeColumn(columnToHide);
			// Alternativa si falla getColumn(String): remover por índice 0 de vista (si es
			// la primera añadida al modelo y no se ha reordenado)
			// columnModel.removeColumn(columnModel.getColumn(0));
		} catch (IllegalArgumentException e) {
			System.err.println("No se pudo encontrar la columna 'ID_ART' para ocultarla.");
			// Intentar remover la primera columna visible, asumiendo que es ID_ART
			try {
				if (columnModel.getColumnCount() > 0) {
					// CUIDADO: Esto removería la primera columna visible, sea cual sea
					// columnModel.removeColumn(columnModel.getColumn(0));
					System.err
							.println("Aviso: Se intentó remover la primera columna visible como fallback para ID_ART.");
				}
			} catch (Exception ex) {
				System.err.println("Fallo al remover columna por índice.");
			}
		}

		// --- Editor Personalizado para Cantidad ---
		// El índice de vista para "Cantidad" ahora es 5 (porque ID_ART en modelo 0 se
		// ocultó)
		// Indices VISTA: Nombre(0), Desc(1), Precio(2), Oferta(3), Stock(4),
		// Cantidad(5)
		int cantidadViewIndex = 5; // Índice de la columna "Cantidad" en la *vista*

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
								int stock = (Integer) model.getValueAt(filaModelo, 5); // Stock Modelo col 5
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
		// Aplicar editor a la columna de vista correcta (índice 5)
		columnModel.getColumn(cantidadViewIndex).setCellEditor(new DefaultCellEditor(quantityEditorField));

		// --- Listener para Validar al Terminar Edición (en el Modelo) ---
		model.addTableModelListener(e -> {
			// Se sigue refiriendo a la columna 6 del MODELO (Cantidad)
			if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
				int filaModelo = e.getFirstRow();
				if (filaModelo < 0 || filaModelo >= model.getRowCount())
					return;

				Object cantidadObj = model.getValueAt(filaModelo, 6);
				Object stockObj = model.getValueAt(filaModelo, 5); // Stock Modelo col 5

				if (cantidadObj == null || cantidadObj.toString().isEmpty()) {
					Object currentValue = model.getValueAt(filaModelo, 6);
					// Establecer a 0 solo si no es ya 0 o null para evitar bucles
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

		// --- ScrollPane ---
		/*JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		getContentPane().add(scrollPane, BorderLayout.CENTER);*/
		
		tableArticulo.setOpaque(false);
	    JScrollPane scrollPane = new JScrollPane(tableArticulo);
	    scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
	    scrollPane.setOpaque(false);
	   // scrollPane.getViewport().setOpaque(true);
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 2)); // Marrón oscuro sólido (color madera)
	    scrollPane.getViewport().setOpaque(true); // Asegúrate de que el viewport sea opaco
	   // scrollPane.getViewport().setBackground(new Color(245, 222, 179, 200)); // Beige claro semitransparente (similar a madera clara)
	    scrollPane.getViewport().setBackground(new Color(245, 222, 179)); // Beige claro opaco (similar a madera clara)
	    tableArticulo.setOpaque(false); // La tabla en sí no necesita ser opaca si el viewport lo es
	    
	    
	}

	/**
	 * Carga (o recarga) los datos de los artículos desde la base de datos a la
	 * tabla.
	 */
	public void cargarDatosTabla() {
		if (tableArticulo.isEditing()) {
			tableArticulo.getCellEditor().stopCellEditing();
		}
		model.setRowCount(0);
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		if (articulos != null && !articulos.isEmpty()) {
			for (Articulo art : articulos.values()) {
				if (art.getStock() > 0) {
					// *** CAMBIO: Asegurar que el ID_ART se añade en la posición 0 del Object[] ***
					model.addRow(new Object[] { art.getId_art(), // Modelo Col 0 (Oculto en vista)
							art.getNombre(), // Modelo Col 1
							art.getDescripcion(), // Modelo Col 2
							art.getPrecio(), // Modelo Col 3
							art.getOferta(), // Modelo Col 4
							art.getStock(), // Modelo Col 5
							0 // Modelo Col 6 - Cantidad inicial 0
					});
				}
			}
		} else {
			System.out.println("No se encontraron artículos.");
		}
		// Ajustar anchos DESPUÉS de que los datos estén en la tabla y esta sea visible
		// SwingUtilities.invokeLater(this::ajustarAnchosColumnaTabla);
	}

	/**
	 * Ajusta el ancho preferido de cada columna visible de la tabla para que se
	 * ajuste al contenido. Llamar DESPUÉS de cargar datos y de que la tabla sea
	 * visible.
	 */
	private void ajustarAnchosColumnaTabla() {
		// Asegurarse de que la tabla tenga un tamaño antes de calcular
		tableArticulo.revalidate();
		if (tableArticulo.getWidth() == 0) {
			// Si la tabla aún no tiene tamaño, posponer o usar pack() antes
			SwingUtilities.invokeLater(this::ajustarAnchosColumnaTabla);
			return;
		}

		TableColumnModel columnModel = tableArticulo.getColumnModel();
		final int PADDING = PADDING_TABLA_CELDA_H * 2;

		for (int columnView = 0; columnView < tableArticulo.getColumnCount(); columnView++) { // Iterar vistas
			TableColumn tableColumn = columnModel.getColumn(columnView);
			int headerWidth = getColumnHeaderWidth(tableArticulo, columnView);
			int contentWidth = getMaximumColumnContentWidth(tableArticulo, columnView);
			int preferredWidth = Math.max(headerWidth, contentWidth) + PADDING;

			// Indices VISTA: Nombre(0), Desc(1), Precio(2), Oferta(3), Stock(4),
			// Cantidad(5)
			if (columnView == 0)
				tableColumn.setMinWidth(150); // Nombre
			if (columnView == 1)
				tableColumn.setMinWidth(200); // Descripción
			if (columnView == 2)
				tableColumn.setMinWidth(75); // Precio
			if (columnView == 3)
				tableColumn.setMinWidth(75); // Oferta
			if (columnView == 4)
				tableColumn.setMinWidth(65); // Stock
			if (columnView == 5) { // Cantidad
				tableColumn.setMinWidth(75);
				tableColumn.setMaxWidth(110); // Limitar ancho máximo de cantidad
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}
		// Forzar redibujo de la cabecera para aplicar anchos
		tableArticulo.getTableHeader().resizeAndRepaint();
	}

	// --- Métodos Auxiliares para Ancho de Columna (Sin cambios) ---
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

	// --- Manejo de Acciones ---
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
		cargarDatosTabla();
		ajustarAnchosColumnaTabla(); // Reajustar
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
			JOptionPane.showMessageDialog(this, "Añade artículos al carrito (cantidad > 0).", "Carrito Vacío",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		VistaCarrito carritoDialog = new VistaCarrito(this, comprasParaCarrito, pedidoActual);
		carritoDialog.setVisible(true);
		cargarDatosTabla();
		ajustarAnchosColumnaTabla(); // Reajustar
	}

	/**
	 * Verifica si todas las cantidades son válidas.
	 */
	private boolean validarCantidadesParaCarrito() {
		if (tableArticulo.isEditing()) {
			if (!tableArticulo.getCellEditor().stopCellEditing()) {
				return false;
			}
		}
		for (int i = 0; i < model.getRowCount(); i++) {
			Object cantidadObj = model.getValueAt(i, 6); // Cantidad Modelo Col 6
			if (cantidadObj != null && !cantidadObj.toString().isEmpty()) {
				try {
					int cantidad = Integer.parseInt(cantidadObj.toString());
					int stock = (Integer) model.getValueAt(i, 5); // Stock Modelo Col 5
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

	/**
	 * Recopila las compras basándose en el modelo de la tabla.
	 */
	private List<Compra> recopilarCompras(int idePed) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			// *** CAMBIO: Obtener ID del MODELO (col 0) y Cantidad del MODELO (col 6) ***
			Object valorId = model.getValueAt(i, 0); // ID Artículo (Modelo col 0)
			Object valorCantidad = model.getValueAt(i, 6); // Cantidad (Modelo col 6)

			if (valorCantidad != null && valorId instanceof Integer) {
				try {
					int cantidadFinal = Integer.parseInt(valorCantidad.toString());
					if (cantidadFinal > 0) { // Solo añadir si la cantidad es positiva
						int idArt = (Integer) valorId;
						Compra palCarro = new Compra(idArt, idePed, cantidadFinal);
						listaCompra.add(palCarro);
					}
				} catch (NumberFormatException e) {
					System.err.println("Error formato al recopilar compra fila " + i + ": " + valorCantidad);
				}
			}
		}
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
			System.err.println("Icono no encontrado: " + path);
			// Crear placeholder
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