package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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

    // --- Componentes UI ---
    private JButton btnConfigArticulos;
    private JButton btnConfigUsuario;
    private JLabel lblTiendaLogo;
    private JLabel lblTituloAdmin;
    private BackgroundPanel contentPaneConFondo;

    // Paneles de estructura
    private JPanel panelTitulo;
    private JPanel panelCentral;
    private JPanel panelBotones;
    private JPanel panelLogo;


    // --- Constantes de Estilo ---
    private static final Font FONT_TITULO_ADMIN = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_BOTON_ADMIN = new Font("Segoe UI", Font.BOLD, 14);
    private static final Color COLOR_BOTON_ADMIN = new Color(0x5C6BC0);
    private static final Color COLOR_BOTON_ADMIN_HOVER = new Color(0x3F51B5);
    private static final Color COLOR_TEXTO_BOTON_ADMIN = Color.WHITE;
    private static final Color COLOR_TITULO_ADMIN = new Color(50, 50, 50);
    private static final Color COLOR_FALLBACK_FONDO = new Color(235, 235, 235);

    // Padding y Espaciado
    private static final int PADDING_GENERAL_ADMIN = 20;
    private static final int PADDING_INTERNO_ADMIN = 10;
    private static final int GAP_BOTONES_ADMIN = 15;

    // Color a hacer transparente en los iconos
    private static final Color COLOR_TRANSPARENTE_ICONO = Color.WHITE;

    // --- Clase Interna para el Panel con Fondo ---
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
                System.err.println("Error al cargar la imagen de fondo '" + imagePath + "': " + e.getMessage());
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


    /**
     * Create the dialog.
     */
    public VentanaIntermedia(JDialog padre) {
        super(padre, "Menú Administrador", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        contentPaneConFondo = new BackgroundPanel("/imagenes/fondoMadera.jpg");
        contentPaneConFondo.setLayout(new BorderLayout(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN));
        contentPaneConFondo.setBorder(new EmptyBorder(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN));
        setContentPane(contentPaneConFondo);

        initComponents(); // Configura y añade componentes

        setPreferredSize(new Dimension(600, 480)); // Tamaño base
        pack(); // Ajusta al contenido preferido (ahora influenciado por el setPreferredSize de panelBotones)
        setMinimumSize(new Dimension(550, 450)); // Mínimo después de pack
        setLocationRelativeTo(padre); // Centrar
    }

    /**
     * Inicializa y organiza los componentes usando Layout Managers.
     * Se aplica la corrección para WindowBuilder en panelBotones.
     */
    private void initComponents() {

        // 1. Panel Título (NORTH)
        panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        lblTituloAdmin = new JLabel("Menú de Administrador");
        lblTituloAdmin.setFont(FONT_TITULO_ADMIN);
        lblTituloAdmin.setForeground(COLOR_TITULO_ADMIN);
        panelTitulo.add(lblTituloAdmin);
        contentPaneConFondo.add(panelTitulo, BorderLayout.NORTH);


        // 2. Panel Central (CENTER)
        panelCentral = new JPanel(new BorderLayout(PADDING_INTERNO_ADMIN * 2, 0));
        panelCentral.setOpaque(false);

        // 2a. Panel Botones (WEST del panelCentral) - **Volvemos a BoxLayout**
        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS)); // Eje Vertical
        panelBotones.setOpaque(false);
        // Padding interno para el panel de botones
        panelBotones.setBorder(new EmptyBorder(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN));

        // Crear botones (aún no añadidos)
        btnConfigUsuario = crearAdminBoton("Gestionar Usuarios", "/iconos/admin.png");
        btnConfigArticulos = crearAdminBoton("Gestionar Artículos", "/iconos/inventory.png");

        // Añadir botones y espaciado al panelBotones
        panelBotones.add(btnConfigUsuario);
        panelBotones.add(Box.createRigidArea(new Dimension(0, GAP_BOTONES_ADMIN))); // Espacio vertical
        panelBotones.add(btnConfigArticulos);
        panelBotones.add(Box.createVerticalGlue()); // Empujar hacia arriba

        // --- !! CORRECCIÓN PARA WINDOWBUILDER !! ---
        // Calcular tamaño preferido explícito para el panel de botones
        // Esto ayuda al diseñador a reservar suficiente espacio vertical
        try {
             // Usar el primer botón para obtener dimensiones preferidas
            Dimension buttonSize = btnConfigUsuario.getPreferredSize();
            int prefWidth = buttonSize.width + (PADDING_INTERNO_ADMIN * 2); // Ancho botón + padding izq/der del panel
            int prefHeight = (buttonSize.height * 2) // Altura de 2 botones
                             + GAP_BOTONES_ADMIN     // Espacio entre ellos
                             + (PADDING_INTERNO_ADMIN * 2); // Padding sup/inf del panel
            panelBotones.setPreferredSize(new Dimension(prefWidth, prefHeight));
        } catch (Exception e) {
             // Fallback por si algo falla (ej, botón aún no dimensionado)
             System.err.println("Advertencia: No se pudo calcular el tamaño preferido para panelBotones. Usando fallback.");
             panelBotones.setPreferredSize(new Dimension(250, 150)); // Un tamaño por defecto
        }
        // ------------------------------------------

        panelCentral.add(panelBotones, BorderLayout.WEST); // Añadir panel de botones al oeste

        // 2b. Panel Logo (CENTER del panelCentral)
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
            System.err.println("Error al cargar el logo: " + e.getMessage());
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

        panelCentral.add(panelLogo, BorderLayout.CENTER); // Añadir panel de logo al centro

        // Añadir panel central al content pane
        contentPaneConFondo.add(panelCentral, BorderLayout.CENTER);
    }

    /**
     * Crea y configura un JButton para el menú de administrador.
     */
    private JButton crearAdminBoton(String texto, String iconoPath) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON_ADMIN);
        btn.setIcon(cargarIcono(iconoPath, 36, 36));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(PADDING_INTERNO_ADMIN);

        // Estilo
        btn.setBackground(COLOR_BOTON_ADMIN);
        btn.setForeground(COLOR_TEXTO_BOTON_ADMIN);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN * 2, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN * 2));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT); // **Restaurado** para BoxLayout

        btn.addActionListener(this);

        // Efecto Hover
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
     * Método auxiliar para abrir otro JDialog.
     */
    public void abrirOpcion(JDialog abrirOpc) {
        if (abrirOpc != null) {
            abrirOpc.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        JDialog ventanaAAbrir = null;

        if (source == btnConfigArticulos) {
             ventanaAAbrir = new AdminConfigArticulos(this);
        } else if (source == btnConfigUsuario) {
             ventanaAAbrir = new AdminConfigUsuario(this);
        }

        if (ventanaAAbrir != null) {
            // Lógica de cierre opcional aquí...
            abrirOpcion(ventanaAAbrir);
        }
    }

    // --- Método Cargar Icono (con transparencia) ---
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
            System.err.println("Icono no encontrado: " + path);
            return crearIconoPlaceholder(width, height);
        }
    }

    // --- Método Crear Icono Placeholder ---
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

    // --- Método makeTransparent para eliminar un color de fondo ---
    private ImageIcon makeTransparent(ImageIcon icon, final Color colorToRemove) {
        if (icon == null) return null;

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

    // --- Clases Placeholder para Compilación ---
     private static class AdminConfigArticulos extends JDialog {
         private static final long serialVersionUID = 1L;
         public AdminConfigArticulos(Dialog owner) {
             super(owner, "Configurar Artículos", true);
             setSize(400, 300);
             setLocationRelativeTo(owner);
             add(new JLabel("Ventana de Configuración de Artículos (Placeholder)"));
             setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         }
     }

     private static class AdminConfigUsuario extends JDialog {
          private static final long serialVersionUID = 1L;
         public AdminConfigUsuario(Dialog owner) {
             super(owner, "Configurar Usuarios", true);
             setSize(400, 300);
             setLocationRelativeTo(owner);
             add(new JLabel("Ventana de Configuración de Usuarios (Placeholder)"));
             setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         }
     }


    // --- Método main (opcional, para probar esta ventana directamente) ---
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JDialog framePadre = new JDialog();
            framePadre.setModal(false);
            framePadre.setTitle("Ventana Padre Ficticia");
            framePadre.setSize(100,100);
            framePadre.setLocationRelativeTo(null);

            try {
                VentanaIntermedia dialog = new VentanaIntermedia(framePadre);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    */
}