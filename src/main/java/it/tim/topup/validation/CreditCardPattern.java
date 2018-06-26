package it.tim.topup.validation;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public enum CreditCardPattern {

    AMEX(Pattern.compile("^3[47]\\d*"),"AM",4),
    VISA(Pattern.compile("^4\\d*"),"CSI"),
    MASTERCARD(Pattern.compile("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720)\\d*"),"CSI"),
    MAESTRO(Pattern.compile("^(5018|5020|5038|6020|6304|6703|6759|676[1-3])\\d*"),"BMA"),
    DINERS(Pattern.compile("^(36|38|30[0-5])\\d*"),"DC"),
    DISCOVER(Pattern.compile("^(6011|65|64[4-9]|622)\\d*"),"csp"),
    UNIONPAY(Pattern.compile("^62\\d*"),"csp"),
    JCB(Pattern.compile("^35\\d*"),"csp"),
    NONE(Pattern.compile("^\\d*"),"csp");

    Pattern pattern;
    int cvvSize = 3;
    String circuit;

    CreditCardPattern(Pattern pattern, String circuit, int cvvSize) {
        this.pattern = pattern;
        this.cvvSize = cvvSize;
        this.circuit = circuit;
    }

    CreditCardPattern(Pattern pattern, String circuit) {
        this.pattern = pattern;
        this.circuit = circuit;
    }

    public boolean matches(String cardNumber) {
        return !StringUtils.isEmpty(cardNumber)
                && this.pattern.matcher(cardNumber).matches();
    }

    public static boolean anyMatch(String cardNumber){
        return getMatch(cardNumber) != NONE;
    }

    public static CreditCardPattern getMatch(String cardNumber){
        if(!StringUtils.isEmpty(cardNumber)) {
            for (CreditCardPattern pattern : values())
                if (pattern.matches(cardNumber)) {
                    return pattern;
                }
        }
        return NONE;
    }

    public int getCvvSize() {
        return cvvSize;
    }

    public String getCircuit() {
        return circuit;
    }
}
