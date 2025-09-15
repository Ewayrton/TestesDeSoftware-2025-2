package com.example;

public class DigitalWallet {
    private final String owner;
    private double balance;
    private boolean verified;
    private boolean locked;

    public DigitalWallet(String owner, double initialBalance) {
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("Owner required");
        if (initialBalance < 0) throw new IllegalArgumentException("Negative initial balance");
        this.owner = owner;
        this.balance = initialBalance;
        this.verified = false;
        this.locked = false;
    }

    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public boolean isVerified() { return verified; }
    public boolean isLocked() { return locked; }

    public void verify() { this.verified = true; }
    public void lock() { this.locked = true; }
    public void unlock() { this.locked = false; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        balance += amount;
    }

    public boolean pay(double amount) {
        ensureActiveAndVerified();
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public void refund(double amount) {
        ensureActiveAndVerified();
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        balance += amount;
    }

    private void ensureActiveAndVerified() {
        if (!verified) throw new IllegalStateException("Wallet not verified");
        if (locked) throw new IllegalStateException("Wallet is locked");
    }
}
