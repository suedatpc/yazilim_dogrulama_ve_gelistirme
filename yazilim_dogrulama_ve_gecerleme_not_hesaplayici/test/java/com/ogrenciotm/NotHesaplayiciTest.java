package com.ogrenciotm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotHesaplayiciTest {

    private final NotHesaplayici hesaplayici = new NotHesaplayici();

    @Test
    @DisplayName("Negatif not girilirse hata firlatilir")
    void negatifNottaHataFirlatir() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> hesaplayici.agirlikliOrtalama(-5, 80));
        assertTrue(ex.getMessage().contains("0 ile 100"));
    }

    @Test
    @DisplayName("100 ustu not girilirse hata firlatilir")
    void yuzUstuNottaHataFirlatir() {
        assertThrows(IllegalArgumentException.class, () -> hesaplayici.agirlikliOrtalama(50, 101));
    }

    @Test
    @DisplayName("Agirlikli ortalama dogru hesaplanir")
    void agirlikliOrtalamaDogruHesaplanir() {
        double sonuc = hesaplayici.agirlikliOrtalama(70, 80);
        assertEquals(76.0, sonuc, 0.0001);
    }

    @Test
    @DisplayName("Sinir degerinde gecis saglanir")
    void sinirDegerindeGecer() {
        assertTrue(hesaplayici.gectiMi(60.0));
    }


}

