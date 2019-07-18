package com.patreon.vault;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CheckoutChargeResponse {
    public String id;
    public String originalId;
    public boolean liveMode;
    public String created;
    public BigDecimal value;
    public String currency;
    public String trackId;
    public int chargeMode;
    public String responseMessage;
    public String responseAdancedInfo;
    public String responseCode;
    public String status;
    public String hasChargeBack;

    public static CheckoutChargeResponse build(BigDecimal cents, String currency) {
        String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
        String dateString = simpleDateFormat.format(new Date());

        CheckoutChargeResponse response = new CheckoutChargeResponse();
        response.id = "charge_test_" + UUID.randomUUID().toString();
        response.originalId = "charge_test_BE5FE99E242R73BD0AD7";
        response.liveMode = true;
        response.created = dateString;
        response.value = cents;
        response.currency = currency;
        response.trackId = UUID.randomUUID().toString();
        response.chargeMode = 1;
        response.responseMessage = "Approved";
        response.responseAdancedInfo = "Approved";
        response.responseCode = "10000";
        response.status = "Authorized";
        response.hasChargeBack = "N";

        return response;
    }
}
