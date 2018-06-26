package it.tim.topup.validation;

import it.tim.topup.common.headers.TimHeaders;
import it.tim.topup.common.headers.TransientHeaderName;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.domain.CreditCardData;
import it.tim.topup.model.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;

/**
 * Created by alongo on 30/04/18.
 */
public class TopupControllerValidator {

    TopupControllerValidator() {}

    public static void validateScratchCardRequest(String cardNumber, String fromMsisdn, String toMsisdn, String subsystem, TimHeaders headers) {
        boolean valid = validateStrings(CommonValidators.validScratchCard, cardNumber);
        valid = valid && validateStrings(CommonValidators.validPhoneNumber, fromMsisdn, toMsisdn);
        valid = valid && Constants.Subsystems.contains(subsystem);
        if(!valid)
            throw new BadRequestException("Missing/Wrong parameters in ScratchCardRequest");

        if(headers == null || headers.getSession() == null
                || StringUtils.isEmpty(headers.getSession().getUserReference()))
            throw new BadRequestException("Missing user reference");
    }

    public static void validateCheckoutRequest(Integer amount, CreditCardData cdc, String fromMsisdn, String toMsisdn, String subsystem, TimHeaders headers) {

        boolean valid = amount != null && amount > 0
                && validateStrings(CommonValidators.validPhoneNumber, fromMsisdn, toMsisdn)
                && Constants.Subsystems.contains(subsystem)
                && CreditCardValidator.isValid.test(cdc);
        if(!valid)
            throw new BadRequestException("Missing/Wrong parameters in CheckoutRequest");

        if(headers == null || headers.getSession() == null
                || StringUtils.isEmpty(headers.getSession().getUserReference()))
            throw new BadRequestException("Missing user reference");

        String deviceType = headers.getHeader(TransientHeaderName.DEVICE_TYPE);
        if(!Constants.DeviceType.contains(deviceType))
            throw new BadRequestException("Bad device type in header: "+deviceType);
    }

    //UTIL

    private static boolean validateStrings(Predicate<String> predicate, String... strings){
        for(String s : strings){
            if(!predicate.test(s))
                return false;
        }
        return true;
    }

}
