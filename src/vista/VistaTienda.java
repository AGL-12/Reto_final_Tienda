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
    private BackgroundPanel panelFondo; // Panel que tendrá el fondo

    // --- Datos ---
    private Cliente localClien;

    // --- Constantes de Estilo ---
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Color COLOR_VALIDATION_ERROR = Color.RED;

    // Padding y Espaciado
    private static final int PADDING_GENERAL = 15;
    private static final int PADDING_INTERNO = 8;
    private static final int PADDING_TABLA_CELDA_V = 5;
    private static final int PADDING_TABLA_CELDA_H = 10;

    /**
     * Constructor
     */
    public VistaTienda(Cliente clien, Frame owner) {
        super(owner, "DYE TOOLS - Tienda", true);
        this.localClien = clien;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // --- Layout para el BackgroundPanel (para contener otros componentes) ---
        panelFondo = new BackgroundPanel("/imagenes/fondoTienda.png");
        panelFondo.setLayout(new BorderLayout()); // Necesario para organizar los demás paneles

        // *** CAMBIO CLAVE: Establecer panelFondo como ContentPane ***
        setContentPane(panelFondo);

        // --- Inicializar Componentes (Cabecera y Botones) ---
        initComponents(); // Se añaden a NORTH y SOUTH

        // --- Configurar Tabla (El ScrollPane se añadirá DENTRO del panelFondo) ---
        configurarTabla();

        // --- Cargar datos y ajustar ---
        cargarDatosTabla();
        ajustarAnchosColumnaTabla();

        // --- Configuración Final Ventana ---
        pack();
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(owner);
    }

    /**
     * Inicializa componentes básicos (Título, Logo, Botones).
     */
    private void initComponents() {
        // --- Panel Cabecera (NORTH) ---
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, PADDING_INTERNO * 2, 0));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(PADDING_INTERNO, 0, PADDING_GENERAL, 0));
        panelCabecera.setOpaque(false); // <<< --- HACER TRANSPARENTE

        lblLogo = new JLabel(cargarIcono("/iconos/tienda_logo.png", 64, 64));
        panelCabecera.add(lblLogo);
        lblTitulo = new JLabel("DYE TOOLS - Catálogo");
        lblTitulo.setFont(FONT_TITULO);
        // Opcional: Cambiar color si no contrasta con el fondo
        // lblTitulo.setForeground(Color.WHITE);
        panelCabecera.add(lblTitulo);
        add(panelCabecera, BorderLayout.NORTH);

        // --- Panel Botones (SOUTH) ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(PADDING_GENERAL, 0, 0, 0));
        panelBotones.setOpaque(false); // <<< --- HACER TRANSPARENTE

        btnUsuario = crearBoton("Mi Cuenta", "/iconos/user.png");
        btnAdmin = crearBoton("Admin Panel", "/iconos/admin.png");
        btnCompra = crearBoton("Ver Carrito", "/iconos/cart.png");
        btnCompra.putClientProperty("JButton.buttonType", "primary");
        btnAdmin.setVisible(localClien != null && localClien.isEsAdmin());

        panelBotones.add(btnUsuario);
        if (btnAdmin.isVisible()) {
            panelBotones.add(Box.createHorizontalStrut(PADDING_INTERNO));
            panelBotones.add(btnAdmin);
        }
        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(btnCompra);
        add(panelBotones, BorderLayout.SOUTH);
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
        // Opcional: Hacer botones semi-transparentes si se quiere
        // btn.setOpaque(false);
        // btn.setContentAreaFilled(false); // Quita el fondo por defecto
        // btn.setBorderPainted(true); // Mantener borde
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
                    case 0: return Integer.class; // ID (Modelo col 0)
                    case 3: return Double.class;  // Precio (Modelo col 3)
                    case 4: return Double.class;  // Oferta (Modelo col 4)
                    case 5: return Integer.class; // Stock (Modelo col 5)
                    case 6: return Integer.class; // Cantidad (Modelo col 6, editable)
                    default: return String.class; // Nombre (1), Desc (2)
                }
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Modelo col 6 = Cantidad
            }
        };
        model.addColumn("ID_ART");     // Modelo Col 0
        model.addColumn("Nombre");     // Modelo Col 1
        model.addColumn("Descripción");  // Modelo Col 2
        model.addColumn("Precio (€)");  // Modelo Col 3
        model.addColumn("Oferta (%)");  // Modelo Col 4
        model.addColumn("Stock");      // Modelo Col 5
        model.addColumn("Cantidad");     // Modelo Col 6

        // --- Creación Tabla ---
        tableArticulo = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // --- Hacer celda transparente ---
                if (c instanceof JComponent) {
                    ((JComponent)c).setOpaque(false);
                }

                // --- Colores de fondo semi-transparentes ---
                Color alternateColor = new Color(230, 240, 255, 100); // Azul claro semi-transparente
                Color baseColor = new Color(255, 255, 255, 120); // Blanco semi-transparente

                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 != 0 ? alternateColor : baseColor);
                    // Asegurar que el texto sea visible
                    c.setForeground(Color.BLACK); // O el color que mejor contraste
                } else {
                    // Color de selección también semi-transparente
                    Color selectionColor = tableArticulo.getSelectionBackground();
                    c.setBackground(new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 150));
                    // Asegurar texto visible en selección
                    c.setForeground(Color.blue); // O un color fijo
                }

                // --- Padding (sin cambios) ---
                if (c instanceof JComponent) {
                   ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(
                           PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H,
                           PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
                }
                // --- Alineación (sin cambios) ---
                if (column >= 3 && column <= 6 && c instanceof JLabel) {
                   ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (c instanceof JLabel) {
                   ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
           }

            // --- Hacer Editor Transparente ---
            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component c = super.prepareEditor(editor, row, column);
                if (c instanceof JComponent) {
                    ((JComponent)c).setOpaque(false);
                }
                return c;
            }
        };

        // --- Propiedades Tabla ---
        tableArticulo.setFont(FONT_TABLA_CELDA);
        tableArticulo.setRowHeight(tableArticulo.getRowHeight() + PADDING_INTERNO * 2);
        tableArticulo.setGridColor(new Color(180, 180, 180, 100)); // Grid semi-transparente
        tableArticulo.setShowGrid(true); // O false si prefieres sin grid
        tableArticulo.setShowHorizontalLines(true);
        tableArticulo.setIntercellSpacing(new Dimension(0, 1));
        tableArticulo.setAutoCreateRowSorter(true);
        tableArticulo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableArticulo.setOpaque(false); // <<< --- HACER TABLA TRANSPARENTE ---
        tableArticulo.setFillsViewportHeight(true); // Ayuda a que el fondo se vea mejor

        // --- Cabecera Tabla ---
        JTableHeader header = tableArticulo.getTableHeader();
        header.setFont(FONT_TABLA_HEADER);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setOpaque(false); // <<< --- HACER CABECERA TRANSPARENTE

        // --- Ocultar Columna ID_ART (sin cambios) ---
        TableColumnModel columnModel = tableArticulo.getColumnModel();
        try {
             TableColumn columnToHide = tableArticulo.getColumn("ID_ART");
             columnModel.removeColumn(columnToHide);
        } catch (IllegalArgumentException e) {
             System.err.println("No se pudo encontrar/ocultar la columna 'ID_ART'.");
        }

        // --- Editor Personalizado para Cantidad ---
        quantityEditorField = new JTextField();
        quantityEditorField.setHorizontalAlignment(SwingConstants.RIGHT);
        quantityEditorField.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
        quantityEditorField.setOpaque(false); // <<< --- HACER EDITOR TRANSPARENTE ---
        quantityEditorField.getDocument().addDocumentListener(new DocumentListener() {
            private void verificarNumero() {
                String text = quantityEditorField.getText();
                boolean valido = text.matches("[0-9]*");
                Color foregroundColor = UIManager.getColor("TextField.foreground"); // Color normal

                if (!valido && !text.isEmpty()) {
                    foregroundColor = COLOR_VALIDATION_ERROR;
                } else if (valido && !text.isEmpty()) {
                    try {
                        int filaEditada = tableArticulo.getEditingRow();
                        if (filaEditada != -1) {
                            int filaModelo = tableArticulo.convertRowIndexToModel(filaEditada);
                             if (filaModelo != -1) {
                                int cantidad = Integer.parseInt(text);
                                int stock = (Integer) model.getValueAt(filaModelo, 5); // Stock Modelo col 5
                                if (cantidad > stock || cantidad < 0) {
                                    foregroundColor = COLOR_VALIDATION_ERROR;
                                }
                             }
                        }
                    } catch (Exception ignored) {
                         foregroundColor = COLOR_VALIDATION_ERROR;
                    }
                }
                // Cambiar color del texto en el editor
                quantityEditorField.setForeground(foregroundColor);
                // Opcional: Cambiar color del caret si es necesario
                // quantityEditorField.setCaretColor(foregroundColor);
            }
            @Override public void insertUpdate(DocumentEvent e) { verificarNumero(); }
            @Override public void removeUpdate(DocumentEvent e) { verificarNumero(); }
            @Override public void changedUpdate(DocumentEvent e) { verificarNumero(); }
       });
        int cantidadViewIndex = 5; // Índice de vista para Cantidad
        columnModel.getColumn(cantidadViewIndex).setCellEditor(new DefaultCellEditor(quantityEditorField));

        // --- Listener del Modelo (sin cambios) ---
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) { // Columna Cantidad (Modelo)
                int filaModelo = e.getFirstRow();
                if (filaModelo < 0 || filaModelo >= model.getRowCount()) return;

                Object cantidadObj = model.getValueAt(filaModelo, 6);
                Object stockObj = model.getValueAt(filaModelo, 5);

                if (cantidadObj == null || cantidadObj.toString().isEmpty()) {
                   Object currentValue = model.getValueAt(filaModelo, 6);
                   if (currentValue != null && !(currentValue instanceof Integer && (Integer)currentValue == 0)){
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
                                   "La cantidad excede el stock disponible (" + stockDisponible + "). Se ajustará.",
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
        JScrollPane scrollPane = new JScrollPane(tableArticulo);
        scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
        scrollPane.setOpaque(false); // El JScrollPane en sí puede ser transparente
        scrollPane.getViewport().setOpaque(true); // Hacer el viewport opaco
        scrollPane.getViewport().setBackground(Color.WHITE); // Establecer el fondo del viewport a blanco

        // *** CAMBIO CLAVE: Añadir el ScrollPane DENTRO del panelFondo ***
        panelFondo.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Carga (o recarga) los datos de los artículos desde la base de datos a la tabla.
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
                    model.addRow(new Object[]{
                            art.getId_art(),       // Modelo Col 0 (Oculto en vista)
                            art.getNombre(),       // Modelo Col 1
                            art.getDescripcion(),  // Modelo Col 2
                            art.getPrecio(),       // Modelo Col 3
                            art.getOferta(),       // Modelo Col 4
                            art.getStock(),        // Modelo Col 5
                            0                      // Modelo Col 6 - Cantidad inicial 0
                    });
                }
            }
        } else {
            System.out.println("No se encontraron artículos.");
        }
        // No es necesario llamar a ajustar anchos aquí si ya se llama tras cargar datos en el constructor
    }

    /**
     * Ajusta el ancho preferido de cada columna visible de la tabla.
     */
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

            // Indices VISTA: Nombre(0), Desc(1), Precio(2), Oferta(3), Stock(4), Cantidad(5)
            if (columnView == 0) tableColumn.setMinWidth(150); // Nombre
            if (columnView == 1) tableColumn.setMinWidth(200); // Descripción
            if (columnView == 2) tableColumn.setMinWidth(75);  // Precio
            if (columnView == 3) tableColumn.setMinWidth(75);  // Oferta
            if (columnView == 4) tableColumn.setMinWidth(65);  // Stock
            if (columnView == 5) { // Cantidad
                tableColumn.setMinWidth(75);
                tableColumn.setMaxWidth(110);
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
        tableArticulo.getTableHeader().resizeAndRepaint();
    }

    // --- Métodos Auxiliares para Ancho de Columna (Sin cambios) ---
    private int getColumnHeaderWidth(JTable table, int columnIndexView) {
        TableColumn column = table.getColumnModel().getColumn(columnIndexView);
        TableCellRenderer renderer = column.getHeaderRenderer();
        if (renderer == null) { renderer = table.getTableHeader().getDefaultRenderer(); }
        Component comp = renderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, columnIndexView);
        return comp.getPreferredSize().width;
    }

    private int getMaximumColumnContentWidth(JTable table, int columnIndexView) {
        int maxWidth = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, columnIndexView);
            // Importante usar prepareRenderer para obtener el componente real con sus paddings, etc.
             Component comp = table.prepareRenderer(renderer, row, columnIndexView);
             // Si el componente tiene borde (como nuestro padding), hay que tenerlo en cuenta
             if (comp instanceof JComponent) {
                 Insets insets = ((JComponent) comp).getInsets();
                 maxWidth = Math.max(maxWidth, comp.getPreferredSize().width + insets.left + insets.right);
             } else {
                maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
             }
        }
        return maxWidth;
    }


    // --- Manejo de Acciones (sin cambios) ---
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnUsuario) mostrarVistaUsuario();
        else if (source == btnAdmin) mostrarVistaAdmin();
        else if (source == btnCompra) abrirCarrito();
    }

    // --- Métodos de Navegación/Acción (sin cambios) ---
    private void mostrarVistaUsuario() {
        if (localClien != null) {
            VistaUsuario vistaUsuario = new VistaUsuario(localClien, this);
            vistaUsuario.setVisible(true);
        } else { JOptionPane.showMessageDialog(this, "Cliente no identificado.", "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void mostrarVistaAdmin() {
        VentanaIntermedia menuAdmin = new VentanaIntermedia(this);
        menuAdmin.setVisible(true);
        // Recargar y reajustar por si cambian datos en admin
        cargarDatosTabla();
        ajustarAnchosColumnaTabla();
    }

    private void abrirCarrito() {
        if (tableArticulo.isEditing()) {
            if (!tableArticulo.getCellEditor().stopCellEditing()) { return; }
        }
        if (!validarCantidadesParaCarrito()) {
            JOptionPane.showMessageDialog(this, "Corrige cantidades inválidas antes de continuar.", "Cantidades Inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pedido pedidoActual = new Pedido(Principal.obtenerUltimoIdPed(), localClien.getId_usu(), 0, LocalDateTime.now());
        List<Compra> comprasParaCarrito = recopilarCompras(pedidoActual.getId_ped());
        if (comprasParaCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Añade artículos al carrito (cantidad > 0).", "Carrito Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        VistaCarrito carritoDialog = new VistaCarrito(this, comprasParaCarrito, pedidoActual);
        carritoDialog.setVisible(true);
        // Recargar y reajustar tras volver del carrito (el stock puede haber cambiado)
        cargarDatosTabla();
        ajustarAnchosColumnaTabla();
    }

    /**
     * Verifica si todas las cantidades son válidas.
     */
    private boolean validarCantidadesParaCarrito() {
        if (tableArticulo.isEditing()) {
            if (!tableArticulo.getCellEditor().stopCellEditing()) { return false; }
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            Object cantidadObj = model.getValueAt(i, 6); // Cantidad Modelo Col 6
            if (cantidadObj != null && !cantidadObj.toString().isEmpty()) {
                try {
                    int cantidad = Integer.parseInt(cantidadObj.toString());
                    int stock = (Integer) model.getValueAt(i, 5); // Stock Modelo Col 5
                    if (cantidad < 0 || cantidad > stock) { return false; }
                } catch (NumberFormatException e) { return false; } // Si no es número válido
            } else {
                 // Si está vacío, podría considerarse 0 (válido), o inválido si prefieres.
                 // Asumimos que vacío o null es 0 y válido por ahora.
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
            Object valorId = model.getValueAt(i, 0);       // ID Artículo (Modelo col 0)
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
                    // Ignorar o loguear si una cantidad no es numérica (debería ser 0 por validación)
                     System.err.println("Error formato al recopilar compra fila " + i + ": " + valorCantidad);
                }
            }
        }
        return listaCompra;
    }

    // --- Método Cargar Icono (sin cambios) ---
    private ImageIcon cargarIcono(String path, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            if (originalIcon.getIconWidth() != width || originalIcon.getIconHeight() != height) {
                Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            } else { return originalIcon; }
        } else {
            System.err.println("Icono no encontrado: " + path);
            // Crear placeholder
            BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.LIGHT_GRAY); g2d.fillRect(0, 0, width, height);
                g2d.setColor(Color.DARK_GRAY); g2d.drawRect(0,0, width-1, height-1);
                g2d.setColor(Color.RED); g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(width/4, height/4, 3*width/4, 3*height/4);
                g2d.drawLine(width/4, 3*height/4, 3*width/4, height/4);
            } finally { g2d.dispose(); }
            return new ImageIcon(placeholder);
        }
    }
    private ImageIcon cargarIcono(String path) { return cargarIcono(path, 16, 16); }


    // ========================================================================
    // ===              CLASE INTERNA PARA EL PANEL DE FONDO              ===
    // ========================================================================
    private class BackgroundPanel extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            loadImage(imagePath);
            // Necesita un layout para poder añadir el JScrollPane encima
            setLayout(new BorderLayout());
             // Hacer el propio panel de fondo transparente por si acaso
             // (aunque paintComponent lo sobreescribirá)
            setOpaque(false);
        }

        private void loadImage(String path) {
            // Usamos getResource de la clase externa (VistaTienda)
            URL imgURL = VistaTienda.this.getClass().getResource(path);
            if (imgURL != null) {
                backgroundImage = new ImageIcon(imgURL).getImage();
            } else {
                System.err.println("No se pudo cargar la imagen de fondo: " + path);
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Importante para limpiar y pintar bordes si los tuviera

            if (backgroundImage != null) {
                // Dibujar imagen escalada para cubrir el panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }

        // Opcional: El tamaño preferido será dictado por el contenido (JScrollPane)
        // y el layout del JDialog, por lo que no es estrictamente necesario aquí.
        /*
        @Override
        public Dimension getPreferredSize() {
            if (backgroundImage != null && getComponentCount() == 0) { // Solo si está vacío
                return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
            } else {
                return super.getPreferredSize();
            }
        }
        */
    }
    // ========================================================================


    // --- Main de Ejemplo (Comentado por defecto) ---
    /*
    public static void main(String[] args) {
        try {
            // Usar FlatLaf recomendado
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            UIManager.put("Button.arc", 10); UIManager.put("Component.arc", 8);
            UIManager.put("Table.showHorizontalLines", true); UIManager.put("Table.showVerticalLines", true); // O false
            UIManager.put("Table.intercellSpacing", new Dimension(0,1));
            // Ajustar colores de FlatLaf si es necesario para el contraste con el fondo
            // UIManager.put("Table.background", new Color(255,255,255,100)); // Ejemplo
            // UIManager.put("TableHeader.background", new Color(200,220,240,150)); // Ejemplo
        } catch (Exception e) { e.printStackTrace(); }

        // Datos de prueba
        Cliente clientePrueba = new Cliente(); clientePrueba.setId_usu(1);
        clientePrueba.setUsuario("testuser"); clientePrueba.setEsAdmin(true);
        // Simular carga de artículos si Principal no está disponible
        // Principal.cargarArticulosIniciales(); // O algo similar

        SwingUtilities.invokeLater(() -> {
            // Crear un Frame padre invisible o uno real si existe
            JFrame framePadre = new JFrame();
            framePadre.setUndecorated(true); // Para que no se vea el frame padre
            framePadre.setLocationRelativeTo(null);

            VistaTienda dialog = new VistaTienda(clientePrueba, framePadre);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // Listener para cerrar el frame padre cuando se cierre el diálogo
             dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                 @Override
                 public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    framePadre.dispose();
                    System.exit(0); // Terminar aplicación si es la ventana principal
                 }
             });

            dialog.setVisible(true); // Mostrar la tienda
        });
    }
    */
}