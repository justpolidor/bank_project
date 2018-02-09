# bank_project

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
MoneyTransfer è l'oggetto java passato a questo endpoint, che prende i dati json ed effettua il bind con quelli al suo interno; qui avviene una validazione:
* tutti i valori non possono essere null
* gli iban devono essere italiani e validi (controllo con un regex).

Se un campo non è inviato (o scritto male), l'applicativo lancia un ValidationException che ritornerà un messaggio di errore con status code 400.

**Gli Iban sono stati generati utilizzando questo servizio: https://bank.codes/iban/generate/italy/**
### POST per effettuare un bonifico 
```json
POST api/account-service/v1/moneytransfer Content-Type: application/json
{
  "payerIban": "IT30C4232333335123456789012",
  "beneficiaryIban": "IT30C4232333335123456789011",
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
### GET per ottenere il bilancio dell account 
Ho deciso di implementare questo endpoint con il verbo GET così da utilizzare in modo canonico i verbi REST.
```json
GET api/account-service/v1/balance/{accountId} Content-Type: application/json
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
