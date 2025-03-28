package vista;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaIntermedia extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnListaCliente;
	private JButton btnConfigArticulos;
	private JButton btnConfigPedidos;
	private JButton btnConfigUsuario;

	/**
	 * Create the dialog.
	 */
	public VentanaIntermedia() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Menu de adminstrador");
		lblNewLabel.setBounds(150, 11, 149, 30);
		getContentPane().add(lblNewLabel);

		btnListaCliente = new JButton("Listar clientes");
		btnListaCliente.setBounds(38, 52, 140, 50);
		getContentPane().add(btnListaCliente);
		btnListaCliente.addActionListener(this);

		btnConfigArticulos = new JButton("Config Articulos");
		btnConfigArticulos.setBounds(247, 150, 140, 50);
		getContentPane().add(btnConfigArticulos);
		btnConfigArticulos.addActionListener(this);

		btnConfigPedidos = new JButton("Config Pedidos");
		btnConfigPedidos.setBounds(247, 52, 140, 50);
		getContentPane().add(btnConfigPedidos);
		btnConfigPedidos.addActionListener(this);

		btnConfigUsuario = new JButton("Config usuario");
		btnConfigUsuario.setBounds(38, 150, 140, 50);
		getContentPane().add(btnConfigUsuario);
		btnConfigUsuario.addActionListener(this);
	}

	public void abrirOpcion(JDialog abrirOpc) {
		abrirOpc.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnConfigArticulos)) {
			AdminConfigArticulos vistaCfgArt = new AdminConfigArticulos();
			abrirOpcion(vistaCfgArt);
		} else if (e.getSource().equals(btnConfigPedidos)) {
			AdminConfigPedidos vistaCfgPed = new AdminConfigPedidos();
			abrirOpcion(vistaCfgPed);
		} else if (e.getSource().equals(btnConfigUsuario)) {
			AdminConfigUsuario vistaCfgUsu = new AdminConfigUsuario();
			abrirOpcion(vistaCfgUsu);
		} else if (e.getSource().equals(btnListaCliente)) {
			AdminListaCliente vistaCfgCli = new AdminListaCliente();
			abrirOpcion(vistaCfgCli);
		}
	}
}
