package it.justin.model;

public class MoneyTransfer {

    private String payerIban;
    private String beneficiaryIban;
    private String beneficiary;
    private String value;
    private String reason;
    private String beneficiaryDate;

    public MoneyTransfer() {
    }

    public MoneyTransfer(String payerIban, String beneficiaryIban, String beneficiary, String value, String reason, String beneficiaryDate) {
        this.payerIban = payerIban;
        this.beneficiaryIban = beneficiaryIban;
        this.beneficiary = beneficiary;
        this.value = value;
        this.reason = reason;
        this.beneficiaryDate = beneficiaryDate;
    }

    public String getPayerIban() {
        return payerIban;
    }

    public void setPayerIban(String payerIban) {
        this.payerIban = payerIban;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBeneficiaryDate() {
        return beneficiaryDate;
    }

    public void setBeneficiaryDate(String beneficiaryDate) {
        this.beneficiaryDate = beneficiaryDate;
    }

    @Override
    public String toString() {
        return "MoneyTransfer{" +
                "payerIban='" + payerIban + '\'' +
                ", beneficiaryIban='" + beneficiaryIban + '\'' +
                ", value='" + value + '\'' +
                ", reason='" + reason + '\'' +
                ", beneficiaryDate='" + beneficiaryDate + '\'' +
                '}';
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
}
