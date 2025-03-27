package vista;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import controlador.Principal;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Seccion;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;

public class AdminConfigArticulos extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField textNombre;
	private JTextField textDescripcion;
	private JTextField textStock;
	private JTextField textPrecio;
	private JTextField textOferta;
	private JButton btnAlta;
	private JButton btnBaja;
	private JButton btnModify;
	private JButton btnSalir;
	private JComboBox comboBoxSeccion;

	/**
	 * Create the dialog.
	 */
	public AdminConfigArticulos() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		JLabel lblNombre = new JLabel("Name");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNombre.setBounds(59, 31, 46, 28);
		getContentPane().add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(117, 38, 161, 20);
		getContentPane().add(textNombre);
		textNombre.setColumns(10);

		JLabel lblDescripcion = new JLabel("Description");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescripcion.setBounds(17, 62, 88, 28);
		getContentPane().add(lblDescripcion);

		textDescripcion = new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setBounds(117, 69, 161, 20);
		getContentPane().add(textDescripcion);

		textStock = new JTextField();
		textStock.setColumns(10);
		textStock.setBounds(117, 103, 161, 20);
		getContentPane().add(textStock);

		textPrecio = new JTextField();
		textPrecio.setColumns(10);
		textPrecio.setBounds(117, 136, 161, 20);
		getContentPane().add(textPrecio);

		textOferta = new JTextField();
		textOferta.setColumns(10);
		textOferta.setBounds(117, 167, 161, 20);
		getContentPane().add(textOferta);

		JLabel lblStock = new JLabel("Stock");
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStock.setBounds(59, 95, 46, 28);
		getContentPane().add(lblStock);

		JLabel lblPrecio = new JLabel("Price");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPrecio.setBounds(57, 128, 48, 28);
		getContentPane().add(lblPrecio);

		JLabel lblOferta = new JLabel("Offer");
		lblOferta.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOferta.setBounds(57, 159, 48, 28);
		getContentPane().add(lblOferta);

		JLabel lblSeccion = new JLabel("Section");
		lblSeccion.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSeccion.setBounds(48, 195, 57, 28);
		getContentPane().add(lblSeccion);

		btnAlta = new JButton("Add");
		btnAlta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAlta.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAlta.setBounds(329, 37, 97, 23);
		getContentPane().add(btnAlta);
		btnAlta.addActionListener(this);

		btnBaja = new JButton("Delete");
		btnBaja.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnBaja.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnBaja.setBounds(329, 84, 97, 23);
		getContentPane().add(btnBaja);
		btnBaja.addActionListener(this);

		btnModify = new JButton("Modify");
		btnModify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnModify.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnModify.setBounds(329, 135, 97, 23);
		getContentPane().add(btnModify);
		btnModify.addActionListener(this);

		btnSalir = new JButton("Exit");
		btnSalir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnSalir.setBounds(329, 202, 97, 23);
		getContentPane().add(btnSalir);

		comboBoxSeccion = new JComboBox<>(Seccion.values());
		comboBoxSeccion.setBounds(117, 201, 161, 22);
		getContentPane().add(comboBoxSeccion);
		comboBoxSeccion.setSelectedIndex(-1);
		btnSalir.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAlta)) {
			añadir();
		}
		if (e.getSource().equals(btnBaja)) {
			eliminar();
		}
		if (e.getSource().equals(btnSalir)) {
			cerrar();
		}
		if (e.getSource().equals(btnModify)) {
			modificar();
		}
	}

	private void modificar() {
		Articulo art = recopilarInformacion();
		try {
			Principal.modificarArticulo(art);
		} catch (modifyError e) {
			e.printStackTrace();
		} finally {
			limpiar();
		}
	}

	private void eliminar() {
		Articulo art = recopilarInformacion();
		try {
			Principal.eliminarArticulo(art);
		} catch (modifyError e) {
			e.printStackTrace();
		} finally {
			limpiar();
		}
	}

	private void limpiar() {
		textNombre.setText("");
		;
		textDescripcion.setText("");
		;
		textStock.setText("");
		textPrecio.setText("");
		textOferta.setText("");
		comboBoxSeccion.setSelectedIndex(-1);

	}

	private void cerrar() {
		dispose();
	}

	private void añadir() {
		Articulo art = recopilarInformacion();
		try {
			Principal.añadirArticulo(art);
		} catch (modifyError e) {
			e.printStackTrace();
		} finally {
			limpiar();
		}
	}

	private Articulo recopilarInformacion() {
		String precioSin = textPrecio.getText();
		String stockSin = textStock.getText();
		String oferta = textOferta.getText();
		int posicion = comboBoxSeccion.getSelectedIndex();
		Articulo art = new Articulo();

		art.setNombre(textNombre.getText());
		art.setDescripcion(textDescripcion.getText());
		art.setStock(Integer.valueOf(stockSin));
		art.setOferta(Float.valueOf(oferta));
		art.setPrecio(Float.valueOf(precioSin));

		if (posicion == 0) {
			art.setSeccion(Seccion.pintura);
		} else if (posicion == 1) {
			art.setSeccion(Seccion.jardineria);
		} else if (posicion == 2) {
			art.setSeccion(Seccion.fontaneria);
		} else if (posicion == 3) {
			art.setSeccion(Seccion.soldadura);
		} else if (posicion == 4) {
			art.setSeccion(Seccion.carpinteria);
		}
		return art;

	}
}
