package it.tim.topup.service;

import it.tim.topup.integration.proxy.SCDelegate;
import it.tim.topup.integration.proxy.RolProxy;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.exception.BadRequestException;
import it.tim.topup.model.exception.CannotRechargeException;
import it.tim.topup.model.exception.GenericException;
import it.tim.topup.model.exception.NotAuthorizedException;
import it.tim.topup.model.integration.RechargeAuthorizationResponse;
import it.tim.topup.model.integration.ScratchCardResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by alongo on 30/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScratchCardServiceTest {

    @Mock
    SCDelegate creditProxy;

    @Mock
    RolProxy customersProxy;


    private ScratchCardService service;

    @Before
    public void init(){
        service = new ScratchCardService(customersProxy, creditProxy);
    }

    @After
    public void cleanup(){
        Mockito.reset(customersProxy);
        Mockito.reset(creditProxy);
    }

    @Test(expected = BadRequestException.class)
    public void authorizeFailsOnNUll() throws Exception {
        service.authorize(null, null, null);
    }

    @Test(expected = NotAuthorizedException.class)
    public void authorizeFailsOnNotOkResponse() throws Exception {
        Mockito.when(creditProxy.authorizeLineForRecharge(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(new RechargeAuthorizationResponse("A","a_line","1"));
        service.authorize("a_ref","a_line", "a_tid");
    }

//    @Test
//    public void authorizeOk() throws Exception {
//        Mockito.when(creditProxy.authorizeLineForRecharge(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(new RechargeAuthorizationResponse("A","a_line","0"));
//        service.authorize("a_ref","a_line", "a_tid");
//    }

//    @Test(expected = CannotRechargeException.class)
//    public void doRechargeFailsOnNotOkResponse() throws Exception {
//        Mockito.when(creditProxy.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
//                .thenReturn(new ScratchCardResponse("a_line", null, "0", "card", "tid"));
//        service.doRecharge("FRRMNG56P12F854D", "11111", "cardType", "cardNumber", "a_line", "a_line", "10.00", "", Constants.Subsystems.MYTIMAPP.name());
//    }

    @Test(expected = GenericException.class)
    public void doRechargeFailsOnWrongFormatAmount() throws Exception {
        Mockito.when(creditProxy.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
                .thenReturn(new ScratchCardResponse("a_line", "AAAA", "0", "card", "tid"));
        service.doRecharge("FRRMNG56P12F854D","11111", "cardType", "cardNumber", "a_line", "a_line", "10.00", "", Constants.Subsystems.MYTIMAPP.name());
    }

    @Test
    public void doRechargeOk() throws Exception {
        Mockito.when(creditProxy.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
                .thenReturn(new ScratchCardResponse("a_line", "25.00", "0", "card", "tid"));
        Double amount = service.doRecharge("FRRMNG56P12F854D","11111", "cardType", "cardNumber", "a_line", "a_line", "10.00", "", Constants.Subsystems.MYTIMAPP.name());
        assertEquals(new Double(25d), amount);
    }

    @Test
    public void rechargeWithScratchCardOk() throws Exception {
        Mockito.when(creditProxy.authorizeLineForRecharge(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new RechargeAuthorizationResponse("A","a_line","0"));
        Mockito.when(creditProxy.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
                .thenReturn(new ScratchCardResponse("a_line", "25.00", "0", "card", "tid"));

        Double amount = service.doRecharge("FRRMNG56P12F854D","a_line", "cardNumber", "cardType", "a_line", "10.00", "", "11111", Constants.Subsystems.MYTIMAPP.name());
        assertEquals(new Double(25d), amount);
    }

}