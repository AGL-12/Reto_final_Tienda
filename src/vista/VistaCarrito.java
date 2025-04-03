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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JLabel lblTotalCompra; // Label para mostrar el total fuera de la tabla

    // Datos del pedido y la compra
    private Pedido localPedido;
    private List<Compra> localListaCompra;

    // Formateador de moneda (definido como constante para reutilizar)
    // Ajusta el Locale según tu necesidad (ej. "en", "US" para USD)
    private static final Locale userLocale = new Locale("es", "ES"); // Español/España para Euros (€)
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(userLocale);


    /**
     * Constructor principal de la vista del carrito.
     *
     * @param vistaTienda La ventana padre (JDialog de la tienda).
     * @param listaCompra La lista de objetos Compra que representan los artículos en el carrito.
     * @param preSetCompra El objeto Pedido preconfigurado (con id_usu, fecha, etc., pero sin ID de pedido aún).
     */
    public VistaCarrito(JDialog vistaTienda, List<Compra> listaCompra, Pedido preSetCompra) {
        super(vistaTienda, "Carrito de Compra", true); // Título de la ventana y modal

        // Almacenar datos importantes
        this.localListaCompra = listaCompra;
        this.localPedido = preSetCompra; // Asignar el pedido preconfigurado

        // --- Configuración Inicial de la Ventana ---
        // Usaremos BorderLayout para una estructura flexible
        getContentPane().setLayout(new BorderLayout(10, 10)); // Layout principal con márgenes H/V
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding general

        // --- 1. Título (NORTH) ---
        JLabel lblTitulo = new JLabel("CARRITO DE COMPRA");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0)); // Espacio inferior
        getContentPane().add(lblTitulo, BorderLayout.NORTH);

        // --- 2. Tabla de Carrito (CENTER) ---
        inicializarTabla(); // Método para configurar la tabla
        JScrollPane scrollPane = new JScrollPane(tablaCarrito);
        scrollPane.setBorder(new EmptyBorder(0, 5, 0, 5)); // Padding lateral para la tabla
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // --- 3. Panel Inferior (SOUTH) - Contendrá Total y Botones ---
        JPanel southPanel = new JPanel(new BorderLayout(10, 0)); // Panel para total y botones

        // --- 3a. Label Total (WEST del South Panel) ---
        // El cálculo del total se hace en inicializarTabla()
        lblTotalCompra = new JLabel("Total: Calculando..."); // Placeholder inicial
        lblTotalCompra.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTotalCompra.setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding
        southPanel.add(lblTotalCompra, BorderLayout.WEST);

        // --- 3b. Panel de Botones (EAST del South Panel) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5)); // Botones alineados a la derecha

        btnComprar = new JButton("COMPRAR");
        btnComprar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnComprar.setPreferredSize(new Dimension(130, 40)); // Tamaño preferido
        btnComprar.addActionListener(this);
        /* // Opcional: Añadir Icono
        try {
            btnComprar.setIcon(new ImageIcon(getClass().getResource("/iconos/buy_cart.png")));
        } catch (Exception e) { System.err.println("Icono Comprar no encontrado"); }
        */

        btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnVolver.setPreferredSize(new Dimension(130, 40)); // Tamaño preferido
        btnVolver.addActionListener(this);
         /* // Opcional: Añadir Icono
        try {
            btnVolver.setIcon(new ImageIcon(getClass().getResource("/iconos/back_arrow.png")));
        } catch (Exception e) { System.err.println("Icono Volver no encontrado"); }
        */

        panelBotones.add(btnComprar);
        panelBotones.add(btnVolver);
        southPanel.add(panelBotones, BorderLayout.EAST);

        // Añadir el panel sur completo (Total + Botones) a la ventana principal
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        // --- Finalización de la Configuración de la Ventana ---
        pack(); // Ajusta el tamaño de la ventana al contenido
        setMinimumSize(new Dimension(650, 400)); // Establecer un tamaño mínimo razonable
        setLocationRelativeTo(vistaTienda); // Centrar relativo a la ventana padre

        // Hacer "Comprar" el botón por defecto (se activa con Enter)
        SwingUtilities.invokeLater(() -> getRootPane().setDefaultButton(btnComprar));

        // Calcular y mostrar el total final después de que todo esté listo
        calcularYMostrarTotal();
    }

    /**
     * Método para inicializar y configurar la JTable del carrito.
     */
    private void inicializarTabla() {
        String[] columnNames = {"Nombre", "Cantidad", "Descuento Ud.", "Precio Final Ud.", "Precio Total"};
        model = new DefaultTableModel(columnNames, 0) {
            // Hacer que las celdas no sean editables directamente en la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            // Opcional: Definir tipos de columna para mejor ordenación
             @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class; // Nombre
                    case 1: return Integer.class; // Cantidad
                    case 2:
                    case 3:
                    case 4: return Float.class; // Descuento, Precio Final, Precio Total
                    default: return Object.class;
                }
            }
        };

        tablaCarrito = new JTable(model);
        tablaCarrito.setFillsViewportHeight(true); // Para que use todo el alto del scrollpane si hay pocas filas
        tablaCarrito.setRowHeight(25); // Aumentar un poco la altura de fila
        tablaCarrito.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13)); // Cabecera en negrita

        // Permitir ordenar por columnas
        tablaCarrito.setAutoCreateRowSorter(true);

        // Configurar Renderers para formato y alineación
        configurarRenderersTabla();

        // Ajustar ancho de columnas
        TableColumnModel tcm = tablaCarrito.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(250); // Nombre más ancho
        tcm.getColumn(1).setPreferredWidth(80);  // Cantidad
        tcm.getColumn(1).setMaxWidth(100);
        tcm.getColumn(2).setPreferredWidth(120); // Descuento Ud.
        tcm.getColumn(3).setPreferredWidth(120); // Precio Final Ud.
        tcm.getColumn(4).setPreferredWidth(130); // Precio Total
    }

     /**
     * Configura los renderers para las celdas de la tabla (formato moneda, alineación).
     */
    private void configurarRenderersTabla() {
        // Renderer genérico para alinear a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        rightRenderer.setBorder(new EmptyBorder(2, 5, 2, 5)); // Añadir padding a celdas

        // Renderer para formato de moneda y alineación derecha
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
             @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // Primero llama al super para obtener el componente base (JLabel)
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Number && c instanceof JLabel) {
                    Number num = (Number) value;
                    JLabel label = (JLabel) c;
                    label.setText(currencyFormat.format(num.doubleValue()));
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                    label.setBorder(new EmptyBorder(2, 5, 2, 5)); // Asegurar padding
                }
                 // Opcional: Cambiar color de fondo para filas alternas
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
                }
                return c;
            }
        };

         // Renderer específico para descuento (maneja 0 o valores pequeños)
        DefaultTableCellRenderer discountRenderer = new DefaultTableCellRenderer() {
             @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                 if (value instanceof Number && c instanceof JLabel) {
                    Number num = (Number) value;
                    JLabel label = (JLabel) c;
                    // Mostrar "-" si el descuento es muy pequeño o cero
                    if (Math.abs(num.doubleValue()) < 0.001) {
                         label.setText("-");
                    } else {
                         label.setText(currencyFormat.format(num.doubleValue()));
                    }
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                    label.setBorder(new EmptyBorder(2, 5, 2, 5)); // Asegurar padding
                }
                 // Opcional: Cambiar color de fondo para filas alternas
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
                }
                return c;
            }
        };

        // Asignar renderers a las columnas
        tablaCarrito.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);     // Cantidad (solo alineación)
        tablaCarrito.getColumnModel().getColumn(2).setCellRenderer(discountRenderer);   // Descuento Ud.
        tablaCarrito.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer);   // Precio Final Ud.
        tablaCarrito.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer);   // Precio Total
         // Opcional: Alinear nombre a la izquierda (por defecto) pero con padding
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(new EmptyBorder(2, 5, 2, 5));
        tablaCarrito.getColumnModel().getColumn(0).setCellRenderer(leftRenderer); // Nombre
    }

    /**
     * Calcula el total de la compra y llena la tabla con los datos.
     * Actualiza el label del total.
     */
    private void calcularYMostrarTotal() {
        float totalCompraCalculado = 0;

        // Limpiar modelo por si acaso se llamara de nuevo
        model.setRowCount(0);

        if (localListaCompra == null || localListaCompra.isEmpty()) {
             lblTotalCompra.setText("Total: " + currencyFormat.format(0));
             // Opcional: Deshabilitar botón comprar si el carrito está vacío
             // btnComprar.setEnabled(false);
             return; // Salir si no hay items
        }

        for (Compra com : localListaCompra) {
            Articulo arti = Principal.buscarArticulo(com.getId_art());
            if (arti != null) {
                // Usa float para los cálculos intermedios como en tu código original
                float precioOriginal = arti.getPrecio();
                float porcentajeOferta = arti.getOferta(); // Asume que es porcentaje (ej. 10 para 10%)
                float descuentoUnidad = (porcentajeOferta / 100.0f) * precioOriginal;
                float precioFinalUnidad = precioOriginal - descuentoUnidad;
                float precioTotalArticulo = precioFinalUnidad * com.getCantidad();

                // Añadir fila a la tabla con los datos calculados
                model.addRow(new Object[]{
                        arti.getNombre(),
                        com.getCantidad(),
                        descuentoUnidad,    // Mostrar descuento por unidad
                        precioFinalUnidad,  // Mostrar precio final por unidad
                        precioTotalArticulo // Mostrar precio total por la cantidad de este artículo
                });

                // Sumar al total general de la compra
                totalCompraCalculado += precioTotalArticulo;
            } else {
                 System.err.println("Advertencia: No se encontró el artículo con ID " + com.getId_art());
                 // Podrías añadir una fila indicando el error o simplemente omitirlo
            }
        }

        // Actualizar el Pedido local con el total calculado
        if (localPedido != null) {
            localPedido.setTotal(totalCompraCalculado);
        }

        // Actualizar el JLabel que muestra el total general
        lblTotalCompra.setText("Total: " + currencyFormat.format(totalCompraCalculado));
         // Habilitar botón comprar si hay items (por si se deshabilitó antes)
         // btnComprar.setEnabled(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnComprar)) {
            // Confirmación antes de comprar
             int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que quieres finalizar la compra por un total de " + currencyFormat.format(localPedido.getTotal()) + "?",
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
             );

             if (confirm == JOptionPane.YES_OPTION) {
                 fullCompra();
             }
        } else if (e.getSource().equals(btnVolver)) {
            this.dispose(); // Cerrar la ventana del carrito
        }
    }

    /**
     * Lógica para procesar la compra final.
     * Guarda el pedido y las compras asociadas en la base de datos.
     */
    private void fullCompra() {
        if (localPedido == null || localListaCompra == null || localListaCompra.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Error: No hay datos de pedido o el carrito está vacío.", "Error Interno", JOptionPane.ERROR_MESSAGE);
             return;
        }

        try {
            // 1. Guardar el pedido para obtener su ID generado
            // Asume que guardarPedido actualiza el objeto localPedido con el ID generado
            Principal.guardarPedido(localPedido);

            // Verificar si se obtuvo un ID de pedido válido
            if (localPedido.getId_ped() <= 0) {
                 throw new SQLException("No se pudo obtener un ID válido para el pedido.");
            }

            // 2. Asociar el ID del pedido a cada línea de compra
            for (Compra compra : localListaCompra) {
                compra.setId_ped(localPedido.getId_ped());
            }

            // 3. Guardar todas las líneas de compra
            Principal.guardarCompra(localListaCompra);

            // 4. Mensaje de éxito y cierre
            JOptionPane.showMessageDialog(this,
                    "¡Compra realizada con éxito!\nID de Pedido: " + localPedido.getId_ped(),
                    "Compra Finalizada",
                    JOptionPane.INFORMATION_MESSAGE);

            // Cerrar la ventana del carrito
            this.dispose();

        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(this,
                "Error al procesar la compra:\n" + ex.getMessage() + "\nPor favor, inténtalo de nuevo o contacta con soporte.",
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Imprimir stack trace para depuración
        } catch (Exception ex) {
             // Captura otras posibles excepciones
             JOptionPane.showMessageDialog(this,
                "Error inesperado durante la compra:\n" + ex.getMessage(),
                "Error Inesperado",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Constructor vacío (si es necesario por alguna razón, aunque no parece usarse aquí)
    public VistaCarrito() {
        // Podría inicializar componentes básicos o dejarlo vacío si no se usa
        super(); // Llama al constructor de JDialog
        System.err.println("Advertencia: Se ha llamado al constructor vacío de VistaCarrito.");
        // Inicializa componentes básicos para evitar NullPointerExceptions si se muestra accidentalmente
         getContentPane().setLayout(new BorderLayout());
         getContentPane().add(new JLabel("Carrito vacío o no inicializado."), BorderLayout.CENTER);
         setSize(300, 200);
         setLocationRelativeTo(null);
    }
}