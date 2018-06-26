package it.tim.topup.model.domain;

import it.tim.topup.validation.CreditCardPattern;

/**
 * Created by alongo on 11/05/18.
 */
public class CreditCardData {

    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvv;
    private String owner;
    private CreditCardPattern cardType;

    // CONSTRUCTOR

    public CreditCardData(String cardNumber, String expireMonth, String expireYear, String cvv, String owner) {
        this.cardNumber = cardNumber;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvv = cvv;
        this.owner = owner;
        this.cardType = null;
    }

    //SINGLETON CARD TYPE

    public CreditCardPattern getCardType() {
        if(cardType == null)
            this.cardType = CreditCardPattern.getMatch(this.getCardNumber());
        return cardType;
    }

    //GETTER

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public String getCvv() {
        return cvv;
    }

    public String getOwner() {
        return owner;
    }
}
