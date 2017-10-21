package it.justin.model;

public class AccountBalance {

    private String balance;
    private String availableBalance;

    public AccountBalance() {
    }

    public AccountBalance(String balance, String availableBalance) {
        this.balance = balance;
        this.availableBalance = availableBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "balance='" + balance + '\'' +
                ", availableBalance='" + availableBalance + '\'' +
                '}';
    }
}
