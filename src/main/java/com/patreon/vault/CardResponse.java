package com.patreon.vault;


import java.util.Map;

public class CardResponse {

    public String nameOnAccount;
    public String paymentType;
    public Map<String, String> creditCard;
    public Map<String, String> address;

    public CardResponse() {
    }
}
