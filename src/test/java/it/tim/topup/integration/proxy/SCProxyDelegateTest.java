package it.tim.topup.integration.proxy;

import it.tim.topup.integration.client.BlackListClient;
import it.tim.topup.integration.client.ScratchCardClient;
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
public class SCProxyDelegateTest {

    @Mock
    BlackListClient blClient;
    
    @Mock
    ScratchCardClient scClient;

    SCProxyDelegate delegate;

    @Before
    public void init(){
        delegate = new SCProxyDelegate(blClient, scClient);
    }

    @After
    public void cleanup(){
        Mockito.reset(blClient, scClient);
    }

    @Test
    public void authorizeLineForRechargeOkResponse(){

        //GIVEN
        String lineNumber = "lineNumber";
        RechargeAuthorizationResponse authResponse = new RechargeAuthorizationResponse("A",lineNumber,"OK");
        ResponseEntity<RechargeAuthorizationResponse> response = new ResponseEntity<RechargeAuthorizationResponse>(authResponse, HttpStatus.OK);
        Mockito.when(blClient.authorizeLineForRecharge(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);

        //WHEN
        String legacyTid = generateTransactionId(24);
        ResponseEntity<RechargeAuthorizationResponse> out = delegate.authorizeLineForRecharge("ASDFGH72P12F839H", lineNumber, legacyTid);

        //THEN
        assertEquals(HttpStatus.OK, out.getStatusCode());
        assertNotNull(out.getBody());
        //assertEquals(lineNumber, out.getBody().getNumLinea());

    }

    @Test
    public void authorizeLineForRechargeKOResponse(){

        //GIVEN
        String lineNumber = "lineNumber";
        ResponseEntity<RechargeAuthorizationResponse> response = new ResponseEntity<RechargeAuthorizationResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(blClient.authorizeLineForRecharge(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);

        //WHEN
        String legacyTid = generateTransactionId(24);
        ResponseEntity<RechargeAuthorizationResponse> out = delegate.authorizeLineForRecharge("ASDFGH72P12F839H", lineNumber, legacyTid);

        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, out.getStatusCode());
        assertNull(out.getBody());

    }

    @Test
    public void reliableAuthorizeLineForRecharge(){
        ResponseEntity<RechargeAuthorizationResponse> out = delegate.reliableAuthorizeLineForRecharge("refCli","lineNumber","tid", null);
        assertEquals(ProxyTemplate.getFallbackResponse(null).getStatusCode(), out.getStatusCode());
    }


    @Test
    public void rechargeWithScratchCardOkResponse(){

        //GIVEN
        String card = "card";
        String amount = "10";
        String lineNumber = "lineNumber";
        ScratchCardRequest request = new ScratchCardRequest();
        
        ScratchCardResponse response = new ScratchCardResponse(lineNumber, amount, "0", card, "tid");
        ResponseEntity<ScratchCardResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        Mockito.when(scClient.rechargeWithScratchCard(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(responseEntity);

        //WHEN
        ResponseEntity<ScratchCardResponse> out = delegate.rechargeWithScratchCard("referName", lineNumber, request);

        //THEN
        assertEquals(HttpStatus.OK, out.getStatusCode());
        assertNotNull(out.getBody());
        assertEquals(lineNumber, out.getBody().getNumLinea());
        assertEquals(amount, out.getBody().getImporto());

    }

    @Test
    public void rechargeWithScratchCardKOResponse(){

        //GIVEN
        ScratchCardRequest request = new ScratchCardRequest();
        ResponseEntity<ScratchCardResponse> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(scClient.rechargeWithScratchCard(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(responseEntity);

        //WHEN
        ResponseEntity<ScratchCardResponse> out = delegate.rechargeWithScratchCard("referName", "lineNumber", request);

        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, out.getStatusCode());
        assertNull(out.getBody());

    }

    @Test
    public void reliableRechargeWithScratchCard(){
        ResponseEntity<ScratchCardResponse> out = delegate.reliableRechargeWithScratchCard("referName", "3331212121", new ScratchCardRequest(), null);
        assertEquals(ProxyTemplate.getFallbackResponse(null).getStatusCode(), out.getStatusCode());
    }

}