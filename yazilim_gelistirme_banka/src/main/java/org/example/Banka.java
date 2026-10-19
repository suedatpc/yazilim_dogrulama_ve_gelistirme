package org.example;

public class Banka {
    private double balance;

    public Banka(double balance){
        if(balance < 0){
            throw new IllegalArgumentException("Bakiye negatif olamaz.");
        }
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void paraYükleme(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Yüklenecek miktar pozitif olmalı.");
        }
        balance += amount;
    }

    public void paraCekme(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Çekilen miktar pozitif olmalı");
        }
        if(amount > balance){
            throw new IllegalArgumentException("Yetersiz bakiye");
        }
        balance -= amount;
    }
}
