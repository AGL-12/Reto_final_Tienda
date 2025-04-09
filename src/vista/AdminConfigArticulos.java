package vista;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color; 
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent; 
import javax.swing.event.DocumentListener; 

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
    private JButton btnModify;
    private JButton btnSalir;
    private JComboBox<Seccion> comboBoxSeccion;
    private JDialog VentanaIntermedia;
    private JComboBox<Articulo> comboBoxArticulo; 
    private Map<Integer, Articulo> articulos;
    private JLabel lblNombre,lblDescripcion,lblStock,lblPrecio,lblArticulo,lblOferta,lblSeccion;
    private DocumentListener validationListener;
    private final Color COLOR_ERROR = Color.RED;
    private final Color COLOR_NORMAL = Color.BLACK;


    /**
     * Create the dialog.
     *
     * @param ventanaIntermedia
     */
    public AdminConfigArticulos(JDialog VentanaIntermedia) {
        super(VentanaIntermedia, "Bienvenido", true);
        this.VentanaIntermedia = VentanaIntermedia;
        this.setModal(true);
        setBounds(100, 100, 450, 350);
        getContentPane().setLayout(null);

        lblNombre = new JLabel("Name");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNombre.setBounds(61, 52, 46, 28);
        getContentPane().add(lblNombre);

        textNombre = new JTextField();
        textNombre.setBounds(117, 59, 183, 20);
        getContentPane().add(textNombre);
        textNombre.setColumns(10);

        lblDescripcion = new JLabel("Description");
        lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblDescripcion.setBounds(19, 84, 98, 28);
        getContentPane().add(lblDescripcion);

        textDescripcion = new JTextField();
        textDescripcion.setColumns(10);
        textDescripcion.setBounds(117, 91, 183, 20);
        getContentPane().add(textDescripcion);

        lblStock = new JLabel("Stock");
        lblStock.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblStock.setBounds(59, 120, 48, 28);
        getContentPane().add(lblStock);

        textStock = new JTextField();
        textStock.setColumns(10);
        textStock.setBounds(117, 122, 183, 20);
        getContentPane().add(textStock);

        lblPrecio = new JLabel("Price");
        lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPrecio.setBounds(59, 148, 48, 28);
        getContentPane().add(lblPrecio);

        textPrecio = new JTextField();
        textPrecio.setColumns(10);
        textPrecio.setBounds(117, 153, 183, 20);
        getContentPane().add(textPrecio);

        lblOferta = new JLabel("Offer");
        lblOferta.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblOferta.setBounds(59, 177, 48, 28);
        getContentPane().add(lblOferta);

        textOferta = new JTextField();
        textOferta.setColumns(10);
        textOferta.setBounds(117, 184, 183, 20);
        getContentPane().add(textOferta);

        lblSeccion = new JLabel("Section");
        lblSeccion.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblSeccion.setBounds(48, 213, 69, 28);
        getContentPane().add(lblSeccion);

        btnAlta = new JButton("Add");
        btnAlta.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnAlta.setBounds(329, 37, 97, 23);
        getContentPane().add(btnAlta);
        btnAlta.addActionListener(this);

        btnModify = new JButton("Modify");
        btnModify.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnModify.setBounds(329, 135, 97, 23);
        getContentPane().add(btnModify);
        btnModify.addActionListener(this);
        btnModify.setEnabled(false);

        btnSalir = new JButton("Exit");
        btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnSalir.setBounds(329, 202, 97, 23);
        getContentPane().add(btnSalir);
        btnSalir.addActionListener(this);

        comboBoxSeccion = new JComboBox<>(Seccion.values());
        comboBoxSeccion.setBounds(117, 219, 183, 22);
        getContentPane().add(comboBoxSeccion);
        comboBoxSeccion.setSelectedIndex(-1);

        lblArticulo = new JLabel("Articulo");
        lblArticulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblArticulo.setBounds(48, 20, 69, 28);
        getContentPane().add(lblArticulo);

        comboBoxArticulo = new JComboBox<>();
        comboBoxArticulo.setBounds(117, 26, 183, 22);
        añadirArticuloCombo();
        getContentPane().add(comboBoxArticulo);
        comboBoxArticulo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(comboBoxArticulo.getSelectedIndex()>0) {
            		camposEditables(false);
            	}else {
            		camposEditables(true);
            	}
                boolean isItemSelected = comboBoxArticulo.getSelectedIndex() > 0;
                btnAlta.setEnabled(!isItemSelected);
                btnModify.setEnabled(isItemSelected);
                enseñarInfo();
            }
        });
        comboBoxArticulo.setSelectedIndex(0);
         if (comboBoxArticulo.getActionListeners().length > 0) {
             comboBoxArticulo.getActionListeners()[0].actionPerformed(
                 new ActionEvent(comboBoxArticulo, ActionEvent.ACTION_PERFORMED, null)
             );
         }


        validationListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarValidacion(e);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarValidacion(e);
            }
            @Override
            public void changedUpdate(DocumentEvent e) { }

            private void actualizarValidacion(DocumentEvent e) {
                Object source = e.getDocument();
                if (source == textStock.getDocument()) {
                    validarCampo(textStock);
                } else if (source == textPrecio.getDocument()) {
                    validarCampo(textPrecio);
                } else if (source == textOferta.getDocument()) {
                    validarCampo(textOferta);
                }
            }
        };

        textStock.getDocument().addDocumentListener(validationListener);
        textPrecio.getDocument().addDocumentListener(validationListener);
        textOferta.getDocument().addDocumentListener(validationListener);

    }
    private void camposEditables(boolean activado) {
        textPrecio.setEditable(activado);
        textOferta.setEditable(activado);
        textNombre.setEditable(activado);
        textDescripcion.setEditable(activado);
        comboBoxSeccion.setEnabled(activado);
    }


     private void validarCampo(JTextField campo) {
        String texto = campo.getText().trim();
        boolean valido = true;

        if (!texto.isEmpty()) { 
            try {
                if (campo == textStock) {
                    int stock = Integer.parseInt(texto);
                    if (stock < 0) {
                        valido = false; 
                    }
                } else if (campo == textPrecio) {
                    Float.parseFloat(texto.replace(',', '.'));
                } else if (campo == textOferta) {
                    Float.parseFloat(texto.replace(',', '.'));
                                    }
            } catch (NumberFormatException nfe) {
                valido = false;
            }
             campo.setForeground(valido ? COLOR_NORMAL : COLOR_ERROR);

        } else { 
             campo.setForeground(COLOR_NORMAL);
        }
    }

    private void añadirArticuloCombo() {
        articulos = Principal.obtenerTodosArticulos();
        Object selectedItem = comboBoxArticulo.getSelectedItem();
        comboBoxArticulo.removeAllItems();
        Articulo artDefault = new Articulo(0, "Selecciona un artículo", "", 0, 0, 0, null);
        comboBoxArticulo.addItem(artDefault);
        if (articulos != null) {
            for (Articulo articulo : articulos.values()) {
                comboBoxArticulo.addItem(articulo);
            }
        }
        if (selectedItem != null && selectedItem instanceof Articulo && ((Articulo)selectedItem).getId_art() != 0) {
             comboBoxArticulo.setSelectedItem(selectedItem);
             if (comboBoxArticulo.getSelectedIndex() <= 0) {
                 comboBoxArticulo.setSelectedIndex(0);
             }
        } else {
             comboBoxArticulo.setSelectedIndex(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAlta  || e.getSource() == btnModify) {
            validarCampo(textStock);
            validarCampo(textPrecio);
            validarCampo(textOferta);

            if (comprobarCampos()) { 
                if (e.getSource() == btnAlta) {
                    añadir();
                } else if (e.getSource() == btnModify) {
                    modificar();
                }
                 añadirArticuloCombo();
                 limpiar(); 
                 comboBoxArticulo.setSelectedIndex(0);
            } else {
                 JOptionPane.showMessageDialog(this,
                        "Error: Todos los campos deben estar completos y con el formato correcto.\n" +
                        "- Stock: Número entero mayor o igual a 0.\n" +
                        "- Precio/Oferta: Número decimal.",
                        "Error en Artículos", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnSalir) {
            cerrar();
        }
    }


    private Articulo enseñarInfo() {
        Articulo art = null;
        textStock.setForeground(COLOR_NORMAL);
        textPrecio.setForeground(COLOR_NORMAL);
        textOferta.setForeground(COLOR_NORMAL);

        if (comboBoxArticulo.getSelectedIndex() > 0) {
            art = (Articulo) comboBoxArticulo.getSelectedItem();
            if (art != null) {
                textNombre.setText(art.getNombre());
                textDescripcion.setText(art.getDescripcion());
                textStock.setText(String.valueOf(art.getStock()));
                textPrecio.setText(String.valueOf(art.getPrecio()).replace('.', ','));
                textOferta.setText(String.valueOf(art.getOferta()).replace('.', ','));
                comboBoxSeccion.setSelectedItem(art.getSeccion());
                 validarCampo(textStock); 
                 validarCampo(textPrecio); 
                 validarCampo(textOferta); 
            } else {
                limpiar();
            }
        } else {
            limpiar();
        }
        return art;
    }

    private void modificar() {
        Articulo art = recopilarInformacion();
        Articulo seleccionado;
        if (art == null) return;

        try {
            seleccionado = (Articulo) comboBoxArticulo.getSelectedItem();
            if (seleccionado != null && seleccionado.getId_art() != 0) {
                 art.setId_art(seleccionado.getId_art());
                 Principal.modificarArticulo(art);
                 JOptionPane.showMessageDialog(this, "Artículo modificado correctamente.");
            } else {
                 JOptionPane.showMessageDialog(this, "Error: No se ha seleccionado un artículo válido para modificar.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (modifyError e) {
            JOptionPane.showMessageDialog(this, "Error al modificar artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }

    private void limpiar() {
        textNombre.setText("");
        textDescripcion.setText("");
        textStock.setText("");
        textPrecio.setText("");
        textOferta.setText("");
        comboBoxSeccion.setSelectedIndex(-1);
        textStock.setForeground(COLOR_NORMAL);
        textPrecio.setForeground(COLOR_NORMAL);
        textOferta.setForeground(COLOR_NORMAL);
    }

    private void cerrar() {
        dispose();
    }

    private void añadir() {
        Articulo art = recopilarInformacion(); 
        if (art == null) return;
        try {
             art.setId_art(0); 
             Principal.añadirArticulo(art);
             JOptionPane.showMessageDialog(this, "Artículo añadido correctamente.");
        } catch (modifyError e) {
            JOptionPane.showMessageDialog(this, "Error al añadir artículo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } 
    }


    private Articulo recopilarInformacion() {
    	Articulo art = new Articulo(); 
    	if (!comprobarCampos()){ 
              return null; 
         }
        try {
            art.setNombre(textNombre.getText().trim());
            art.setDescripcion(textDescripcion.getText().trim());
            art.setStock(Integer.parseInt(textStock.getText().trim()));
            art.setPrecio(Float.parseFloat(textPrecio.getText().trim().replace(',', '.')));
            art.setOferta(Float.parseFloat(textOferta.getText().trim().replace(',', '.')));
            art.setSeccion((Seccion) comboBoxSeccion.getSelectedItem());
            return art;
        } catch (NumberFormatException e) {
             return null;
        } catch (Exception e){
             return null;
        }

    }


    private boolean comprobarCampos() {
        boolean verificador = true; 
        int stock;

        if (textNombre.getText().trim().isEmpty() ||
            textDescripcion.getText().trim().isEmpty() ||
            textStock.getText().trim().isEmpty() ||
            textPrecio.getText().trim().isEmpty() ||
            textOferta.getText().trim().isEmpty()) {
            verificador = false; 
        }

        if (comboBoxSeccion.getSelectedIndex() == -1) {
            verificador = false; 
        }

        if (textStock.getForeground() == COLOR_ERROR ||
            textPrecio.getForeground() == COLOR_ERROR ||
            textOferta.getForeground() == COLOR_ERROR) {
            verificador = false;
        }

        if (verificador) { 
             try {
                 stock = Integer.parseInt(textStock.getText().trim());
                 if (stock < 0) {
                     verificador = false;
                     textStock.setForeground(COLOR_ERROR);
                 }
                 Float.parseFloat(textPrecio.getText().trim().replace(',', '.'));
                 Float.parseFloat(textOferta.getText().trim().replace(',', '.'));
             } catch (NumberFormatException e) {
                 verificador = false;
             }
        }
        return verificador; 
    }
}