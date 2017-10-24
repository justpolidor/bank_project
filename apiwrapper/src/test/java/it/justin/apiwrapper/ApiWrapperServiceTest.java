package it.justin.apiwrapper;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

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

    @Autowired
    private ApiWrapperService apiWrapperService;

    @Autowired
    private ApiWrapperServiceProperties apiWrapperServiceProperties;

    @Before
    public void contextLoads() throws Exception {
        this.apiWrapperServiceProperties.
                setBalanceEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/getbalance/"+accountNumber);
        this.apiWrapperServiceProperties
                .setMoneyTranferEndpoint("http://localhost:"+this.wireMock.port() + "/api/account-service/v1/moneytransfer");
    }

    @Test
    public void getAccountBalanceData() throws Exception{
        LOG.info(apiWrapperServiceProperties.getBalanceEndpoint());
        this.wireMock.stubFor(any(urlEqualTo("/api/account-service/v1/getbalance/"+accountNumber))
                .willReturn(aResponse().withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\n" +
                        "  \"status\": {\n" +
                        "    \"code\": \"OK\",\n" +
                        "    \"description\": \"Mock Balance Retrieved\"\n" +
                        "  },\n" +
                        "  \"error\": {\n" +
                        "    \"description\": \"\"\n" +
                        "  },\n" +
                        "  \"payload\": [\n" +
                        "    {\n" +
                        "      \"balance\": \"100\",\n" +
                        "      \"availableBalance\": \"200\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")));
        AccountBalanceResponse accountBalanceResponse = this.apiWrapperService.getAccountBalance(accountNumber);
        assertThat(accountBalanceResponse.getBalance()).isEqualTo("100");
        assertThat(accountBalanceResponse.getAvailableBalance()).isEqualTo("200");

    }

    @Test
    public void postMoneyTransfer() throws Exception{
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
                .withBody("{\n" +
                        "    \"status\": {\n" +
                        "        \"code\": \"OK\",\n" +
                        "        \"description\": \"Mock Money Transfer Successful\"\n" +
                        "    },\n" +
                        "    \"error\": {\n" +
                        "        \"description\": \"\"\n" +
                        "    },\n" +
                        "    \"payload\": [\n" +
                        "        {\n" +
                        "            \"RichiestaBonificoResponse\": {\n" +
                        "                \"BAIServiceOutput\": {\n" +
                        "                    \"IDBonifico\": \"xxxxxxxx\",\n" +
                        "                    \"ModalitaEsecuzione\": \"REAL_TIME\",\n" +
                        "                    \"Esito\": \"OK\",\n" +
                        "                    \"WarningMessage\": null,\n" +
                        "                    \"WarningMessageForPDF\": null,\n" +
                        "                    \"Motivazione\": \"ESAURITO\",\n" +
                        "                    \"CRO\": \"47220358711\",\n" +
                        "                    \"NumeroContabile\": \"xxxxxxxx\",\n" +
                        "                    \"CondizioniBonifico\": {\n" +
                        "                        \"Condizione\": {\n" +
                        "                            \"CodiceCondizione\": \"xxxxxxxx\",\n" +
                        "                            \"Descrizione\": \" Comm.Bon.Stessa Banca \",\n" +
                        "                            \"Divisa\": null,\n" +
                        "                            \"isPenale\": null,\n" +
                        "                            \"Valore\": \"0.00\"\n" +
                        "                        }\n" +
                        "                    },\n" +
                        "                    \"DataValutaBeneficiario\": \"05/07/2016\",\n" +
                        "                    \"DataValutaOrdinante\": \"05/07/2016\",\n" +
                        "                    \"IdContabile\": \"1074392698\",\n" +
                        "                    \"Ordinante\": \"ProvaSender\",\n" +
                        "                    \"Causale\": \"Bonifico in PRE\",\n" +
                        "                    \"Beneficiario\": \"ProvaReceiver\",\n" +
                        "                    \"NumeroProtocolloOperazione\": \"20160705000000010191\",\n" +
                        "                    \"TipoAgevolazione\": null,\n" +
                        "                    \"BICBeneficiario\": \"SELBIT2BXXX\",\n" +
                        "                    \"TipoSpese\": \"SHA\",\n" +
                        "                    \"ContoAddebitoSpese\": {\n" +
                        "                        \"Conto\": {\n" +
                        "                            \"IDConto\": null,\n" +
                        "                            \"NumeroConto\": null,\n" +
                        "                            \"TipoConto\": null,\n" +
                        "                            \"IBAN\": null\n" +
                        "                        }\n" +
                        "                    },\n" +
                        "                    \"ContoOrdinante\": \"52724226990\",\n" +
                        "                    \"NomeBeneficiario\": \"ProvaReceiver\",\n" +
                        "                    \"IndirizzoBeneficiario\": null,\n" +
                        "                    \"ContoBeneficiario\": \"IT08W03268223000EM000000101\",\n" +
                        "                    \"BICBancaBeneficiario\": \"SELBIT2BXXX\",\n" +
                        "                    \"NomeBancaBeneficiario\": null,\n" +
                        "                    \"CittaBancaBeneficiario\": null,\n" +
                        "                    \"PaeseBancaBeneficiario\": \"IT\",\n" +
                        "                    \"Importo\": null,\n" +
                        "                    \"ImportoOrdine\": {\n" +
                        "                        \"ValoreImporto\": \"1.00\",\n" +
                        "                        \"DivisaImporto\": {\n" +
                        "                            \"CodiceDivisa\": \"EUR\"\n" +
                        "                        }\n" +
                        "                    },\n" +
                        "                    \"DataOrdine\": \"05/07/2016\",\n" +
                        "                    \"DataRegolamento\": \"05/07/2016\",\n" +
                        "                    \"DataEsecuzione\": \"05/07/2016\",\n" +
                        "                    \"URI\": null,\n" +
                        "                    \"NumeroProtocollo\": null,\n" +
                        "                    \"Cambi\": null,\n" +
                        "                    \"ControvaloreAddebito\": null\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}")));
        MoneyTransferResponse moneyTransferResponse = this.apiWrapperService.
                doMoneyTransfer(new MoneyTransfer("x","x","Justin","1.00","x","x"));
        assertThat(moneyTransferResponse.getEsito()).isEqualTo("OK");
    }

}
