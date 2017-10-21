package it.justin.apiwrapper.dto.accountBalance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceResponse {

    private String balance;
    private String availableBalance;

    public AccountBalanceResponse() {
    }

    public AccountBalanceResponse(String balance, String availableBalance) {
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
        return "Payload{" +
                "balance='" + balance + '\'' +
                ", availableBalance='" + availableBalance + '\'' +
                '}';
    }
}
