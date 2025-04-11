package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controlador.Principal;
import excepciones.LoginError;
import modelo.Cliente;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.Dimension;
// Imports para placeholder de icono y carga de imagen
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO; // Importar ImageIO
import java.io.InputStream;
import java.awt.Toolkit; // Importar InputStream

public class VistaLogIn extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLogin;
	private JTextField txtUser;
	private JPasswordField txtContra;
	private JLabel lblIconUser;
	private JLabel lblIconPass;
	private JLabel lblTitulo;
	private JLabel lblOlvidoContrasena;
	private JButton btnSignIn;
	private JButton btnLogIn;

	// --- Constantes de Estilo ---
	// (Se mantienen igual que en la versión anterior)
	private final Color COLOR_FONDO = UIManager.getColor("Panel.background") != null
			? UIManager.getColor("Panel.background")
			: new Color(240, 245, 248);
	private final Color COLOR_PANEL = UIManager.getColor("TextField.background") != null
			? UIManager.getColor("TextField.background")
			: Color.WHITE;
	private final Color COLOR_BOTON_PRIMARIO = new Color(52, 152, 219); // Azul
	private final Color COLOR_BOTON_PRIMARIO_HOVER = new Color(41, 128, 185); // Azul más oscuro
	private final Color COLOR_TEXTO_BOTON_PRIMARIO = Color.WHITE;
	private final Color COLOR_BOTON_SECUNDARIO = COLOR_PANEL; // Fondo igual al panel
	private final Color COLOR_BOTON_SECUNDARIO_HOVER = UIManager.getColor("Button.hoverBackground") != null
			? UIManager.getColor("Button.hoverBackground")
			: new Color(230, 240, 248);
	private final Color COLOR_BORDE_TEXTO_SECUNDARIO = COLOR_BOTON_PRIMARIO; // Azul para borde/texto
	private final Color COLOR_ENLACE = Color.GRAY;
	private final Color COLOR_TITULO = UIManager.getColor("Label.foreground") != null
			? UIManager.getColor("Label.foreground")
			: new Color(50, 50, 50);
	private final Font FUENTE_GENERAL = new Font("Segoe UI", Font.PLAIN, 14);
	private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
	private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
	private final Font FUENTE_ENLACE = new Font("Segoe UI", Font.PLAIN, 12);

	/**
	 * Create the frame.
	 */
	public VistaLogIn() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VistaLogIn.class.getResource("/imagenes/logoColor.jpg")));
		// Intenta usar el Look & Feel del sistema como fallback si FlatLaf no está
		// configurado
		try {
			// Si usas FlatLaf, debería estar configurado en Principal.java
			// Este bloque es más un fallback al L&F nativo.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("No se pudo establecer el LookAndFeel del sistema: " + e.getMessage());
		}

		setTitle("Iniciar Sesión");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		contentPane = new JPanel(new BorderLayout(20, 20));
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
		setContentPane(contentPane);

		panelLogin = new JPanel();
		panelLogin.setBackground(COLOR_PANEL);
		panelLogin.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), // Borde fino
				new EmptyBorder(30, 40, 30, 40) // Padding interno
		));
		contentPane.add(panelLogin, BorderLayout.CENTER);

		lblTitulo = new JLabel("Bienvenido");
		lblTitulo.setFont(FUENTE_TITULO);
		lblTitulo.setForeground(COLOR_TITULO);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// --- Inicialización de Iconos ---
		// *** CAMBIO: Usar path absoluto desde classpath root ***
		// Asegúrate que 'resources/iconos/usuario.png' exista y 'resources' sea una
		// carpeta de fuentes/recursos.
		lblIconUser = new JLabel(cargarImagen("/iconos/usuario.png", 20, 20));
		lblIconUser.setPreferredSize(new Dimension(25, 25)); // Tamaño del contenedor del JLabel
		lblIconUser.setHorizontalAlignment(SwingConstants.CENTER); // Centrar icono dentro del JLabel

		txtUser = new JTextField();
		txtUser.setFont(FUENTE_GENERAL);
		txtUser.setColumns(15);

		// *** CAMBIO: Usar path absoluto desde classpath root ***
		// Asegúrate que 'resources/iconos/candado.png' exista.
		lblIconPass = new JLabel(cargarImagen("/iconos/candado.png", 20, 20));
		lblIconPass.setPreferredSize(new Dimension(25, 25));
		lblIconPass.setHorizontalAlignment(SwingConstants.CENTER);

		txtContra = new JPasswordField();
		txtContra.setFont(FUENTE_GENERAL);
		txtContra.setColumns(15);

		// --- Botón Ingresar ---
		btnLogIn = new JButton("Ingresar");
		btnLogIn.setFont(FUENTE_BOTON);
		btnLogIn.setBackground(COLOR_BOTON_PRIMARIO);
		btnLogIn.setForeground(COLOR_TEXTO_BOTON_PRIMARIO);
		btnLogIn.setFocusPainted(false);
		btnLogIn.setBorderPainted(false);
		btnLogIn.setOpaque(true); // Importante si no se usa un LaF que lo maneje
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

		// --- Botón Registrarte ---
		btnSignIn = new JButton("Registrarte");
		btnSignIn.setFont(FUENTE_BOTON);
		btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO);
		btnSignIn.setForeground(COLOR_BORDE_TEXTO_SECUNDARIO);
		btnSignIn.setBorder(new LineBorder(COLOR_BORDE_TEXTO_SECUNDARIO, 1));
		btnSignIn.setOpaque(true);
		btnSignIn.setFocusPainted(false);
		btnSignIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSignIn.addActionListener(this);
		btnSignIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnSignIn.setBackground(COLOR_BOTON_SECUNDARIO);
			}
		});

		// --- Enlace Olvido Contraseña ---
		lblOlvidoContrasena = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>");
		lblOlvidoContrasena.setFont(FUENTE_ENLACE);
		lblOlvidoContrasena.setForeground(COLOR_ENLACE);
		lblOlvidoContrasena.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblOlvidoContrasena.setHorizontalAlignment(SwingConstants.CENTER);
		lblOlvidoContrasena.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(VistaLogIn.this, "Función 'Olvidé contraseña' no implementada.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
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

		// --- Layout del panelLogin usando GroupLayout ---
		GroupLayout gl_panelLogin = new GroupLayout(panelLogin);
		gl_panelLogin.setAutoCreateGaps(true);
		gl_panelLogin.setAutoCreateContainerGaps(true);

		gl_panelLogin.setHorizontalGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER)
				.addComponent(lblTitulo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panelLogin.createSequentialGroup()
						.addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtUser, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
				.addGroup(gl_panelLogin.createSequentialGroup()
						.addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtContra, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
				.addComponent(btnLogIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(btnSignIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(lblOlvidoContrasena, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));

		gl_panelLogin
				.setVerticalGroup(gl_panelLogin.createSequentialGroup().addComponent(lblTitulo).addGap(25)
						.addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER, false)
								.addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(15)
						.addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER, false)
								.addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(txtContra, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(30).addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(10).addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(20).addComponent(lblOlvidoContrasena).addContainerGap(20, Short.MAX_VALUE));

		panelLogin.setLayout(gl_panelLogin);

		pack(); // Ajusta tamaño a los componentes
		setLocationRelativeTo(null); // Centra en pantalla DESPUÉS de pack()

	}

	// --- Métodos de Lógica ---

	protected void comprobar() {
		Cliente clien = new Cliente();
		clien.setUsuario(txtUser.getText() != null ? txtUser.getText() : "");
		clien.setContra(txtContra.getPassword() != null ? new String(txtContra.getPassword()) : "");

		if (clien.getUsuario().isEmpty() || clien.getContra().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Usuario y contraseña no pueden estar vacíos.", "Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			Cliente clienteLogueado = Principal.login(clien);
			System.out.println("Login exitoso para: " + clienteLogueado.getUsuario());
			VistaTienda tienda = new VistaTienda(clienteLogueado, this);
			tienda.setVisible(true);
			this.dispose(); // Cierra la ventana de login

		} catch (LoginError e) {
			e.visualizarMen(); // Muestra el mensaje de error de LoginError
			limpiar();
		} catch (Exception ex) {
			System.err.println("Error inesperado durante el login: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado. Inténtalo de nuevo.", "Error",
					JOptionPane.ERROR_MESSAGE);
			limpiar();
		}
	}

	private void limpiar() {
		// txtUser.setText(""); // Opcional: no limpiar usuario si falla contraseña
		txtContra.setText("");
		txtContra.requestFocusInWindow(); // Poner foco en contraseña si falla
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSignIn) {
			abrirVistaRegistro();
		} else if (e.getSource() == btnLogIn) {
			comprobar();
		}
	}

	private void abrirVistaRegistro() {
		this.setVisible(false);
		// Pasar 'this' para que la ventana de registro pueda volver a mostrar esta si
		// cancela
		VistaUsuario usuario = new VistaUsuario(null, this);
		usuario.setVisible(true);

		this.setVisible(true);
	}

	/**
	 * Método para cargar una imagen desde una ruta DENTRO DEL CLASSPATH y ajustarla
	 * al tamaño adecuado. La ruta debe ser absoluta desde la raíz del classpath,
	 * ej: "/iconos/imagen.png"
	 *
	 * @param ruta   La ruta del recurso dentro del classpath.
	 * @param width  El ancho deseado para el icono.
	 * @param height La altura deseada para el icono.
	 * @return Un ImageIcon redimensionado o un placeholder si falla la carga.
	 */
	private ImageIcon cargarImagen(String ruta, int width, int height) {
		// *** CAMBIO: Usar getResourceAsStream para buscar desde la raíz del classpath
		// ***
		InputStream imgStream = getClass().getResourceAsStream(ruta);

		if (imgStream != null) {
			try {
				// Leer la imagen desde el InputStream
				BufferedImage originalImage = ImageIO.read(imgStream);
				// Cerrar el stream es buena práctica
				imgStream.close();

				// Escalar si es necesario
				Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				return new ImageIcon(scaledImage);

			} catch (java.io.IOException e) {
				System.err.println("Error al leer la imagen desde la ruta: " + ruta + " - " + e.getMessage());
			}
		} else {
			// Si getResourceAsStream devuelve null, el recurso no se encontró en el
			// classpath
			System.err.println("No se pudo encontrar el recurso de imagen en el classpath: " + ruta);
			System.err.println("Asegúrate de que la carpeta 'resources' esté en el Build Path y la ruta '" + ruta
					+ "' sea correcta.");

		}

		// --- Crear un icono placeholder simple en caso de error ---
		BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = placeholder.createGraphics();
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, width - 1, height - 1); // Borde
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(1)); // Línea más fina para icono pequeño
			// Dibujar una '?' simple
			g2d.setFont(new Font("SansSerif", Font.BOLD, Math.min(width, height) * 3 / 4));
			g2d.drawString("?", width / 4, height * 3 / 4);
			// g2d.drawLine(width / 4, height / 4, 3 * width / 4, 3 * height / 4); // X
			// anterior
			// g2d.drawLine(width / 4, 3 * height / 4, 3 * width / 4, height / 4);
		} finally {
			g2d.dispose();
		}
		return new ImageIcon(placeholder);
	}

}