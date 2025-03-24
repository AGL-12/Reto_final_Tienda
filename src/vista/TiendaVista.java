package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JComboBox;

public class TiendaVista extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TiendaVista dialog = new TiendaVista();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TiendaVista() {
		setBounds(100, 100, 542, 400);
		getContentPane().setLayout(null);
		
		table = new JTable();
		table.setBounds(10, 106, 510, 248);
		getContentPane().add(table);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(431, 11, 89, 23);
		getContentPane().add(btnNewButton);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(21, 11, 145, 22);
		getContentPane().add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(246, 11, 145, 22);
		getContentPane().add(comboBox_1);
	}
}
