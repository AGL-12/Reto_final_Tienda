package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;

public class VentanaRegistro extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblUsuario;
	private JTextField textUsuario;
	private JTextField textDNI;
	private JTextField textDireccion;
	private JTextField textCorreo;
	private JPasswordField passwordField;
	private JTextField textCuentaBancaria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VentanaRegistro dialog = new VentanaRegistro();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public VentanaRegistro() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsuario.setBounds(52, 40, 59, 22);
		contentPanel.add(lblUsuario);
		
		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblContraseña.setBounds(23, 65, 88, 22);
		contentPanel.add(lblContraseña);
		
		JLabel lblDNI = new JLabel("DNI");
		lblDNI.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDNI.setBounds(80, 96, 31, 22);
		contentPanel.add(lblDNI);
		
		JLabel lblDireccion_1 = new JLabel("Direccion");
		lblDireccion_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDireccion_1.setBounds(40, 129, 71, 22);
		contentPanel.add(lblDireccion_1);
		
		JLabel lblCorreo = new JLabel("Correo");
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCorreo.setBounds(59, 159, 52, 22);
		contentPanel.add(lblCorreo);
		
		textUsuario = new JTextField();
		textUsuario.setBounds(121, 44, 116, 20);
		contentPanel.add(textUsuario);
		textUsuario.setColumns(10);
		
		textDNI = new JTextField();
		textDNI.setColumns(10);
		textDNI.setBounds(121, 100, 116, 20);
		contentPanel.add(textDNI);
		
		textDireccion = new JTextField();
		textDireccion.setColumns(10);
		textDireccion.setBounds(121, 133, 116, 20);
		contentPanel.add(textDireccion);
		
		textCorreo = new JTextField();
		textCorreo.setColumns(10);
		textCorreo.setBounds(121, 163, 116, 20);
		contentPanel.add(textCorreo);
		
		JLabel lblMetodoDePago = new JLabel("Método de pago");
		lblMetodoDePago.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMetodoDePago.setBounds(10, 192, 137, 22);
		contentPanel.add(lblMetodoDePago);
		
		JRadioButton rdbtnVisa = new JRadioButton("Visa");
		rdbtnVisa.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rdbtnVisa.setBounds(153, 195, 65, 23);
		contentPanel.add(rdbtnVisa);
		
		JRadioButton rdbtnMastercard = new JRadioButton("Mastercard");
		rdbtnMastercard.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rdbtnMastercard.setBounds(230, 195, 116, 23);
		contentPanel.add(rdbtnMastercard);
		
		JRadioButton rdbtnPaypal = new JRadioButton("Paypal");
		rdbtnPaypal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rdbtnPaypal.setBounds(353, 195, 81, 23);
		contentPanel.add(rdbtnPaypal);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(121, 69, 116, 20);
		contentPanel.add(passwordField);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnEnviar.setBounds(317, 43, 107, 23);
		contentPanel.add(btnEnviar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnCancelar.setBounds(317, 81, 107, 23);
		contentPanel.add(btnCancelar);
		
		JLabel lblCuentaBancaria = new JLabel("Cuenta Bancaria");
		lblCuentaBancaria.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCuentaBancaria.setBounds(10, 225, 127, 22);
		contentPanel.add(lblCuentaBancaria);
		
		textCuentaBancaria = new JTextField();
		textCuentaBancaria.setColumns(10);
		textCuentaBancaria.setBounds(147, 229, 116, 20);
		contentPanel.add(textCuentaBancaria);
	}
}
