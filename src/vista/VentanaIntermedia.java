package vista;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaIntermedia extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnConfigArticulos;
	private JButton btnConfigPedidos;
	private JButton btnConfigUsuario;

	/**
	 * Create the dialog.
	 */
	public VentanaIntermedia(JDialog padre) {
		super(padre, "Menu Admin", true);
		this.setLocationRelativeTo(null);
		setBounds(100, 100, 640, 515);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Menu de adminstrador");
		lblNewLabel.setBounds(246, 11, 149, 30);
		getContentPane().add(lblNewLabel);

		btnConfigArticulos = new JButton("Config Articulos");
		btnConfigArticulos.setBounds(38, 346, 140, 50);
		getContentPane().add(btnConfigArticulos);
		btnConfigArticulos.addActionListener(this);

		btnConfigPedidos = new JButton("Config Pedidos");
		btnConfigPedidos.setBounds(38, 82, 140, 50);
		getContentPane().add(btnConfigPedidos);
		btnConfigPedidos.addActionListener(this);

		btnConfigUsuario = new JButton("Config usuario");
		btnConfigUsuario.setBounds(38, 214, 140, 50);
		getContentPane().add(btnConfigUsuario);
		
		JLabel lblNewLabel_1 = new JLabel("we");
		lblNewLabel_1.setBounds(268, 64, 350, 350);
		getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setIcon(null);
		btnConfigUsuario.addActionListener(this);
	}

	public void abrirOpcion(JDialog abrirOpc) {
		abrirOpc.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnConfigArticulos)) {
			AdminConfigArticulos vistaCfgArt = new AdminConfigArticulos(this);
			abrirOpcion(vistaCfgArt);
		} else if (e.getSource().equals(btnConfigPedidos)) {
			AdminConfigPedidos vistaCfgPed = new AdminConfigPedidos(this);
			abrirOpcion(vistaCfgPed);
		} else if (e.getSource().equals(btnConfigUsuario)) {
			AdminConfigUsuario vistaCfgUsu = new AdminConfigUsuario(this);
			abrirOpcion(vistaCfgUsu);
		}
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
