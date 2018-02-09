package it.justin.service;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import it.justin.apiwrapper.ApiWrapperServiceProperties;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;

import it.justin.model.MoneyTransfer;
import it.justin.service.restintegration.AccountService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceTest {

    private static final Logger LOG = getLogger(AccountServiceTest.class);
    private static final Long accountNumber = 1L;

    @Rule
    public WireMockRule wireMock = new WireMockRule(
            WireMockConfiguration.options().dynamicPort());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        AccountBalanceResponse accountBalanceResponse = this.accountService.balance(accountNumber);
        assertThat(accountBalanceResponse.getBalance()).isEqualTo("100");
        assertThat(accountBalanceResponse.getAvailableBalance()).isEqualTo("200");

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
        MoneyTransferResponse moneyTransferResponse = this.accountService.moneyTransfer(moneyTransfer);
        assertThat(moneyTransferResponse.getEsito()).isEqualTo("OK");
    }

    @Test
    public void accountBalanceGetShouldBe404() throws Exception{
        this.wireMock.stubFor(any(urlEqualTo("/api/account-service/v1/getbalance/"+accountNumber))
                .willReturn(aResponse().withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        this.thrown.expect(RestClientException.class);
        this.accountService.balance(accountNumber);
    }

    @Test
    public void moneyTransferShouldReturn400() throws Exception{
        MoneyTransfer moneyTransfer = new MoneyTransfer("IT07M0326849130052XX33r2r80XX18","IT07M03268491300532f2f2XX380XX19","Justin","1.00","Api first example","01/01/2017");
        this.wireMock.stubFor(post(urlEqualTo("/api/account-service/v1/moneytransfer"))
                .withRequestBody(equalToJson("{\n" +
                        "  \"payer_iban\": \"IT07M0326849130052XX33r2r80XX18\",\n" +
                        "  \"beneficiary_iban\": \"IT07M03268491300532f2f2XX380XX19\",\n" +
                        "  \"beneficiary\": \"Justin\",\n" +
                        "  \"reason\": \"Api first example\",\n" +
                        "  \"beneficiary_date\": \"01/01/2017\"\n" +
                        "}",true,true))
                .willReturn(aResponse().withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        this.thrown.expect(HttpClientErrorException.class);
        this.accountService.moneyTransfer(moneyTransfer);

    }

    @Test
    public void moneyTransferShoudContainValidITIban() throws Exception{
        MoneyTransfer moneyTransfer = new MoneyTransfer("IT07M0326849130fw052XX33r2r80XX18","IT07M03268491300532f2f2XX380XX19","Justin","1.00","Api first example","01/01/2017");
        this.wireMock.stubFor(post(urlEqualTo("/api/account-service/v1/moneytransfer"))
                .withRequestBody(equalToJson("{\n" +
                        "  \"payer_iban\": \"IT07M0326849130fw052XX33r2r80XX18\",\n" +
                        "  \"beneficiary_iban\": \"IT07M03268491300532f2f2XX380XX19\",\n" +
                        "  \"beneficiary\": \"Justin\",\n" +
                        "  \"value\": \"1.00\",\n" +
                        "  \"reason\": \"Api first example\",\n" +
                        "  \"beneficiary_date\": \"01/01/2017\"\n" +
                        "}",true,true))
                .willReturn(aResponse().withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        this.thrown.expect(HttpClientErrorException.class);
        this.accountService.moneyTransfer(moneyTransfer);
    }



}
