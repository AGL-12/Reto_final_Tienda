package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import modelo.Metodo;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.Principal;
import excepciones.AltaError;
import excepciones.DropError;
import excepciones.modifyError;
import modelo.Cliente;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.net.URL;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;

public class VistaUsuario extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblUsuario;
	private JLabel lblTitulo;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel lblPassword;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_8;
	private JLabel label_9;
	private JLabel lblDni;
	private JLabel lblDireccion;
	private JLabel lblMetodoPago;
	private JLabel lblNumeroCuenta;
	private JLabel lblEmail;
	private JTextField textUser;
	private JTextField textDni;
	private JTextField textEmail;
	private JTextField textDireccion;
	private JButton btnMostrarPedidos;
	private JButton btnRegistrarse;
	private JButton btnModificar;
	private JRadioButton rdbtnVisa;
	private JRadioButton rdbtnMastercard;
	private JRadioButton rdbtnPaypal;
	private JTextField textNumeroCuenta;
	private JPasswordField passwordFieldContra;
	private ButtonGroup rdGroup;
	private Cliente localClien;
	private JButton btnDrop;

	/**
	 * Launch the application.

	/**
	 * Create the dialog.
	 */
//	public VistaUsuario(Cliente clien, JFrame login, boolean modal) {
//		super(login);
//		super.setModal(modal);
//		setBounds(100, 100, 702, 392);
//		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
//		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//		getContentPane().add(contentPanel);
//		GridBagLayout gbl_contentPanel = new GridBagLayout();
//		gbl_contentPanel.columnWidths = new int[] { 114, 131, 114, 81, 213, 0 };
//		gbl_contentPanel.rowHeights = new int[] { 26, 3, 26, 26, 26, 26, 26, 31, 28, 26, 71, 0 };
//		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
//		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
//				Double.MIN_VALUE };
//		contentPanel.setLayout(gbl_contentPanel);
//		{
//			label_2 = new JLabel("");
//		}
//		{
//			label_1 = new JLabel("");
//		}
//		{
//			lblTitulo = new JLabel("USUARIO");
//			lblTitulo.setToolTipText("");
//			lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
//		}
//		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
//		gbc_lblTitulo.fill = GridBagConstraints.VERTICAL;
//		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
//		gbc_lblTitulo.gridheight = 2;
//		gbc_lblTitulo.gridx = 2;
//		gbc_lblTitulo.gridy = 0;
//		contentPanel.add(lblTitulo, gbc_lblTitulo);
//		GridBagConstraints gbc_label_1 = new GridBagConstraints();
//		gbc_label_1.fill = GridBagConstraints.BOTH;
//		gbc_label_1.insets = new Insets(0, 0, 5, 0);
//		gbc_label_1.gridwidth = 2;
//		gbc_label_1.gridx = 3;
//		gbc_label_1.gridy = 0;
//		contentPanel.add(label_1, gbc_label_1);
//		GridBagConstraints gbc_label_2 = new GridBagConstraints();
//		gbc_label_2.fill = GridBagConstraints.BOTH;
//		gbc_label_2.insets = new Insets(0, 0, 5, 0);
//		gbc_label_2.gridheight = 2;
//		gbc_label_2.gridx = 4;
//		gbc_label_2.gridy = 1;
//		contentPanel.add(label_2, gbc_label_2);
//		{
//			lblUsuario = new JLabel("User");
//			lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
//		}
//		GridBagConstraints gbc_lblUsuario = new GridBagConstraints();
//		gbc_lblUsuario.fill = GridBagConstraints.VERTICAL;
//		gbc_lblUsuario.insets = new Insets(0, 0, 5, 5);
//		gbc_lblUsuario.gridx = 0;
//		gbc_lblUsuario.gridy = 3;
//		contentPanel.add(lblUsuario, gbc_lblUsuario);
//		{
//			label_3 = new JLabel("");
//		}
//		{
//			textUser = new JTextField();
//			textUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			textUser.setColumns(10);
//			GridBagConstraints gbc_textUser = new GridBagConstraints();
//			gbc_textUser.gridwidth = 3;
//			gbc_textUser.insets = new Insets(0, 0, 5, 5);
//			gbc_textUser.fill = GridBagConstraints.HORIZONTAL;
//			gbc_textUser.gridx = 1;
//			gbc_textUser.gridy = 3;
//			contentPanel.add(textUser, gbc_textUser);
//		}
//		GridBagConstraints gbc_label_3 = new GridBagConstraints();
//		gbc_label_3.fill = GridBagConstraints.BOTH;
//		gbc_label_3.insets = new Insets(0, 0, 5, 0);
//		gbc_label_3.gridx = 4;
//		gbc_label_3.gridy = 3;
//		contentPanel.add(label_3, gbc_label_3);
//		{
//			label_4 = new JLabel("");
//		}
//		{
//			lblPassword = new JLabel("Password");
//			lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
//		}
//		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
//		gbc_lblPassword.fill = GridBagConstraints.VERTICAL;
//		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
//		gbc_lblPassword.gridx = 0;
//		gbc_lblPassword.gridy = 4;
//		contentPanel.add(lblPassword, gbc_lblPassword);
//		{
//			passwordFieldContra = new JPasswordField();
//			GridBagConstraints gbc_passwordFieldContra = new GridBagConstraints();
//			gbc_passwordFieldContra.gridwidth = 3;
//			gbc_passwordFieldContra.insets = new Insets(0, 0, 5, 5);
//			gbc_passwordFieldContra.fill = GridBagConstraints.HORIZONTAL;
//			gbc_passwordFieldContra.gridx = 1;
//			gbc_passwordFieldContra.gridy = 4;
//			contentPanel.add(passwordFieldContra, gbc_passwordFieldContra);
//		}
//		GridBagConstraints gbc_label_4 = new GridBagConstraints();
//		gbc_label_4.fill = GridBagConstraints.BOTH;
//		gbc_label_4.insets = new Insets(0, 0, 5, 0);
//		gbc_label_4.gridx = 4;
//		gbc_label_4.gridy = 4;
//		contentPanel.add(label_4, gbc_label_4);
//		lblDni = new JLabel("DNI/NIE");
//		lblDni.setFont(new Font("Tahoma", Font.BOLD, 12));
//		GridBagConstraints gbc_lblDni = new GridBagConstraints();
//		gbc_lblDni.fill = GridBagConstraints.VERTICAL;
//		gbc_lblDni.insets = new Insets(0, 0, 5, 5);
//		gbc_lblDni.gridx = 0;
//		gbc_lblDni.gridy = 5;
//		contentPanel.add(lblDni, gbc_lblDni);
//		{
//			textDni = new JTextField();
//			textDni.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			textDni.setColumns(10);
//			GridBagConstraints gbc_textDni = new GridBagConstraints();
//			gbc_textDni.gridwidth = 3;
//			gbc_textDni.insets = new Insets(0, 0, 5, 5);
//			gbc_textDni.fill = GridBagConstraints.HORIZONTAL;
//			gbc_textDni.gridx = 1;
//			gbc_textDni.gridy = 5;
//			contentPanel.add(textDni, gbc_textDni);
//		}
//		{
//			label_6 = new JLabel("");
//		}
//		GridBagConstraints gbc_label_6 = new GridBagConstraints();
//		gbc_label_6.fill = GridBagConstraints.BOTH;
//		gbc_label_6.insets = new Insets(0, 0, 5, 0);
//		gbc_label_6.gridx = 4;
//		gbc_label_6.gridy = 5;
//		contentPanel.add(label_6, gbc_label_6);
//		{
//			lblEmail = new JLabel("EMAIL");
//			lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
//			GridBagConstraints gbc_lblEmail = new GridBagConstraints();
//			gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
//			gbc_lblEmail.gridx = 0;
//			gbc_lblEmail.gridy = 6;
//			contentPanel.add(lblEmail, gbc_lblEmail);
//		}
//		{
//			textEmail = new JTextField();
//			textEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			textEmail.setColumns(10);
//			GridBagConstraints gbc_textEmail = new GridBagConstraints();
//			gbc_textEmail.gridwidth = 3;
//			gbc_textEmail.insets = new Insets(0, 0, 5, 5);
//			gbc_textEmail.fill = GridBagConstraints.HORIZONTAL;
//			gbc_textEmail.gridx = 1;
//			gbc_textEmail.gridy = 6;
//			contentPanel.add(textEmail, gbc_textEmail);
//		}
//		{
//			label_8 = new JLabel("");
//		}
//		GridBagConstraints gbc_label_8 = new GridBagConstraints();
//		gbc_label_8.fill = GridBagConstraints.BOTH;
//		gbc_label_8.insets = new Insets(0, 0, 5, 0);
//		gbc_label_8.gridx = 4;
//		gbc_label_8.gridy = 6;
//		contentPanel.add(label_8, gbc_label_8);
//		lblDireccion = new JLabel("ADRESS");
//		lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 12));
//		GridBagConstraints gbc_lblDireccion = new GridBagConstraints();
//		gbc_lblDireccion.fill = GridBagConstraints.VERTICAL;
//		gbc_lblDireccion.insets = new Insets(0, 0, 5, 5);
//		gbc_lblDireccion.gridx = 0;
//		gbc_lblDireccion.gridy = 7;
//		contentPanel.add(lblDireccion, gbc_lblDireccion);
//		{
//			textDireccion = new JTextField();
//			textDireccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			textDireccion.setColumns(10);
//			GridBagConstraints gbc_textDireccion = new GridBagConstraints();
//			gbc_textDireccion.gridwidth = 3;
//			gbc_textDireccion.insets = new Insets(0, 0, 5, 5);
//			gbc_textDireccion.fill = GridBagConstraints.HORIZONTAL;
//			gbc_textDireccion.gridx = 1;
//			gbc_textDireccion.gridy = 7;
//			contentPanel.add(textDireccion, gbc_textDireccion);
//		}
//		{
//			label_9 = new JLabel("");
//		}
//		GridBagConstraints gbc_label_9 = new GridBagConstraints();
//		gbc_label_9.fill = GridBagConstraints.BOTH;
//		gbc_label_9.insets = new Insets(0, 0, 5, 0);
//		gbc_label_9.gridx = 4;
//		gbc_label_9.gridy = 7;
//		contentPanel.add(label_9, gbc_label_9);
//		lblMetodoPago = new JLabel("PAYMENT METHOD");
//		lblMetodoPago.setFont(new Font("Tahoma", Font.BOLD, 12));
//		GridBagConstraints gbc_lblMetodoPago = new GridBagConstraints();
//		gbc_lblMetodoPago.fill = GridBagConstraints.VERTICAL;
//		gbc_lblMetodoPago.insets = new Insets(0, 0, 5, 5);
//		gbc_lblMetodoPago.gridx = 0;
//		gbc_lblMetodoPago.gridy = 8;
//		contentPanel.add(lblMetodoPago, gbc_lblMetodoPago);
//		{
//
//			rdbtnVisa = new JRadioButton("VISA");
//			rdbtnVisa.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			GridBagConstraints gbc_rdbtnVisa = new GridBagConstraints();
//			gbc_rdbtnVisa.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnVisa.gridx = 1;
//			gbc_rdbtnVisa.gridy = 8;
//			contentPanel.add(rdbtnVisa, gbc_rdbtnVisa);
//		}
//		{
//			rdbtnMastercard = new JRadioButton("MASTERCARD");
//			rdbtnMastercard.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			GridBagConstraints gbc_rdbtnMastercard = new GridBagConstraints();
//			gbc_rdbtnMastercard.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnMastercard.gridx = 2;
//			gbc_rdbtnMastercard.gridy = 8;
//			contentPanel.add(rdbtnMastercard, gbc_rdbtnMastercard);
//		}
//		{
//			rdbtnPaypal = new JRadioButton("PAYPAL");
//			rdbtnPaypal.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			GridBagConstraints gbc_rdbtnPaypal = new GridBagConstraints();
//			gbc_rdbtnPaypal.insets = new Insets(0, 0, 5, 5);
//			gbc_rdbtnPaypal.gridx = 3;
//			gbc_rdbtnPaypal.gridy = 8;
//			contentPanel.add(rdbtnPaypal, gbc_rdbtnPaypal);
//		}
//		// Crear un ButtonGroup y agregar los botones
//		rdGroup = new ButtonGroup();
//		rdGroup.add(rdbtnVisa);
//		rdGroup.add(rdbtnMastercard);
//		rdGroup.add(rdbtnPaypal);
//		lblNumeroCuenta = new JLabel("ACCOUNT NUMBER");
//		lblNumeroCuenta.setFont(new Font("Tahoma", Font.BOLD, 12));
//		GridBagConstraints gbc_lblNumeroCuenta = new GridBagConstraints();
//		gbc_lblNumeroCuenta.anchor = GridBagConstraints.EAST;
//		gbc_lblNumeroCuenta.fill = GridBagConstraints.VERTICAL;
//		gbc_lblNumeroCuenta.insets = new Insets(0, 0, 5, 5);
//		gbc_lblNumeroCuenta.gridx = 0;
//		gbc_lblNumeroCuenta.gridy = 9;
//		contentPanel.add(lblNumeroCuenta, gbc_lblNumeroCuenta);
//		{
//			textNumeroCuenta = new JTextField();
//			textNumeroCuenta.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			textNumeroCuenta.setColumns(10);
//			GridBagConstraints gbc_textNumeroCuenta = new GridBagConstraints();
//			gbc_textNumeroCuenta.gridwidth = 3;
//			gbc_textNumeroCuenta.insets = new Insets(0, 0, 5, 5);
//			gbc_textNumeroCuenta.fill = GridBagConstraints.HORIZONTAL;
//			gbc_textNumeroCuenta.gridx = 1;
//			gbc_textNumeroCuenta.gridy = 9;
//			contentPanel.add(textNumeroCuenta, gbc_textNumeroCuenta);
//		}
//		{
//			btnRegistrarse = new JButton("SIGN UP");
//			btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 14));
//			GridBagConstraints gbc_btnRegistrarse = new GridBagConstraints();
//			gbc_btnRegistrarse.insets = new Insets(0, 0, 0, 5);
//			gbc_btnRegistrarse.gridx = 1;
//			gbc_btnRegistrarse.gridy = 10;
//			contentPanel.add(btnRegistrarse, gbc_btnRegistrarse);
//			btnRegistrarse.addActionListener(this);
//
//		}
//		{
//			btnModificar = new JButton("MODIFY");
//			btnModificar.setFont(new Font("Tahoma", Font.BOLD, 14));
//			GridBagConstraints gbc_btnModificar = new GridBagConstraints();
//			gbc_btnModificar.insets = new Insets(0, 0, 0, 5);
//			gbc_btnModificar.gridx = 2;
//			gbc_btnModificar.gridy = 10;
//			contentPanel.add(btnModificar, gbc_btnModificar);
//			btnModificar.addActionListener(this);
//
//		}
//		{
//			btnMostrarPedidos = new JButton("ORDERS");
//			btnMostrarPedidos.setFont(new Font("Tahoma", Font.BOLD, 14));
//			GridBagConstraints gbc_btnMostrarPedidos = new GridBagConstraints();
//			gbc_btnMostrarPedidos.gridx = 4;
//			gbc_btnMostrarPedidos.gridy = 10;
//			contentPanel.add(btnMostrarPedidos, gbc_btnMostrarPedidos);
//			btnMostrarPedidos.addActionListener(this);
//
//		}
//		{
//			label_5 = new JLabel("");
//			getContentPane().add(label_5);
//		}
//		if (clien == null) {
//			btnModificar.setEnabled(false);
//			btnMostrarPedidos.setEnabled(false);
//
//		} else {
//			btnRegistrarse.setEnabled(false);
//		}
//	}

	/**
	 * @wbp.parser.constructor
	 */
	public VistaUsuario(Cliente clien, Object ventPadre, boolean modal) {
		super(ventPadre instanceof JFrame ? (JFrame) ventPadre
				: ventPadre instanceof JDialog ? (JDialog) ventPadre : null);
		localClien = clien;

		setModal(modal);

		inicializarComponentes();

		configureButtons(clien);

	}

	private void configureButtons(Cliente clien) {
		if (clien == null) {
			btnModificar.setEnabled(false);
			btnMostrarPedidos.setEnabled(false);
			btnDrop.setEnabled(false);
		} else {
			btnRegistrarse.setEnabled(false);
			textUser.setText(clien.getUsuario());
			passwordFieldContra.setText(clien.getContra());
			textDni.setText(clien.getDni());
			textEmail.setText(clien.getCorreo());
			textDireccion.setText(clien.getDireccion());
			if (clien.getMetodo_pago() == Metodo.visa) {
				rdbtnVisa.setSelected(true);
			} else if (clien.getMetodo_pago() == Metodo.mastercard) {
				rdbtnMastercard.setSelected(true);
			} else if (clien.getMetodo_pago() == Metodo.paypal) {
				rdbtnPaypal.setSelected(true);
			}
			textNumeroCuenta.setText(clien.getNum_cuenta());
		}
	}

	private void inicializarComponentes() {
		setBounds(100, 100, 702, 392);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 114, 131, 114, 81, 213, 0 };
		gbl_contentPanel.rowHeights = new int[] { 26, 3, 26, 26, 26, 26, 26, 31, 28, 26, 71, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		addComponetes(gbl_contentPanel);
	}

	private void addComponetes(GridBagLayout gbl_contentPanel) {
		label_2 = new JLabel("");

		label_1 = new JLabel("");

		lblTitulo = new JLabel("USUARIO");
		lblTitulo.setToolTipText("");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));

		GridBagConstraints gbc_lblTitulo = new GridBagConstraints();
		gbc_lblTitulo.fill = GridBagConstraints.VERTICAL;
		gbc_lblTitulo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitulo.gridheight = 2;
		gbc_lblTitulo.gridx = 2;
		gbc_lblTitulo.gridy = 0;
		contentPanel.add(lblTitulo, gbc_lblTitulo);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.fill = GridBagConstraints.BOTH;
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridwidth = 2;
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 0;
		contentPanel.add(label_1, gbc_label_1);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.fill = GridBagConstraints.BOTH;
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridheight = 2;
		gbc_label_2.gridx = 4;
		gbc_label_2.gridy = 1;
		contentPanel.add(label_2, gbc_label_2);

		lblUsuario = new JLabel("User");
		lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));

		GridBagConstraints gbc_lblUsuario = new GridBagConstraints();
		gbc_lblUsuario.fill = GridBagConstraints.VERTICAL;
		gbc_lblUsuario.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsuario.gridx = 0;
		gbc_lblUsuario.gridy = 3;
		contentPanel.add(lblUsuario, gbc_lblUsuario);

		label_3 = new JLabel("");

		textUser = new JTextField();
		textUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textUser.setColumns(10);
		GridBagConstraints gbc_textUser = new GridBagConstraints();
		gbc_textUser.gridwidth = 3;
		gbc_textUser.insets = new Insets(0, 0, 5, 5);
		gbc_textUser.fill = GridBagConstraints.HORIZONTAL;
		gbc_textUser.gridx = 1;
		gbc_textUser.gridy = 3;
		contentPanel.add(textUser, gbc_textUser);

		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.fill = GridBagConstraints.BOTH;
		gbc_label_3.insets = new Insets(0, 0, 5, 0);
		gbc_label_3.gridx = 4;
		gbc_label_3.gridy = 3;
		contentPanel.add(label_3, gbc_label_3);

		label_4 = new JLabel("");

		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));

		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.fill = GridBagConstraints.VERTICAL;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 4;
		contentPanel.add(lblPassword, gbc_lblPassword);

		passwordFieldContra = new JPasswordField();
		GridBagConstraints gbc_passwordFieldContra = new GridBagConstraints();
		gbc_passwordFieldContra.gridwidth = 3;
		gbc_passwordFieldContra.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldContra.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldContra.gridx = 1;
		gbc_passwordFieldContra.gridy = 4;
		contentPanel.add(passwordFieldContra, gbc_passwordFieldContra);

		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.fill = GridBagConstraints.BOTH;
		gbc_label_4.insets = new Insets(0, 0, 5, 0);
		gbc_label_4.gridx = 4;
		gbc_label_4.gridy = 4;
		contentPanel.add(label_4, gbc_label_4);
		lblDni = new JLabel("DNI/NIE");
		lblDni.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblDni = new GridBagConstraints();
		gbc_lblDni.fill = GridBagConstraints.VERTICAL;
		gbc_lblDni.insets = new Insets(0, 0, 5, 5);
		gbc_lblDni.gridx = 0;
		gbc_lblDni.gridy = 5;
		contentPanel.add(lblDni, gbc_lblDni);

		textDni = new JTextField();
		textDni.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDni.setColumns(10);
		GridBagConstraints gbc_textDni = new GridBagConstraints();
		gbc_textDni.gridwidth = 3;
		gbc_textDni.insets = new Insets(0, 0, 5, 5);
		gbc_textDni.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDni.gridx = 1;
		gbc_textDni.gridy = 5;
		contentPanel.add(textDni, gbc_textDni);

		label_6 = new JLabel("");

		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.fill = GridBagConstraints.BOTH;
		gbc_label_6.insets = new Insets(0, 0, 5, 0);
		gbc_label_6.gridx = 4;
		gbc_label_6.gridy = 5;
		contentPanel.add(label_6, gbc_label_6);

		lblEmail = new JLabel("EMAIL");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 6;
		contentPanel.add(lblEmail, gbc_lblEmail);

		textEmail = new JTextField();
		textEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textEmail.setColumns(10);
		GridBagConstraints gbc_textEmail = new GridBagConstraints();
		gbc_textEmail.gridwidth = 3;
		gbc_textEmail.insets = new Insets(0, 0, 5, 5);
		gbc_textEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_textEmail.gridx = 1;
		gbc_textEmail.gridy = 6;
		contentPanel.add(textEmail, gbc_textEmail);

		label_8 = new JLabel("");

		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.fill = GridBagConstraints.BOTH;
		gbc_label_8.insets = new Insets(0, 0, 5, 0);
		gbc_label_8.gridx = 4;
		gbc_label_8.gridy = 6;
		contentPanel.add(label_8, gbc_label_8);
		lblDireccion = new JLabel("ADRESS");
		lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblDireccion = new GridBagConstraints();
		gbc_lblDireccion.fill = GridBagConstraints.VERTICAL;
		gbc_lblDireccion.insets = new Insets(0, 0, 5, 5);
		gbc_lblDireccion.gridx = 0;
		gbc_lblDireccion.gridy = 7;
		contentPanel.add(lblDireccion, gbc_lblDireccion);

		textDireccion = new JTextField();
		textDireccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textDireccion.setColumns(10);
		GridBagConstraints gbc_textDireccion = new GridBagConstraints();
		gbc_textDireccion.gridwidth = 3;
		gbc_textDireccion.insets = new Insets(0, 0, 5, 5);
		gbc_textDireccion.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDireccion.gridx = 1;
		gbc_textDireccion.gridy = 7;
		contentPanel.add(textDireccion, gbc_textDireccion);

		label_9 = new JLabel("");

		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.fill = GridBagConstraints.BOTH;
		gbc_label_9.insets = new Insets(0, 0, 5, 0);
		gbc_label_9.gridx = 4;
		gbc_label_9.gridy = 7;
		contentPanel.add(label_9, gbc_label_9);
		lblMetodoPago = new JLabel("PAYMENT METHOD");
		lblMetodoPago.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblMetodoPago = new GridBagConstraints();
		gbc_lblMetodoPago.fill = GridBagConstraints.VERTICAL;
		gbc_lblMetodoPago.insets = new Insets(0, 0, 5, 5);
		gbc_lblMetodoPago.gridx = 0;
		gbc_lblMetodoPago.gridy = 8;
		contentPanel.add(lblMetodoPago, gbc_lblMetodoPago);

		rdbtnVisa = new JRadioButton("VISA");
		rdbtnVisa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_rdbtnVisa = new GridBagConstraints();
		gbc_rdbtnVisa.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnVisa.gridx = 1;
		gbc_rdbtnVisa.gridy = 8;
		contentPanel.add(rdbtnVisa, gbc_rdbtnVisa);

		rdbtnMastercard = new JRadioButton("MASTERCARD");
		rdbtnMastercard.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_rdbtnMastercard = new GridBagConstraints();
		gbc_rdbtnMastercard.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnMastercard.gridx = 2;
		gbc_rdbtnMastercard.gridy = 8;
		contentPanel.add(rdbtnMastercard, gbc_rdbtnMastercard);

		rdbtnPaypal = new JRadioButton("PAYPAL");
		rdbtnPaypal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_rdbtnPaypal = new GridBagConstraints();
		gbc_rdbtnPaypal.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPaypal.gridx = 3;
		gbc_rdbtnPaypal.gridy = 8;
		contentPanel.add(rdbtnPaypal, gbc_rdbtnPaypal);

		// Crear un ButtonGroup y agregar los botones
		rdGroup = new ButtonGroup();
		rdGroup.add(rdbtnVisa);
		rdGroup.add(rdbtnMastercard);
		rdGroup.add(rdbtnPaypal);
		lblNumeroCuenta = new JLabel("ACCOUNT NUMBER");
		lblNumeroCuenta.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblNumeroCuenta = new GridBagConstraints();
		gbc_lblNumeroCuenta.anchor = GridBagConstraints.EAST;
		gbc_lblNumeroCuenta.fill = GridBagConstraints.VERTICAL;
		gbc_lblNumeroCuenta.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumeroCuenta.gridx = 0;
		gbc_lblNumeroCuenta.gridy = 9;
		contentPanel.add(lblNumeroCuenta, gbc_lblNumeroCuenta);

		textNumeroCuenta = new JTextField();
		textNumeroCuenta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textNumeroCuenta.setColumns(10);
		GridBagConstraints gbc_textNumeroCuenta = new GridBagConstraints();
		gbc_textNumeroCuenta.gridwidth = 3;
		gbc_textNumeroCuenta.insets = new Insets(0, 0, 5, 5);
		gbc_textNumeroCuenta.fill = GridBagConstraints.HORIZONTAL;
		gbc_textNumeroCuenta.gridx = 1;
		gbc_textNumeroCuenta.gridy = 9;
		contentPanel.add(textNumeroCuenta, gbc_textNumeroCuenta);

		btnRegistrarse = new JButton("SIGN UP");
		btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnRegistrarse = new GridBagConstraints();
		gbc_btnRegistrarse.insets = new Insets(0, 0, 0, 5);
		gbc_btnRegistrarse.gridx = 1;
		gbc_btnRegistrarse.gridy = 10;
		contentPanel.add(btnRegistrarse, gbc_btnRegistrarse);
		btnRegistrarse.addActionListener(this);

		btnModificar = new JButton("MODIFY");
		btnModificar.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnModificar = new GridBagConstraints();
		gbc_btnModificar.insets = new Insets(0, 0, 0, 5);
		gbc_btnModificar.gridx = 2;
		gbc_btnModificar.gridy = 10;
		contentPanel.add(btnModificar, gbc_btnModificar);
		btnModificar.addActionListener(this);

		btnDrop = new JButton("DROP");
		btnDrop.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnDrop = new GridBagConstraints();
		gbc_btnDrop.insets = new Insets(0, 0, 0, 5);
		gbc_btnDrop.gridx = 3;
		gbc_btnDrop.gridy = 10;
		contentPanel.add(btnDrop, gbc_btnDrop);
		btnDrop.addActionListener(this);

		btnMostrarPedidos = new JButton("ORDERS");
		btnMostrarPedidos.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnMostrarPedidos = new GridBagConstraints();
		gbc_btnMostrarPedidos.gridx = 4;
		gbc_btnMostrarPedidos.gridy = 10;
		contentPanel.add(btnMostrarPedidos, gbc_btnMostrarPedidos);
		btnMostrarPedidos.addActionListener(this);

		label_5 = new JLabel("");
		getContentPane().add(label_5);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(btnRegistrarse)) {
			alta();
		} else if (e.getSource().equals(btnModificar)) {
			modificar(localClien);
		} else if (e.getSource().equals(btnDrop)) {
			baja(localClien);

		}
	}

	private void alta() {
		Cliente clien = new Cliente();
		clien.setId_usu(Principal.obtenerNewIdCliente());
		clien.setUsuario(textUser.getText());
		clien.setContra(new String(passwordFieldContra.getPassword()));
		clien.setCorreo(textEmail.getText());
		clien.setDireccion(textDireccion.getText());
		clien.setDni(textDni.getText());
		clien.setNum_cuenta(textNumeroCuenta.getText());

		if (rdbtnVisa.isSelected()) {
			clien.setMetodo_pago(Metodo.visa);
		} else if (rdbtnMastercard.isSelected()) {
			clien.setMetodo_pago(Metodo.mastercard);
		} else if (rdbtnPaypal.isSelected()) {
			clien.setMetodo_pago(Metodo.paypal);
		} else {
			System.out.println("No se ha seleccionado un método de pago");
		}
		try {
			Principal.altaCliente(clien);
			JOptionPane.showMessageDialog(this, "User registered successfully!");
		} catch (AltaError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispose();
	}

	private void modificar(Cliente clien) {
		clien.setUsuario(textUser.getText());
		clien.setContra(new String(passwordFieldContra.getPassword()));
		clien.setCorreo(textEmail.getText());
		clien.setDireccion(textDireccion.getText());
		clien.setDni(textDni.getText());
		clien.setNum_cuenta(textNumeroCuenta.getText());

		if (rdbtnVisa.isSelected()) {
			clien.setMetodo_pago(Metodo.visa);
		} else if (rdbtnMastercard.isSelected()) {
			clien.setMetodo_pago(Metodo.mastercard);
		} else if (rdbtnPaypal.isSelected()) {
			clien.setMetodo_pago(Metodo.paypal);
		}
		try {
			Principal.modificarCliente(clien);
			JOptionPane.showMessageDialog(this, "Modificacion exitosa", "Mensaje", DISPOSE_ON_CLOSE);
		} catch (modifyError e) {
			// TODO Auto-generated catch block
			e.visualizarMen();
		}

		dispose();
	}

	private void baja(Cliente clien) {
		try {
			Principal.bajaCliente(clien);
			JOptionPane.showMessageDialog(this, "Baja exitosa", "Mensaje", DISPOSE_ON_CLOSE);
		} catch (DropError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(this, "Baja exitosa", "Mensaje", DISPOSE_ON_CLOSE);
		dispose();
	}

}
