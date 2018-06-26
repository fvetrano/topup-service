package it.tim.topup.integration.proxy;

import it.tim.topup.model.integration.RechargeAuthorizationResponse;
import it.tim.topup.model.integration.ScratchCardRequest;
import it.tim.topup.model.integration.ScratchCardResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static it.tim.topup.service.IdsGenerator.generateTransactionId;
import static org.junit.Assert.*;

/**
 * Created by alongo on 27/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SCProxyTest {

    @Mock
    SCProxyDelegate delegate;

    SCDelegate proxy;

    @Before
    public void init(){
        proxy = new SCDelegate(delegate);
    }

    @After
    public void cleanup(){
        Mockito.reset(delegate);
    }

    @Test
    public void authorizeLineForRecharge() throws Exception {

        String lineNum = "lineNum";
        Mockito.when(delegate.authorizeLineForRecharge(Mockito.anyString(),Mockito.anyString(),Mockito.anyString()))
                .thenReturn(new ResponseEntity<>(
                        new RechargeAuthorizationResponse("A", lineNum, "OK"),
                        HttpStatus.OK));

        String legacyTid = generateTransactionId(24);
        RechargeAuthorizationResponse rechargeAuthorizationResponse = proxy.authorizeLineForRecharge("FRRCFT79P12F836G",lineNum, legacyTid);
        assertNotNull(rechargeAuthorizationResponse);
        //assertEquals(lineNum, rechargeAuthorizationResponse.getNumLinea());
        //assertEquals("A", rechargeAuthorizationResponse.getTransactionIdLegacy());
        //assertEquals("0", rechargeAuthorizationResponse.getStatoUtenza());

    }


    @Test
    public void rechargeWithScratchCard() throws Exception {
        String lineNum = "lineNum";
        String amount = "amount";
        Mockito.when(delegate.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
                .thenReturn(new ResponseEntity<>(
                		 new ScratchCardResponse(lineNum, amount, "0", "card", "tid"),
                        HttpStatus.OK));

        ScratchCardResponse scratchCardResponse = proxy.rechargeWithScratchCard("refName", lineNum, new ScratchCardRequest());
        assertNotNull(scratchCardResponse);
        assertEquals(lineNum, scratchCardResponse.getNumLinea());
        assertEquals(amount, scratchCardResponse.getImporto());
        assertEquals("0", scratchCardResponse.getEsito());
    }

    @Test
    public void getSubsystemName() throws Exception {
        assertEquals(SCDelegate.SUBSYSTEM_NAME, proxy.getSubsystemName());
    }

}