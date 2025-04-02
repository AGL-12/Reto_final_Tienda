package vista;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import controlador.Principal;
import excepciones.modifyError;
import modelo.Articulo;
import modelo.Cliente;
import modelo.Seccion;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

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
	private JDialog VentanaIntermedia;
	private JComboBox comboBoxArticulo;
	private Map<Integer, Articulo> articulos=Principal.obtenerTodosArticulos();;

	/**
	 * Create the dialog.
	 * @param ventanaIntermedia 
	 */
	public AdminConfigArticulos(JDialog VentanaIntermedia) {
		super(VentanaIntermedia, "Bienvendido", true);
		this.setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		JLabel lblNombre = new JLabel("Name");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNombre.setBounds(61, 52, 46, 28);
		getContentPane().add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(117, 59, 183, 20);
		getContentPane().add(textNombre);
		textNombre.setColumns(10);

		JLabel lblDescripcion = new JLabel("Description");
		lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescripcion.setBounds(19, 84, 88, 28);
		getContentPane().add(lblDescripcion);

		textDescripcion = new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setBounds(117, 91, 183, 20);
		getContentPane().add(textDescripcion);

		textStock = new JTextField();
		textStock.setColumns(10);
		textStock.setBounds(117, 122, 183, 20);
		getContentPane().add(textStock);

		textPrecio = new JTextField();
		textPrecio.setColumns(10);
		textPrecio.setBounds(117, 153, 183, 20);
		getContentPane().add(textPrecio);

		textOferta = new JTextField();
		textOferta.setColumns(10);
		textOferta.setBounds(115, 184, 185, 20);
		getContentPane().add(textOferta);

		JLabel lblStock = new JLabel("Stock");
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStock.setBounds(59, 120, 46, 28);
		getContentPane().add(lblStock);

		JLabel lblPrecio = new JLabel("Price");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPrecio.setBounds(59, 148, 48, 28);
		getContentPane().add(lblPrecio);

		JLabel lblOferta = new JLabel("Offer");
		lblOferta.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOferta.setBounds(59, 177, 48, 28);
		getContentPane().add(lblOferta);

		JLabel lblSeccion = new JLabel("Section");
		lblSeccion.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSeccion.setBounds(48, 213, 57, 28);
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
		btnBaja.setEnabled(false);

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
		btnModify.setEnabled(false);
		
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
		comboBoxSeccion.setBounds(117, 219, 183, 22);
		getContentPane().add(comboBoxSeccion);
		comboBoxSeccion.setSelectedIndex(-1);
		
		comboBoxArticulo = new JComboBox();
		comboBoxArticulo.setBounds(117, 26, 183, 22);
		añadirArticuloCombo();
		getContentPane().add(comboBoxArticulo);
		comboBoxArticulo.setSelectedIndex(0);
        comboBoxArticulo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(comboBoxArticulo.getSelectedIndex()!=0) {
        			btnAlta.setEnabled(false);
            	}else {
        			btnAlta.setEnabled(true);

            	}
    			enseñarInfo();
            }
        });
		
		JLabel lblArticulo = new JLabel("Articulo");
		lblArticulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblArticulo.setBounds(48, 20, 59, 28);
		getContentPane().add(lblArticulo);
		btnSalir.addActionListener(this);

	}

	private void añadirArticuloCombo() {
		articulos= Principal.obtenerTodosArticulos();
		Articulo artDefault= new Articulo(0,"Selecciona un artículo","",0,0,0,null);
		comboBoxArticulo.removeAllItems();
        comboBoxArticulo.addItem(artDefault);
        for (Articulo articulo : articulos.values()) {
            comboBoxArticulo.addItem(articulo);
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnAlta)) {
			añadir();
			añadirArticuloCombo();
		}
		if (e.getSource().equals(btnBaja)) {
			eliminar();
			añadirArticuloCombo();
		}
		if (e.getSource().equals(btnSalir)) {
			cerrar();
		}
		if (e.getSource().equals(btnModify)) {
			modificar();
			añadirArticuloCombo();
		}
	}

	private Articulo enseñarInfo() {
		Articulo art=null;
		if(comboBoxArticulo.getSelectedIndex()>0) {
			btnBaja.setEnabled(true);
			btnModify.setEnabled(true);
			art=(Articulo) (comboBoxArticulo.getSelectedItem());
			textNombre.setText(art.getNombre());
			textDescripcion.setText(art.getDescripcion());
			textStock.setText(String.valueOf(art.getStock())); 
			textPrecio.setText(String.valueOf(art.getPrecio())); 
			textOferta.setText(String.valueOf(art.getOferta()));
			
			if (art.getSeccion()==Seccion.pintura) {
				comboBoxSeccion.setSelectedIndex(0);
			} else if (art.getSeccion()==Seccion.jardineria) {
				comboBoxSeccion.setSelectedIndex(1);
			} else if (art.getSeccion()==Seccion.fontaneria) {
				comboBoxSeccion.setSelectedIndex(2);
			} else if (art.getSeccion()==Seccion.soldadura) {
				comboBoxSeccion.setSelectedIndex(3);
			} else if (art.getSeccion()==Seccion.carpinteria) {
				comboBoxSeccion.setSelectedIndex(4);
			}
		}else {
			btnBaja.setEnabled(false);
			btnModify.setEnabled(false);
			limpiar();
		}
		return art;
	}

	private void modificar() {
		Articulo art = recopilarInformacion();
		try {
			Principal.modificarArticulo(art);
		} catch (modifyError e) {
			e.printStackTrace();
		} finally {
			limpiar();
			comboBoxArticulo.setSelectedIndex(0);
		}
	}

	private void eliminar() {
		Articulo art= enseñarInfo();
		try {
			if(art!=null) {
				Principal.eliminarArticulo(art);
			}else {
				JOptionPane.showMessageDialog(null, "El articulo seleccionado no ha sido eliminado", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (modifyError e) {
			e.printStackTrace();
		} finally {
			limpiar();
			comboBoxArticulo.setSelectedIndex(0);

		}
	}

	private void limpiar() {
		textNombre.setText("");
		textDescripcion.setText("");
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
