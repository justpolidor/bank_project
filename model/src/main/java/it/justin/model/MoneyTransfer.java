package it.justin.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MoneyTransfer {

    @NotNull(message = "A Valid IT IBAN required")
    @Pattern(regexp = "IT\\d{2}[ ][a-zA-Z]\\d{3}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{3}|IT\\d{2}[a-zA-Z]\\d{22}")
    private String payerIban;
    @NotNull(message = "A Valid IT IBAN required")
    @Pattern(regexp = "IT\\d{2}[ ][a-zA-Z]\\d{3}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{4}[ ]\\d{3}|IT\\d{2}[a-zA-Z]\\d{22}")
    private String beneficiaryIban;
    @NotNull(message = "Beneficiary required")
    private String beneficiary;
    @NotNull(message = "Value required")
    private String value;
    @NotNull(message = "Reason Required")
    private String reason;
    @NotNull(message = "Beneficiary date required")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyTransfer that = (MoneyTransfer) o;

        if (payerIban != null ? !payerIban.equals(that.payerIban) : that.payerIban != null) return false;
        if (beneficiaryIban != null ? !beneficiaryIban.equals(that.beneficiaryIban) : that.beneficiaryIban != null)
            return false;
        if (beneficiary != null ? !beneficiary.equals(that.beneficiary) : that.beneficiary != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        return beneficiaryDate != null ? beneficiaryDate.equals(that.beneficiaryDate) : that.beneficiaryDate == null;
    }

    @Override
    public int hashCode() {
        int result = payerIban != null ? payerIban.hashCode() : 0;
        result = 31 * result + (beneficiaryIban != null ? beneficiaryIban.hashCode() : 0);
        result = 31 * result + (beneficiary != null ? beneficiary.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (beneficiaryDate != null ? beneficiaryDate.hashCode() : 0);
        return result;
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
