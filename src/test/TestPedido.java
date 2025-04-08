package test;

import modelo.Pedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestPedido {

    @Test
    public void testConstructorConParametros() {
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 8, 12, 30);
        Pedido pedido = new Pedido(1, 10, 99.99f, fecha);

        assertEquals(1, pedido.getId_ped());
        assertEquals(10, pedido.getId_usu());
        assertEquals(99.99f, pedido.getTotal());
        assertEquals(fecha, pedido.getFecha_compra());
    }

    @Test
    public void testConstructorVacio() {
        Pedido pedido = new Pedido();

        assertEquals(0, pedido.getId_ped());
        assertEquals(0, pedido.getId_usu());
        assertEquals(0.0f, pedido.getTotal());
        assertNull(pedido.getFecha_compra());
    }

    @Test
    public void testSettersYGetters() {
        Pedido pedido = new Pedido();
        LocalDateTime nuevaFecha = LocalDateTime.now();

        pedido.setId_ped(5);
        pedido.setId_usu(20);
        pedido.setTotal(150.75f);
        pedido.setFecha_compra(nuevaFecha);

        assertEquals(5, pedido.getId_ped());
        assertEquals(20, pedido.getId_usu());
        assertEquals(150.75f, pedido.getTotal());
        assertEquals(nuevaFecha, pedido.getFecha_compra());
    }

    @Test
    public void testToString() {
        LocalDateTime fecha = LocalDateTime.of(2025, 1, 1, 10, 0);
        Pedido pedido = new Pedido(2, 99, 200.50f, fecha);
        String esperado = "Pedido [id_ped=2, id_usu=99, total=200.5, fecha_compra=" + fecha + "]";
        assertEquals(esperado, pedido.toString());
    }
}
