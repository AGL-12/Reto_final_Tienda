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

		public BackgroundPanel(String imagePath) {
			try {
				URL imgUrl = getClass().getResource(imagePath);
				if (imgUrl == null) {
					throw new IOException("Recurso no encontrado: " + imagePath);
				}
				backgroundImage = ImageIO.read(imgUrl);
			} catch (IOException | IllegalArgumentException e) {
				e.getMessage();
				backgroundImage = null;
			}
			setOpaque(backgroundImage == null);
		}

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

	private void initComponents() {

		panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelTitulo.setOpaque(false);
		lblTituloAdmin = new JLabel("Menú de Administrador");
		lblTituloAdmin.setFont(FONT_TITULO_ADMIN);
		lblTituloAdmin.setForeground(COLOR_TITULO_ADMIN);
		panelTitulo.add(lblTituloAdmin);
		contentPaneConFondo.add(panelTitulo, BorderLayout.NORTH);

		panelCentral = new JPanel(new BorderLayout(PADDING_INTERNO_ADMIN * 2, 0));
		panelCentral.setOpaque(false);

		panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
		panelBotones.setOpaque(false);
		panelBotones.setBorder(new EmptyBorder(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN,
				PADDING_INTERNO_ADMIN));

		btnConfigUsuario = crearAdminBoton("Gestionar Usuarios", "/iconos/admin.png");
		btnConfigArticulos = crearAdminBoton("Gestionar Artículos", "/iconos/inventory.png");

		panelBotones.add(btnConfigUsuario);
		panelBotones.add(Box.createRigidArea(new Dimension(0, GAP_BOTONES_ADMIN)));
		panelBotones.add(btnConfigArticulos);

		panelCentral.add(panelBotones, BorderLayout.WEST);

		panelLogo = new JPanel(new GridBagLayout());
		panelLogo.setOpaque(false);

		lblTiendaLogo = new JLabel();
		lblTiendaLogo.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			URL logoUrl = getClass().getResource("/imagenes/tienda_brico_admin.jpg");
			if (logoUrl == null) {
				throw new NullPointerException("Recurso no encontrado: /imagenes/tienda_brico_admin.jpg");
			}
			Image img = new ImageIcon(logoUrl).getImage();
			Image imgEscalada = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
			lblTiendaLogo.setIcon(new ImageIcon(imgEscalada));
		} catch (NullPointerException | IllegalArgumentException e) {
			e.getMessage();
			lblTiendaLogo.setText("Logo no disponible");
			lblTiendaLogo.setPreferredSize(new Dimension(300, 300));
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
		panelInferior.setOpaque(false);

		btnVolver = new JButton("Volver");
		btnVolver.setFont(FONT_BOTON_ADMIN);
		btnVolver.setBackground(COLOR_BOTON_ADMIN);
		btnVolver.setForeground(COLOR_TEXTO_BOTON_ADMIN);
		btnVolver.setOpaque(true);
		btnVolver.setContentAreaFilled(true);
		btnVolver.setBorderPainted(false);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// Usamos un margen un poco más ajustado para el botón volver
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

	public void abrirOpcion(JDialog abrirOpc) {
		if (abrirOpc != null) {
			this.setVisible(false);
			abrirOpc.setVisible(true);
			// Solo volvemos a hacer visible si la ventana hija era modal
			// y esta ventana no ha sido 'disposed'
			if (abrirOpc.isModal() && this.isDisplayable()) {
				this.setVisible(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		JDialog ventanaAAbrir = null;

		if (source == btnConfigArticulos) {
			ventanaAAbrir = new AdminConfigArticulos(this);
			abrirOpcion(ventanaAAbrir);
		} else if (source == btnConfigUsuario) {
			ventanaAAbrir = new AdminConfigUsuario(this);
			abrirOpcion(ventanaAAbrir);
		} else if (source == btnVolver) {
			this.dispose();
		}
	}

	private ImageIcon cargarIcono(String path, int width, int height) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon originalIcon = new ImageIcon(imgURL);
			Image image = originalIcon.getImage();

			if (originalIcon.getIconWidth() != width || originalIcon.getIconHeight() != height) {
				image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			}
			ImageIcon scaledIcon = new ImageIcon(image);
			return makeTransparent(scaledIcon, COLOR_TRANSPARENTE_ICONO);

		} else {
			return crearIconoPlaceholder(width, height);
		}
	}

	private ImageIcon crearIconoPlaceholder(int width, int height) {
		BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = placeholder.createGraphics();
		try {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(0, 0, width - 1, height - 1);
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(1, 1, width - 2, height - 2);
			g2d.drawLine(1, height - 2, width - 2, 1);
		} finally {
			g2d.dispose();
		}
		return new ImageIcon(placeholder);
	}

	private ImageIcon makeTransparent(ImageIcon icon, final Color colorToRemove) {
		if (icon == null)
			return null;

		Image image = icon.getImage();
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = colorToRemove.getRGB() | 0xFF000000;

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
		Image transparentImage = Toolkit.getDefaultToolkit().createImage(ip);
		return new ImageIcon(transparentImage);
	}

	static class AdminConfigArticulos extends JDialog {
		private static final long serialVersionUID = 1L;

		public AdminConfigArticulos(Dialog owner) {
			super(owner, "Configurar Artículos", true);
			setSize(400, 300);
			setLocationRelativeTo(owner);
			setLayout(new BorderLayout());
			add(new JLabel("Ventana de Configuración de Artículos", SwingConstants.CENTER), BorderLayout.CENTER);
			JButton closeButton = new JButton("Cerrar");
			closeButton.addActionListener(e -> dispose());
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(closeButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}

	static class AdminConfigUsuario extends JDialog {
		private static final long serialVersionUID = 1L;

		public AdminConfigUsuario(Dialog owner) {
			super(owner, "Configurar Usuarios", true);
			setSize(400, 300);
			setLocationRelativeTo(owner);
			setLayout(new BorderLayout());
			add(new JLabel("Ventana de Configuración de Usuarios", SwingConstants.CENTER), BorderLayout.CENTER);
			JButton closeButton = new JButton("Cerrar");
			closeButton.addActionListener(e -> dispose());
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(closeButton);
			add(buttonPanel, BorderLayout.SOUTH);
		}
	}
}