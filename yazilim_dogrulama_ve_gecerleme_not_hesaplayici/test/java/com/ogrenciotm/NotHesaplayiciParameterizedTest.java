package com.ogrenciotm;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotHesaplayiciParameterizedTest {

    private static final String EXCEL_YOLU = "test-data/Book1.xlsx";
    private static final NotHesaplayici hesaplayici = new NotHesaplayici();

    @ParameterizedTest(name = "{index} => ortalama = {0}, beklenen = {1}")
    @CsvSource({
            "95.0, AA",
            "87.0, BA",
            "82.0, BB",
            "75.0, CB",
            "61.0, CC",
            "50.0, DC",
            "49.9, FF"
    })
    @DisplayName("CsvSource ile agirlikli not hesaplama testi")
    public void testCsvSourceileOrtalamaHesaplama(double ortalama, String beklenenHarfNotu){
        assertEquals(beklenenHarfNotu, hesaplayici.harfNotu(ortalama));
    }

    @ParameterizedTest(name = "{index} => ortalama={0}, beklenen={1}")
    @MethodSource("harfNotuExcelVerileri")
    @DisplayName("Harf notu Excel dosyasindan okunan satirlarla test edilir")
    public void testExcelVerileriyleOrtalamaHesaplama(double ortalama, String beklenenHarfNotu){
        assertEquals(beklenenHarfNotu, hesaplayici.harfNotu(ortalama));
    }

    @ParameterizedTest(name = "{index} => vize = {0}, fin = {1}, beklenen = {2}")
    @MethodSource("ortalamaVerileri")
    @DisplayName("Ortalama birden fazla farklı senaryoda doğru hesaplanır")
    public void testOrtalamaMethodSourceIle(int vize, int fin, double beklenenOrt){
        assertEquals(beklenenOrt, hesaplayici.agirlikliOrtalama(vize, fin), 0.0001);
    }

    @ParameterizedTest(name = "{index} => ortalama={0}, gecerMi={1}")
    @CsvSource({
            "59.9, false",
            "60.0, true",
            "100.0, true"
    })
    @DisplayName("Gecme durumu sinir degerler ile test edilir")
    public void testCsvSourceIleSinirDegerlerGecme(double ortalama, boolean beklenen){
        assertEquals(beklenen, hesaplayici.gectiMi(ortalama));
         /* another way
         if (beklenen) {
            assertTrue(hesaplayici.gectiMi(ortalama));
        } else {
            assertFalse(hesaplayici.gectiMi(ortalama));
        }
          */
    }


    static Stream<Arguments> ortalamaVerileri() {
        return Stream.of(
                Arguments.of(100, 100, 100.0),
                Arguments.of(50, 50, 50.0),
                Arguments.of(40, 90, 70.0),
                Arguments.of(0, 100, 60.0)
        );
    }

    static Stream<Arguments> harfNotuExcelVerileri() {
        try (InputStream in = NotHesaplayiciParameterizedTest.class.getClassLoader().getResourceAsStream(EXCEL_YOLU)) {
            if (in == null) {
                throw new IllegalStateException("Excel dosyasi bulunamadi: " + EXCEL_YOLU);
            }

            try (Workbook workbook = WorkbookFactory.create(in)) {
                Sheet sheet = workbook.getSheetAt(0);
                DataFormatter formatter = new DataFormatter();
                List<Arguments> args = new ArrayList<>();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null || row.getCell(0) == null || row.getCell(1) == null) {
                        continue;
                    }

                    double ortalama = hucreyiDoubleYap(row.getCell(0), formatter);
                    String beklenenHarf = formatter.formatCellValue(row.getCell(1)).trim();
                    if (!beklenenHarf.isEmpty()) {
                        args.add(Arguments.of(ortalama, beklenenHarf));
                    }
                }

                if (args.isEmpty()) {
                    throw new IllegalStateException("Excel dosyasinda veri yok: " + EXCEL_YOLU);
                }
                return args.stream();
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel verileri okunamadi", e);
        }
    }

    private static double hucreyiDoubleYap(Cell hucre, DataFormatter formatter) {
        if (hucre.getCellType() == CellType.NUMERIC) {
            return hucre.getNumericCellValue();
        }
        return Double.parseDouble(formatter.formatCellValue(hucre));
    }

}
