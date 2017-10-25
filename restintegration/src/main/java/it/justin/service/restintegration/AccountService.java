package it.justin.service.restintegration;

import it.justin.apiwrapper.dto.accountBalance.AccountBalanceResponse;
import it.justin.apiwrapper.dto.moneyTransfer.MoneyTransferResponse;
import it.justin.model.MoneyTransfer;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account-service/v1")
public interface AccountService {

    @GET
    @Path("/getbalance/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    AccountBalanceResponse getBalance(@PathParam("accountId") Long accountId);

    @POST
    @Path("/moneytransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    MoneyTransferResponse doMoneyTransfer(@Valid MoneyTransfer moneyTransfer);
}
