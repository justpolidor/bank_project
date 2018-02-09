package it.justin.service;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import it.justin.apiwrapper.ApiWrapperService;

import it.justin.apiwrapper.ApiWrapperServiceImpl;
import it.justin.apiwrapper.ApiWrapperServiceProperties;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import it.justin.service.impl.AccountServiceImpl;
import it.justin.service.restintegration.AccountService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicesIntegrationTest {

    private static final Logger LOG = getLogger(ServicesIntegrationTest.class);
    private static final Long accountNumber = 1L;

    @Rule
    public WireMockRule wireMock = new WireMockRule(
            WireMockConfiguration.options().dynamicPort());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ApiWrapperService apiWrapperService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApiWrapperServiceProperties apiWrapperServiceProperties;

    @Before
    public void contextLoads() throws Exception {

        this.apiWrapperServiceProperties.
                setBalanceEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/getbalance/"+accountNumber);
        this.apiWrapperServiceProperties
                .setMoneyTranferEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/moneytransfer");
        LOG.info(String.valueOf(this.wireMock.port()));
    }

    @Test
    public void accountBalanceDataShouldBeEqual() throws Exception{
        this.wireMock.stubFor(any(urlEqualTo("/api/account-service/v1/getbalance/"+accountNumber))
                .willReturn(aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("accountBalanceResponse.json")));
        LOG.info(this.apiWrapperServiceProperties.getBalanceEndpoint());
        AccountBalanceResponse accountServiceBalanceResponse = this.accountService.getBalance(1L);
        AccountBalanceResponse apiWrapperBalanceResponse = this.apiWrapperService.getAccountBalance(1L);
        assertEquals(accountServiceBalanceResponse.getBalance(), apiWrapperBalanceResponse.getBalance());
        assertEquals(accountServiceBalanceResponse.getAvailableBalance(), apiWrapperBalanceResponse.getAvailableBalance());
    }

    @Test
    public void moneyTransferShouldBeEqual() throws Exception{
        MoneyTransfer moneyTransfer = new MoneyTransfer("IT07M0326849130052XX33r2r80XX18","IT07M03268491300532f2f2XX380XX19","Justin","1.00","Api first example","01/01/2017");
        this.wireMock.stubFor(post(urlEqualTo("/api/account-service/v1/moneytransfer"))
                .withRequestBody(equalToJson("{\n" +
                        "  \"payer_iban\": \"IT07M0326849130052XX33r2r80XX18\",\n" +
                        "  \"beneficiary_iban\": \"IT07M03268491300532f2f2XX380XX19\",\n" +
                        "  \"beneficiary\": \"Justin\",\n" +
                        "  \"value\": \"1.00\",\n" +
                        "  \"reason\": \"Api first example\",\n" +
                        "  \"beneficiary_date\": \"01/01/2017\"\n" +
                        "}",true,true))
                .willReturn(aResponse().withStatus(201)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("moneyTransferResponse.json")));

        MoneyTransferResponse apiWrapperMoneyTransferResponse = this.apiWrapperService.doMoneyTransfer(moneyTransfer);
        MoneyTransferResponse accountServiceMoneyTransferResponse = this.accountService.doMoneyTransfer(moneyTransfer);

        assertEquals(apiWrapperMoneyTransferResponse, accountServiceMoneyTransferResponse);
    }
}
