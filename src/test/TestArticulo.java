package test;

import modelo.Articulo;
import modelo.Seccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestArticulo {

    private Articulo articulo;
    private Seccion seccion;

    @BeforeEach
    public void setUp() {
        articulo = new Articulo(1, "Producto", "Descripción", 10, 100.0f, 20.0f, seccion);
    }

    @Test
    public void testConstructorConParametros() {
        assertEquals(1, articulo.getId_art());
        assertEquals("Producto", articulo.getNombre());
        assertEquals("Descripción", articulo.getDescripcion());
        assertEquals(10, articulo.getStock());
        assertEquals(100.0f, articulo.getPrecio());
        assertEquals(20.0f, articulo.getOferta());
        assertEquals(seccion, articulo.getSeccion());
    }
    @Test
    public void testConstructorVacio() {
    	Articulo art= new Articulo();
        assertEquals(0,art.getId_art());
        assertNull(art.getNombre());
        assertNull(art.getDescripcion());
        assertEquals(0,art.getStock());
        assertEquals(0,art.getPrecio());
        assertEquals(0,art.getOferta());
        assertNull(art.getSeccion());
    }

    @Test
    public void testSettersYGetters() {
        articulo.setId_art(2);
        articulo.setNombre("Nuevo");
        articulo.setDescripcion("Nueva descripción");
        articulo.setStock(5);
        articulo.setPrecio(50.0f);
        articulo.setOferta(10.0f);
        articulo.setSeccion(Seccion.fontaneria);

        assertEquals(2, articulo.getId_art());
        assertEquals("Nuevo", articulo.getNombre());
        assertEquals("Nueva descripción", articulo.getDescripcion());
        assertEquals(5, articulo.getStock());
        assertEquals(50.0f, articulo.getPrecio());
        assertEquals(10.0f, articulo.getOferta());
        assertEquals("fontaneria",articulo.getSeccion().toString());
    }

    @Test
    public void testToString() {
        assertEquals("1-Producto", articulo.toString());
    }
}

