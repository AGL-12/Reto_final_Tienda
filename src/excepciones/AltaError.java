package excepciones;

import javax.swing.JOptionPane;

public class AltaError extends Exception {
	private static final long serialVersionUID = 1L;
	private String mensaje;

	public AltaError(String mensaje) {
		this.mensaje = mensaje;
	}

	public void visualizarMen() {
		JOptionPane.showMessageDialog(null, this.mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
