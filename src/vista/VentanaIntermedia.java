package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaIntermedia extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public VentanaIntermedia() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nombre Usuario");
		lblNewLabel.setBounds(330, 11, 98, 24);
		getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Listar clientes");
		btnNewButton.setBounds(37, 93, 140, 50);
		getContentPane().add(btnNewButton);
		
		JButton btnConfigArticulos = new JButton("Config Articulos");
		btnConfigArticulos.setBounds(253, 93, 140, 50);
		getContentPane().add(btnConfigArticulos);
	}
}
