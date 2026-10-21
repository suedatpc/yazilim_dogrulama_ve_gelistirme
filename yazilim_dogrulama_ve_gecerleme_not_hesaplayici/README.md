# Ogrenci Otomasyon - JUnit Ornekleri

Bu proje, JUnit 5 ile klasik unit test ve parameterized test orneklerini gosterir.

## Icerik

- `src/main/java/com/ogrenciotm/NotHesaplayici.java`
  - Agirlikli ortalama hesaplama
  - Harf notu belirleme
  - Gecme durumunu kontrol etme
- `test/java/com/ogrenciotm/NotHesaplayiciTest.java`
  - Temel unit testler (`@Test`)
  - Hata firlatma testleri (`assertThrows`)
- `test/java/com/ogrenciotm/NotHesaplayiciParameterizedTest.java`
  - `@CsvSource` ile coklu veri seti
  - `@MethodSource` ile dinamik test verisi
  - Excel dosyasindan (`.xlsx`) veri okuma ornegi
- `test/resources/test-data/not_harf_notlari.xlsx`
  - Excel tabanli parameterized test verisi

## Excel ornek format

Ilk satir baslik olmali:

- A kolonu: `ortalama`
- B kolonu: `beklenenHarf`

## Calistirma

```bash
mvn test
```

## Uygulama Calistirma

```bash
mvn -q exec:java -Dexec.mainClass="com.ogrenciotm.Main"
```
