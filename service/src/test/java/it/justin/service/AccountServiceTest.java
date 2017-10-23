package it.justin.service;

import com.google.gson.JsonObject;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;

import it.justin.model.MoneyTransfer;
import it.justin.service.restintegration.AccountService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountService accountService;

    @Test
    public void shouldAccountBalanceResponseBeEqual(){
        AccountBalanceResponse serviceAccountBalanceResponse = this.accountService.getBalance(1L);
        AccountBalanceResponse accountBalanceResponse = this.testRestTemplate
                .getForObject("http://localhost:"+String.valueOf(port)+"/api/account-service/v1/getbalance/1", AccountBalanceResponse.class);

        assertEquals(serviceAccountBalanceResponse, accountBalanceResponse);
    }

    @Test
    public void shouldMoneyTransferResponseBeEqual(){
        HttpHeaders headers = new HttpHeaders();
        MoneyTransfer moneyTransfer =new MoneyTransfer("x","x","x","1","test","23/10/2017");
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("payer_iban", moneyTransfer.getPayerIban());
        jsonObject.addProperty("beneficiary_iban", moneyTransfer.getBeneficiaryIban());
        jsonObject.addProperty("beneficiary", moneyTransfer.getBeneficiary());
        jsonObject.addProperty("value", moneyTransfer.getValue());
        jsonObject.addProperty("reason", moneyTransfer.getReason());
        jsonObject.addProperty("beneficiary_date", moneyTransfer.getBeneficiaryDate());

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);

        MoneyTransferResponse serviceMoneyTransferResponse = this.accountService
                .doMoneyTransfer(moneyTransfer);

        MoneyTransferResponse moneyTransferResponse = this.testRestTemplate
                .postForObject("http://localhost:"+port+"/api/account-service/v1/moneytransfer",
                        httpEntity,MoneyTransferResponse.class);
        assertEquals(serviceMoneyTransferResponse, moneyTransferResponse);
    }

}
