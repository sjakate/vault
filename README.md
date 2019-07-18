# Vault Repo


The goal behind a vault is to have a way for services to tokenize PII and card PCI data. This service exposes 2 resources
for this: users and cards respectively. Contract for an MVP api is below

Basic Auth Headers

Admin user
```
Authorization: Basic dXNlcj1hZG1pbiZwYXNzd29yZD0xMjM=
```
Non-admin user
```
Authorization: Basic dXNlcj10ZXN0JnBhc3N3b3JkPTEyMw==
```

```
// Tokenize PII

POST /users
{
   key1: value1,
   keyN: valueN
}

=>
{
   "token": "uuid"
}


GET  /users/:token
=>
{
   key1: value1,
   keyN: valueN
}

------------------------------

// Tokenize card PCI data

POST /cards
{
    "nameOnAccount": "Sampath Thummati",
    "paymentType": "CREDIT_CARD",
    "creditCard": {
        "pan": "4264281500066112",
        "expirationDate": "04-2020",
        "postalCode": "94537",
        "cvv": "123"
    },
    "address": {
      "street1": "123 Main Street.",
      "city": "Union City",
      "state": "CA",
      "postalCode": "94537",
      "countryCode": "US"
    }
   ....                             // can have up to N optional fields
}

=>
{
   "token": "uuid"
}

GET /cards/:token
=>

{
    "nameOnAccount": "Sampath Thummati",
    "paymentType": "CREDIT_CARD",
    "creditCard": {
        "pan": "4264281500066112",
        "expirationDate": "04-2020",
        "postalCode": "94537",
        "cvv": "123"
    },
    "address": {
      "street1": "123 Main Street.",
      "city": "Union City",
      "state": "CA",
      "postalCode": "94537",
      "countryCode": "US"
    }
    ...
}

------------------------------

// Run card updater for a card

POST /internal/cardupdater
{
   "card_token" : "uuid",           // required
}

=> 200

```


**Run**

```java -jar target/vault-1.0-jar-with-dependencies.jar```
