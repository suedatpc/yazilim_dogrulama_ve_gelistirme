package com.ogrenciotm;

public class NotHesaplayici {

    private void dogrulaNotAraligi(int not) {
        if (not < 0 || not > 100) {
            throw new IllegalArgumentException("Not 0 ile 100 arasinda olmalidir: " + not);
        }
    }

    public double agirlikliOrtalama(int vize, int fin) {
        dogrulaNotAraligi(vize);
        dogrulaNotAraligi(fin);
        return (vize * 0.4) + (fin * 0.6);
    }

    public String harfNotu(double ortalama) {
        if (ortalama >= 90) {
            return "AA";
        }
        if (ortalama >= 85) {
            return "BA";
        }
        if (ortalama >= 80) {
            return "BB";
        }
        if (ortalama >= 70) {
            return "CB";
        }
        if (ortalama >= 60) {
            return "CC";
        }
        if (ortalama >= 50) {
            return "DC";
        }
        return "FF";
    }

    public boolean gectiMi(double ortalama) {
        return ortalama >= 50;
    }


}

