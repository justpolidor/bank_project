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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountBalanceResponse that = (AccountBalanceResponse) o;

        if (balance != null ? !balance.equals(that.balance) : that.balance != null) return false;
        return availableBalance != null ? availableBalance.equals(that.availableBalance) : that.availableBalance == null;
    }

    @Override
    public int hashCode() {
        int result = balance != null ? balance.hashCode() : 0;
        result = 31 * result + (availableBalance != null ? availableBalance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "balance='" + balance + '\'' +
                ", availableBalance='" + availableBalance + '\'' +
                '}';
    }
}
