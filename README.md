# bank_project
Banca sella interview project using platfr.io APIs

Il progetto è stato implementato utilizzando una struttura a moduli e integrato Jersey Jax-rs per l'implementazione REST (https://jersey.github.io).

Le tecnologie utilizzate sono:

1. Spring Boot (Java 8)
2. Jersey Jax-rs per implementazione REST
3. Postman per le chiamate e ambiente di test
4. Intellij IDEA IDE
5. Maven

## Endpoints
### POST per effettuare un bonifico (porta 8080)
```json
POST api/account-service/v1/moneytransfer Content-Type: application/json
{
  "payerIban": "IT07M0326849130052XX33r2r80XX18",
  "beneficiaryIban": "IT07M03268491300532f2f2XX380XX19",
  "beneficiary": "Justin",
  "value": "1.00",
  "reason": "Api first example",
  "beneficiaryDate": "01/01/2017"
}
```

## risposta
```json
{
    "IDBonifico": "xxxxxxxx",
    "Esito": "OK"
}
```

### GET per ottenere il bilancio dell account (porta 8080)
```json
GET api/account-service/v1/getbalance/{accountId} Content-Type: application/json
```
## risposta
```json
{
    "balance": "470.65",
    "availableBalance": "450"
}
```
