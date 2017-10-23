package it.justin.service;

import it.justin.apiwrapper.ApiWrapperService;

import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import it.justin.service.restintegration.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicesIntegrationTest {

    @Mock
    private AccountService accountService;

    @Mock
    private ApiWrapperService apiWrapperService;

    @Before
    public void setupMockService(){
        MockitoAnnotations.initMocks(this);
        when(this.apiWrapperService.getAccountBalance(2L)).thenReturn(new AccountBalanceResponse("100","0"));
        when(this.accountService.doMoneyTransfer
                (new MoneyTransfer("a","a","a","2","test","24/10/2017")))
                .thenReturn(new MoneyTransferResponse("a","KO"));
    }

    @Test
    public void shouldAccountBalanceResoponsesBeEqual(){
        AccountBalanceResponse accountServiceAccountBalanceResponse = this.accountService.getBalance(1L);
        AccountBalanceResponse apiWrapperServiceAccountBalanceResponse = this.apiWrapperService.getAccountBalance(1L);

        assertEquals(accountServiceAccountBalanceResponse, apiWrapperServiceAccountBalanceResponse);
    }

    @Test
    public void shouldMoneyTransferResponsesBeEqual(){
        MoneyTransferResponse accountServiceMoneyTransferResponse = this.accountService
                .doMoneyTransfer(new MoneyTransfer("x","x","x","1","test","23/10/2017"));

        MoneyTransferResponse apiWrapperServiceMoneyTransferResponse = this.apiWrapperService
                .doMoneyTransfer(new MoneyTransfer("x","x","x","1","test","23/10/2017"));

        assertEquals(accountServiceMoneyTransferResponse, apiWrapperServiceMoneyTransferResponse);
    }

    @Test
    public void shouldServiceAccountBalanceNotBeEquals(){
        AccountBalanceResponse accountServiceAccountBalanceResponse = this.accountService.getBalance(2L);

        assertNotEquals(accountServiceAccountBalanceResponse, new AccountBalanceResponse("300","100"));
    }

    @Test
    public void shouldApiWrapperAccountBalanceNotBeEquals(){
        AccountBalanceResponse apiWrapperServiceAccountBalanceResponse = this.apiWrapperService.getAccountBalance(2L);

        assertNotEquals(apiWrapperServiceAccountBalanceResponse, new AccountBalanceResponse("21","10"));
    }

    @Test
    public void shoulServiceMoneyTransferNotBeEquals(){
        MoneyTransferResponse accountServiceMoneyTransferResponse = this.accountService
                .doMoneyTransfer(new MoneyTransfer("a","a","a","2","test","24/10/2017"));

        assertNotEquals(accountServiceMoneyTransferResponse, new AccountBalanceResponse("211","55"));
    }

    @Test
    public void shoulApiWrapperMoneyTransferNotBeEquals(){
        MoneyTransferResponse accountServiceMoneyTransferResponse = this.apiWrapperService
                .doMoneyTransfer(new MoneyTransfer("a","a","a","2","test","24/10/2017"));

        assertNotEquals(accountServiceMoneyTransferResponse, new AccountBalanceResponse("211","55"));
    }
}
