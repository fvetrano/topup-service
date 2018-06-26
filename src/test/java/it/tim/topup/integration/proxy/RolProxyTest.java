package it.tim.topup.integration.proxy;

import it.tim.topup.model.integration.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class RolProxyTest {

    @Mock
    RolProxyDelegate delegate;

    RolProxy proxy;

    @Before
    public void init(){
        proxy = new RolProxy(delegate);
    }

    @After
    public void cleanup(){
        Mockito.reset(delegate);
    }

    @Test
    public void getSubsystemName() throws Exception {
        assertEquals(RolProxy.SUBSYSTEM_NAME, proxy.getSubsystemName());
    }

    @Test
    public void onlineRefill() {
        Mockito.when(delegate.onlineRefill(anyObject()))
                .thenReturn(ResponseEntity.ok().build());
        proxy.onlineRefill(new OnlineRefillRequest());
    }

//    @Test
//    public void mobileOffers() {
//        MobileOffersResponse clientResponse = new MobileOffersResponse();
//        Mockito.when(delegate.mobileOffers(anyString(), anyString(), anyObject()))
//                .thenReturn(new ResponseEntity<>(clientResponse, HttpStatus.OK));
//
//        MobileOffersResponse response
//                = proxy.mobileOffers("aCustomer","aLine", new MobileOffersRequest());
//
//        assertNotNull(response);
//    }

    @Test
    public void fraudsPrevention() {
        FraudPreventionResponse clientResponse = new FraudPreventionResponse();
        Mockito.when(delegate.fraudsPrevention(anyObject()))
                .thenReturn(new ResponseEntity<>(clientResponse, HttpStatus.OK));

        FraudPreventionResponse response
                = proxy.fraudsPrevention(new FraudPreventionRequest());

        assertNotNull(response);
    }
}