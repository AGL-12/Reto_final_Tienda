package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import controlador.Principal;
import excepciones.LoginError;
import modelo.Cliente;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class VistaLogIn extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JPasswordField txtContra;

	private JButton btnSalir;
	private JButton btnCheck;

	/**
	 * Create the frame.
	 */
	public VistaLogIn() {
		setTitle("Hola Mundo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 350);
		setMinimumSize(new Dimension(450, 280));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Usuario: ");
		lblNewLabel.setBounds(78, 58, 75, 14);
		contentPane.add(lblNewLabel);

		txtUser = new JTextField();
		txtUser.setBounds(158, 55, 284, 20);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setBounds(78, 83, 75, 14);
		contentPane.add(lblContrasea);

		txtContra = new JPasswordField();
		txtContra.setBounds(158, 80, 284, 20);
		txtContra.setColumns(10);
		contentPane.add(txtContra);

		btnCheck = new JButton("comprobar");
		btnCheck.setBounds(78, 207, 148, 82);
		btnCheck.addActionListener(this);
		contentPane.add(btnCheck);

		btnSalir = new JButton("Registrarte");
		btnSalir.setBounds(280, 207, 162, 82);
		btnSalir.addActionListener(this);
		contentPane.add(btnSalir);
	}

	protected void comprobar() {
		// creo objeto usuario para manejar los datos
		Cliente clien = new Cliente();
		clien.setUsuario(txtUser.getText());
		clien.setContra(new String(txtContra.getPassword()));

		// comprobar login correcto
		try {
			Principal.login(clien);
//			Menu startMenu = new Menu(this);
//			this.setVisible(false);
//			startMenu.setVisible(true);
		} catch (LoginError e) {
			e.visualizarMen();
			txtUser.setText("");
			txtContra.setText("");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSalir)) {
			this.dispose();
		} else if (e.getSource().equals(btnCheck)) {
			comprobar();
		}
	}
}
