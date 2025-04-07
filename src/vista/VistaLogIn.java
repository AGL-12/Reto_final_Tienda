package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // Necesario para el borde del botón de registro

import controlador.Principal; // Asumiendo que existe este paquete y clase
import excepciones.LoginError; // Asumiendo que existe este paquete y clase
import modelo.Cliente; // Asumiendo que existe este paquete y clase

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class VistaLogIn extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane; // Panel principal del JFrame
    private JPanel panelLogin;  // Panel central para los controles
    private JTextField txtUser;
    private JPasswordField txtContra;
    private JLabel lblIconUser;
    private JLabel lblIconPass;
    private JLabel lblTitulo;
    private JLabel lblOlvidoContrasena;

    private JButton btnSignIn; // Botón Registrar (AHORA ACTIVO)
    private JButton btnLogIn;

    // Colores y Fuentes (puedes ajustarlos)
    private final Color COLOR_FONDO = new Color(240, 245, 248);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BOTON_PRIMARIO = new Color(52, 152, 219); // Azul para Ingresar
    private final Color COLOR_BOTON_PRIMARIO_HOVER = new Color(41, 128, 185);
    private final Color COLOR_TEXTO_BOTON_PRIMARIO = Color.WHITE;
    private final Color COLOR_BOTON_SECUNDARIO = Color.WHITE; // Blanco para Registrarse
    private final Color COLOR_BOTON_SECUNDARIO_HOVER = new Color(230, 240, 248); // Azul muy pálido para hover
    private final Color COLOR_BORDE_TEXTO_SECUNDARIO = new Color(52, 152, 219); // Azul para borde/texto
    private final Color COLOR_ENLACE = Color.GRAY;
    private final Color COLOR_TITULO = new Color(50, 50, 50);
    private final Font FUENTE_GENERAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FUENTE_ENLACE = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Create the frame.
     */
    public VistaLogIn() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo establecer el LookAndFeel del sistema: " + e.getMessage());
        }

        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(20, 20));
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(contentPane);

        panelLogin = new JPanel();
        panelLogin.setBackground(COLOR_PANEL);
        panelLogin.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPane.add(panelLogin, BorderLayout.CENTER);

        lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblIconUser = new JLabel("👤");
        lblIconUser.setFont(FUENTE_GENERAL);
        lblIconUser.setForeground(Color.GRAY);
        lblIconUser.setPreferredSize(new Dimension(25, 25));
        lblIconUser.setHorizontalAlignment(SwingConstants.CENTER);

        txtUser = new JTextField();
        txtUser.setFont(FUENTE_GENERAL);
        txtUser.setColumns(15);

        lblIconPass = new JLabel("🔒");
        lblIconPass.setFont(FUENTE_GENERAL);
        lblIconPass.setForeground(Color.GRAY);
        lblIconPass.setPreferredSize(new Dimension(25, 25));
        lblIconPass.setHorizontalAlignment(SwingConstants.CENTER);

        txtContra = new JPasswordField();
        txtContra.setFont(FUENTE_GENERAL);
        txtContra.setColumns(15);

        btnLogIn = new JButton("Ingresar");
        btnLogIn.setFont(FUENTE_BOTON);
        btnLogIn.setBackground(COLOR_BOTON_PRIMARIO);
        btnLogIn.setForeground(COLOR_TEXTO_BOTON_PRIMARIO);
        btnLogIn.setFocusPainted(false);
        btnLogIn.setBorderPainted(false);
        btnLogIn.setOpaque(true);
        btnLogIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogIn.addActionListener(this);
        btnLogIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogIn.setBackground(COLOR_BOTON_PRIMARIO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogIn.setBackground(COLOR_BOTON_PRIMARIO);
            }
        });

        // --- Botón Registrarte (AHORA ACTIVO) ---
        btnSignIn = new JButton("Registrarte");
        btnSignIn.setFont(FUENTE_BOTON);
        btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO); // Fondo blanco
        btnSignIn.setForeground(COLOR_BORDE_TEXTO_SECUNDARIO); // Texto azul
        // Borde del color del texto/borde secundario
        btnSignIn.setBorder(new LineBorder(COLOR_BORDE_TEXTO_SECUNDARIO, 1)); // Borde fino azul
        btnSignIn.setOpaque(true); // Necesario para el fondo blanco
        btnSignIn.setFocusPainted(false);
        btnSignIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSignIn.addActionListener(this); // Añadir ActionListener
        btnSignIn.addMouseListener(new MouseAdapter() {
             @Override
            public void mouseEntered(MouseEvent e) {
                // Cambia fondo ligeramente al pasar el ratón
                btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                 // Vuelve al fondo original
                btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO);
            }
        });
        // --- Fin Botón Registrarte ---

        lblOlvidoContrasena = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>");
        lblOlvidoContrasena.setFont(FUENTE_ENLACE);
        lblOlvidoContrasena.setForeground(COLOR_ENLACE);
        lblOlvidoContrasena.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblOlvidoContrasena.setHorizontalAlignment(SwingConstants.CENTER);
        lblOlvidoContrasena.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clic en Olvidé contraseña");
            }
             @Override
            public void mouseEntered(MouseEvent e) {
                 lblOlvidoContrasena.setForeground(COLOR_BOTON_PRIMARIO);
            }
             @Override
            public void mouseExited(MouseEvent e) {
                 lblOlvidoContrasena.setForeground(COLOR_ENLACE);
            }
        });

        // --- Layout del panelLogin usando GroupLayout (ajustado para dos botones) ---
        GroupLayout gl_panelLogin = new GroupLayout(panelLogin);
        gl_panelLogin.setAutoCreateGaps(true);
        gl_panelLogin.setAutoCreateContainerGaps(true);

        gl_panelLogin.setHorizontalGroup(
            gl_panelLogin.createParallelGroup(Alignment.CENTER) // Centra todo
                .addComponent(lblTitulo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gl_panelLogin.createSequentialGroup()
                    .addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(txtUser, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addGroup(gl_panelLogin.createSequentialGroup()
                    .addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(txtContra, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addComponent(btnLogIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Botón Ingresar
                .addComponent(btnSignIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Botón Registrarte
                .addComponent(lblOlvidoContrasena, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gl_panelLogin.setVerticalGroup(
            gl_panelLogin.createSequentialGroup()
                .addComponent(lblTitulo)
                .addGap(25)
                .addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER)
                    .addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(15)
                .addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER)
                    .addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(30)
                .addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE) // Botón Ingresar
                .addGap(10) // Espacio entre botones
                .addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE) // Botón Registrarte
                .addGap(20)
                .addComponent(lblOlvidoContrasena)
                .addContainerGap(20, Short.MAX_VALUE)
        );

        panelLogin.setLayout(gl_panelLogin);

        pack();
        setLocationRelativeTo(null);
    }

    // --- Métodos de Lógica ---

    protected void comprobar() {
        Cliente clien = new Cliente();
        clien.setUsuario(txtUser.getText() != null ? txtUser.getText() : "");
        clien.setContra(txtContra.getPassword() != null ? new String(txtContra.getPassword()) : "");

        try {
             Cliente clienteLogueado = Principal.login(clien);
             System.out.println("Login exitoso para: " + clienteLogueado.getUsuario());

             VistaTienda tienda = new VistaTienda(clienteLogueado, null); // Pasar null si VistaTienda no necesita referencia a esta vista
             tienda.setVisible(true);
             this.dispose();

        } catch (LoginError e) {
            e.visualizarMen();
             limpiar();
        } catch (Exception ex) {
             System.err.println("Error inesperado durante el login: " + ex.getMessage());
             ex.printStackTrace();
             javax.swing.JOptionPane.showMessageDialog(this,
                     "Ocurrió un error inesperado. Inténtalo de nuevo.",
                     "Error",
                     javax.swing.JOptionPane.ERROR_MESSAGE);
             limpiar();
        }
    }

    private void limpiar() {
        txtUser.setText("");
        txtContra.setText("");
        txtUser.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignIn) { // Si se pulsa Registrarte
             abrirVistaRegistro();
        } else if (e.getSource() == btnLogIn) { // Si se pulsa Ingresar
            comprobar();
        }
    }

    // --- Método para abrir registro (AHORA ACTIVO) ---
    private void abrirVistaRegistro() {
        this.setVisible(false); // Oculta la ventana de login

        // Asume que VistaUsuario existe y tiene un constructor que acepta 'this' como referencia
        // para poder volver a mostrar esta ventana si es necesario.
        // Si VistaUsuario no necesita la referencia, puedes pasar null.
        VistaUsuario usuario = new VistaUsuario(null, this); // Necesitas VistaUsuario
        usuario.setVisible(true); // Muestra la ventana de registro

        // NOTA: Esta ventana (Login) ahora está oculta.
        // Si quieres que se cierre permanentemente al abrir registro, usa this.dispose(); aquí.
        // Si quieres que vuelva a aparecer cuando VistaUsuario se cierre,
        // VistaUsuario necesitará tener la referencia 'this' (como está ahora)
        // y llamar a vistaLoginReferencia.setVisible(true); cuando se cierre.
    }


    // --- Método main para probar la ventana ---
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VistaLogIn frame = new VistaLogIn();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}