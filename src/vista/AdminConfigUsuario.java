package vista;

import javax.swing.JDialog;
import javax.swing.JFrame; // Importar JFrame para el main de prueba
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities; // Para invokeLater
import javax.swing.UIManager; // Para Look and Feel
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer; // Para alinear celdas
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // Para cabecera
import javax.swing.table.TableCellRenderer; // Para prepareRenderer
import javax.swing.table.TableColumn; // Para ajuste de ancho
import javax.swing.table.TableColumnModel; // Para ajuste de ancho

import controlador.Principal;
import modelo.Cliente;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane; // Para mensajes
import javax.swing.JPanel; // Para panel de botón
import javax.swing.JScrollPane;
import javax.swing.SwingConstants; // Para alineación
// Imports para iconos (si se usan)
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout; // Para panel de botón
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets; // Para márgenes de botón
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import javax.swing.ImageIcon;


public class AdminConfigUsuario extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // --- Componentes UI ---
    private DefaultTableModel model;
    private JTable tableListaUsuarios;
    private JButton btnAbrirUsuario; // Renombrado
    private JLabel lblTitulo;

    // --- Datos ---
    private Map<Integer, Cliente> listaClienteTod; // Mapa para acceso rápido por ID
    private Cliente localClien; 
    // --- Constantes de Estilo ---
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
    private static final int PADDING_GENERAL = 15;
    private static final int PADDING_INTERNO = 10;
    private static final int PADDING_TABLA_CELDA_V = 5;
    private static final int PADDING_TABLA_CELDA_H = 8;


    /**
     * Create the dialog.
     */
    public AdminConfigUsuario(JDialog ventanaIntermedia) {
        super(ventanaIntermedia, "Administrar Usuarios", true); // Título más descriptivo

        // --- Configuración Ventana ---
        // Usar BorderLayout para estructura adaptable
        setLayout(new BorderLayout(PADDING_INTERNO, PADDING_INTERNO));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));

        // --- Listener para Refrescar al Activar ---
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                refrescarTabla();
                ajustarAnchosColumnaTabla(); // Reajustar anchos después de refrescar
            }
        });

        // --- Inicializar Componentes ---
        initComponents();
        configurarTabla();

        // --- Carga Inicial y Ajuste ---
        refrescarTabla();
        ajustarAnchosColumnaTabla(); // Ajuste inicial

        // --- Configuración Final ---
        pack(); // Ajustar tamaño al contenido
        setMinimumSize(new Dimension(750, 400)); // Establecer tamaño mínimo
        setLocationRelativeTo(ventanaIntermedia); // Centrar respecto al padre
    }

    /**
     * Inicializa los componentes básicos (Título, Panel de Botón).
     */
    private void initComponents() {
        // Título (NORTH)
        lblTitulo = new JLabel("Selecciona un Usuario para Abrir");
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(0, 0, PADDING_INTERNO, 0)); // Espacio inferior
        add(lblTitulo, BorderLayout.NORTH);

        // Panel para el Botón (SOUTH) - Usa FlowLayout para centrar el botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, PADDING_INTERNO / 2));
        // panelBoton.setBackground(UIManager.getColor("Panel.background")); // Heredar fondo

        // Botón para abrir usuario
        btnAbrirUsuario = new JButton("Abrir Usuario Seleccionado");
        btnAbrirUsuario.setFont(FONT_BOTON);
        btnAbrirUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAbrirUsuario.setFocusPainted(false);
        // Icono Opcional (ejemplo: icono de "abrir" o "editar")
         btnAbrirUsuario.setIcon(cargarIcono("/iconos/abrir.png", 16, 16)); // Cambia ruta si es necesario
        // Estilo Primario (FlatLaf)
        btnAbrirUsuario.putClientProperty("JButton.buttonType", "primary");
        // Padding interno
        btnAbrirUsuario.setMargin(new Insets(PADDING_INTERNO / 2, PADDING_INTERNO, PADDING_INTERNO / 2, PADDING_INTERNO));
        btnAbrirUsuario.addActionListener(this);

        panelBoton.add(btnAbrirUsuario);
        add(panelBoton, BorderLayout.SOUTH);
    }

    /**
     * Configura la JTable: modelo, renderers, propiedades, cabecera, scrollpane.
     */
    private void configurarTabla() {
        // --- Modelo ---
        model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            // *** CAMBIO: Ninguna celda editable ***
            @Override public boolean isCellEditable(int row, int column) { return false; }
            // Definir tipos para ordenación
            @Override public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    // *** CAMBIO: Indices del modelo ahora empiezan desde 0 visible ***
                    case 0: return Integer.class; // ID Cliente (Modelo Col 0)
                    case 5: return Integer.class; // Total Compras (Modelo Col 5)
                    default: return String.class; // Usuario(1), DNI(2), Correo(3), Direccion(4)
                }
            }
        };
        // *** CAMBIO: Columnas sin Checkbox ***
        model.addColumn("ID");             // Modelo Col 0
        model.addColumn("Usuario");        // Modelo Col 1
        model.addColumn("DNI");            // Modelo Col 2
        model.addColumn("Correo");         // Modelo Col 3
        model.addColumn("Dirección");      // Modelo Col 4
        model.addColumn("Nº Compras");     // Modelo Col 5

        // --- Creación Tabla ---
        tableListaUsuarios = new JTable(model) {
            // Renderer para estilo de celdas (padding, colores alternos)
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                // Padding
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(new EmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
                }
                // Colores alternos
                if (!isRowSelected(row)) {
                    Color bg = UIManager.getColor(row % 2 == 0 ? "Table.background" : "Table.alternateRowColor");
                    if (bg == null) { bg = (row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)); } // Fallback
                    c.setBackground(bg);
                }
                // Alinear números a la derecha (ID y Nº Compras - Modelo Col 0 y 5)
                if ((column == 0 || column == 5) && c instanceof JLabel) {
                     ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        };

        // --- Propiedades Tabla ---
        tableListaUsuarios.setFont(FONT_TABLA_CELDA);
        tableListaUsuarios.setRowHeight(tableListaUsuarios.getRowHeight() + PADDING_INTERNO); // Más altura
        tableListaUsuarios.setGridColor(UIManager.getColor("Table.gridColor"));
        tableListaUsuarios.setShowGrid(false);
        tableListaUsuarios.setShowHorizontalLines(true); // Solo líneas horizontales
        tableListaUsuarios.setIntercellSpacing(new Dimension(0, 1));
        tableListaUsuarios.setAutoCreateRowSorter(true); // Permitir ordenar
        // *** CAMBIO: Selección de fila única ***
        tableListaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableListaUsuarios.setFillsViewportHeight(true);

        // --- Cabecera Tabla ---
        JTableHeader header = tableListaUsuarios.getTableHeader();
        header.setFont(FONT_TABLA_HEADER.deriveFont(Font.BOLD)); // Fuente Negrita
        header.setReorderingAllowed(false); // No reordenar
        header.setResizingAllowed(false);   // No redimensionar

        // --- ScrollPane (CENTER) ---
        JScrollPane scrollPane = new JScrollPane(tableListaUsuarios);
        scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder")); // Borde del LaF
        add(scrollPane, BorderLayout.CENTER);
    }


    /**
     * Refresca los datos mostrados en la tabla.
     */
    private void refrescarTabla() {
        // Limpiar tabla antes de recargar
        model.setRowCount(0);

        // Obtener datos actualizados
        listaClienteTod = Principal.listarCliente();

        if (listaClienteTod != null && !listaClienteTod.isEmpty()) {
            for (Cliente cliens : listaClienteTod.values()) {
                // *** CAMBIO: Añadir fila sin la columna de checkbox ***
                model.addRow(new Object[]{
                        cliens.getId_usu(),
                        cliens.getUsuario(),
                        cliens.getDni(),
                        cliens.getCorreo(),
                        cliens.getDireccion(),
                        cliens.getListaCompra() != null ? cliens.getListaCompra().size() : 0 // Manejar lista null
                });
            }
        } else {
            System.out.println("No hay clientes para mostrar.");
            // Opcional: mostrar mensaje en la tabla o panel
        }
    }


    /**
     * Ajusta el ancho preferido de cada columna visible de la tabla.
     */
    private void ajustarAnchosColumnaTabla() {
         tableListaUsuarios.revalidate();
         if (tableListaUsuarios.getWidth() == 0) {
             SwingUtilities.invokeLater(this::ajustarAnchosColumnaTabla);
             return;
         }
        TableColumnModel columnModel = tableListaUsuarios.getColumnModel();
        final int PADDING = PADDING_TABLA_CELDA_H * 2;

        for (int columnView = 0; columnView < tableListaUsuarios.getColumnCount(); columnView++) {
            TableColumn tableColumn = columnModel.getColumn(columnView);
            int headerWidth = getColumnHeaderWidth(tableListaUsuarios, columnView);
            int contentWidth = getMaximumColumnContentWidth(tableListaUsuarios, columnView);
            int preferredWidth = Math.max(headerWidth, contentWidth) + PADDING;

            // Indices VISTA: ID(0), Usuario(1), DNI(2), Correo(3), Direccion(4), Nº Compras(5)
            if (columnView == 0) { // ID
                 tableColumn.setMinWidth(60);
                 tableColumn.setMaxWidth(80);
            } else if (columnView == 1) { // Usuario
                 tableColumn.setMinWidth(100);
            } else if (columnView == 2) { // DNI
                 tableColumn.setMinWidth(90);
                 tableColumn.setMaxWidth(120);
            } else if (columnView == 3) { // Correo
                 tableColumn.setMinWidth(180);
            } else if (columnView == 4) { // Dirección
                 tableColumn.setMinWidth(200);
            } else if (columnView == 5) { // Nº Compras
                 tableColumn.setMinWidth(80);
                 tableColumn.setMaxWidth(100);
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
        tableListaUsuarios.getTableHeader().resizeAndRepaint();
    }

    // --- Métodos Auxiliares para Ancho de Columna ---
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
            Component comp = table.prepareRenderer(renderer, row, columnIndexView);
            maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
        }
        return maxWidth;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAbrirUsuario)) {
            // *** CAMBIO: Obtener fila seleccionada en lugar de buscar checkbox ***
            int selectedRowView = tableListaUsuarios.getSelectedRow();

            if (selectedRowView != -1) { // Si hay una fila seleccionada
                // Convertir índice de vista a índice de modelo (importante si la tabla se ordena)
                int selectedRowModel = tableListaUsuarios.convertRowIndexToModel(selectedRowView);

                // Obtener el ID del cliente del MODELO (columna 0)
                int idCliente = (int) model.getValueAt(selectedRowModel, 0);
                localClien = listaClienteTod.get(idCliente); // Buscar en el mapa

                if (localClien != null) {
                    // Abrir VistaUsuario pasando el cliente encontrado y esta ventana como padre
                    VistaUsuario vista = new VistaUsuario(localClien, this);
                    vista.setVisible(true);
                } else {
                    // Error (no debería pasar si el ID viene de la tabla cargada desde el mapa)
                    JOptionPane.showMessageDialog(this, "Error: No se encontró el cliente con ID " + idCliente, "Error Interno", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Si no hay fila seleccionada
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario de la lista.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    // --- Método Cargar Icono (copiado de otras vistas) ---
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
         // Placeholder
         BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2d = placeholder.createGraphics();
         try {
             g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g2d.setColor(Color.LIGHT_GRAY); g2d.fillRect(0, 0, width, height);
             g2d.setColor(Color.DARK_GRAY); g2d.drawRect(0,0, width-1, height-1);
             g2d.setColor(Color.BLUE); g2d.setStroke(new BasicStroke(1));
             g2d.setFont(new Font("SansSerif", Font.BOLD, Math.min(width, height) * 3 / 4));
             g2d.drawString("?", width/4, height * 3/4); // Placeholder '?'
         } finally { g2d.dispose(); }
         return new ImageIcon(placeholder);
     }

     /* // --- Main de Ejemplo (para pruebas aisladas) ---
     public static void main(String[] args) {
         try {
              // Configurar FlatLaf para la prueba
              UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
              UIManager.put("Button.arc", 8);
              UIManager.put("Component.arc", 8);
         } catch (Exception ex) {
              System.err.println("Failed to initialize LaF");
         }

         SwingUtilities.invokeLater(() -> {
             // Crear un JDialog padre falso o nulo para la prueba
             JDialog fakeParent = new JDialog((JFrame)null, "Padre Falso", false);
             AdminConfigUsuario dialog = new AdminConfigUsuario(fakeParent);
             dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
             dialog.setVisible(true);
         });
     }
     */
}