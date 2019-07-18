package com.patreon.vault;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public class StripeChargeResponse {
    public String id;
    public String object;
    public BigDecimal amount;

    @JsonProperty("amount_refunded")
    public BigDecimal amountRefunded;

    @JsonProperty("balance_transaction")
    public String balanceTransaction;

    public boolean captured;
    public long created;
    public String currency;
    public boolean livemode;
    public boolean paid;

    public static StripeChargeResponse build(BigDecimal cents, String currency) {

        StripeChargeResponse response = new StripeChargeResponse();
        response.id = "ch_" + UUID.randomUUID().toString();
        response.object = "charge";
        response.amount = cents;
        response.amountRefunded = new BigDecimal(0);
        response.balanceTransaction = "txn_" + UUID.randomUUID().toString();
        response.created = System.currentTimeMillis() / 1000L;
        response.paid = true;
        response.currency = currency;

        return response;
    }
}
