package it.justin.apiwrapper.dto.moneyTransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransferResponse {

    @JsonProperty("IDBonifico")
    private String idBonifico;
    @JsonProperty("Esito")
    private String esito;

    public MoneyTransferResponse() {
    }

    public MoneyTransferResponse(String idBonifico, String esito) {
        this.idBonifico = idBonifico;
        this.esito = esito;
    }

    public String getIdBonifico() {
        return idBonifico;
    }

    public void setIdBonifico(String idBonifico) {
        this.idBonifico = idBonifico;
    }

    public String getEsito() {
        return esito;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    @Override
    public String toString() {
        return "MoneyTransferResponse{" +
                "idBonifico='" + idBonifico + '\'' +
                ", esito='" + esito + '\'' +
                '}';
    }
}
