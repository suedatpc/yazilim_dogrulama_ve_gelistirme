package com.ogrenciotm;

public class Main {
    public static void main(String[] args) {
        NotHesaplayici hesaplayici = new NotHesaplayici();

        int vize = 70;
        int fin = 80;

        double ortalama = hesaplayici.agirlikliOrtalama(vize, fin);
        String harfNotu = hesaplayici.harfNotu(ortalama);
        boolean gecti = hesaplayici.gectiMi(ortalama);

        System.out.println("Vize: " + vize + ", Final: " + fin);
        System.out.println("Ortalama: " + ortalama);
        System.out.println("Harf Notu: " + harfNotu);
        System.out.println("Durum: " + (gecti ? "Gecti" : "Kaldi"));
    }
}

