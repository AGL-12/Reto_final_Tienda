package test;

import modelo.Compra;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCompra {

    @Test
    public void testConstructorConParametros() {
        Compra compra = new Compra(1, 100, 3);

        assertEquals(1, compra.getId_art());
        assertEquals(100, compra.getId_ped());
        assertEquals(3, compra.getCantidad());
    }

    @Test
    public void testSettersYGetters() {
        Compra compra = new Compra(0, 0, 0);

        compra.setId_art(5);
        compra.setId_ped(200);
        compra.setCantidad(10);

        assertEquals(5, compra.getId_art());
        assertEquals(200, compra.getId_ped());
        assertEquals(10, compra.getCantidad());
    }

    @Test
    public void testToString() {
        Compra compra = new Compra(2, 150, 4);
        String esperado = "Compra [id_art=2, id_ped=150, cantidad=4]";
        assertEquals(esperado, compra.toString());
    }
}
