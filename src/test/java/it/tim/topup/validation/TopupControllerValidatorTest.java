package it.tim.topup.validation;

import it.tim.topup.common.headers.TimHeaders;
import it.tim.topup.common.headers.TimSession;
import it.tim.topup.common.headers.TransientHeaderName;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.domain.CreditCardData;
import it.tim.topup.model.exception.BadRequestException;
import it.tim.topup.model.web.CheckoutRequest;
import it.tim.topup.model.web.ScratchCardRequest;
import org.junit.Test;

/**
 * Created by alongo on 30/04/18.
 */
public class TopupControllerValidatorTest {

    @Test
    public void validatePrivateConstructor() throws Exception {
        new TopupControllerValidator();
    }

    @Test
    public void validateScratchCardRequestOk() throws Exception {
        ScratchCardRequest request = new ScratchCardRequest(
                "1111111111111111",
                "3400000001",
                "3400000002",
                Constants.Subsystems.MYTIMAPP.name()
        );
        TopupControllerValidator.validateScratchCardRequest(
                "1111111111111111",
                "3400000001",
                "3400000002",
                Constants.Subsystems.MYTIMAPP.name()
                , getTimHeader("a reference", "JAVA")
        );
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardNoCardNumber() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                null, "3400000001", "3400000002", Constants.Subsystems.MYTIMAPP.name()
                , getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardNoFromMsisnd() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                "1234", null, "3400000002", Constants.Subsystems.MYTIMAPP.name()
                , getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardNoToMsisdn() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                "1234", "3400000001", null, Constants.Subsystems.MYTIMAPP.name()
                , getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardNoSubSys() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                "1234", "3400000001", "3400000002", null
                , getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardUnsupportedSubSys() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                "1234", "3400000001", "3400000002", "A_SUBSYS"
                , getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateScratchCardNoUserReference() throws Exception {
        TopupControllerValidator.validateScratchCardRequest(
                "1234", "3400000001", "3400000002", Constants.Subsystems.MYTIMAPP.name()
                , getTimHeader(null, "JAVA"));
    }

    @Test
    public void validateCheckoutRequestOk() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "3400000001", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadAmount() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                null, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "3400000001", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadCDC() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, null, "3400000001", "3400000001", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadFromMsisdn() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "123", "3400000001", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadToMsisdn() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadSubsys() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "3400000002", "A Subsys", getTimHeader("a reference", "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadCustomer() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "3400000002", Constants.Subsystems.MYTIMAPP.name(), getTimHeader(null, "JAVA"));
    }

    @Test(expected = BadRequestException.class)
    public void validateCheckoutRequestKoBadDeviceType() throws Exception {
        TopupControllerValidator.validateCheckoutRequest(
                20, new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"), "3400000001", "3400000002", Constants.Subsystems.MYTIMAPP.name(), getTimHeader("a reference", "SOME"));
    }


    private TimHeaders getTimHeader(String userReference, String deviceType){
        TimHeaders headers = new TimHeaders();
        headers.setHeader(TransientHeaderName.DEVICE_TYPE, deviceType);
        TimSession session = new TimSession();
        session.setUserReference(userReference);
        headers.setSession(session);
        return headers;
    }

}