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

    private final ApiWrapperServiceProperties apiWrapperServiceProperties;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private HttpHeaders headers;

    @Autowired
    public ApiWrapperServiceImpl(ApiWrapperServiceProperties apiWrapperServiceProperties) {
        this.restTemplate = new RestTemplateBuilder().build();
        this.headers = new HttpHeaders();
        this.objectMapper = new ObjectMapper();
        this.apiWrapperServiceProperties = apiWrapperServiceProperties;
    }

    public AccountBalanceResponse getAccountBalance(Long accountNumber) {
        LOG.info(this.getClass().getName() + " -> getAccountBalance() with account number:" + accountNumber);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("accountNumber", accountNumber);
        LOG.info("POST TO " + apiWrapperServiceProperties.getBalanceEndpoint());
        HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), headers);
        String response = restTemplate.postForObject(apiWrapperServiceProperties.getBalanceEndpoint(), httpEntity, String.class);
        String balance = null;
        String availableBalance = null;

        LOG.info(response);

        try {
            JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);
            JsonNode jsonArray = jsonNode.get("payload");
            balance = jsonArray.get(0).get("balance").textValue();
            availableBalance = jsonArray.get(0).get("availableBalance").textValue();
        } catch (IOException e) {
            LOG.error("Exception message: "+e.getMessage());
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
        String response = restTemplate.postForObject(apiWrapperServiceProperties.getMoneyTranferEndpoint(),httpEntity, String.class);
        LOG.info(response);
        String esito = null;
        String idBonifico = null;

        try {
            JsonNode jsonNode = objectMapper.readValue(response, JsonNode.class);
            JsonNode BAIServiceOutputJson = jsonNode.get("payload").get(0).get("RichiestaBonificoResponse").get("BAIServiceOutput");
            esito = BAIServiceOutputJson.get("Esito").asText();
            idBonifico = BAIServiceOutputJson.get("IDBonifico").asText();
        } catch (IOException e) {
            LOG.error("Exception message: "+e.getMessage());
        }

        LOG.info("esito: "+ esito + " | idBonifico: "+idBonifico);
        return new MoneyTransferResponse(idBonifico, esito);
    }
}
