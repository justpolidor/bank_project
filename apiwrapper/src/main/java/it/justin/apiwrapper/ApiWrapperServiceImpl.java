package it.justin.apiwrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@PropertySource("classpath:application.properties")
public class ApiWrapperServiceImpl implements ApiWrapperService {

    private static final Logger LOG = getLogger(ApiWrapperServiceImpl.class);

    @Value("${apiwrapper.endpoint.url.getbalance}")
    private String getBalanceEndpoint;

    @Value("${apiwrapper.endpoint.url.moneytransfer}")
    private String moneyTranferEndpoint;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    public ApiWrapperServiceImpl() {
        this.restTemplate = new RestTemplateBuilder().build();
    }

    public AccountBalanceResponse getAccountBalance(Long accountNumber) {
        LOG.info(this.getClass().getName() + " -> getAccountBalance() with account number:" + accountNumber);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("accountNumber", accountNumber);

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        String response = restTemplate.postForObject(getBalanceEndpoint, httpEntity, String.class);
        String balance = null;
        String availableBalance = null;

        try {
            JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);
            JsonNode jsonArray = jsonNode.get("payload");
            balance = jsonArray.get(0).get("balance").textValue();
            availableBalance = jsonArray.get(0).get("availableBalance").textValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Balance: "+ balance +" | availableBalance: "+availableBalance);
        return new AccountBalanceResponse(balance, availableBalance);
    }


    public MoneyTransferResponse doMoneyTransfer(MoneyTransfer moneyTransfer) {
        LOG.info(this.getClass().getName() + " -> doMoneyTransfer() with moneyTransfer:"+moneyTransfer);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("payer_iban", moneyTransfer.getPayerIban());
        jsonObject.addProperty("beneficiary_iban", moneyTransfer.getBeneficiaryIban());
        jsonObject.addProperty("beneficiary", moneyTransfer.getBeneficiary());
        jsonObject.addProperty("value", moneyTransfer.getValue());
        jsonObject.addProperty("reason", moneyTransfer.getReason());
        jsonObject.addProperty("beneficiary_date", moneyTransfer.getBeneficiaryDate());

        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        String response = restTemplate.postForObject(moneyTranferEndpoint,httpEntity, String.class);

        String esito = null;
        String idBonifico = null;

        try {
            JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);
            JsonNode BAIServiceOutputJson = jsonNode.get("payload").get(0).get("RichiestaBonificoResponse").get("BAIServiceOutput");
            esito = BAIServiceOutputJson.get("Esito").asText();
            idBonifico = BAIServiceOutputJson.get("IDBonifico").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("esito: "+ esito + " | idBonifico: "+idBonifico);
        return new MoneyTransferResponse(idBonifico, esito);
    }
}
