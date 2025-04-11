package test;

import modelo.Cliente;
import modelo.Compra;
import modelo.Metodo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestCliente {

    private Cliente cliente;
    private Metodo metodoPago;
    private Map<Integer, Compra> listaCompra;

    @BeforeEach
    public void setUp() {
        listaCompra = new HashMap<>();
        listaCompra.put(1, new Compra(0,0,0)); 

        cliente = new Cliente(
                101,
                "usuarioTest",
                "pass123",
                "12345678X",
                "test@mail.com",
                "Calle Falsa 123",
                metodoPago,
                "ES1234567890",
                true,
                listaCompra
        );
    }

    @Test
    public void testConstructorConParametros() {
        assertEquals(101, cliente.getId_usu());
        assertEquals("usuarioTest", cliente.getUsuario());
        assertEquals("pass123", cliente.getContra());
        assertEquals("12345678X", cliente.getDni());
        assertEquals("test@mail.com", cliente.getCorreo());
        assertEquals("Calle Falsa 123", cliente.getDireccion());
        assertEquals(metodoPago, cliente.getMetodo_pago());
        assertEquals("ES1234567890", cliente.getNum_cuenta());
        assertTrue(cliente.isEsAdmin());
        assertEquals(listaCompra, cliente.getListaCompra());
    }

    @Test
    public void testConstructorVacio() {
        Cliente c = new Cliente();
        assertEquals(0, c.getId_usu());
        assertNull(c.getUsuario());
        assertNull(c.getContra());
        assertNull(c.getDni());
        assertNull(c.getCorreo());
        assertNull(c.getDireccion());
        assertNull(c.getMetodo_pago());
        assertNull(c.getNum_cuenta());
        assertFalse(c.isEsAdmin());
        assertNotNull(c.getListaCompra());
        assertTrue(c.getListaCompra().isEmpty());
    }

    @Test
    public void testSettersYGetters() {

        Map<Integer, Compra> nuevasCompras = new HashMap<>();
        nuevasCompras.put(2, new Compra(1,1,1));

        cliente.setId_usu(202);
        cliente.setUsuario("nuevoUsuario");
        cliente.setContra("nuevaPass");
        cliente.setDni("87654321Z");
        cliente.setCorreo("nuevo@mail.com");
        cliente.setDireccion("Otra calle 456");
        cliente.setMetodo_pago(Metodo.visa);
        cliente.setNum_cuenta("ES0987654321");
        cliente.setEsAdmin(false);
        cliente.setListaPedido(nuevasCompras);

        assertEquals(202, cliente.getId_usu());
        assertEquals("nuevoUsuario", cliente.getUsuario());
        assertEquals("nuevaPass", cliente.getContra());
        assertEquals("87654321Z", cliente.getDni());
        assertEquals("nuevo@mail.com", cliente.getCorreo());
        assertEquals("Otra calle 456", cliente.getDireccion());
        assertEquals(Metodo.visa, cliente.getMetodo_pago());
        assertEquals("ES0987654321", cliente.getNum_cuenta());
        assertFalse(cliente.isEsAdmin());
        assertEquals(nuevasCompras, cliente.getListaCompra());
    }

    @Test
    public void testToString() {
        String resultado = cliente.toString();
        assertTrue(resultado.contains("usuarioTest"));
        assertTrue(resultado.contains("pass123"));
        assertTrue(resultado.contains("12345678X"));
        assertTrue(resultado.contains("test@mail.com"));
    }
}
