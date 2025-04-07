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
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

// Import de MigLayout (Asegúrate de tener la librería en tu proyecto)
import net.miginfocom.swing.MigLayout;

// Import específico de FlatLaf (COMENTADO - Descomentar si tienes FlatLaf >= 1.0 y quieres placeholders/outline)
// import com.formdev.flatlaf.FlatClientProperties;

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
    private Cliente localClien;

    // --- Constantes de Estilo ---
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TEXTO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_RADIO = new Font("Segoe UI", Font.PLAIN, 12);

    private static final int PADDING_GENERAL = 20; // Espacio exterior e interior principal
    private static final int PADDING_INTERNO = 10; // Espacio menor
    private static final String GAP_LABEL_COMPONENT = "rel";  // Gap entre etiqueta y campo (estándar)
    private static final String GAP_ROW = "unrel"; // Gap entre filas (estándar mayor)
    private static final String GAP_SECTION = "18"; // Gap vertical más grande entre secciones

    // private static final Color COLOR_VALIDATION_ERROR = Color.RED; // Ya no se usa para resaltar campos


    /**
     * Constructor.
     */
    public VistaUsuario(Cliente clien, Window ventPadre) {
        super(ventPadre, clien == null ? "Registro de Usuario" : "Mis Datos", ModalityType.APPLICATION_MODAL);
        this.localClien = clien;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // ASUNCIÓN: FlatLaf ya está configurado en Principal.java

        initComponents();
        configureView();
        pack();
        setMinimumSize(new Dimension(580, getHeight())); // Ancho mínimo, altura ajustada por pack()
        setLocationRelativeTo(ventPadre);
    }

    /**
     * Inicializa y organiza los componentes.
     */
    private void initComponents() {
        // Panel Principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, PADDING_GENERAL));
        mainPanel.setBackground(UIManager.getColor("Panel.background")); // Usar color del tema
        mainPanel.setBorder(new EmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));
        setContentPane(mainPanel);

        // Título (NORTH)
        lblTitulo = new JLabel(localClien == null ? "NUEVO USUARIO" : "DATOS DEL USUARIO", JLabel.CENTER);
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setIcon(cargarIcono("/iconos/user_profile.png", 24, 24)); // Icono para título (opcional)
        lblTitulo.setIconTextGap(PADDING_INTERNO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, PADDING_INTERNO, 0)); // Espacio debajo
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario (CENTER) con MigLayout
        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets " + PADDING_INTERNO, // fill horizontal, padding interno
                "[align label]" + GAP_LABEL_COMPONENT + "[grow, fill]", // Columnas: label, gap, component
                "" // Filas: usar gaps definidos en wrap
        ));
        formPanel.setBackground(UIManager.getColor("Panel.background"));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Campos del Formulario ---
        formPanel.add(createLabel("Usuario:"));
        textUser = new JTextField();
        textUser.setFont(FONT_TEXTO);
        formPanel.add(textUser, "wrap " + GAP_ROW);

        formPanel.add(createLabel("Contraseña:"));
        passwordFieldContra = new JPasswordField();
        passwordFieldContra.setFont(FONT_TEXTO);
        formPanel.add(passwordFieldContra, "wrap " + GAP_ROW);

        formPanel.add(createLabel("DNI/NIE:"));
        textDni = new JTextField();
        textDni.setFont(FONT_TEXTO);
        formPanel.add(textDni, "wrap " + GAP_ROW);

        formPanel.add(createLabel("Email:"));
        textEmail = new JTextField();
        textEmail.setFont(FONT_TEXTO);
        formPanel.add(textEmail, "wrap " + GAP_ROW);

        formPanel.add(createLabel("Dirección:"));
        textDireccion = new JTextField();
        textDireccion.setFont(FONT_TEXTO);
        formPanel.add(textDireccion, "wrap " + GAP_SECTION); // Más espacio antes de la siguiente sección

        // --- Sección Método de Pago ---
        JPanel paymentPanel = new JPanel(new MigLayout(
            "insets 0, gapx " + PADDING_INTERNO, // Sin insets, gap horizontal
            "[]" + PADDING_INTERNO + "[]" + PADDING_INTERNO + "[]" // 3 columnas para radios
        ));
        paymentPanel.setOpaque(false); // Hacer transparente para heredar fondo del formPanel
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
            UIManager.getBorder("TitledBorder.border"), "Método de Pago",
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL, UIManager.getColor("TitledBorder.titleColor")
        ));

        rdbtnVisa = createRadioButton("Visa");
        rdbtnMastercard = createRadioButton("Mastercard");
        rdbtnPaypal = createRadioButton("PayPal");

        paymentMethodGroup = new ButtonGroup();
        paymentMethodGroup.add(rdbtnVisa);
        paymentMethodGroup.add(rdbtnMastercard);
        paymentMethodGroup.add(rdbtnPaypal);

        paymentPanel.add(rdbtnVisa);
        paymentPanel.add(rdbtnMastercard);
        paymentPanel.add(rdbtnPaypal);
        // Añadir al formPanel, ocupando 2 columnas, creciendo horizontalmente
        formPanel.add(paymentPanel, "span 2, growx, wrap " + GAP_ROW);

        // --- Número de Cuenta/Tarjeta ---
        formPanel.add(createLabel("Nº Tarjeta/Cuenta:"));
        textNumeroCuenta = new JTextField();
        textNumeroCuenta.setFont(FONT_TEXTO);
        formPanel.add(textNumeroCuenta, "wrap " + GAP_SECTION); // Más espacio después

        // --- Panel de Botones (SOUTH) ---
        JPanel buttonPanel = new JPanel(new MigLayout(
                "fillx, insets " + PADDING_INTERNO + " 0 0 0", // fill H, padding solo arriba
                "[left, grow]" + GAP_SECTION + "[right]" // Columnas: izq(crece), gap grande, der
        ));
        buttonPanel.setBackground(UIManager.getColor("Panel.background"));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Crear Botones ---
        // Iconos: Asegúrate de tenerlos en /iconos/
        btnRegistrarse = crearBoton("Registrar", "/iconos/register.png");
        btnRegistrarse.putClientProperty("JButton.buttonType", "primary");

        btnModificar = crearBoton("Guardar Cambios", "/iconos/save.png");
        btnModificar.putClientProperty("JButton.buttonType", "primary");

        btnDrop = crearBoton("Dar de Baja", "/iconos/delete.png");
        btnDrop.putClientProperty("JButton.buttonType", "danger"); // Estilo peligro FlatLaf

        btnMostrarPedidos = crearBoton("Mis Pedidos", "/iconos/orders.png");

        // --- Añadir Botones al Panel ---
        // Añadir botones de acción (izquierda) directamente a MigLayout
        buttonPanel.add(btnRegistrarse, "split 3, flowx, gapright " + GAP_ROW); // split 3, flujo horizontal, gap derecho
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnDrop, "gapleft " + GAP_SECTION); // Gap grande a la izq de Baja

        // Añadir botón Mis Pedidos (derecha)
        buttonPanel.add(btnMostrarPedidos, "cell 1 0"); // celda 1 (segunda columna), fila 0
    }

    /** Helper para crear JLabels estándar */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        // El color se hereda del LaF
        return label;
    }

    /** Helper para crear JRadioButtons estándar */
    private JRadioButton createRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(FONT_RADIO);
        rb.setOpaque(false); // Hacer transparente para heredar fondo
        return rb;
    }

    /** Helper para crear JButtons estándar */
    private JButton crearBoton(String texto, String iconoPath) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON);
        if (iconoPath != null && !iconoPath.isEmpty()) {
            ImageIcon icon = cargarIcono(iconoPath, 16, 16); // Iconos 16x16
            if (icon != null && icon.getIconWidth() > 0) {
                 btn.setIcon(icon);
                 btn.setHorizontalTextPosition(SwingConstants.RIGHT);
                 btn.setIconTextGap(PADDING_INTERNO / 2);
            }
        }
        btn.addActionListener(this);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Usar márgenes para padding interno
        btn.setMargin(new Insets(PADDING_INTERNO / 2 + 1, PADDING_INTERNO, PADDING_INTERNO / 2 + 1, PADDING_INTERNO));
        return btn;
    }

    /**
     * Configura la visibilidad de botones y carga datos si es modificación.
     */
    private void configureView() {
        boolean isRegisterMode = (localClien == null);

        btnRegistrarse.setVisible(isRegisterMode);
        btnModificar.setVisible(!isRegisterMode);
        btnMostrarPedidos.setVisible(!isRegisterMode);
        btnDrop.setVisible(!isRegisterMode);

        if (isRegisterMode) {
            lblTitulo.setText("NUEVO USUARIO");
            rdbtnVisa.setSelected(true);
        } else {
            lblTitulo.setText("DATOS DEL USUARIO");
            // Cargar datos existentes
            textUser.setText(localClien.getUsuario());
            textDni.setText(localClien.getDni());
            textEmail.setText(localClien.getCorreo());
            textDireccion.setText(localClien.getDireccion());
            textNumeroCuenta.setText(localClien.getNum_cuenta());
            // No precargar contraseña

            Metodo metodo = localClien.getMetodo_pago();
            if (metodo == Metodo.visa) rdbtnVisa.setSelected(true);
            else if (metodo == Metodo.mastercard) rdbtnMastercard.setSelected(true);
            else if (metodo == Metodo.paypal) rdbtnPaypal.setSelected(true);
            else paymentMethodGroup.clearSelection();
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

    // --- Métodos de Lógica (Alta, Modificar, Baja, MostrarPedidos) ---
    // (La lógica interna no cambia)

    private void alta() {
        if (!validarCampos(false)) return;
        Cliente nuevoCliente = new Cliente();
        // ID se asignará en DAO/BD o con Principal.obtenerNewIdCliente() si es necesario aquí
        nuevoCliente.setUsuario(textUser.getText().trim());
        nuevoCliente.setContra(String.valueOf(passwordFieldContra.getPassword()));
        nuevoCliente.setCorreo(textEmail.getText().trim());
        nuevoCliente.setDireccion(textDireccion.getText().trim());
        nuevoCliente.setDni(textDni.getText().trim().toUpperCase());
        nuevoCliente.setNum_cuenta(textNumeroCuenta.getText().trim());
        nuevoCliente.setMetodo_pago(obtenerMetodoPagoSeleccionado());
        nuevoCliente.setEsAdmin(false);
        try {
            Principal.altaCliente(nuevoCliente);
            JOptionPane.showMessageDialog(this, "Usuario '" + nuevoCliente.getUsuario() + "' registrado.",
                    "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (AltaError e) { e.visualizarMen(); }
        catch (Exception e) { handleGenericError(e, "registro"); }
    }

    private void modificar() {
        if (localClien == null || !validarCampos(true)) return;
        Cliente clienteModificado = new Cliente();
        clienteModificado.setId_usu(localClien.getId_usu());
        clienteModificado.setUsuario(textUser.getText().trim());
        clienteModificado.setCorreo(textEmail.getText().trim());
        clienteModificado.setDireccion(textDireccion.getText().trim());
        clienteModificado.setDni(textDni.getText().trim().toUpperCase());
        clienteModificado.setNum_cuenta(textNumeroCuenta.getText().trim());
        clienteModificado.setMetodo_pago(obtenerMetodoPagoSeleccionado());
        clienteModificado.setEsAdmin(localClien.isEsAdmin());

        String newPassword = String.valueOf(passwordFieldContra.getPassword());
        if (!newPassword.isEmpty()) {
             if (newPassword.length() >= 6 && newPassword.matches(".*[a-zA-Z].*") && newPassword.matches(".*[0-9].*")) {
                clienteModificado.setContra(newPassword);
             } else {
                 JOptionPane.showMessageDialog(this, "Contraseña nueva inválida. No se guardará.", "Error Contraseña", JOptionPane.WARNING_MESSAGE);
                 return;
             }
        } else { clienteModificado.setContra(localClien.getContra()); }

        try {
            Principal.modificarCliente(clienteModificado);
            this.localClien = clienteModificado;
            JOptionPane.showMessageDialog(this, "Datos actualizados.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (modifyError ex) { ex.visualizarMen(); }
        catch (Exception ex) { handleGenericError(ex, "modificación"); }
    }

    private void mostrarPedidos() {
        if (localClien != null) {
            // Asumiendo que VerPedidosCliente también usa FlatLaf y tiene estilo moderno
            VerPedidosCliente vistaPedidoClien = new VerPedidosCliente(this, localClien);
            vistaPedidoClien.setVisible(true);
        }
    }

    private void baja() {
        if (localClien == null) return;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres ELIMINAR tu cuenta?\nUsuario: " + localClien.getUsuario() + "\n¡Acción IRREVERSIBLE!",
                "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                Principal.bajaCliente(localClien);
                JOptionPane.showMessageDialog(this, "Usuario eliminado.", "Baja Exitosa", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // Window owner = getOwner(); if (owner != null) { owner.dispose(); } // Opcional: cerrar padre
            } catch (DropError e) { e.visualizarMen(); }
            catch (Exception ex) { handleGenericError(ex, "baja"); }
        }
    }

    private Metodo obtenerMetodoPagoSeleccionado() {
        if (rdbtnVisa.isSelected()) return Metodo.visa;
        if (rdbtnMastercard.isSelected()) return Metodo.mastercard;
        if (rdbtnPaypal.isSelected()) return Metodo.paypal;
        return null;
    }

    /** Validación de Campos (refinada) */
    private boolean validarCampos(boolean modificarModo) {
        StringBuilder errores = new StringBuilder();
        final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        final String DNI_REGEX = "^\\d{8}[A-HJ-NP-TV-Z]$";
        final String NIE_REGEX = "^[XYZxyz]\\d{7}[A-HJ-NP-TV-Z]$";

        // Validaciones...
        if (textUser.getText().trim().isEmpty()) errores.append("- Usuario obligatorio.\n");

        String password = String.valueOf(passwordFieldContra.getPassword());
        if (!modificarModo && password.isEmpty()) {
            errores.append("- Contraseña obligatoria.\n");
        } else if (!password.isEmpty()) {
             if (password.length() < 6) errores.append("- Contraseña: Mínimo 6 caracteres.\n");
             if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
                 errores.append("- Contraseña: Debe incluir letras y números.\n");
             }
        }
        String dniNie = textDni.getText().trim().toUpperCase();
        if (dniNie.isEmpty()) {
            errores.append("- DNI/NIE obligatorio.\n");
        } else if (!dniNie.matches(DNI_REGEX) && !dniNie.matches(NIE_REGEX)) {
             errores.append("- Formato DNI/NIE incorrecto.\n");
        }
        String email = textEmail.getText().trim();
        if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
            errores.append("- Formato Email incorrecto.\n");
        }
        if (textDireccion.getText().trim().isEmpty()) {
            errores.append("- Dirección obligatoria.\n");
        }
        if (obtenerMetodoPagoSeleccionado() == null) {
            errores.append("- Método de pago obligatorio.\n");
        }
        if (textNumeroCuenta.getText().trim().isEmpty()) {
            errores.append("- Nº Tarjeta/Cuenta obligatorio.\n");
        }

        // Mostrar Errores si existen
        if (errores.length() > 0) {
            JTextArea textArea = new JTextArea("Por favor, corrige los errores:\n\n" + errores.toString());
            textArea.setEditable(false); textArea.setOpaque(false); textArea.setFont(FONT_TEXTO);
            JScrollPane scrollPane = new JScrollPane(textArea) {
                 @Override public Dimension getPreferredSize() { return new Dimension(350, 150); }
            };
            JOptionPane.showMessageDialog(this, scrollPane, "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true; // Todo válido
    }

    /** Manejador genérico de excepciones */
    private void handleGenericError(Exception ex, String action) {
         ex.printStackTrace();
         JOptionPane.showMessageDialog(this, "Error inesperado durante la " + action + ".",
                 "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Método Cargar Icono (robusto con placeholder) */
    private ImageIcon cargarIcono(String path, int width, int height) {
        InputStream imgStream = getClass().getResourceAsStream(path);
        if (imgStream != null) {
            try {
                BufferedImage originalImage = ImageIO.read(imgStream);
                imgStream.close();
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } catch (Exception e) { System.err.println("Error leer icono: " + path + " - " + e.getMessage()); }
        } else { System.err.println("Icono no encontrado: " + path); }
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
             VistaUsuario dialogRegistro = new VistaUsuario(null, null);
             dialogRegistro.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
             dialogRegistro.setVisible(true);
         });
     }
     */
}