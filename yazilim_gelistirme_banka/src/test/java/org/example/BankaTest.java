package org.example;

import org.junit.jupiter.api.Test;

import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankaTest {
    
    @Test
    public void testBakiye(){
        Banka hesap = new Banka(100.00);
        assertEquals(100.00, hesap.getBalance());
    }

    @Test
    public void testParaYükleme(){
        Banka hesap = new Banka(100.00);
        hesap.paraYükleme(50);
        assertEquals(150, hesap.getBalance());
    }

    @Test
    public void testParaCekme(){
        Banka hesap = new Banka(100.00);
        hesap.paraCekme(50);
        assertEquals(50, hesap.getBalance());
    }

    @Test
    public void testNegatifParaYükleme(){
        Banka hesap = new Banka(100);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {hesap.paraYükleme(-50.0);});
        assertEquals("Yüklenecek miktar pozitif olmalı.", exception.getMessage());
    }

    @Test
    public void testNegatifParaÇekme(){
        Banka hesap = new Banka(100);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {hesap.paraCekme(-50);});
        assertEquals("Çekilen miktar pozitif olmalı", exception.getMessage());
    }

    @Test
    public void testYetersizBakiyeykenParaCekme(){
        Banka hesap = new Banka(100);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {hesap.paraCekme(150);});
        assertEquals("Yetersiz bakiye", exception.getMessage());
    }
}
