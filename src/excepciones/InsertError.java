package excepciones;

import javax.swing.JOptionPane;

public class InsertError extends Exception {
	private static final long serialVersionUID = 1L;
	private String mensaje;

	public InsertError(String s) {
		this.mensaje = s;
	}

	public void visualizarMen() {
		JOptionPane.showMessageDialog(null, this.mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
