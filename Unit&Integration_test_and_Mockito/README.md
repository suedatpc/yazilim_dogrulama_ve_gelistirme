# Ogrenci Otomasyon - Unit ve Integration Test Ornegi

Bu proje, katmanli bir Java/Maven ornegidir ve su akislarin testini icerir:

- Ogrenci kaydi (`registerStudent`)
- Ogrenci guncelleme (`updateStudent`)
- Esnek arama (`searchStudents`)
- Akademik durum siniflandirma (`HONOR`, `GOOD`, `PROBATION`)
- Toplu istatistik hesaplama (`calculateStatistics`)

## Proje Yapisi

- `src/main/java/org/example/domain/Student.java`
- `src/main/java/org/example/domain/AcademicStanding.java`
- `src/main/java/org/example/service/StudentService.java`
- `src/main/java/org/example/service/StudentRegistrationRequest.java`
- `src/main/java/org/example/service/StudentUpdateRequest.java`
- `src/main/java/org/example/service/StudentSearchCriteria.java`
- `src/main/java/org/example/service/StudentStatistics.java`
- `src/main/java/org/example/repository/StudentRepository.java`
- `src/main/java/org/example/repository/JdbcStudentRepository.java`
- `src/main/java/org/example/Main.java`

## Test Kapsami

- Unit test: `src/test/java/org/example/service/StudentServiceTest.java`
  - Dogrulama kurallari
  - Normalizasyon (trim/lowercase)
  - Guncelleme davranisi
  - Arama filtreleri
  - Istatistik hesaplama

- Integration test:
  - `src/test/java/org/example/repository/JdbcStudentRepositoryIT.java`
  - `src/test/java/org/example/service/StudentServiceIT.java`

## Test Komutlari

Sadece unit testler:

```bash
mvn test
```

Unit + integration testler:

```bash
mvn verify
```

## Demo Calistirma

`Main` sinifi, kayit + guncelleme + arama + istatistik akisini tek yerde gosterir.

```bash
mvn -q exec:java -Dexec.mainClass="org.example.Main"
```

> Not: `exec:java` komutu icin `exec-maven-plugin` eklenebilir. IDE'den `Main` sinifini dogrudan calistirmak da yeterlidir.
