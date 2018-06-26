package it.tim.topup.model.integration;

import lombok.Data;

@Data
public class CallPagamS2S {

	
    private String shopLogin;
    private String uicCode;
    private String amount;
    private String shopTransactionId;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String buyerName;
    private String buyerEmail;
    private String languageId;
    private String cvv;
    private String min;
    private String transKey;
    private String paRes;
    private String customInfo;
    private String idea;
    private String requestToken;
    private String tokenValue;
    private String clientIP;
    private String itemType;
    private String recurrent;
    private String subMerchantId;
	
	
}
