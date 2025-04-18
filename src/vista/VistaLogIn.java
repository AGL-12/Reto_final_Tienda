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
import java.awt.Graphics;
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

import java.io.IOException;
import java.io.InputStream;
import java.awt.Toolkit; // Importar Toolkit
import java.awt.image.FilteredImageSource; // Necesario para el filtro
import java.awt.image.ImageFilter;      // Necesario para el filtro
import java.awt.image.ImageProducer;    // Necesario para el filtro
import java.awt.image.RGBImageFilter;   // Necesario para el filtro

public class VistaLogIn extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane; // El contenedor principal estándar
	private JPanel panelLogin;  // El panel con los controles de login
	private JTextField txtUser;
	private JPasswordField txtContra;
	private JLabel lblIconUser;
	private JLabel lblIconPass;
	private JLabel lblLogo;      // Añadido para el logo
	private JButton btnSignIn;
	private JButton btnLogIn;
	private BufferedImage backgroundImage; // Mantenemos la carga de la imagen

	// --- NUEVA Paleta de Colores - Tonos Cálidos/Madera ---
	private static final Color COLOR_FONDO_FALLBACK = new Color(210, 190, 170);
	private static final Color COLOR_PANEL = new Color(210, 180, 140, 200); // Con algo de transparencia alfa
	private static final Color COLOR_BOTON_PRIMARIO = new Color(139, 69, 19);
	private static final Color COLOR_BOTON_PRIMARIO_HOVER = new Color(115, 55, 10);
	private static final Color COLOR_TEXTO_BOTON_PRIMARIO = Color.WHITE;
	private static final Color COLOR_BOTON_SECUNDARIO = COLOR_PANEL;
	private static final Color COLOR_BOTON_SECUNDARIO_HOVER = new Color(230, 230, 215);
	private static final Color COLOR_BORDE_TEXTO_SECUNDARIO = COLOR_BOTON_PRIMARIO;
	private static final Color COLOR_TITULO = new Color(80, 40, 0);

	// Fuentes
	private static final Font FUENTE_GENERAL = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);

	// Color a hacer transparente en los iconos (Blanco puro)
    private static final Color COLOR_TRANSPARENTE_ICONO = Color.WHITE;

	/**
	 * Create the frame.
	 */
	public VistaLogIn() {
		// Icono de la ventana
		try {
			InputStream imgStream = VistaLogIn.class.getResourceAsStream("/imagenes/logoColor.jpg");
			if (imgStream != null) {
				BufferedImage iconImage = ImageIO.read(imgStream);
				if (iconImage != null) {
					setIconImage(Toolkit.getDefaultToolkit().createImage(iconImage.getSource()));
				} else {
					System.err.println("ImageIO.read devolvió null para el icono de ventana.");
				}
				imgStream.close();
			} else {
				System.err.println("No se pudo cargar el icono de la ventana: /imagenes/logoColor.jpg");
			}
		} catch (IOException e) {
			System.err.println("Error al leer el icono de la ventana: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error inesperado al establecer el icono de la ventana: " + e.getMessage());
		}


		// Carga la imagen de fondo
		try {
			InputStream bgStream = getClass().getResourceAsStream("/imagenes/fondoMadera.jpg");
			if (bgStream != null) {
				backgroundImage = ImageIO.read(bgStream);
				bgStream.close();
			} else {
				System.err.println("Recurso de imagen de fondo no encontrado: /imagenes/fondoMadera.jpg");
			}
		} catch (IOException e) {
			System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
			backgroundImage = null;
		}

		// Configuración básica del JFrame
		setTitle("Iniciar Sesión");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// ContentPane con fondo
		contentPane = new JPanel(new BorderLayout(20, 20)) {
            private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			}
		};
		contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
		contentPane.setBackground(COLOR_FONDO_FALLBACK);
		setContentPane(contentPane);


		// --- Panel de Login ---
		panelLogin = new JPanel();
		panelLogin.setBackground(COLOR_PANEL);
		// panelLogin.setOpaque(false); // Descomenta si quieres que el panel sea totalmente transparente
		panelLogin.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(Color.LIGHT_GRAY, 1),
				new EmptyBorder(30, 40, 30, 40)
		));
		contentPane.add(panelLogin, BorderLayout.CENTER);


		// --- Componentes dentro de panelLogin ---
		lblLogo = new JLabel(cargarImagen("/imagenes/logoColor.jpg", 180, 60, false)); // false = no intentar hacer transparente el logo
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

		// Icono Usuario - intentar hacer blanco transparente
		lblIconUser = new JLabel(cargarImagen("/iconos/usuario.png", 20, 20, true)); // true = intentar hacer transparente
		lblIconUser.setPreferredSize(new Dimension(25, 25));
		lblIconUser.setHorizontalAlignment(SwingConstants.CENTER);

		txtUser = new JTextField();
		txtUser.setFont(FUENTE_GENERAL);
		txtUser.setColumns(15);

		// Icono Contraseña - intentar hacer blanco transparente
		lblIconPass = new JLabel(cargarImagen("/iconos/candado.png", 20, 20, true)); // true = intentar hacer transparente
		lblIconPass.setPreferredSize(new Dimension(25, 25));
		lblIconPass.setHorizontalAlignment(SwingConstants.CENTER);

		txtContra = new JPasswordField();
		txtContra.setFont(FUENTE_GENERAL);
		txtContra.setColumns(15);

		btnLogIn = new JButton("Ingresar");
		configurarBotonPrimario(btnLogIn);
		btnLogIn.addActionListener(this);

		btnSignIn = new JButton("Registrarte");
		configurarBotonSecundario(btnSignIn);
		btnSignIn.addActionListener(this);


		// --- Layout del panelLogin usando GroupLayout ---
		GroupLayout gl_panelLogin = new GroupLayout(panelLogin);
		gl_panelLogin.setAutoCreateGaps(true);
		gl_panelLogin.setAutoCreateContainerGaps(true);

		gl_panelLogin.setHorizontalGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER)
				.addComponent(lblLogo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panelLogin.createSequentialGroup()
						.addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtUser, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
				.addGroup(gl_panelLogin.createSequentialGroup()
						.addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtContra, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
				.addComponent(btnLogIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(btnSignIn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);

		gl_panelLogin.setVerticalGroup(gl_panelLogin.createSequentialGroup()
				.addComponent(lblLogo)
				.addGap(25)
				.addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER, false)
						.addComponent(lblIconUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
				.addGap(15)
				.addGroup(gl_panelLogin.createParallelGroup(Alignment.CENTER, false)
						.addComponent(lblIconPass, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtContra, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
				.addGap(30)
				.addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
				.addGap(10)
				.addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
		);

		panelLogin.setLayout(gl_panelLogin);

		// --- Finalización de la Configuración del Frame ---
		pack();
		setMinimumSize(new Dimension(450, 500));
		setLocationRelativeTo(null);

		// Look & Feel del sistema
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("No se pudo establecer el LookAndFeel del sistema: " + e.getMessage());
		}
	}


	// --- Métodos de ayuda para configurar botones ---
	private void configurarBotonPrimario(JButton button) {
        button.setFont(FUENTE_BOTON);
        button.setBackground(COLOR_BOTON_PRIMARIO);
        button.setForeground(COLOR_TEXTO_BOTON_PRIMARIO);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_BOTON_PRIMARIO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_BOTON_PRIMARIO);
            }
        });
    }

    private void configurarBotonSecundario(JButton button) {
        button.setFont(FUENTE_BOTON);
        button.setBackground(COLOR_BOTON_SECUNDARIO);
        button.setForeground(COLOR_BORDE_TEXTO_SECUNDARIO);
        button.setBorder(new LineBorder(COLOR_BORDE_TEXTO_SECUNDARIO, 1));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
             @Override
            public void mouseEntered(MouseEvent e) {
                 button.setContentAreaFilled(true);
                 button.setOpaque(true);
                 button.setBackground(COLOR_BOTON_SECUNDARIO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                 button.setContentAreaFilled(false);
                 button.setOpaque(false);
            }
        });
    }

	// --- Métodos de Lógica ---
	protected void comprobar() {
        Cliente clien = new Cliente();
        String usuario = txtUser.getText() != null ? txtUser.getText() : "";
        String contra = txtContra.getPassword() != null ? new String(txtContra.getPassword()) : "";

        clien.setUsuario(usuario);
        clien.setContra(contra);

        if (usuario.isEmpty() || contra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña no pueden estar vacíos.", "Error de Entrada",
                    JOptionPane.WARNING_MESSAGE);
            limpiar();
            return;
        }

        try {
            Cliente clienteLogueado = Principal.login(clien);
            System.out.println("Login exitoso para: " + clienteLogueado.getUsuario());
            this.setVisible(false);
            VistaTienda tienda = new VistaTienda(clienteLogueado, this);
            tienda.setVisible(true);
            this.dispose();

        } catch (LoginError e) {
            e.visualizarMen();
            limpiar();
        } catch (Exception ex) {
            System.err.println("Error inesperado durante el login: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al intentar iniciar sesión.\nPor favor, inténtalo de nuevo o contacta al soporte.",
                    "Error Crítico",
                    JOptionPane.ERROR_MESSAGE);
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
        if (e.getSource() == btnSignIn) {
            abrirVistaRegistro();
        } else if (e.getSource() == btnLogIn) {
            comprobar();
        }
    }

    private void abrirVistaRegistro() {
        this.setVisible(false);
        VistaUsuario usuario = new VistaUsuario(null, this);
        usuario.setVisible(true);
        // this.dispose(); // Opcional: cierra esta ventana al abrir la de registro
    }


	// --- Método Cargar Imagen y Placeholder (MODIFICADO) ---

	/**
	 * Carga una imagen desde el classpath, la escala y opcionalmente intenta
	 * hacer transparente un color específico (blanco por defecto).
	 *
	 * @param ruta Ruta del recurso de la imagen dentro del classpath (ej: "/iconos/img.png").
	 * @param width Ancho deseado para la imagen escalada.
	 * @param height Alto deseado para la imagen escalada.
	 * @param hacerTransparente Si es true, intenta convertir los píxeles blancos en transparentes.
	 * @return Un ImageIcon escalado (y posiblemente con transparencia modificada) o un placeholder si falla.
	 */
	private ImageIcon cargarImagen(String ruta, int width, int height, boolean hacerTransparente) {
		InputStream imgStream = getClass().getResourceAsStream(ruta);
		if (imgStream != null) {
			try {
				BufferedImage originalImage = ImageIO.read(imgStream);
				if (originalImage != null) {
					Image imgFinal = originalImage; // Empezamos con la original

					// --- INICIO: Código para hacer blanco transparente (si se solicita) ---
					if (hacerTransparente) {
						ImageFilter filter = new RGBImageFilter() {
							// Determinar el color marcador (blanco puro en este caso)
							// El formato 0xFFRRGGBB representa el color opaco
							public int markerRGB = COLOR_TRANSPARENTE_ICONO.getRGB() | 0xFF000000;

							@Override
							public final int filterRGB(int x, int y, int rgb) {
								// Comparamos el píxel actual (rgb) con el color marcador.
								// El `& 0xFFFFFFFF` asegura que comparamos sólo el color, ignorando info alfa si la hubiera.
								if ((rgb | 0xFF000000) == markerRGB) {
									// Si el color coincide con el marcador (blanco), lo hacemos transparente.
									// Devolvemos 0, que representa un píxel completamente transparente (ARGB = 0x00000000).
									return 0x00FFFFFF & rgb; // Mantiene el color pero con alfa 0
                                    // return 0; // Alternativa más simple: totalmente transparente negro
								} else {
									// Si no coincide, dejamos el píxel como estaba.
									return rgb;
								}
							}
						};

						// Aplicamos el filtro a la imagen original
						ImageProducer ip = new FilteredImageSource(originalImage.getSource(), filter);
						// Creamos la nueva imagen (que ahora puede tener transparencia)
						imgFinal = Toolkit.getDefaultToolkit().createImage(ip);
					}
					// --- FIN: Código para hacer blanco transparente ---

					// Escalar la imagen resultante (original o filtrada)
					Image scaledImage = imgFinal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
					return new ImageIcon(scaledImage);

				} else {
					System.err.println("Error: ImageIO.read devolvió null para la ruta: " + ruta);
					return crearPlaceholderIcon(width, height);
				}
			} catch (java.io.IOException e) {
				System.err.println("Error de IO al leer la imagen: " + ruta + " - " + e.getMessage());
			} catch (Exception e) {
				System.err.println("Error inesperado al cargar/procesar/escalar imagen: " + ruta + " - " + e.getMessage());
                e.printStackTrace(); // Imprime más detalles del error
			} finally {
				try { imgStream.close(); } catch (IOException ioException) { /* Ignorar */ }
			}
		} else {
			System.err.println("No se pudo encontrar el recurso de imagen en el classpath: " + ruta);
		}
		// Si algo falla, devolver placeholder
		return crearPlaceholderIcon(width, height);
	}

	// Sobrecarga del método para compatibilidad (por defecto no hace transparente)
	private ImageIcon cargarImagen(String ruta, int width, int height) {
		return cargarImagen(ruta, width, height, false);
	}

	// --- Método crearPlaceholderIcon (sin cambios) ---
	private ImageIcon crearPlaceholderIcon(int width, int height) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(0, 0, width - 1, height - 1);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(1));
            int fontSize = Math.min(width, height) / 2;
            g2d.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, fontSize)));
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            String txt = "?";
            int stringWidth = fm.stringWidth(txt);
            int ascent = fm.getAscent();
            int descent = fm.getDescent();
            g2d.drawString(txt, (width - stringWidth) / 2, (height - (ascent + descent)) / 2 + ascent);
        } finally {
            g2d.dispose();
        }
        return new ImageIcon(placeholder);
    }

	/* // --- Método main (opcional, para probar) ---
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
                    System.err.println("No se pudo establecer el LookAndFeel del sistema.");
                }
				new VistaLogIn().setVisible(true);
			}
		});
	}
    */
}