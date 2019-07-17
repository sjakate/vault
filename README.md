# Vault Repo


The goal behind a vault is to have a way for services to tokenize PII and card PCI data. This service exposes 2 resources
for this: users and cards respectively. Contract for an MVP api is below

```
POST /users
{
   "first_name" : "name", // required
   "last_name" : "name",  // required
   "ssn": "ssn"           // required
   ...                    // can have up to N optional fields
}

=>
{
   "token": "uuid"
}


GET  /users/:token
=>
{
   "first_name" : "name",
   "last_name" : "name",
   "ssn": "ssn"
   ...
}

------------------------------

POST /cards
{
   "name" : "Patrenau P",           // required
   "pan" : "4111111111111111",      // required
   "expiration": "07-07-2022",      // required
   ....                             // can have up to N optional fields
}

=>
{
   "token": "uuid"
}

GET /cards/:token
=>

{
   "name" : "Patrenau P",
   "pan" : "4111111111111111",
   "expiration": "07-07-2022"
    ...
}

```

**Run**

```java -jar target/vault-1.0-jar-with-dependencies.jar```
