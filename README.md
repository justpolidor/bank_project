# bank_project
Banca sella interview project using platfr.io APIs

Il progetto è stato implementato utilizzando una struttura a moduli e integrato Jersey Jax-rs per l'implementazione REST (https://jersey.github.io).

Le tecnologie utilizzate sono:

1. Spring Boot (Java 8)
2. Jersey Jax-rs per implementazione REST
3. Postman per le chiamate e ambiente di test
4. Intellij IDEA IDE
5. Maven
6. Wiremock per i test

(porta utilizzata è quella di default, 8080)
## Endpoints
### POST per effettuare un bonifico 
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
Se un campo non è inviato (o scritto male), l'applicativo lancia un ValidationException ritornerà un messaggio di errore con status code 400
### GET per ottenere il bilancio dell account 
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
Nota: si può utilizzare il wrapper maven fornito direttamente nella cartella.

Possibili miglioramenti futuri: 
* implementazione di messaggi custom di successo/errore; 
* gestione eccezioni con bind dell'errore su un pojo e ritorno del messaggio in JSON.
