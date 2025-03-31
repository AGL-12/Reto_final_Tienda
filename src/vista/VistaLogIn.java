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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class VistaLogIn extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JPasswordField txtContra;

	private JButton btnSignIn;
	private JButton btnLogIn;

	/**
	 * Create the frame.
	 */
	public VistaLogIn() {
		setTitle("Hola Mundo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 350);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Usuario: ");

		txtUser = new JTextField();
		txtUser.setColumns(10);

		JLabel lblContrasea = new JLabel("Contraseña:");

		txtContra = new JPasswordField();
		txtContra.setColumns(10);

		btnLogIn = new JButton("comprobar");
		btnLogIn.addActionListener(this);

		btnSignIn = new JButton("Registrarte");
		btnSignIn.addActionListener(this);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_contentPane.createSequentialGroup().addContainerGap(42, Short.MAX_VALUE).addGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
								.addGap(5)
								.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblContrasea, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
								.addGap(5)
								.addComponent(txtContra, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)))
						.addGap(31)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(35)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(3).addComponent(lblNewLabel))
								.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(5)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(3).addComponent(lblContrasea))
								.addComponent(txtContra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnLogIn, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSignIn, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
						.addGap(45)));
		contentPane.setLayout(gl_contentPane);
		pack();
	}

	protected void comprobar() {
		// creo objeto usuario para manejar los datos
		Cliente clien = new Cliente();
		clien.setUsuario(txtUser.getText());
		clien.setContra(new String(txtContra.getPassword()));

		// comprobar login correcto
		try {
			clien = Principal.login(clien);

			this.setVisible(false);

			VistaTienda tienda = new VistaTienda(clien, this);
			tienda.setVisible(true);

			this.setVisible(true);
		} catch (LoginError e) {
			e.visualizarMen();
		} finally {
			limpiar();
		}
	}

	private void limpiar() {
		txtUser.setText("");
		txtContra.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSignIn)) {
			abrirVistaRegistro();
		} else if (e.getSource().equals(btnLogIn)) {
			comprobar();
		}
	}

	private void abrirVistaRegistro() {

		this.setVisible(false);

		VistaUsuario usuario = new VistaUsuario(null, this);
		usuario.setVisible(true);

		this.setVisible(true);
	}
}
