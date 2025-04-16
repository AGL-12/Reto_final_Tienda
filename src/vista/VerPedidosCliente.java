package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics; // Necesario para pintar botón redondeado (opcional)
import java.awt.Graphics2D; // Necesario para pintar botón redondeado (opcional)
import java.awt.Insets;
import java.awt.RenderingHints; // Necesario para pintar botón redondeado (opcional)
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.net.URL; // Necesario para getResource

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import controlador.Principal;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Pedido;

import com.formdev.flatlaf.FlatLightLaf; // Para el main de ejemplo

public class VerPedidosCliente extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTable tablePedidos;
    private DefaultTableModel modelPedidos;
    private JTabbedPane tabbedPane;
    private BufferedImage backgroundImage;

    // --- Constantes de Estilo (Igual que antes) ---
    private static final Font FONT_TABLA_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TABLA_CELDA = new Font("Segoe UI", Font.PLAIN, 12);
    private static final int PADDING_TABLA_CELDA_V = 5;
    private static final int PADDING_TABLA_CELDA_H = 10;
    private static final Color COLOR_FONDO_VIEWPORT = new Color(245, 222, 179);
    private static final Color COLOR_BORDE_SCROLLPANE = new Color(101, 67, 33);
    private static final Color COLOR_CELDA_FONDO = new Color(245, 245, 220, 180);
    private static final Color COLOR_SELECCION_FUENTE = Color.blue;

    // --- Constantes para la Cabecera (Igual que antes) ---
    private static final Color HEADER_BACKGROUND = new Color(210, 180, 140);
    private static final Color HEADER_FOREGROUND = new Color(101, 67, 33);
    private static final Color HEADER_BORDER_COLOR = new Color(139, 69, 19);
    private static final Font HEADER_FONT = FONT_TABLA_HEADER;
    private static final int HEADER_VPADDING = PADDING_TABLA_CELDA_V / 2;
    private static final int HEADER_HPADDING = PADDING_TABLA_CELDA_H;

    // --- Nuevas Constantes/Variables para Pestañas ---
    private static final Font FONT_PESTANA_TITULO = new Font("Segoe UI", Font.PLAIN, 12); // Fuente para el título de la pestaña
    private static final Color COLOR_BOTON_CERRAR_HOVER = new Color(255, 99, 71, 200); // Rojo tomate semi-transparente para hover
    private static final Color COLOR_BOTON_CERRAR_NORMAL = new Color(160, 160, 160); // Gris para la 'x'

    /**
     * Create the dialog.
     */
    public VerPedidosCliente(JDialog padre, Cliente clien) {
        super(padre, "Pedidos de " + (clien != null ? clien.getUsuario() : "Cliente Desconocido"), true);
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 500)); // Un poco más grande por defecto
        
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

        // --- TabbedPane ---
        // Podríamos configurar aquí propiedades si no usamos LaF, pero es mejor usar UIManager
        tabbedPane = new JTabbedPane();
        // tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // Útil si hay muchas pestañas

        add(tabbedPane, BorderLayout.CENTER);

        // --- Panel y Tabla de Pedidos ---
        JPanel panelPedidos = new JPanel(new BorderLayout());
        panelPedidos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelPedidos.setOpaque(false);

        // --- Modelo Tabla Pedidos --- (Sin cambios)
        modelPedidos = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
             @Override public boolean isCellEditable(int r, int c){ return false; }
             @Override
             public Class<?> getColumnClass(int i) { /* ... sin cambios ... */
                switch (i) {
                    case 0: return Integer.class;
                    case 1: return String.class;
                    case 2: return Object.class; // O Date.class si es Date
                    default: return Object.class;
                }
            }
        };
        modelPedidos.addColumn("Id Pedido");
        modelPedidos.addColumn("Precio Total (€)");
        modelPedidos.addColumn("Fecha de Compra");

        // --- Carga Datos Pedidos --- (Sin cambios)
        if (clien != null) { /* ... sin cambios ... */
             List<Pedido> pedidos = Principal.obtenerPedidosCliente(clien.getId_usu());
             if (pedidos != null && !pedidos.isEmpty()) {
                 for (Pedido ped : pedidos) {
                     String precioTotalFormateado = String.format(java.util.Locale.US, "%.2f", ped.getTotal());
                     modelPedidos.addRow(new Object[]{ped.getId_ped(), precioTotalFormateado, ped.getFecha_compra()});
                 }
             } else { modelPedidos.addRow(new Object[]{null, "No hay pedidos", null}); }
        } else { modelPedidos.addRow(new Object[]{null, "Cliente no válido", null}); }


        // --- Creación Tabla Pedidos con ESTILO VISTACARRITO --- (Sin cambios)
        tablePedidos = new JTable(modelPedidos) { /* ... prepareRenderer sin cambios ... */
             private static final long serialVersionUID = 1L;
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) { ((JComponent) c).setOpaque(false); }
                c.setBackground(COLOR_CELDA_FONDO);
                if (!isRowSelected(row)) { c.setForeground(Color.BLACK); }
                else { c.setForeground(COLOR_SELECCION_FUENTE); }
                if (c instanceof JLabel) {
                   JLabel label = (JLabel) c;
                   label.setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
                   if (column == 1) { label.setHorizontalAlignment(SwingConstants.RIGHT); }
                   else if (column == 0 || column == 2) { label.setHorizontalAlignment(SwingConstants.CENTER); }
                   else { label.setHorizontalAlignment(SwingConstants.LEFT); }
                } return c;
            }
        };
        // --- Aplicar Estilos Generales de Tabla --- (Sin cambios)
        tablePedidos.setFont(FONT_TABLA_CELDA);
        tablePedidos.setRowHeight(tablePedidos.getRowHeight() + 10);
        tablePedidos.setShowGrid(false); tablePedidos.setShowHorizontalLines(true); tablePedidos.setShowVerticalLines(false);
        tablePedidos.setIntercellSpacing(new Dimension(0, 1));
        tablePedidos.setAutoCreateRowSorter(true); tablePedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePedidos.setOpaque(false); tablePedidos.setFillsViewportHeight(true);
        // --- Estilo Cabecera --- (Sin cambios)
        applyCustomHeaderRenderer(tablePedidos);
        // --- ScrollPane --- (Sin cambios)
        JScrollPane scrollPane = new JScrollPane(tablePedidos);
        scrollPane.setOpaque(true); scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(COLOR_FONDO_VIEWPORT);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SCROLLPANE, 2));
        panelPedidos.add(scrollPane, BorderLayout.CENTER);
        // --- Listener Doble Click --- (Sin cambios)
        tablePedidos.addMouseListener(new MouseAdapter() { /* ... sin cambios ... */
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
                                 Component tabComp = tabbedPane.getTabComponentAt(i); String tituloActual = "";
                                 if (tabComp instanceof JPanel) {
                                     for(Component child : ((JPanel)tabComp).getComponents()) {
                                         if (child instanceof JLabel) { tituloActual = ((JLabel)child).getText(); break; }
                                     }
                                 }
                                 if (tituloActual.isEmpty()) { tituloActual = tabbedPane.getTitleAt(i); }
                                 if (tituloBuscado.equals(tituloActual)) {
                                     pestanaExiste = true; tabbedPane.setSelectedIndex(i); break;
                                 }
                             }
                             if (!pestanaExiste) { agregarPestañaArticulos(idPedido); }
                         } else { System.err.println("ID no es Integer: " + idObj); }
                    }
                }
            }
        });

        // --- Añadir Pestaña Pedidos ---
        tabbedPane.addTab("Mis Pedidos", panelPedidos); // Título Pestaña Inicial

        // --- Ajustar anchos --- (Sin cambios)
        adjustColumnWidths(tablePedidos);

        // --- Configuración Final Diálogo ---
        pack();
        setLocationRelativeTo(padre);
    }

    /**
     * Crea y añade una nueva pestaña mostrando los artículos de un pedido específico.
     * @param idPedido El ID del pedido a mostrar.
     */
    private void agregarPestañaArticulos(int idPedido) {
        JPanel panelArticulos = new JPanel(new BorderLayout());
        panelArticulos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelArticulos.setOpaque(false);

        // --- Modelo Tabla Detalles --- (Sin cambios)
        DefaultTableModel modelDetalle = new DefaultTableModel(){ /* ... sin cambios ... */
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c){ return false; }
            @Override public Class<?> getColumnClass(int i) {
                 switch (i) {
                    case 0: return String.class; case 1: return Integer.class;
                    case 2: return String.class; case 3: return String.class;
                    case 4: return String.class; default: return Object.class;
                }
            }
        };
        modelDetalle.addColumn("Artículo"); modelDetalle.addColumn("Cant.");
        modelDetalle.addColumn("P. Unit. (€)"); modelDetalle.addColumn("Dto. (%)");
        modelDetalle.addColumn("P. Total (€)");

        // --- Carga Datos Artículos --- (Sin cambios)
        List<Articulo> articulos = Principal.obtenerArticulosPorPedido(idPedido);
        if (articulos != null && !articulos.isEmpty()) { /* ... sin cambios ... */
             for (Articulo art : articulos) {
                int cantidad = Principal.obtenerCantidadArticuloEnPedido(idPedido, art.getId_art());
                float precioOriginal = art.getPrecio(); float descuento = art.getOferta();
                float precioConDescuento = precioOriginal * (1 - (descuento / 100.0f));
                float precioTotalArticulo = precioConDescuento * cantidad;
                String pUnitF = String.format(java.util.Locale.US, "%.2f", precioConDescuento);
                String dtoF = String.format(java.util.Locale.US, "%.0f", descuento);
                String pTotalF = String.format(java.util.Locale.US, "%.2f", precioTotalArticulo);
                modelDetalle.addRow(new Object[]{ art.getNombre(), cantidad, pUnitF, dtoF, pTotalF });
            }
        } else { modelDetalle.addRow(new Object[]{"No hay artículos", null, null, null, null}); }

        // --- Creación Tabla Detalles con ESTILO VISTACARRITO --- (Sin cambios)
        JTable tableDetalle = new JTable(modelDetalle) { /* ... prepareRenderer sin cambios ... */
             private static final long serialVersionUID = 1L;
             @Override
             public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                 Component c = super.prepareRenderer(renderer, row, col);
                 if (c instanceof JComponent) { ((JComponent) c).setOpaque(false); }
                 c.setBackground(COLOR_CELDA_FONDO);
                 if (!isRowSelected(row)) { c.setForeground(Color.BLACK); }
                 else { c.setForeground(COLOR_SELECCION_FUENTE); }
                 if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(BorderFactory.createEmptyBorder(PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H, PADDING_TABLA_CELDA_V, PADDING_TABLA_CELDA_H));
                    if (col >= 1) { label.setHorizontalAlignment(SwingConstants.RIGHT); }
                    else { label.setHorizontalAlignment(SwingConstants.LEFT); }
                 } return c;
             }
        };
        // --- Aplicar Estilos Generales de Tabla --- (Sin cambios)
        tableDetalle.setFont(FONT_TABLA_CELDA); tableDetalle.setRowHeight(tableDetalle.getRowHeight() + 10);
        tableDetalle.setShowGrid(false); tableDetalle.setShowHorizontalLines(true); tableDetalle.setShowVerticalLines(false);
        tableDetalle.setIntercellSpacing(new Dimension(0, 1)); tableDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDetalle.setOpaque(false); tableDetalle.setFillsViewportHeight(true);
        // --- Estilo Cabecera --- (Sin cambios)
        applyCustomHeaderRenderer(tableDetalle);
        // --- ScrollPane --- (Sin cambios)
        JScrollPane scrollPaneDetalle = new JScrollPane(tableDetalle);
        scrollPaneDetalle.setOpaque(true); scrollPaneDetalle.getViewport().setOpaque(true);
        scrollPaneDetalle.getViewport().setBackground(COLOR_FONDO_VIEWPORT);
        scrollPaneDetalle.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SCROLLPANE, 2));
        panelArticulos.add(scrollPaneDetalle, BorderLayout.CENTER);

        // --- Añadir Pestaña con Cabecera Personalizada ---
        String tituloPestana = "Pedido " + idPedido;
        tabbedPane.addTab(null, panelArticulos); // Añadir sin título inicial, se pondrá en el componente
        int index = tabbedPane.indexOfComponent(panelArticulos);

        // *** Crear Componente de Pestaña MEJORADO ***
        JPanel tabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Espaciado horizontal 5px
        tabComponent.setOpaque(false); // Muy importante para integrarse con el LaF

        JLabel titleLabel = new JLabel(tituloPestana);
        titleLabel.setFont(FONT_PESTANA_TITULO); // Usar fuente definida
        // El LaF debería manejar el color del texto (seleccionado/no seleccionado)
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); // Espacio entre título y botón

        // *** Botón de Cierre MEJORADO ***
        JButton closeButton = new JButton("X"); // Una 'X' simple pero en mayúscula
        closeButton.setFont(new Font("Arial", Font.BOLD, 11)); // Un poco más pequeña y negrita
        closeButton.setForeground(COLOR_BOTON_CERRAR_NORMAL); // Color gris por defecto
        closeButton.setMargin(new Insets(1, 1, 1, 1)); // Margen interno mínimo para que no sea tan apretado
        closeButton.setPreferredSize(new Dimension(18, 18)); // Ligeramente más grande
        closeButton.setToolTipText("Cerrar Pestaña (Pedido " + idPedido + ")"); // Tooltip más específico
        closeButton.setVerticalAlignment(SwingConstants.CENTER);
        closeButton.setContentAreaFilled(false); // Sin fondo por defecto
        closeButton.setBorderPainted(false); // Sin borde por defecto
        closeButton.setFocusPainted(false); // Sin el recuadro de foco
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setOpaque(false);

        // --- Efecto Hover Mejorado para el Botón ---
        closeButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                closeButton.setContentAreaFilled(true); // Mostrar fondo al pasar por encima
                closeButton.setOpaque(true);
                closeButton.setBackground(COLOR_BOTON_CERRAR_HOVER); // Usar color hover definido
                closeButton.setForeground(Color.WHITE); // Texto blanco sobre fondo rojo
            }
            @Override public void mouseExited(MouseEvent e) {
                closeButton.setContentAreaFilled(false); // Ocultar fondo al salir
                closeButton.setOpaque(false);
                closeButton.setForeground(COLOR_BOTON_CERRAR_NORMAL); // Restaurar color de texto gris
            }
        });

        // --- Acción de Cierre (igual) ---
        closeButton.addActionListener(e -> {
            int tabIndex = tabbedPane.indexOfTabComponent(tabComponent);
            if (tabIndex != -1) {
                tabbedPane.remove(tabIndex);
            }
        });

        // --- Añadir componentes al panel de la pestaña ---
        tabComponent.add(titleLabel);
        tabComponent.add(closeButton);

        // Establecer este panel como el componente de la pestaña
        // Esto sobreescribe cualquier título puesto con addTab(titulo, componente)
        tabbedPane.setTabComponentAt(index, tabComponent);
        // Opcional: Poner un tooltip a la pestaña entera
        tabbedPane.setToolTipTextAt(index, "Ver detalles del Pedido " + idPedido);

        // --- Ajustar anchos --- (Sin cambios)
        adjustColumnWidths(tableDetalle);

        // --- Seleccionar la nueva pestaña ---
        tabbedPane.setSelectedComponent(panelArticulos);
    }


    // --- MÉTODO HELPER para aplicar el Renderer de Cabecera --- (Sin cambios)
    private void applyCustomHeaderRenderer(JTable table) { /* ... sin cambios ... */
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false); header.setResizingAllowed(true);
        header.setDefaultRenderer(new TableCellRenderer() {
            private final TableCellRenderer defaultRenderer = table.getTableHeader().getDefaultRenderer();
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = defaultRenderer.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp; label.setFont(HEADER_FONT);
                    label.setBackground(HEADER_BACKGROUND); label.setForeground(HEADER_FOREGROUND);
                    label.setOpaque(true);
                    Border pad = BorderFactory.createEmptyBorder(HEADER_VPADDING, HEADER_HPADDING, HEADER_VPADDING, HEADER_HPADDING);
                    Border line = BorderFactory.createMatteBorder(0, 0, 2, 0, HEADER_BORDER_COLOR);
                    label.setBorder(BorderFactory.createCompoundBorder(line, pad));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } return comp;
            }
        });
        header.repaint();
    }

    // --- adjustColumnWidths --- (Sin cambios)
    private void adjustColumnWidths(JTable table) { /* ... sin cambios ... */
         SwingUtilities.invokeLater(() -> {
            TableColumnModel columnModel = table.getColumnModel();
            final int PADDING = 15;
            for (int column = 0; column < table.getColumnCount(); column++) {
                TableColumn tableColumn = columnModel.getColumn(column); int maxWidth = 0;
                TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
                if (headerRenderer == null) { headerRenderer = table.getTableHeader().getDefaultRenderer(); }
                Component headerComp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
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