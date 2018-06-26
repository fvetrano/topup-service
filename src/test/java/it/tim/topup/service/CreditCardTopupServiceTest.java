package it.tim.topup.service;

import it.tim.topup.integration.client.OBGClient;
import it.tim.topup.integration.proxy.RolProxy;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.domain.CreditCardData;
import it.tim.topup.model.exception.GenericException;
import it.tim.topup.model.integration.FraudPreventionResponse;
import it.tim.topup.model.integration.GestPayS2S;
import it.tim.topup.model.integration.MobileOffersResponse;
import it.tim.topup.model.integration.OPSCData;
import it.tim.topup.model.integration.OnlineRefillRequest;
import it.tim.topup.model.integration.UserInfoResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardTopupServiceTest {

    @Mock
    private RolProxy customersProxy;

    @Mock
    private OBGClient obgClient;


    private CreditCardTopupService service;

    @Before
    public void init(){
        service = new CreditCardTopupService(customersProxy, obgClient);
    }

    @After
    public void cleanup(){
        Mockito.reset(customersProxy);
        Mockito.reset(obgClient);
    }

//    @Test
//    public void checkoutCF() {
//
//    	OPSCData o = new OPSCData();
//    	UserInfoResponse u = new UserInfoResponse();
//    	u.setOpscdata(o);
//        MobileOffersResponse t = new MobileOffersResponse();
//       
//        t.setUserInfo_Response(u);
//        
//        //t.getUserInfo_Response().setOpscdata(new MobileOffersResponse().getUserInfo_Response().getOpscdata());
//        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject())).thenReturn(t);
//        Mockito.when(customersProxy.fraudsPrevention(anyObject())).thenReturn(new FraudPreventionResponse());
//
//        //CallPagamS2SResponse resp = new CallPagamS2SResponse();
//        //CallPagamS2SResponse.CallPagamS2SResult value = new CallPagamS2SResponse.CallPagamS2SResult();
//       // GestPayS2S gestPayS2S = new GestPayS2S();
//        //Whitebox.setInternalState(value,"content",gestPayS2S);
//        //resp.setCallPagamS2SResult(value);
//        //Mockito.when(obgClient.callPagamS2S(anyObject())).thenReturn(resp);
//		
//        String checkout = service.checkout(
//                "LNGLSN86P10H703Z",
//                "3400000001",
//                20,
//                new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"),
//                Constants.Subsystems.MYTIMAPP.toString(),
//                "TIID",
//                "android",
//                "a date",
//                "a trans id legacy"
//        );
//    
//        Assert.assertNotNull(checkout);
//    }

//    @Test
//    public void checkoutVAT() {
//
//    	/*
//        MobileOffersResponse t = new MobileOffersResponse();
//        t.setOpscData(new MobileOffersResponse.OpscData());
//        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject())).thenReturn(t);
//        Mockito.when(customersProxy.fraudsPrevention(anyObject())).thenReturn(new FraudPreventionResponse());
//
//        CallPagamS2SResponse resp = new CallPagamS2SResponse();
//        CallPagamS2SResponse.CallPagamS2SResult value = new CallPagamS2SResponse.CallPagamS2SResult();
//        GestPayS2S gestPayS2S = new GestPayS2S();
//        Whitebox.setInternalState(value,"content",gestPayS2S);
//        resp.setCallPagamS2SResult(value);
//        Mockito.when(obgClient.callPagamS2S(anyObject())).thenReturn(resp);
//		*/
//        String checkout = service.checkout(
//                "12345678910",
//                "3400000001",
//                20,
//                new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"),
//                Constants.Subsystems.MYTIMAPP.toString(),
//                "TIID",
//                "android",
//                "a date",
//                "a trans id legacy"
//        );
//
//        Assert.assertNotNull(checkout);
//
//    }

    @Test(expected = GenericException.class)
    public void checkoutIncompleteResponseByMobileOffers() {

    	/*
        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject())).thenReturn(new MobileOffersResponse());
        Mockito.when(customersProxy.fraudsPrevention(anyObject())).thenReturn(new FraudPreventionResponse());
        CallPagamS2SResponse resp = new CallPagamS2SResponse();
        CallPagamS2SResponse.CallPagamS2SResult value = new CallPagamS2SResponse.CallPagamS2SResult();
        GestPayS2S gestPayS2S = new GestPayS2S();
        Whitebox.setInternalState(value,"content",gestPayS2S);
        resp.setCallPagamS2SResult(value);
        Mockito.when(obgClient.callPagamS2S(anyObject())).thenReturn(resp);
		*/
        String checkout = service.checkout(
                "LNGLSN86P10H703Z",
                "3400000001",
                20,
                new CreditCardData("4129339599543824", "10", "2020", "123", "Someone"),
                Constants.Subsystems.MYTIMAPP.toString(),
                "TIID",
                "android",
                "a date",
                "a trans id legacy"
        );

        Assert.assertNotNull(checkout);

    }
}