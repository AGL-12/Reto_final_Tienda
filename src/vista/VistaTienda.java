package vista;

import modelo.Articulo;
import modelo.Cliente;
import modelo.Compra;
import modelo.Pedido;
import controlador.Principal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Importa FlatLaf si lo vas a usar (recuerda añadir la dependencia y configurarlo en tu main)
// import com.formdev.flatlaf.FlatClientProperties; // Para propiedades específicas de FlatLaf si es necesario

public class VistaTienda extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L; // Mantener si es necesario

	// --- Componentes de la UI ---
	private JTable tableArticulo;
	private JButton btnUsuario, btnCompra, btnAdmin;
	private JLabel lblTitulo;
	private DefaultTableModel model; // Hacerlo miembro de la clase para acceso fácil

	// --- Datos ---
	private Cliente localClien;

	// --- Constantes (opcional, para mejor mantenimiento) ---
	private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 18); // Fuente moderna
	private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
	private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
	private static final int PADDING_GENERAL = 10; // Espaciado general
	private static final int PADDING_INTERNO = 5; // Espaciado entre componentes

	/**
	 * Constructor de la ventana principal de la tienda.
	 * 
	 * @param clien El cliente que ha iniciado sesión.
	 * @param owner La ventana padre (normalmente el JFrame principal o la ventana
	 *              de login).
	 */
	public VistaTienda(Cliente clien, Frame owner) {
		// Configuración inicial del JDialog
		super(owner, "DYE TOOLS - Tienda", true); // Título más descriptivo, modal al owner
		this.localClien = clien;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Cierra solo esta ventana al pulsar la 'X'

		// --- Configuración del Layout Principal ---
		// Usamos BorderLayout para estructurar: Título(Norte), Tabla(Centro),
		// Botones(Sur)
		setLayout(new BorderLayout(PADDING_GENERAL, PADDING_GENERAL));
		// Añadimos un borde vacío alrededor de todo el contenido para dar aire
		((JPanel) getContentPane()).setBorder(
				BorderFactory.createEmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));

		// --- Inicializar Componentes ---
		initComponents();
		configurarTabla();

		// --- Configuración Final de la Ventana ---
		pack(); // Ajusta el tamaño de la ventana al contenido preferido de los componentes
		setMinimumSize(new Dimension(550, 400)); // Establece un tamaño mínimo razonable
		setLocationRelativeTo(owner); // Centra la ventana respecto al padre
	}

	/**
	 * Inicializa y configura los componentes básicos de la UI (Título, Botones).
	 */
	private void initComponents() {
		// --- Panel Título (NORTH) ---
		lblTitulo = new JLabel("DYE TOOLS - Catálogo", JLabel.CENTER); // Centrar título
		lblTitulo.setFont(FONT_TITULO);
		lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING_GENERAL, 0)); // Espacio debajo del título
		add(lblTitulo, BorderLayout.NORTH);

		// --- Panel Botones (SOUTH) ---
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING_INTERNO, 0)); // FlowLayout centrado
																									// con gaps H/V
		panelBotones.setBorder(BorderFactory.createEmptyBorder(PADDING_GENERAL, 0, 0, 0)); // Espacio encima de los
																							// botones

		// Botón Usuario
		btnUsuario = new JButton("Mi Cuenta");
		btnUsuario.setFont(FONT_BOTON);
		btnUsuario.setIcon(cargarIcono("/iconos/user.png")); // Placeholder - Carga tu icono!
		btnUsuario.addActionListener(this);
		panelBotones.add(btnUsuario);

		// Botón Admin (condicional)
		btnAdmin = new JButton("Admin Panel");
		btnAdmin.setFont(FONT_BOTON);
		btnAdmin.setIcon(cargarIcono("/iconos/admin.png")); // Placeholder - Carga tu icono!
		btnAdmin.addActionListener(this);
		// Visibilidad basada en el cliente (maneja el caso de cliente null también)
		btnAdmin.setVisible(localClien != null && localClien.isEsAdmin());
		panelBotones.add(btnAdmin);

		// Espaciador flexible entre grupos de botones (si Admin es visible)
		if (btnAdmin.isVisible()) {
			panelBotones.add(Box.createHorizontalStrut(50)); // Espacio fijo
			// O podrías tener un layout más complejo (ej GridBagLayout) para alinear a
			// izquierda y derecha
		}

		// Botón Compra
		btnCompra = new JButton("Ver Carrito");
		btnCompra.setFont(FONT_BOTON);
		btnCompra.setIcon(cargarIcono("/iconos/cart.png")); // Placeholder - Carga tu icono!
		btnCompra.addActionListener(this);
		panelBotones.add(btnCompra);

		add(panelBotones, BorderLayout.SOUTH);
	}

	/**
	 * Configura la JTable (modelo, columnas, renderers, listener).
	 */
	private void configurarTabla() {
		tableArticulo = new JTable();
		tableArticulo.setFont(FONT_TABLA_CELDA); // Fuente para las celdas
		tableArticulo.setRowHeight(tableArticulo.getRowHeight() + PADDING_INTERNO); // Más altura de fila
		tableArticulo.setIntercellSpacing(new Dimension(PADDING_INTERNO, PADDING_INTERNO / 2)); // Espaciado entre
																								// celdas

		// --- Modelo de la Tabla ---
		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L; // Mantener si es necesario

			// Define los tipos de datos de las columnas para una correcta edición y
			// renderizado
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return Integer.class; // id_art (oculto)
				case 1:
					return String.class; // Nombre
				case 2:
					return String.class; // Descripción
				case 3:
					return Double.class; // Precio
				case 4:
					return Double.class; // Oferta
				case 5:
					return Integer.class; // Stock
				case 6:
					return Integer.class; // Cantidad a comprar (editable)
				default:
					return Object.class;
				}
			}

			// Hacer editable solo la columna "Cantidad"
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6; // Solo la columna 6 (Cantidad) es editable
			}
		};
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (tableArticulo != null) {
					cargarDatosTabla();
				}
			}

		});

		// Añadir columnas al modelo (incluyendo id_art aunque se oculte)
		model.addColumn("ID_ART"); // Columna 0
		model.addColumn("Nombre"); // Columna 1
		model.addColumn("Descripción"); // Columna 2
		model.addColumn("Precio (€)"); // Columna 3 - Añadir unidad
		model.addColumn("Oferta (%)"); // Columna 4 - Añadir unidad
		model.addColumn("Stock"); // Columna 5
		model.addColumn("Cantidad"); // Columna 6

		tableArticulo.setModel(model);

		// --- Configuración de Columnas (Ocultar ID, Anchos) ---
		TableColumnModel columnModel = tableArticulo.getColumnModel();

		// Ocultar la columna ID_ART (columna 0)
		columnModel.removeColumn(columnModel.getColumn(0));
		// OJO: Aunque se quite de la vista, sigue existiendo en el *modelo* (índice 0).
		// Las referencias posteriores a columnas en la *vista* se desplazan (Nombre es
		// ahora 0 en vista, pero 1 en modelo)
		// Es más seguro referirse a columnas por su índice en el *modelo* al
		// obtener/establecer datos.

		// Ajustar anchos preferidos (ajusta estos valores según necesidad)
		columnModel.getColumn(0).setPreferredWidth(150); // Nombre (índice 0 en vista)
		columnModel.getColumn(1).setPreferredWidth(200); // Descripción (índice 1 en vista)
		columnModel.getColumn(2).setPreferredWidth(70); // Precio (índice 2 en vista)
		columnModel.getColumn(3).setPreferredWidth(70); // Oferta (índice 3 en vista)
		columnModel.getColumn(4).setPreferredWidth(60); // Stock (índice 4 en vista)
		columnModel.getColumn(5).setPreferredWidth(70); // Cantidad (índice 5 en vista)

		// --- Configuración Adicional de la Tabla ---
		tableArticulo.setAutoCreateRowSorter(true); // Permitir ordenar por columnas
		tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo seleccionar una fila/celda a la vez
		tableArticulo.setCellSelectionEnabled(false); // Seleccionar filas enteras mejor que celdas
		tableArticulo.setRowSelectionAllowed(true);

		// Configurar cabecera de la tabla
		JTableHeader header = tableArticulo.getTableHeader();
		header.setFont(FONT_TABLA_HEADER);
		header.setReorderingAllowed(false);

		// Para que pinte bien el fondo (especialmente con L&F modernos)
		tableArticulo.setFillsViewportHeight(true);

		tableArticulo.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				JTextField field = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row,
						column);

				// Crear un DocumentListener para detectar cambios mientras se escribe
				field.getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						verificarNumero(field);
					}

					// Verificar si el valor es numérico
					private void verificarNumero(JTextField field) {
						String text = field.getText();

						// Si el texto no es un número, deshabilitar el botón
						if (!text.matches("[0-9]*")) {
							field.setForeground(Color.RED);
							btnCompra.setEnabled(false); // Deshabilitar el botón
						} else {
							field.setForeground(Color.BLACK);
							btnCompra.setEnabled(true); // Habilitar el botón
						}
					}
				});

				return field; // Devolver el JTextField para que funcione como editor de la celda
			}
		});

		// --- Listener para validar la cantidad ingresada ---
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// Verificar si el cambio fue una ACTUALIZACIÓN en la columna de CANTIDAD
				// (índice 6 del *modelo*)
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
					int filaModelo = e.getFirstRow(); // Índice de la fila en el *modelo*

					// Obtener valores del *modelo* usando los índices del modelo
					Object cantidadObj = model.getValueAt(filaModelo, 6); // Cantidad (col 6)
					Object stockObj = model.getValueAt(filaModelo, 5); // Stock (col 5)

					// Asegurarse de que los valores no sean nulos y sean Integers
					if (cantidadObj != null && stockObj instanceof Integer) {
						int stockDisponible = (Integer) stockObj;
						try {
							int cantidadIngresada = Integer.parseInt(cantidadObj.toString());
							// Validar que la cantidad no sea negativa y no exceda el stock
							if (cantidadIngresada < 0) {
								// No permitir negativos, poner a 0
								model.setValueAt(0, filaModelo, 6);
							} else if (cantidadIngresada > stockDisponible) {
								// Si excede el stock, advertir y ajustar al máximo posible (stock)
								model.setValueAt(stockDisponible, filaModelo, 6);
							}
						} catch (NumberFormatException e2) {
							model.setValueAt(null, filaModelo, 6);
							btnCompra.setEnabled(true);
						}
					}
				}
			}
		});

		// --- Añadir Tabla con ScrollPane (CENTER) ---
		JScrollPane scrollPane = new JScrollPane(tableArticulo);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, PADDING_INTERNO, 0, PADDING_INTERNO)); // Padding
																										// lateral para
																										// el scroll
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Carga (o recarga) los datos de los artículos desde la base de datos a la
	 * tabla.
	 */
	public void cargarDatosTabla() {
		// Limpiar tabla antes de cargar nuevos datos
		model.setRowCount(0);

		// Obtener los artículos del controlador/DAO
		Map<Integer, Articulo> articulos = Principal.obtenerTodosArticulos();

		if (articulos != null && !articulos.isEmpty()) {
			for (Articulo art : articulos.values()) {
				// Añadir solo artículos con stock > 0
				if (art.getStock() > 0) {
					model.addRow(new Object[] { art.getId_art(), // Col 0 (Modelo) - ID (oculto en vista)
							art.getNombre(), // Col 1 (Modelo) - Nombre
							art.getDescripcion(), // Col 2 (Modelo) - Descripción
							art.getPrecio(), // Col 3 (Modelo) - Precio (usar Double)
							art.getOferta(), // Col 4 (Modelo) - Oferta (usar Double)
							art.getStock(), // Col 5 (Modelo) - Stock
							null // Col 6 (Modelo) - Cantidad inicial (null o 0)
					});
				}
			}
		} else {
			// Opcional: Mostrar un mensaje si no hay artículos
			System.out.println("No se encontraron artículos o hubo un error al cargarlos.");
			// Podrías mostrar un JLabel en lugar de la tabla indicando "No hay artículos
			// disponibles".
		}
	}

	/**
	 * Maneja los eventos de clic en los botones.
	 * 
	 * @param e El evento de acción.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == btnUsuario) {
			mostrarVistaUsuario();
		} else if (source == btnAdmin) {
			mostrarVistaAdmin();
		} else if (source == btnCompra) {
			abrirCarrito();
		}
	}

	/**
	 * Muestra la ventana de gestión de datos del usuario.
	 */
	private void mostrarVistaUsuario() {
		if (localClien != null) {
			// Crear y mostrar la ventana de usuario, pasándole esta ventana como 'owner'
			// Asumiendo que VistaUsuario es un JDialog
			VistaUsuario vistaUsuario = new VistaUsuario(localClien, this); // 'this' es el JDialog VistaTienda
			vistaUsuario.setVisible(true);
			// No es necesario ocultar/mostrar VistaTienda si VistaUsuario es modal
		} else {
			JOptionPane.showMessageDialog(this, "No hay un cliente identificado.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Muestra la ventana intermedia de administración.
	 */
	private void mostrarVistaAdmin() {
		// Asumiendo que VentanaIntermedia es un JDialog o JFrame que toma un Owner
		// Si es JDialog, pasar 'this'. Si necesita JFrame, pasar (Frame)getOwner()
		VentanaIntermedia menuAdmin = new VentanaIntermedia(this); // Pasando este JDialog como owner
		menuAdmin.setVisible(true);
		// No es necesario ocultar/mostrar VistaTienda si VentanaIntermedia es modal
		// Si tras cerrar la ventana admin hay cambios (ej. nuevo artículo), refrescar:
		// cargarDatosTabla();
	}

	/**
	 * Recopila los artículos seleccionados (con cantidad > 0) y abre la ventana del
	 * carrito.
	 */
	private void abrirCarrito() {
		Pedido pedidoActual = new Pedido(Principal.obtenerUltimoIdPed(), localClien.getId_usu(), 0,
				LocalDateTime.now());
		List<Compra> comprasParaCarrito = recopilarCompras(pedidoActual.getId_ped());

		if (comprasParaCarrito.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No has seleccionado ningún artículo o cantidad.", "Carrito Vacío",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		VistaCarrito carritoDialog = new VistaCarrito(this, comprasParaCarrito, pedidoActual);
		carritoDialog.setVisible(true);
	}

	/**
	 * Recorre el modelo de la tabla y crea una lista de objetos Compra para los
	 * artículos con cantidad > 0.
	 * @param idePed 
	 * 
	 * @return Una lista de objetos Compra.
	 */
	private List<Compra> recopilarCompras(int idePed) {
		List<Compra> listaCompra = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			// Obtener cantidad del modelo (índice 6)
			Object valor = model.getValueAt(i, 6);

			if (tableArticulo.isEditing() && tableArticulo.getEditingRow() == i) {
				Component editor = tableArticulo.getEditorComponent();
				if (editor instanceof JTextField txtField) {
					valor = txtField.getText();
				}
			}
			if (valor != null) {
				try {
					int cantidadFinal = Integer.parseInt(valor.toString());
					if (cantidadFinal > 0) {
						Compra palCarro = new Compra((Integer) model.getValueAt(i, 0), idePed, cantidadFinal);
						listaCompra.add(palCarro);
					}
				} catch (NumberFormatException e) {
					model.setValueAt(null, i, 6);
				}
			}
		}
		return listaCompra;
	}

	/**
	 * Método auxiliar para cargar iconos. Maneja errores si el icono no se
	 * encuentra.
	 * 
	 * @param path Ruta del icono dentro del classpath (ej. "/iconos/user.png").
	 * @return Un ImageIcon o null si no se pudo cargar.
	 */
	private ImageIcon cargarIcono(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			// Escalar icono si es necesario (ejemplo a 16x16)
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image image = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
			return new ImageIcon(image);
			// return new ImageIcon(imgURL); // Sin escalar
		} else {
			System.err.println("No se pudo encontrar el icono: " + path);
			return null; // O un icono por defecto
		}
	}
}