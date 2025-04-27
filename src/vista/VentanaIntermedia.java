package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.net.URL;
 

public class VentanaIntermedia extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton btnConfigArticulos;
	private JButton btnConfigUsuario;
	private JButton btnVolver;
	private JLabel lblTiendaLogo;
	private JLabel lblTituloAdmin;
	private BackgroundPanel contentPaneConFondo;

	private JPanel panelTitulo;
	private JPanel panelCentral;
	private JPanel panelBotones;
	private JPanel panelLogo;
	private JPanel panelInferior;

	private static final Font FONT_TITULO_ADMIN = new Font("Segoe UI", Font.BOLD, 20);
	private static final Font FONT_BOTON_ADMIN = new Font("Segoe UI", Font.BOLD, 14);
	private static final Color COLOR_BOTON_ADMIN = new Color(0x5C6BC0);
	private static final Color COLOR_BOTON_ADMIN_HOVER = new Color(0x3F51B5);
	private static final Color COLOR_TEXTO_BOTON_ADMIN = Color.WHITE;
	private static final Color COLOR_TITULO_ADMIN = new Color(50, 50, 50);
	private static final Color COLOR_FALLBACK_FONDO = new Color(235, 235, 235);

	private static final int PADDING_GENERAL_ADMIN = 20;
	private static final int PADDING_INTERNO_ADMIN = 10;
	private static final int GAP_BOTONES_ADMIN = 15;

	private static final Color COLOR_TRANSPARENTE_ICONO = Color.WHITE;

	private static class BackgroundPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private transient BufferedImage backgroundImage;
		
		/**
		 * Crea un nuevo BackgroundPanel con la imagen especificada como fondo.
		 * @param imagePath La ruta al recurso de imagen.
		 */
		public BackgroundPanel(String imagePath) {
			try {
				URL imgUrl = getClass().getResource(imagePath);
				if (imgUrl == null) {
					
					backgroundImage = null;
				} else {
					backgroundImage = ImageIO.read(imgUrl);
				}
			} catch (IOException | IllegalArgumentException e) {
				e.getMessage();
				backgroundImage = null;
			}
			setOpaque(backgroundImage == null);
		}
		
		/**
		 * Dibuja el panel y su contenido. Si backgroundImage no es nulo,
		 * dibuja la imagen de fondo escalada para llenar el panel. De lo contrario,
		 * rellena el fondo con un color de reserva.
		 * @param g El contexto gráfico a usar para dibujar.
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			} else {
				g.setColor(COLOR_FALLBACK_FONDO);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
	}

	public VentanaIntermedia(JDialog padre) {
		super(padre, "Menú Administrador", true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPaneConFondo = new BackgroundPanel("/imagenes/fondoMadera.jpg");
		if (contentPaneConFondo.backgroundImage == null) {
			contentPaneConFondo.setBackground(COLOR_FALLBACK_FONDO);
			contentPaneConFondo.setOpaque(true);
		}
		contentPaneConFondo.setLayout(new BorderLayout(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN));
		contentPaneConFondo.setBorder(new EmptyBorder(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN,
				PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN));
		setContentPane(contentPaneConFondo);

		initComponents();

		setPreferredSize(new Dimension(600, 480));
		pack();
		setMinimumSize(new Dimension(550, 450));
		setLocationRelativeTo(padre);
	}

	/**
	 * Inicializa y configura todos los componentes de la interfaz de usuario
	 * de la ventana de administrador. Crea paneles, etiquetas, botones y los
	 * organiza en el layout de la ventana. También carga imágenes y configura estilos.
	 */
	private void initComponents() {
		panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelTitulo.setOpaque(false); // Hacer transparente para ver el fondo
		lblTituloAdmin = new JLabel("Menú de Administrador");
		lblTituloAdmin.setFont(FONT_TITULO_ADMIN);
		lblTituloAdmin.setForeground(COLOR_TITULO_ADMIN);
		panelTitulo.add(lblTituloAdmin);
		contentPaneConFondo.add(panelTitulo, BorderLayout.NORTH);

		panelCentral = new JPanel(new BorderLayout(PADDING_INTERNO_ADMIN * 2, 0));
		panelCentral.setOpaque(false); // Hacer transparente para ver el fondo

		panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
		panelBotones.setOpaque(false); // Hacer transparente para ver el fondo
		panelBotones.setBorder(new EmptyBorder(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN,
				PADDING_INTERNO_ADMIN));

		btnConfigUsuario = crearAdminBoton("Gestionar Usuarios", "/iconos/admin.png");
		btnConfigArticulos = crearAdminBoton("Gestionar Artículos", "/iconos/inventory.png");

		panelBotones.add(btnConfigUsuario);
		panelBotones.add(Box.createRigidArea(new Dimension(0, GAP_BOTONES_ADMIN)));
		panelBotones.add(btnConfigArticulos);

		panelCentral.add(panelBotones, BorderLayout.WEST);

		panelLogo = new JPanel(new GridBagLayout());
		panelLogo.setOpaque(false); // Hacer transparente para ver el fondo

		lblTiendaLogo = new JLabel();
		lblTiendaLogo.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			URL logoUrl = getClass().getResource("/imagenes/tienda_brico_admin.jpg");
			if (logoUrl == null) {
				throw new NullPointerException("Recurso no encontrado: /imagenes/tienda_brico_admin.jpg");
			}
			Image img = new ImageIcon(logoUrl).getImage();
			int maxAncho = 250; 
			int anchoOriginal = img.getWidth(null);
			int altoOriginal = img.getHeight(null);
            if (anchoOriginal <= 0 || altoOriginal <=0) { // Check for invalid image dimensions
                throw new IllegalArgumentException("Dimensiones de imagen inválidas para el logo.");
            }
			int nuevoAncho = maxAncho;
			int nuevoAlto = (int) (((double) altoOriginal / anchoOriginal) * nuevoAncho);

			Image imgEscalada = img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
			lblTiendaLogo.setIcon(new ImageIcon(imgEscalada));
		} catch (NullPointerException | IllegalArgumentException e) {
			e.getMessage();
			lblTiendaLogo.setText("Logo no disponible");
			lblTiendaLogo.setPreferredSize(new Dimension(250, 250));
			lblTiendaLogo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}

		GridBagConstraints gbcLogo = new GridBagConstraints();
		gbcLogo.gridx = 0;
		gbcLogo.gridy = 0;
		gbcLogo.anchor = GridBagConstraints.CENTER;
		gbcLogo.weightx = 1.0;
		gbcLogo.weighty = 1.0;
		panelLogo.add(lblTiendaLogo, gbcLogo);

		panelCentral.add(panelLogo, BorderLayout.CENTER);
		contentPaneConFondo.add(panelCentral, BorderLayout.CENTER);

		panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelInferior.setOpaque(false); // Hacer transparente para ver el fondo

		btnVolver = new JButton("Volver");
		btnVolver.setFont(FONT_BOTON_ADMIN);
		btnVolver.setBackground(COLOR_BOTON_ADMIN);
		btnVolver.setForeground(COLOR_TEXTO_BOTON_ADMIN);
		btnVolver.setOpaque(true); 
		btnVolver.setContentAreaFilled(true);
		btnVolver.setBorderPainted(false);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVolver.setMargin(new Insets(PADDING_INTERNO_ADMIN / 2 + 2, PADDING_INTERNO_ADMIN + 5,
				PADDING_INTERNO_ADMIN / 2 + 2, PADDING_INTERNO_ADMIN + 5));
		btnVolver.addActionListener(this);

		final Color originalBgVolver = btnVolver.getBackground();
		final Color hoverBgVolver = COLOR_BOTON_ADMIN_HOVER;
		btnVolver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnVolver.setBackground(hoverBgVolver);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnVolver.setBackground(originalBgVolver);
			}
		});

		panelInferior.add(btnVolver);
		contentPaneConFondo.add(panelInferior, BorderLayout.SOUTH);
	}

	/**
	 * Crea y configura un botón con un estilo específico para el menú de administrador.
	 * Incluye texto, un icono cargado de un recurso y efectos visuales (hover).
	 *
	 * @param texto El texto que se mostrará en el botón.
	 * @param iconoPath La ruta al recurso de imagen para el icono del botón.
	 * @return El JButton configurado.
	 */
	private JButton crearAdminBoton(String texto, String iconoPath) {
		JButton btn = new JButton(texto);
		btn.setFont(FONT_BOTON_ADMIN);
		btn.setIcon(cargarIcono(iconoPath, 36, 36));
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setIconTextGap(PADDING_INTERNO_ADMIN);

		btn.setBackground(COLOR_BOTON_ADMIN);
		btn.setForeground(COLOR_TEXTO_BOTON_ADMIN);
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setMargin(new Insets(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN * 2, PADDING_INTERNO_ADMIN,
				PADDING_INTERNO_ADMIN * 2));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT); 
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, btn.getPreferredSize().height));

		btn.addActionListener(this);

		final Color originalBg = btn.getBackground();
		final Color hoverBg = COLOR_BOTON_ADMIN_HOVER;
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(hoverBg);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(originalBg);
			}
		});
		return btn;
	}

	/**
	 * Abre una ventana de diálogo especificada, ocultando la ventana actual
	 * si la ventana a abrir es modal, y volviéndola a hacer visible después
	 * si esta ventana no ha sido cerrada.
	 *
	 * @param abrirOpc La JDialog a abrir.
	 */
	public void abrirOpcion(JDialog abrirOpc) {
		if (abrirOpc != null) {
			this.setVisible(false); 
			abrirOpc.setVisible(true); // Mostrar la ventana hija (modal)
            // La ejecución se pausa aquí hasta que abrirOpc se cierre

            // Cuando se cierra la ventana hija:
			if (this.isDisplayable()) { // Comprobar si la ventana padre todavía existe
				this.setVisible(true); // Volver a mostrar la ventana padre
			}
            // Considera liberar recursos de la ventana hija si ya no se necesita
            abrirOpc.dispose(); 
		}
	}

	/**
	 * Maneja los eventos de acción disparados por los botones en este diálogo.
	 * Identifica qué botón fue presionado y abre la ventana de configuración
	 * correspondiente (Usuarios o Artículos), o cierra la ventana actual si se pulsa "Volver".
	 *
	 * @param e El ActionEvent que ocurrió.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		JDialog ventanaAAbrir = null;

		if (source == btnConfigArticulos) {
			// ¡¡IMPORTANTE!! Asegúrate de que 'new AdminConfigArticulos(this)'
			// llama al constructor de tu clase EXTERNA AdminConfigArticulos.java
			ventanaAAbrir = new AdminConfigArticulos(this);
			abrirOpcion(ventanaAAbrir);
		} else if (source == btnConfigUsuario) {
			ventanaAAbrir = new AdminConfigUsuario(this);
			abrirOpcion(ventanaAAbrir);
		} else if (source == btnVolver) {
			this.dispose();
		}
	}

	/**
	 * Carga un icono desde un recurso dado, lo escala al tamaño especificado
	 * y opcionalmente lo hace transparente eliminando un color específico.
	 * Si el recurso no se encuentra, crea y devuelve un icono de marcador de posición.
	 *
	 * @param path La ruta al recurso del icono.
	 * @param width El ancho deseado para el icono escalado.
	 * @param height La altura deseada para el icono escalado.
	 * @return Un ImageIcon escalado y procesado, o un marcador de posición si falla la carga.
	 */
	private ImageIcon cargarIcono(String path, int width, int height) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			try {
				ImageIcon originalIcon = new ImageIcon(imgURL);
				Image image = originalIcon.getImage();

				if (originalIcon.getIconWidth() != width || originalIcon.getIconHeight() != height) {
					image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				}

				if (COLOR_TRANSPARENTE_ICONO != null) {
					ImageFilter filter = new RGBImageFilter() {
						public int markerRGB = COLOR_TRANSPARENTE_ICONO.getRGB() | 0xFF000000;
						@Override
						public final int filterRGB(int x, int y, int rgb) {
							if ((rgb | 0xFF000000) == markerRGB) {
								return 0x00FFFFFF & rgb; 
							} else {
								return rgb;
							}
						}
					};
					ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
					image = Toolkit.getDefaultToolkit().createImage(ip);
				}
				return new ImageIcon(image);
			} catch (Exception e) {
				e.getMessage();
				return crearIconoPlaceholder(width, height);
			}
		} else {
			return crearIconoPlaceholder(width, height);
		}
	}

	/**
	 * Crea un icono de marcador de posición (placeholder) visualmente reconocible
	 * para usar cuando un recurso de icono real no se puede cargar.
	 * Dibuja un cuadrado con un borde y una X en diagonal.
	 *
	 * @param width El ancho del icono placeholder.
	 * @param height La altura del icono placeholder.
	 * @return Un ImageIcon que representa un icono de marcador de posición.
	 */
	private ImageIcon crearIconoPlaceholder(int width, int height) {
		BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = placeholder.createGraphics();
		try {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); 
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setComposite(AlphaComposite.SrcOver);
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, width - 1, height - 1);
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(2, 2, width - 3, height - 3);
			g2d.drawLine(2, height - 3, width - 3, 2);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		} finally {
			g2d.dispose();
		}
		return new ImageIcon(placeholder);
	}


}