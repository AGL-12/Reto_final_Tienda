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
import javax.swing.border.TitledBorder; // Para agrupar radio buttons
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Para cargar iconos
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Import de MigLayout (Asegúrate de tener la librería en tu proyecto)
import net.miginfocom.swing.MigLayout;

public class VistaUsuario extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // --- Componentes de la UI ---
    private JTextField textUser, textDni, textEmail, textDireccion, textNumeroCuenta;
    private JPasswordField passwordFieldContra;
    private JRadioButton rdbtnVisa, rdbtnMastercard, rdbtnPaypal;
    private ButtonGroup paymentMethodGroup; // Grupo para radio buttons
    private JButton btnRegistrarse, btnModificar, btnDrop, btnMostrarPedidos;
    private JLabel lblTitulo;

    // --- Datos ---
    private Cliente localClien; // El cliente actual (null si es registro)

    // --- Constantes de Estilo (Ajusta según tus preferencias) ---
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TEXTO = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    private static final int PADDING_GENERAL = 15; // Espaciado exterior
    private static final String GAP_LABEL_COMPONENT = "rel"; // Espacio entre etiqueta y campo
    private static final String GAP_ROW = "unrel";        // Espacio entre filas

    /**
     * Constructor para la ventana de datos de Usuario (Registro o Modificación).
     * @param clien El cliente a modificar, o null si es un nuevo registro.
     * @param ventPadre La ventana padre (JFrame o JDialog).
     */
    public VistaUsuario(Cliente clien, Window ventPadre) { // Acepta Window (JFrame o JDialog)
        super(ventPadre, clien == null ? "Registro de Usuario" : "Mis Datos", ModalityType.APPLICATION_MODAL); // Título dinámico y modal
        this.localClien = clien;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();    // Inicializa y organiza los componentes
        configureView();   // Carga datos si es modificación, ajusta botones
        pack();              // Ajusta tamaño al contenido
        setMinimumSize(getSize()); // Evita que sea más pequeña
        setLocationRelativeTo(ventPadre); // Centra respecto al padre
    }

    /**
     * Inicializa y organiza los componentes usando MigLayout.
     */
    private void initComponents() {
        // --- Panel Principal con BorderLayout ---
        JPanel mainPanel = new JPanel(new BorderLayout(0, PADDING_GENERAL)); // Espacio vertical entre secciones
        mainPanel.setBorder(new EmptyBorder(PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL, PADDING_GENERAL));
        setContentPane(mainPanel); // Establece este como el panel principal del JDialog

        // --- Título (NORTH) ---
        lblTitulo = new JLabel(localClien == null ? "NUEVO USUARIO" : "DATOS DEL USUARIO", JLabel.CENTER);
        lblTitulo.setFont(FONT_TITULO);
        // lblTitulo.setIcon(cargarIcono("/iconos/usuario_titulo.png")); // Icono opcional para el título
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- Panel de Formulario (CENTER) con MigLayout ---
        // "fillx": Hace que el panel ocupe el ancho disponible.
        // "wrap 2": Indica que después de 2 componentes (label + field), salte a la siguiente fila.
        // "[]": Define las propiedades de las filas (sin restricciones especiales aquí).
        // "[align label]": Define la columna de etiquetas (alineadas a la derecha por defecto).
        // "[]": Define las propiedades de las columnas de espacio entre label y componente.
        // "[grow]": Define la columna de componentes (que crezcan horizontalmente).
        JPanel formPanel = new JPanel(new MigLayout(
                "fillx, insets 0",                 // Layout constraints: fill horizontalmente, sin insets propios
                "[align label]" + GAP_LABEL_COMPONENT + "[grow, fill]", // Column constraints: label, gap, component (grow+fill)
                ""                                 // Row constraints: default gaps defined by unrel below
        ));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- Campos del Formulario ---
        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(FONT_LABEL);
        formPanel.add(lblUsuario);
        textUser = new JTextField();
        textUser.setFont(FONT_TEXTO);
        formPanel.add(textUser, "wrap " + GAP_ROW); // wrap: ir a nueva línea, unrel: espacio vertical

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(FONT_LABEL);
        formPanel.add(lblPassword);
        passwordFieldContra = new JPasswordField();
        passwordFieldContra.setFont(FONT_TEXTO);
        formPanel.add(passwordFieldContra, "wrap " + GAP_ROW);

        // DNI/NIE
        JLabel lblDni = new JLabel("DNI/NIE:");
        lblDni.setFont(FONT_LABEL);
        formPanel.add(lblDni);
        textDni = new JTextField();
        textDni.setFont(FONT_TEXTO);
        formPanel.add(textDni, "wrap " + GAP_ROW);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(FONT_LABEL);
        formPanel.add(lblEmail);
        textEmail = new JTextField();
        textEmail.setFont(FONT_TEXTO);
        formPanel.add(textEmail, "wrap " + GAP_ROW);

        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(FONT_LABEL);
        formPanel.add(lblDireccion);
        textDireccion = new JTextField();
        textDireccion.setFont(FONT_TEXTO);
        formPanel.add(textDireccion, "wrap " + GAP_ROW);

        // --- Sección Método de Pago (Panel anidado) ---
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_GENERAL / 2, 0)); // FlowLayout para los radio buttons
        // Añadir un borde con título para agrupar visualmente
        paymentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), // Tipo de borde
                "Método de Pago",                 // Título
                TitledBorder.LEFT,                  // Alineación del título
                TitledBorder.TOP,                   // Posición del título
                FONT_LABEL                          // Fuente del título
        ));

        rdbtnVisa = new JRadioButton("Visa");
        rdbtnVisa.setFont(FONT_TEXTO);
        rdbtnMastercard = new JRadioButton("Mastercard");
        rdbtnMastercard.setFont(FONT_TEXTO);
        rdbtnPaypal = new JRadioButton("PayPal");
        rdbtnPaypal.setFont(FONT_TEXTO);

        paymentMethodGroup = new ButtonGroup(); // Asegura que solo uno esté seleccionado
        paymentMethodGroup.add(rdbtnVisa);
        paymentMethodGroup.add(rdbtnMastercard);
        paymentMethodGroup.add(rdbtnPaypal);

        paymentPanel.add(rdbtnVisa);
        paymentPanel.add(rdbtnMastercard);
        paymentPanel.add(rdbtnPaypal);

        // Añadir el panel de pago al formulario principal, ocupando todo el ancho (span)
        formPanel.add(paymentPanel, "span 2, growx, wrap " + GAP_ROW); // span 2: ocupa 2 columnas, growx: crece horizontalmente

        // Número de Cuenta/Tarjeta
        JLabel lblNumeroCuenta = new JLabel("Nº Tarjeta/Cuenta:");
        lblNumeroCuenta.setFont(FONT_LABEL);
        formPanel.add(lblNumeroCuenta);
        textNumeroCuenta = new JTextField();
        textNumeroCuenta.setFont(FONT_TEXTO);
        formPanel.add(textNumeroCuenta, "wrap " + GAP_ROW);

        // --- Panel de Botones (SOUTH) ---
        // Usamos MigLayout también para controlar el flujo y posible alineación
        JPanel buttonPanel = new JPanel(new MigLayout(
                "fillx, insets 0", // Ocupa ancho, sin márgenes propios
                "[grow, left][right]" // Dos grupos de columnas: una que crece a la izq, otra a la derecha
        ));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Botones de Acción (Modificar/Registrar/Baja) - Agrupados a la izquierda
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PADDING_GENERAL / 2, 0));
        btnRegistrarse = new JButton("Registrar");
        btnRegistrarse.setFont(FONT_BOTON);
        btnRegistrarse.setIcon(cargarIcono("/iconos/register.png")); // Placeholder
        btnRegistrarse.addActionListener(this);
        actionButtonPanel.add(btnRegistrarse);

        btnModificar = new JButton("Guardar Cambios");
        btnModificar.setFont(FONT_BOTON);
        btnModificar.setIcon(cargarIcono("/iconos/save.png")); // Placeholder
        btnModificar.addActionListener(this);
        actionButtonPanel.add(btnModificar);

        btnDrop = new JButton("Dar de Baja");
        btnDrop.setFont(FONT_BOTON);
        btnDrop.setIcon(cargarIcono("/iconos/delete.png")); // Placeholder
        // Estilo "peligroso" (si usas FlatLaf, puedes añadir propiedades)
        // btnDrop.putClientProperty("JButton.buttonType", "danger"); // Ejemplo FlatLaf
        btnDrop.setForeground(Color.RED); // Color básico si no usas FlatLaf
        btnDrop.addActionListener(this);
        actionButtonPanel.add(btnDrop);

        buttonPanel.add(actionButtonPanel, "cell 0 0"); // Añadir al primer grupo de columnas (izquierda)

        // Botón Mostrar Pedidos - A la derecha
        btnMostrarPedidos = new JButton("Mis Pedidos");
        btnMostrarPedidos.setFont(FONT_BOTON);
        btnMostrarPedidos.setIcon(cargarIcono("/iconos/orders.png")); // Placeholder
        btnMostrarPedidos.addActionListener(this);
        buttonPanel.add(btnMostrarPedidos, "cell 1 0, align right"); // Añadir al segundo grupo (derecha)

    }

    /**
     * Configura la visibilidad/estado de los botones y carga los datos del cliente
     * si estamos en modo modificación.
     */
    private void configureView() {
        if (localClien == null) { // Modo Registro
            btnModificar.setVisible(false);
            btnMostrarPedidos.setVisible(false);
            btnDrop.setVisible(false);
            btnRegistrarse.setVisible(true);
            // Opcional: Poner un método de pago por defecto
            rdbtnVisa.setSelected(true);
        } else { // Modo Modificación
            btnRegistrarse.setVisible(false);
            btnModificar.setVisible(true);
            btnMostrarPedidos.setVisible(true);
            btnDrop.setVisible(true);

            // Cargar datos del cliente en los campos
            textUser.setText(localClien.getUsuario());
            // Por seguridad, no solemos precargar la contraseña existente en un campo editable.
            // Dejarlo vacío para que el usuario la introduzca si quiere cambiarla.
            // Si quieres mostrarla (no recomendado): passwordFieldContra.setText(localClien.getContra());
            textDni.setText(localClien.getDni());
            textEmail.setText(localClien.getCorreo());
            textDireccion.setText(localClien.getDireccion());
            textNumeroCuenta.setText(localClien.getNum_cuenta());

            // Seleccionar el método de pago guardado
            Metodo metodo = localClien.getMetodo_pago();
            if (metodo == Metodo.visa) {
                rdbtnVisa.setSelected(true);
            } else if (metodo == Metodo.mastercard) {
                rdbtnMastercard.setSelected(true);
            } else if (metodo == Metodo.paypal) {
                rdbtnPaypal.setSelected(true);
            } else {
                paymentMethodGroup.clearSelection(); // Ninguno si no coincide o es null
            }

            // Opcional: Hacer campos no modificables (ej. DNI, email si son clave)
            // textUser.setEditable(false); // Ejemplo: no permitir cambiar nombre de usuario
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnRegistrarse) {
            alta();
        } else if (source == btnModificar) {
            modificar();
        } else if (source == btnMostrarPedidos) {
            mostrarPedidos();
        } else if (source == btnDrop) {
            baja();
        }
    }

    /**
     * Recopila datos del formulario y llama al controlador para dar de alta un cliente.
     */
    private void alta() {
        // Validación básica (podría ser más exhaustiva)
        if (!validarCampos()) return;

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId_usu(Principal.obtenerNewIdCliente()); // Asumiendo que la BD/Controlador asigna el ID
        nuevoCliente.setUsuario(textUser.getText().trim());
        nuevoCliente.setContra(String.valueOf(passwordFieldContra.getPassword())); // Mejor forma de obtener password
        nuevoCliente.setCorreo(textEmail.getText().trim());
        nuevoCliente.setDireccion(textDireccion.getText().trim());
        nuevoCliente.setDni(textDni.getText().trim());
        nuevoCliente.setNum_cuenta(textNumeroCuenta.getText().trim());
        nuevoCliente.setMetodo_pago(obtenerMetodoPagoSeleccionado());
        nuevoCliente.setEsAdmin(false); // Por defecto, los nuevos usuarios no son admin

        try {
            Principal.altaCliente(nuevoCliente);
            JOptionPane.showMessageDialog(null, "Cliente creado exitosamente.");
        } catch (AltaError e) {
    		e.visualizarMen();
        }
    }

    /**
     * Recopila datos del formulario y llama al controlador para modificar el cliente actual.
     */
    private void modificar() {
        if (localClien == null) return; // No debería ocurrir si el botón está bien configurado

        // Validación básica
         if (!validarCampos(true)) return; // Permitir contraseña vacía en modificación

        // Actualizar el objeto Cliente local con los datos del formulario
        localClien.setUsuario(textUser.getText().trim());
        String newPassword = String.valueOf(passwordFieldContra.getPassword());
        if (!newPassword.isEmpty()) { // Solo actualizar la contraseña si se escribió algo nuevo
            localClien.setContra(newPassword);
        }
        localClien.setCorreo(textEmail.getText().trim());
        localClien.setDireccion(textDireccion.getText().trim());
        localClien.setDni(textDni.getText().trim());
        localClien.setNum_cuenta(textNumeroCuenta.getText().trim());
        localClien.setMetodo_pago(obtenerMetodoPagoSeleccionado());

        try {
            Principal.modificarCliente(localClien);
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cierra la ventana
        } catch (modifyError ex) {
            ex.visualizarMen(); // Usa el método propio de la excepción
            // Opcionalmente: ex.printStackTrace();
        } catch (Exception ex) {
             ex.printStackTrace();
             JOptionPane.showMessageDialog(this, "Ha ocurrido un error inesperado al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la ventana de pedidos del cliente.
     */
    private void mostrarPedidos() {
        if (localClien != null) {
            // Asumiendo que VerPedidosCliente es un JDialog que toma esta ventana como padre
            VerPedidosCliente vistaPedidoClien = new VerPedidosCliente(this, localClien);
            vistaPedidoClien.setVisible(true);
        }
    }

    /**
     * Solicita confirmación y llama al controlador para dar de baja al cliente actual.
     */
    private void baja() {
        if (localClien == null) return;

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar tu cuenta?\n¡Esta acción no se puede deshacer!",
                "Confirmar Baja de Usuario",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE); // Mensaje de advertencia

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                Principal.bajaCliente(localClien);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Baja Exitosa", JOptionPane.INFORMATION_MESSAGE);
                // Aquí deberías manejar qué pasa después:
                // - Cerrar esta ventana: dispose();
                // - ¿Quizás cerrar sesión y volver al login? (Necesitaría comunicación con la ventana principal)
                dispose(); // Cierra la ventana actual
            } catch (DropError e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario:\n" + e.getMessage(), "Error de Baja", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                 ex.printStackTrace();
                 JOptionPane.showMessageDialog(this, "Ha ocurrido un error inesperado al dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
             JOptionPane.showMessageDialog(this, "La baja ha sido cancelada.", "Baja Cancelada", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Obtiene el método de pago seleccionado en los radio buttons.
     * @return El enum Metodo correspondiente, o null si no hay selección.
     */
    private Metodo obtenerMetodoPagoSeleccionado() {
        if (rdbtnVisa.isSelected()) {
            return Metodo.visa;
        } else if (rdbtnMastercard.isSelected()) {
            return Metodo.mastercard;
        } else if (rdbtnPaypal.isSelected()) {
            return Metodo.paypal;
        }
        return null; // Ninguno seleccionado
    }

     /**
     * Validación simple de campos obligatorios.
     * @param modificarModo Si es true, permite que la contraseña esté vacía.
     * @return true si los campos básicos son válidos, false en caso contrario.
     */
    /*private boolean validarCampos(boolean modificarModo) {
        StringBuilder errores = new StringBuilder();

        if (textUser.getText().trim().isEmpty()) errores.append("- El nombre de usuario no puede estar vacío.\n");
        if (!modificarModo && String.valueOf(passwordFieldContra.getPassword()).isEmpty()) {
             errores.append("- La contraseña no puede estar vacía.\n");
        }
        if (textDni.getText().trim().isEmpty()) errores.append("- El DNI/NIE no puede estar vacío.\n");
        // Validación simple de email (podría ser más compleja con regex)
        if (textEmail.getText().trim().isEmpty() || !textEmail.getText().contains("@")) {
             errores.append("- Introduce un email válido.\n");
        }
        if (textDireccion.getText().trim().isEmpty()) errores.append("- La dirección no puede estar vacía.\n");
        if (obtenerMetodoPagoSeleccionado() == null) errores.append("- Debes seleccionar un método de pago.\n");
        if (textNumeroCuenta.getText().trim().isEmpty()) errores.append("- El número de tarjeta/cuenta no puede estar vacío.\n");


        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this, "Por favor, corrige los siguientes errores:\n\n" + errores.toString(), "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }*/

     /** Sobrecarga para llamar a validarCampos en modo no-modificación (registro) */
    private boolean validarCampos() {
        return validarCampos(false);
    }

    /**
     * Método auxiliar para cargar iconos desde el classpath.
     * @param path Ruta relativa al icono (ej. "/iconos/save.png").
     * @return ImageIcon o null si no se encuentra.
     */
    private ImageIcon cargarIcono(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            // Escalar icono si es necesario (ejemplo a 16x16)
             ImageIcon originalIcon = new ImageIcon(imgURL);
             Image image = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
             return new ImageIcon(image);
           // return new ImageIcon(imgURL); // Sin escalar
        } else {
            System.err.println("Advertencia: No se pudo encontrar el icono: " + path);
            return null; // Devuelve null para que el botón no muestre un icono roto
        }
    }
    
    private boolean validarCampos(boolean modificarModo) {
        StringBuilder errores = new StringBuilder();

        // Validación de Usuario
        if (textUser.getText().trim().isEmpty()) errores.append("- El nombre de usuario no puede estar vacío.\n");

        // Validación de Contraseña
        String password = String.valueOf(passwordFieldContra.getPassword());
        if (!modificarModo && password.isEmpty()) {
            errores.append("- La contraseña no puede estar vacía.\n");
        } else if (password.length() < 6) {
            errores.append("- La contraseña debe tener al menos 6 caracteres.\n");
        } else if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            errores.append("- La contraseña debe contener al menos una letra y un número.\n");
        }

        // Validación de DNI/NIE (con posibilidad de guiones)
        String dni = textDni.getText().trim();
        if (dni.isEmpty()) {
            errores.append("- El DNI/NIE no puede estar vacío.\n");
        } else {
            // Expresión regular para validar el DNI con o sin guiones
            Pattern dniPattern = Pattern.compile("^\\d{8}[A-Za-z]$");
            Matcher matcher = dniPattern.matcher(dni.replaceAll("-", ""));
            if (!matcher.matches()) {
                errores.append("- El DNI/NIE no es válido. Debe tener 8 dígitos seguidos de una letra.\n");
            }
        }

        // Validación de Email
        String email = textEmail.getText().trim();
        if (email.isEmpty() || !email.contains("@") || !email.matches("^[\\w._%+-]+@[A-Za-z0-9.-]+\\.(com|es|org|net|edu|gov|info|io)$")) {
            errores.append("- Introduce un email válido (como ejemplo: mikel@gmail.com).\n");
        }

        // Validación de Dirección
        if (textDireccion.getText().trim().isEmpty()) errores.append("- La dirección no puede estar vacía.\n");

        // Validación de Método de Pago
        if (obtenerMetodoPagoSeleccionado() == null) errores.append("- Debes seleccionar un método de pago.\n");

        // Validación de Número de Cuenta
        if (textNumeroCuenta.getText().trim().isEmpty()) errores.append("- El número de tarjeta/cuenta no puede estar vacío.\n");

        // Mostrar errores si los hay
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this, "Por favor, corrige los siguientes errores:\n\n" + errores.toString(), "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}