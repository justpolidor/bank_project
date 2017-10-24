package it.justin.apiwrapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ApiWrapperServiceProperties {

    @Value("${apiwrapper.endpoint.url.getbalance}")
    private String balanceEndpoint;

    @Value("${apiwrapper.endpoint.url.moneytransfer}")
    private String moneyTranferEndpoint;

    public String getBalanceEndpoint() {
        return balanceEndpoint;
    }

    public void setBalanceEndpoint(String balanceEndpoint) {
        this.balanceEndpoint = balanceEndpoint;
    }

    public String getMoneyTranferEndpoint() {
        return moneyTranferEndpoint;
    }

    public void setMoneyTranferEndpoint(String moneyTranferEndpoint) {
        this.moneyTranferEndpoint = moneyTranferEndpoint;
    }
}
