package it.justin.apiwrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiWrapperServiceTest {


    @Value("${apiwrapper.endpoint.url.getbalance}")
    private String getBalanceEndpoint;

    @Value("${apiwrapper.endpoint.url.moneytransfer}")
    private String moneyTranferEndpoint;

    @Autowired
    private ApiWrapperService apiWrapperService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private AccountBalanceResponse serviceAccountBalance;

    private MoneyTransferResponse serviceMoneyTransfer;

    private HttpHeaders headers;

    private ObjectMapper objectMapper;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.serviceAccountBalance = apiWrapperService.getAccountBalance(1L);
        this.serviceMoneyTransfer = apiWrapperService.doMoneyTransfer(new MoneyTransfer("x","x","x","1","test","23/10/2017"));
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldAccountBalanceResponseBeEquals() throws IOException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("accountNumber", "1");

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), this.headers);
        String response = this.testRestTemplate.postForObject(getBalanceEndpoint, httpEntity, String.class);
        String balance = "";
        String availableBalance = "";

        JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);
        JsonNode jsonArray = jsonNode.get("payload");
        balance = jsonArray.get(0).get("balance").textValue();
        availableBalance = jsonArray.get(0).get("availableBalance").textValue();

        assertEquals(this.serviceAccountBalance, new AccountBalanceResponse(balance,availableBalance));
    }

    @Test
    public void shouldMoneyTransferResponseBeEquals() throws IOException {
        JsonObject jsonObject = new JsonObject();
        MoneyTransfer moneyTransfer = new MoneyTransfer();

        jsonObject.addProperty("payer_iban", moneyTransfer.getPayerIban());
        jsonObject.addProperty("beneficiary_iban", moneyTransfer.getBeneficiaryIban());
        jsonObject.addProperty("beneficiary", moneyTransfer.getBeneficiary());
        jsonObject.addProperty("value", moneyTransfer.getValue());
        jsonObject.addProperty("reason", moneyTransfer.getReason());
        jsonObject.addProperty("beneficiary_date", moneyTransfer.getBeneficiaryDate());

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        String response = this.testRestTemplate.postForObject(this.moneyTranferEndpoint, httpEntity, String.class);

        String esito = "";
        String idBonifico = "";

        JsonNode jsonNode = this.objectMapper.readValue(response, JsonNode.class);
        JsonNode BAIServiceOutputJson = jsonNode.get("payload").get(0).get("RichiestaBonificoResponse").get("BAIServiceOutput");
        esito = BAIServiceOutputJson.get("Esito").asText();
        idBonifico = BAIServiceOutputJson.get("IDBonifico").asText();

        assertEquals(this.serviceMoneyTransfer, new MoneyTransferResponse(idBonifico,esito));

    }

}
