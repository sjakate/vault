package com.patreon.vault;

import java.math.BigDecimal;

public class GatewayChargeRequest {
    public String cardToken;
    public String gateway;
    public BigDecimal cents;
    public String currency;

    public GatewayChargeRequest() {}
}
