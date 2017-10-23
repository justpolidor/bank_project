package it.justin.apiwrapper;

import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;

public interface ApiWrapperService {

    AccountBalanceResponse getAccountBalance(Long accountNumber);
    MoneyTransferResponse doMoneyTransfer(MoneyTransfer moneyTransfer);
}
