package vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder; // Importar EmptyBorder
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource; // Necesario para makeTransparent
import java.awt.image.ImageFilter;       // Necesario para makeTransparent
import java.awt.image.ImageProducer;    // Necesario para makeTransparent
import java.awt.image.RGBImageFilter;   // Necesario para makeTransparent
import java.io.IOException;
import java.net.URL; // Necesario para cargarIcono

public class VentanaIntermedia extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    // --- Componentes UI ---
    private JButton btnConfigArticulos;
    private JButton btnConfigUsuario;
    private JLabel lblTiendaLogo;
    private JLabel lblTituloAdmin; // Renombrado para claridad
    private BufferedImage backgroundImage;

    // --- Constantes de Estilo (Puedes compartirlas o redefinirlas) ---
    private static final Font FONT_TITULO_ADMIN = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_BOTON_ADMIN = new Font("Segoe UI", Font.BOLD, 14); // Un poco más grande para admin
    private static final Color COLOR_BOTON_ADMIN = new Color(0x5C6BC0); // Un color diferente para admin (Indigo light)
    private static final Color COLOR_BOTON_ADMIN_HOVER = new Color(0x3F51B5); // Indigo normal
    private static final Color COLOR_TEXTO_BOTON_ADMIN = Color.WHITE;
    private static final Color COLOR_TITULO_ADMIN = new Color(50, 50, 50); // Color oscuro para título

    // Padding y Espaciado
    private static final int PADDING_GENERAL_ADMIN = 20;
    private static final int PADDING_INTERNO_ADMIN = 10;
    private static final int GAP_BOTONES_ADMIN = 15; // Espacio vertical entre botones

    // Color a hacer transparente en los iconos (normalmente blanco)
    private static final Color COLOR_TRANSPARENTE_ICONO = Color.WHITE;

    /**
     * Create the dialog.
     */
    public VentanaIntermedia(JDialog padre) {
        super(padre, "Menú Administrador", true); // Título más descriptivo
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // --- Cargar Imagen de Fondo ---
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/imagenes/fondoMadera.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen de fondo '/imagenes/fondoMadera.jpg': " + e.getMessage());
            // Establecer un color de fondo alternativo si la imagen no carga
            // (Se hará en el panel personalizado)
        }

        // --- Usar un JPanel con pintura personalizada como ContentPane ---
        setContentPane(new JPanel(new BorderLayout(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN)) { // BorderLayout para estructura general
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Dibujar la imagen de fondo para que cubra todo el panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback si no hay imagen
                    g.setColor(new Color(235, 235, 235)); // Un gris claro
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        });
        // Hacer el contentPane opaco si no hay imagen, transparente si la hay
        ((JPanel) getContentPane()).setOpaque(backgroundImage == null);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN, PADDING_GENERAL_ADMIN));

        // --- Inicializar y Configurar Componentes con Layout ---
        initComponents(); // Ahora este método configura y añade con layouts

        // --- Configuración Final Ventana ---
        setMinimumSize(new Dimension(600, 480)); // Establecer tamaño mínimo
        pack(); // Ajustar al contenido preferido por los layouts
        setLocationRelativeTo(padre); // Centrar respecto al padre
    }

    /**
     * Inicializa y organiza los componentes usando Layout Managers.
     */
    private void initComponents() {

        // 1. Panel Título (NORTH)
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrar título
        panelTitulo.setOpaque(false); // Hacer transparente para ver fondo
        lblTituloAdmin = new JLabel("Menú de Administrador");
        lblTituloAdmin.setFont(FONT_TITULO_ADMIN);
        lblTituloAdmin.setForeground(COLOR_TITULO_ADMIN); // Color de texto para el título
        // lblTituloAdmin.setForeground(Color.WHITE); // Alternativa si el fondo es muy oscuro
        panelTitulo.add(lblTituloAdmin);
        getContentPane().add(panelTitulo, BorderLayout.NORTH);


        // 2. Panel Central (CENTER) - Contendrá botones a la izquierda y logo a la derecha
        JPanel panelCentral = new JPanel(new BorderLayout(PADDING_INTERNO_ADMIN * 2, 0)); // Espacio entre botones y logo
        panelCentral.setOpaque(false);

        // 2a. Panel Botones (WEST del panelCentral)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS)); // Alinear botones verticalmente
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(PADDING_INTERNO_ADMIN * 2, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN)); // Padding

        // Crear y añadir botones
        btnConfigUsuario = crearAdminBoton("Gestionar Usuarios", "/iconos/admin.png"); // Icono representativo
        btnConfigArticulos = crearAdminBoton("Gestionar Artículos", "/iconos/inventory.png"); // Icono representativo

        panelBotones.add(btnConfigUsuario);
        panelBotones.add(Box.createRigidArea(new Dimension(0, GAP_BOTONES_ADMIN))); // Espacio vertical
        panelBotones.add(btnConfigArticulos);
        panelBotones.add(Box.createVerticalGlue()); // Empujar botones hacia arriba si hay espacio extra

        panelCentral.add(panelBotones, BorderLayout.WEST);

        // 2b. Panel Logo (CENTER o EAST del panelCentral)
        JPanel panelLogo = new JPanel(new GridBagLayout()); // GridBagLayout para centrar fácilmente el logo
        panelLogo.setOpaque(false);

        // Cargar y escalar logo
        try {
            Image img = new ImageIcon(this.getClass().getResource("/imagenes/logoColor.jpg")).getImage();
            // Ajustar tamaño del logo si es necesario, 300x300 puede ser suficiente
            Image imgEscalada = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            lblTiendaLogo = new JLabel(new ImageIcon(imgEscalada));
        } catch (NullPointerException e) {
             System.err.println("Error: No se pudo cargar el logo '/imagenes/logoColor.jpg'");
             lblTiendaLogo = new JLabel("Logo no disponible"); // Placeholder texto
             lblTiendaLogo.setPreferredSize(new Dimension(300, 300));
             lblTiendaLogo.setHorizontalAlignment(SwingConstants.CENTER);
             lblTiendaLogo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        // Añadir logo al panelLogo centrado
        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.anchor = GridBagConstraints.CENTER; // Centrar el componente
        gbcLogo.weightx = 1.0; // Permitir que ocupe espacio horizontal
        gbcLogo.weighty = 1.0; // Permitir que ocupe espacio vertical
        panelLogo.add(lblTiendaLogo, gbcLogo);

        panelCentral.add(panelLogo, BorderLayout.CENTER); // Añadir panel logo

        // Añadir panel central al content pane
        getContentPane().add(panelCentral, BorderLayout.CENTER);
    }

    /**
     * Crea y configura un JButton para el menú de administrador.
     */
    private JButton crearAdminBoton(String texto, String iconoPath) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BOTON_ADMIN);
        btn.setIcon(cargarIcono(iconoPath, 36, 36)); // Cargar icono (ya aplica transparencia si es necesario)
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Alinear texto e icono a la izquierda
        btn.setIconTextGap(PADDING_INTERNO_ADMIN); // Espacio entre icono y texto

        // Estilo del botón
        btn.setBackground(COLOR_BOTON_ADMIN);
        btn.setForeground(COLOR_TEXTO_BOTON_ADMIN);
        btn.setOpaque(true); // Necesario para que eladmi fondo se vea
        btn.setContentAreaFilled(true); // Para L&F modernos
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Padding interno del botón
        btn.setMargin(new Insets(PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN * 2, PADDING_INTERNO_ADMIN, PADDING_INTERNO_ADMIN * 2));
        // Tamaño preferido/máximo para que tengan el mismo ancho
        btn.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear a la izquierda en BoxLayout
        // btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, btn.getPreferredSize().height + 10)); // Opcional: Forzar misma altura

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
     * @param abrirOpc El JDialog a mostrar.
     */
    public void abrirOpcion(JDialog abrirOpc) {
        if (abrirOpc != null) {
            abrirOpc.setVisible(true);
            // Nota: El dispose() del JDialog abierto debería hacerse dentro de su propia lógica
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Cerrar esta ventana intermedia ANTES de abrir la siguiente
        // this.dispose(); // Opcional: descomentar si quieres que se cierre al elegir una opción

        if (e.getSource() == btnConfigArticulos) {
            // Asumiendo que AdminConfigArticulos existe y es un JDialog
            // AdminConfigArticulos vistaCfgArt = new AdminConfigArticulos(this);
            // abrirOpcion(vistaCfgArt);
             System.out.println("Abrir configuración de artículos..."); // Placeholder
        } else if (e.getSource() == btnConfigUsuario) {
            // Asumiendo que AdminConfigUsuario existe y es un JDialog
            // AdminConfigUsuario vistaCfgUsu = new AdminConfigUsuario(this);
            // abrirOpcion(vistaCfgUsu);
            System.out.println("Abrir configuración de usuarios..."); // Placeholder
        }

        // Si NO se hizo dispose() antes, hacerlo después si la nueva ventana se abrió correctamente.
        // Si la nueva ventana es modal (true), esta línea se ejecutará DESPUÉS de que se cierre.
        // Si la nueva ventana no es modal (false), esta línea se ejecutará inmediatamente.
        // if (!this.isDisplayable()) { // Si ya se hizo dispose antes, no hacer nada
        // } else {
        //       dispose(); // Cerrar esta ventana después de abrir la otra (si no es modal)
        // }
    }

    // --- Método Cargar Icono ---
    // Modificado para aplicar transparencia si es necesario
    private ImageIcon cargarIcono(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            ImageIcon finalIcon;

            // Escalar si es necesario
            if (originalIcon.getIconWidth() != width || originalIcon.getIconHeight() != height) {
                Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                finalIcon = new ImageIcon(image);
            } else {
                finalIcon = originalIcon;
            }

            // Aplicar transparencia
            // Asegúrate de que COLOR_TRANSPARENTE_ICONO es el color de fondo real de tus iconos
            return makeTransparent(finalIcon, COLOR_TRANSPARENTE_ICONO);

        } else {
            System.err.println("Icono no encontrado: " + path);
            // Crear placeholder si falla la carga
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
            g2d.drawLine(width / 4, height / 4, 3 * width / 4, 3 * height / 4);
            g2d.drawLine(width / 4, 3 * height / 4, 3 * width / 4, height / 4);
        } finally {
            g2d.dispose();
        }
        return new ImageIcon(placeholder);
    }

    // --- Método makeTransparent para eliminar un color de fondo específico ---
    /**
     * Convierte un color específico de un ImageIcon en transparente.
     * @param icon El ImageIcon original.
     * @param colorToRemove El Color que se hará transparente.
     * @return Un nuevo ImageIcon con la transparencia aplicada.
     */
    private ImageIcon makeTransparent(ImageIcon icon, final Color colorToRemove) {
        if (icon == null) return null;

        Image image = icon.getImage();

        // Crea un filtro para reemplazar el color especificado con transparencia.
        ImageFilter filter = new RGBImageFilter() {
            // El color que buscamos. Los bits alfa se ignoran en la comparación inicial.
            // Usamos 0xFF000000 para asegurar que comparamos solo RGB.
            public int markerRGB = colorToRemove.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                // Compara el pixel actual (ignorando su alfa) con el color a eliminar.
                if ((rgb | 0xFF000000) == markerRGB) {
                    // El color coincide, hazlo transparente.
                    // Mantenemos los bits de color (0x00FFFFFF) y ponemos los bits alfa a 0 (0x00...).
                    return 0x00FFFFFF & rgb;
                } else {
                    // El color no coincide, deja el píxel como está.
                    // Esto preserva la posible transparencia original del píxel si la tuviera.
                    return rgb;
                }
            }
        };

        // Aplica el filtro a la imagen.
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        Image transparentImage = Toolkit.getDefaultToolkit().createImage(ip);

        // Espera a que la imagen se cargue completamente (importante)
        // Podríamos usar MediaTracker, pero para iconos pequeños esto suele ser suficiente.
        // ImageIcon loadTracker = new ImageIcon(transparentImage);

        return new ImageIcon(transparentImage); // Retorna el nuevo ImageIcon con transparencia
    }

    // --- Clases Placeholder para que compile (reemplazar con las reales) ---
    // Si no tienes estas clases, el código de actionPerformed fallará.
    // Deberás crear estas clases JDialog o comentar/modificar las líneas en actionPerformed.
    /*
    private static class AdminConfigArticulos extends JDialog {
        public AdminConfigArticulos(Dialog owner) {
            super(owner, "Configurar Artículos", true);
            setSize(400, 300);
            setLocationRelativeTo(owner);
            add(new JLabel("Ventana de Configuración de Artículos"));
        }
    }

    private static class AdminConfigUsuario extends JDialog {
        public AdminConfigUsuario(Dialog owner) {
            super(owner, "Configurar Usuarios", true);
            setSize(400, 300);
            setLocationRelativeTo(owner);
            add(new JLabel("Ventana de Configuración de Usuarios"));
        }
    }
    */

}