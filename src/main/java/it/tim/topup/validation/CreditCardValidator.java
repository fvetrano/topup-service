package it.tim.topup.validation;

import it.tim.topup.model.domain.CreditCardData;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by alongo on 11/05/18.
 */
@Slf4j
public class CreditCardValidator {

    private CreditCardValidator() {}

    private static final Pattern CDC_NUMBER = Pattern.compile("[0-9]{12,16}");
    private static final Pattern CVV = Pattern.compile("[0-9]{3,4}");


    public static final Predicate<CreditCardData> isValid = cdc -> {

        if(cdc == null) {
            log.error("Invalid cdc data: null");
            return false;
        }

        if(cdc.getCardType() == CreditCardPattern.NONE){
            log.error("Cannot identify cdc circuit for number: {}", cdc.getCardNumber());
            return false;
        }

        if(!validNumber(cdc.getCardNumber())) {
            log.error("Invalid cdc number: {}",cdc.getCardNumber());
            return false;
        }

        if(!validCVV(cdc.getCvv(), cdc.getCardType())){
            log.error("Invalid cdc cvv: {}",cdc.getCvv());
            return false;
        }

        if(!validDates(cdc.getExpireYear(), cdc.getExpireMonth())){
            log.error("Invalid cdc dates (may also be expired): {} - {}", cdc.getExpireYear(), cdc.getExpireMonth());
            return false;
        }

        return true;
    };

    //INNER VALIDATION

    static boolean validDates(String year, String month){
        if(!CommonValidators.validYear.test(year) || !CommonValidators.validMonth.test(month)) return false;
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        LocalDate expireDate = LocalDate.of(yearInt, monthInt, 1).plusMonths(1);
        return expireDate.isAfter(LocalDate.now());
    }

    static boolean validCVV(String cvv, CreditCardPattern pattern){
        return CommonValidators.notEmpty.test(cvv) && CVV.matcher(cvv).matches()
                && cvv.length() == pattern.getCvvSize();
    }

    static boolean validNumber(String cardNumber){
        return CDC_NUMBER.matcher(cardNumber).matches()
                && luhnCheck(cardNumber);
    }

    static boolean luhnCheck(String ccNumber){
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--){
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate){
                n *= 2;
                if (n > 9){
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

}
