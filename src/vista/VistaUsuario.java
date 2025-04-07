package vista;

import modelo.Cliente;
import modelo.Metodo;
import controlador.Principal;
import excepciones.AltaError;
import excepciones.DropError;
import excepciones.modifyError;

// Imports de Swing y AWT
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage; // Para placeholder icono
import java.io.InputStream; // Para cargar icono
import java.net.URL; // Para cargar icono (alternativa)
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO; // Para cargar icono

// Import de MigLayout (Asegúrate de tener la librería en tu proyecto)
import net.miginfocom.swing.MigLayout; // Importar MigLayout

public class VistaUsuario extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // --- Componentes de la UI ---
    private JTextField textUser, textDni, textEmail, textDireccion, textNumeroCuenta;
    private JPasswordField passwordFieldContra;
    private JRadioButton rdbtnVisa, rdbtnMastercard, rdbtnPaypal;
    private ButtonGroup paymentMethodGroup;
    private JButton btnRegistrarse, btnModificar, btnDrop, btnMostrarPedidos;
    private JLabel lblTitulo;

    // --- Datos ---
    private Cliente localClien; // El cliente actual (null si es registro)

    // --- Constantes de Estilo ---
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20); // Ligeramente más grande
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13); // Ligeramente más grande
    private static final Font FONT_TEXTO = new Font("Segoe UI", Font.PLAIN, 13); // Ligeramente más grande
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_RADIO = new Font("Segoe UI", Font.PLAIN, 12);

    private static final int PADDING_GENERAL = 20; // Más espaciado general
    private static final int PADDING_INTERNO = 10; // Espaciado dentro de paneles
    private static final String GAP_LABEL_COMPONENT = "rel";  // Gap estándar entre etiqueta y campo
    private static final String GAP_ROW = "unrel"; // Gap estándar entre filas (un poco más grande)
    private static final String GAP_GROUP = "push"; // Gap grande entre grupos de botones

    private static final Color COLOR_VALIDATION_ERROR = Color.RED; // Mantener para errores

    /**
     * Constructor.
     */
    public VistaUsuario(Cliente clien, Window ventPadre) {
        super(ventPadre, clien == null ? "Registro de Usuario" : "Mis Datos", ModalityType.APPLICATION_MODAL);
        this.localClien = clien;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Configurar Look and Feel (Asumimos que FlatLaf ya está activo desde Principal)
        // Opcional: Establecer propiedades específicas aquí si no son globales
        // UIManager.put("Button.arc", 8);
        // UIManager.put("Component.arc", 5);

        initComponents();
        configureView();
        pack();
        setMinimumSize(new Dimension(550, getHeight())); // Ancho mínimo, altura ajustada
        setLocationRelativeTo(ventPadre);
    }

    /**
     * Inicializa y organiza los componentes usando MigLayout.
     */
    private void initComponents() {
        // Panel Principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, PADDING_GENERAL));
        // Usar color de fondo del tema actual
        mainPanel.setBackground(UIManager.getColor("Panel.background"));
        mainPanel.setBorder(new EmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));
        setContentPane(mainPanel);

        // Título (NORTH)
        lblTitulo = new JLabel(localClien == null ? "NUEVO USUARIO" : "DATOS DEL USUARIO", JLabel.CENTER);
        lblTitulo.setFont(FONT_TITULO);
        // lblTitulo.setIcon(cargarIcono("/iconos/user_profile.png", 24, 24)); // Icono opcional
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario (CENTER) con MigLayout
        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets " + PADDING_INTERNO, // fill horizontal, con padding interno
                "[align label]" + GAP_LABEL_COMPONENT + "[grow, fill]", // Columnas: label, gap, component (grow)
                "" // Filas: usar gaps por defecto ('unrel')
        ));
        // Heredar color de fondo
        formPanel.setBackground(UIManager.getColor("Panel.background"));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Campos del Formulario ---
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(FONT_LABEL);
        formPanel.add(lblUsuario);
        textUser = new JTextField();
        textUser.setFont(FONT_TEXTO);
        formPanel.add(textUser, "wrap " + GAP_ROW);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(FONT_LABEL);
        formPanel.add(lblPassword);
        passwordFieldContra = new JPasswordField();
        passwordFieldContra.setFont(FONT_TEXTO);
        formPanel.add(passwordFieldContra, "wrap " + GAP_ROW);

        JLabel lblDni = new JLabel("DNI/NIE:");
        lblDni.setFont(FONT_LABEL);
        formPanel.add(lblDni);
        textDni = new JTextField();
        textDni.setFont(FONT_TEXTO);
        formPanel.add(textDni, "wrap " + GAP_ROW);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(FONT_LABEL);
        formPanel.add(lblEmail);
        textEmail = new JTextField();
        textEmail.setFont(FONT_TEXTO);
        formPanel.add(textEmail, "wrap " + GAP_ROW);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(FONT_LABEL);
        formPanel.add(lblDireccion);
        textDireccion = new JTextField();
        textDireccion.setFont(FONT_TEXTO);
        formPanel.add(textDireccion, "wrap " + GAP_ROW);

        // --- Sección Método de Pago ---
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_INTERNO, 0));
        paymentPanel.setBackground(UIManager.getColor("Panel.background")); // Heredar fondo
        // Usar borde con título proporcionado por el LaF
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
            UIManager.getBorder("TitledBorder.border"), // Obtener borde del LaF
            "Método de Pago", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL,
            UIManager.getColor("TitledBorder.titleColor") // Obtener color del LaF
        ));

        rdbtnVisa = new JRadioButton("Visa");
        rdbtnVisa.setFont(FONT_RADIO);
        rdbtnMastercard = new JRadioButton("Mastercard");
        rdbtnMastercard.setFont(FONT_RADIO);
        rdbtnPaypal = new JRadioButton("PayPal");
        rdbtnPaypal.setFont(FONT_RADIO);

        // Heredar fondo para radios
        rdbtnVisa.setBackground(paymentPanel.getBackground());
        rdbtnMastercard.setBackground(paymentPanel.getBackground());
        rdbtnPaypal.setBackground(paymentPanel.getBackground());

        paymentMethodGroup = new ButtonGroup();
        paymentMethodGroup.add(rdbtnVisa);
        paymentMethodGroup.add(rdbtnMastercard);
        paymentMethodGroup.add(rdbtnPaypal);

        paymentPanel.add(rdbtnVisa);
        paymentPanel.add(rdbtnMastercard);
        paymentPanel.add(rdbtnPaypal);
        formPanel.add(paymentPanel, "span 2, growx, wrap " + GAP_ROW); // Ocupa ambas columnas

        // Número de Cuenta/Tarjeta
        JLabel lblNumeroCuenta = new JLabel("Nº Tarjeta/Cuenta:");
        lblNumeroCuenta.setFont(FONT_LABEL);
        formPanel.add(lblNumeroCuenta);
        textNumeroCuenta = new JTextField();
        textNumeroCuenta.setFont(FONT_TEXTO);
        formPanel.add(textNumeroCuenta, "wrap " + GAP_ROW);

        // --- Panel de Botones (SOUTH) ---
        JPanel buttonPanel = new JPanel(new MigLayout(
                "fillx, insets " + PADDING_INTERNO + " 0 0 0", // fill horizontal, padding superior
                "[left, grow]" + GAP_GROUP + "[right]" // Columnas: izquierda (crece), gap grande, derecha
        ));
        buttonPanel.setBackground(UIManager.getColor("Panel.background")); // Heredar fondo
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Botones de Acción (izquierda)
        btnRegistrarse = crearBoton("Registrar", "/iconos/register.png");
        btnRegistrarse.putClientProperty("JButton.buttonType", "primary"); // Marcar como primario

        btnModificar = crearBoton("Guardar Cambios", "/iconos/save.png");
        btnModificar.putClientProperty("JButton.buttonType", "primary"); // Marcar como primario

        btnDrop = crearBoton("Dar de Baja", "/iconos/delete.png");
        // Indicar a FlatLaf que es un botón de peligro (lo pintará de rojo usualmente)
        btnDrop.putClientProperty("JButton.buttonType", "danger");

        // Agrupar botones de acción en un subpanel si se desea control más fino
        // O añadirlos directamente a MigLayout
        buttonPanel.add(btnRegistrarse, "split 3, flowx"); // split 3 indica 3 componentes en esta celda, flowx los pone seguidos
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnDrop, "gapleft unrel"); // Espacio antes de "Dar de Baja"

        // Botón Mostrar Pedidos (derecha)
        btnMostrarPedidos = crearBoton("Mis Pedidos", "/iconos/orders.png");
        buttonPanel.add(btnMostrarPedidos, "cell 1 0"); // Poner en la segunda columna (índice 1), fila 0

    }

     /**
     * Crea y configura un JButton estándar con opción de icono.
     */
    private JButton crearBoton(String texto, String iconoPath) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON);
        // Cargar icono opcionalmente (ej. 20x20)
        if (iconoPath != null && !iconoPath.isEmpty()) {
            ImageIcon icon = cargarIcono(iconoPath, 20, 20); // Iconos un poco más grandes
            if (icon != null && icon.getIconWidth() > 0) {
                 btn.setIcon(icon);
                 // btn.setHorizontalTextPosition(SwingConstants.RIGHT); // Texto a la derecha del icono
            }
        }
        btn.addActionListener(this);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false); // Dejar que FlatLaf maneje el foco
        // Padding interno del botón
        btn.setMargin(new Insets(PADDING_INTERNO / 2, PADDING_INTERNO, PADDING_INTERNO / 2, PADDING_INTERNO));
        return btn;
    }


    /**
     * Configura la visibilidad de botones y carga datos si es modificación.
     */
    private void configureView() {
        if (localClien == null) { // Modo Registro
            btnModificar.setVisible(false);
            btnMostrarPedidos.setVisible(false);
            btnDrop.setVisible(false);
            btnRegistrarse.setVisible(true);
            rdbtnVisa.setSelected(true); // Por defecto Visa
        } else { // Modo Modificación
            btnRegistrarse.setVisible(false);
            btnModificar.setVisible(true);
            btnMostrarPedidos.setVisible(true);
            btnDrop.setVisible(true);

            // Cargar datos
            textUser.setText(localClien.getUsuario());
            textDni.setText(localClien.getDni());
            textEmail.setText(localClien.getCorreo());
            textDireccion.setText(localClien.getDireccion());
            textNumeroCuenta.setText(localClien.getNum_cuenta());
            // No precargar contraseña por seguridad

            // Seleccionar método de pago
            Metodo metodo = localClien.getMetodo_pago();
            if (metodo == Metodo.visa) rdbtnVisa.setSelected(true);
            else if (metodo == Metodo.mastercard) rdbtnMastercard.setSelected(true);
            else if (metodo == Metodo.paypal) rdbtnPaypal.setSelected(true);
            else paymentMethodGroup.clearSelection();

            // Ejemplo: Hacer usuario no editable en modo modificación
            // textUser.setEditable(false);
            // textUser.setForeground(UIManager.getColor("Label.disabledForeground"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnRegistrarse) alta();
        else if (source == btnModificar) modificar();
        else if (source == btnMostrarPedidos) mostrarPedidos();
        else if (source == btnDrop) baja();
    }

    /** Lógica de Alta */
    private void alta() {
        if (!validarCampos(false)) return; // Validar para registro

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId_usu(Principal.obtenerNewIdCliente());
        nuevoCliente.setUsuario(textUser.getText().trim());
        nuevoCliente.setContra(String.valueOf(passwordFieldContra.getPassword())); // Ya validada longitud/contenido
        nuevoCliente.setCorreo(textEmail.getText().trim());
        nuevoCliente.setDireccion(textDireccion.getText().trim());
        nuevoCliente.setDni(textDni.getText().trim().toUpperCase()); // Guardar DNI en mayúsculas
        nuevoCliente.setNum_cuenta(textNumeroCuenta.getText().trim());
        nuevoCliente.setMetodo_pago(obtenerMetodoPagoSeleccionado());
        nuevoCliente.setEsAdmin(false);

        try {
            Principal.altaCliente(nuevoCliente);
            JOptionPane.showMessageDialog(this, "Usuario '" + nuevoCliente.getUsuario() + "' registrado con éxito.",
                    "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cerrar ventana después del registro exitoso
        } catch (AltaError e) {
            e.visualizarMen(); // Mostrar mensaje de la excepción
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado durante el registro.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Lógica de Modificación */
    private void modificar() {
        if (localClien == null) return;
        if (!validarCampos(true)) return; // Validar para modificación (permite pass vacío)

        localClien.setUsuario(textUser.getText().trim());
        String newPassword = String.valueOf(passwordFieldContra.getPassword());
        // Solo actualizar contraseña si se introdujo una nueva válida
        if (!newPassword.isEmpty()) {
             if (newPassword.length() >= 6 && newPassword.matches(".*[a-zA-Z].*") && newPassword.matches(".*[0-9].*")) {
                localClien.setContra(newPassword);
             } else {
                 // Si se introdujo algo pero no es válido, avisar y no guardar (ya validado en validarCampos, pero doble check)
                 JOptionPane.showMessageDialog(this, "La nueva contraseña introducida no cumple los requisitos y no se guardará.",
                         "Contraseña Inválida", JOptionPane.WARNING_MESSAGE);
                 return; // Detener modificación si la nueva contraseña es inválida
             }
        } // Si estaba vacío, no se toca la contraseña existente

        localClien.setCorreo(textEmail.getText().trim());
        localClien.setDireccion(textDireccion.getText().trim());
        localClien.setDni(textDni.getText().trim().toUpperCase()); // Guardar DNI en mayúsculas
        localClien.setNum_cuenta(textNumeroCuenta.getText().trim());
        localClien.setMetodo_pago(obtenerMetodoPagoSeleccionado());

        try {
            Principal.modificarCliente(localClien);
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.",
                    "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (modifyError ex) {
            ex.visualizarMen();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado al modificar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Mostrar Pedidos */
    private void mostrarPedidos() {
        if (localClien != null) {
            VerPedidosCliente vistaPedidoClien = new VerPedidosCliente(this, localClien);
            vistaPedidoClien.setVisible(true);
        }
    }

    /** Lógica de Baja */
    private void baja() {
        if (localClien == null) return;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres ELIMINAR tu cuenta?\nUsuario: " + localClien.getUsuario() + "\n¡Esta acción es IRREVERSIBLE!",
                "Confirmar Baja Definitiva", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                Principal.bajaCliente(localClien);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.",
                        "Baja Exitosa", JOptionPane.INFORMATION_MESSAGE);
                // Decidir qué hacer después: cerrar esta ventana y ¿la que la llamó?
                // Por ahora, solo cerramos esta. La ventana padre (AdminConfigUsuario o Tienda) seguirá abierta.
                dispose();
                // Si quisiéramos cerrar también la ventana padre (suponiendo que es un JDialog)
                // Window parent = getOwner();
                // if (parent != null) { parent.dispose(); }
            } catch (DropError e) {
                e.visualizarMen();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inesperado al dar de baja.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Obtener Método de Pago */
    private Metodo obtenerMetodoPagoSeleccionado() {
        if (rdbtnVisa.isSelected()) return Metodo.visa;
        if (rdbtnMastercard.isSelected()) return Metodo.mastercard;
        if (rdbtnPaypal.isSelected()) return Metodo.paypal;
        return null;
    }

    /** Validación de Campos (Mejorada) */
    private boolean validarCampos(boolean modificarModo) {
        StringBuilder errores = new StringBuilder();
        final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        // DNI: 8 números + 1 letra (case-insensitive)
        final String DNI_REGEX = "^\\d{8}[A-Za-z]$";
        // NIE: X, Y o Z + 7 números + 1 letra (case-insensitive)
        final String NIE_REGEX = "^[XYZxyz]\\d{7}[A-Za-z]$";

        // Usuario
        if (textUser.getText().trim().isEmpty()) {
            errores.append("- El nombre de usuario es obligatorio.\n");
        }

        // Contraseña
        String password = String.valueOf(passwordFieldContra.getPassword());
        if (!modificarModo && password.isEmpty()) { // Obligatoria en registro
            errores.append("- La contraseña es obligatoria.\n");
        } else if (!password.isEmpty()) { // Validar si no está vacía (en registro o si se introduce en modificación)
             if (password.length() < 6) {
                 errores.append("- La contraseña debe tener al menos 6 caracteres.\n");
             }
             if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
                 errores.append("- La contraseña debe contener letras y números.\n");
             }
        }

        // DNI/NIE
        String dniNie = textDni.getText().trim().toUpperCase(); // Validar en mayúsculas
        if (dniNie.isEmpty()) {
            errores.append("- El DNI/NIE es obligatorio.\n");
        } else if (!dniNie.matches(DNI_REGEX) && !dniNie.matches(NIE_REGEX)) {
             errores.append("- Formato de DNI/NIE incorrecto.\n");
        }
        // Podría añadirse validación de letra del DNI/NIE aquí si fuera necesario

        // Email
        String email = textEmail.getText().trim();
        if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
            errores.append("- Formato de Email incorrecto.\n");
        }

        // Dirección
        if (textDireccion.getText().trim().isEmpty()) {
            errores.append("- La dirección es obligatoria.\n");
        }

        // Método de Pago
        if (obtenerMetodoPagoSeleccionado() == null) {
            errores.append("- Debes seleccionar un método de pago.\n");
        }

        // Número de Cuenta/Tarjeta (Validación básica de no vacío)
        if (textNumeroCuenta.getText().trim().isEmpty()) {
            errores.append("- El número de tarjeta/cuenta es obligatorio.\n");
        } // Podría añadirse validación de formato (Luhn, etc.) si fuera necesario


        // Mostrar errores
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this, "Por favor, corrige los siguientes errores:\n\n" + errores.toString(),
                    "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            // Opcional: Poner foco en el primer campo con error
            // findFirstInvalidComponent().requestFocusInWindow();
            return false;
        }

        return true; // Todo válido
    }


    /** Método Cargar Icono */
    private ImageIcon cargarIcono(String path, int width, int height) {
        InputStream imgStream = getClass().getResourceAsStream(path);
        if (imgStream != null) {
            try {
                BufferedImage originalImage = ImageIO.read(imgStream);
                imgStream.close();
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } catch (Exception e) {
                 System.err.println("Error al leer icono: " + path + " - " + e.getMessage());
            }
        } else {
            System.err.println("Icono no encontrado en classpath: " + path);
        }
        // Placeholder
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.LIGHT_GRAY); g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.DARK_GRAY); g2d.drawRect(0,0, width-1, height-1);
            g2d.setColor(UIManager.getColor("Label.foreground") != null ? UIManager.getColor("Label.foreground") : Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.BOLD, Math.min(width, height) * 2 / 3));
            g2d.drawString("?", width/3, height * 3/4);
        } finally { g2d.dispose(); }
        return new ImageIcon(placeholder);
    }

     /* // --- Main de Ejemplo ---
     public static void main(String[] args) {
         try { UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); }
         catch (Exception ex) { System.err.println("Failed to initialize LaF"); }

         SwingUtilities.invokeLater(() -> {
             // Prueba modo Registro
             VistaUsuario dialogRegistro = new VistaUsuario(null, null); // Sin padre
             dialogRegistro.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
             dialogRegistro.setVisible(true);

             // // Prueba modo Modificación (necesitarías crear un Cliente de prueba)
             // Cliente clienteExistente = new Cliente();
             // clienteExistente.setId_usu(1); clienteExistente.setUsuario("user1"); // etc.
             // VistaUsuario dialogModificar = new VistaUsuario(clienteExistente, null);
             // dialogModificar.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
             // dialogModificar.setVisible(true);
         });
     }
     */
}