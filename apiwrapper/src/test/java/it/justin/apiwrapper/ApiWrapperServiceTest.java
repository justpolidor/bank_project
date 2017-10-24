package it.justin.apiwrapper;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiWrapperServiceTest {

    private static final Logger LOG = getLogger(ApiWrapperServiceTest.class);
    private static final Long accountNumber = 1L;

    @Rule
    public WireMockRule wireMock = new WireMockRule(
            WireMockConfiguration.options().dynamicPort());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ApiWrapperService apiWrapperService;

    @Autowired
    private ApiWrapperServiceProperties apiWrapperServiceProperties;


    @Before
    public void contextLoads() throws Exception {
        this.apiWrapperServiceProperties.
                setBalanceEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/getbalance/");
        this.apiWrapperServiceProperties
                .setMoneyTranferEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/moneytransfer");
    }

    @Test
    public void accountBalanceDataShouldBeEqual() throws Exception{
        LOG.info(apiWrapperServiceProperties.getBalanceEndpoint());
        this.wireMock.stubFor(any(urlEqualTo("/api/account-service/v1/getbalance/"+accountNumber))
                .willReturn(aResponse().withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("accountBalanceResponse.json")));
        AccountBalanceResponse accountBalanceResponse = this.apiWrapperService.getAccountBalance(accountNumber);
        assertThat(accountBalanceResponse.getBalance()).isEqualTo("100");
        assertThat(accountBalanceResponse.getAvailableBalance()).isEqualTo("200");

    }

    @Test
    public void moneyTransferShouldBeEqual() throws Exception{
        this.wireMock.stubFor(post(urlEqualTo("/api/account-service/v1/moneytransfer"))
                .withRequestBody(equalToJson("{\n" +
                        "  \"payer_iban\": \"x\",\n" +
                        "  \"beneficiary_iban\": \"x\",\n" +
                        "  \"beneficiary\": \"Justin\",\n" +
                        "  \"value\": \"1.00\",\n" +
                        "  \"reason\": \"x\",\n" +
                        "  \"beneficiary_date\": \"x\"\n" +
                        "}"))
                .willReturn(aResponse().withStatus(201)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile("moneyTransferResponse.json")));
        MoneyTransferResponse moneyTransferResponse = this.apiWrapperService.
                doMoneyTransfer(new MoneyTransfer("x","x","Justin","1.00","x","x"));
        assertThat(moneyTransferResponse.getEsito()).isEqualTo("OK");
    }

    @Test
    public void accountBalanceWhenNotFoundShouldHave404Code()throws Exception{
        this.wireMock.stubFor(any(urlEqualTo("/api/account-service/v1/getbalance/2"))
        .willReturn(aResponse().withStatus(404)));
        this.thrown.expect(HttpStatusCodeException.class);
        this.apiWrapperService.getAccountBalance(2L);

    }

}