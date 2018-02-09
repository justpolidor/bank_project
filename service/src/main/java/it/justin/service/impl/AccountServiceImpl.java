package it.justin.service.impl;

import it.justin.apiwrapper.ApiWrapperService;
import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;
import it.justin.service.restintegration.AccountService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOG = getLogger(AccountServiceImpl.class);

    private final ApiWrapperService apiWrapperService;

    @Autowired
    public AccountServiceImpl(ApiWrapperService apiWrapperService) {
        this.apiWrapperService = apiWrapperService;
    }

    public AccountBalanceResponse getBalance(Long accountNumber) {
        LOG.info(this.getClass().getName() + "-> getBalance() with accountID:"+accountNumber);
        return apiWrapperService.getAccountBalance(accountNumber);
    }

    public MoneyTransferResponse doMoneyTransfer(MoneyTransfer moneyTransfer) {
        LOG.info(this.getClass().getName() + "-> doMoneyTransfer() with moneyTransfer:"+moneyTransfer.toString());
        return apiWrapperService.doMoneyTransfer(moneyTransfer);
    }
}
