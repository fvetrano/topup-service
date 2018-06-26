package it.tim.topup.web.controller;

import it.tim.topup.common.headers.TimHeaders;
import it.tim.topup.common.headers.TimSession;
import it.tim.topup.common.headers.TransientHeaderName;
import it.tim.topup.integration.client.OBGClient;
import it.tim.topup.integration.proxy.CmsProxy;
import it.tim.topup.integration.proxy.SCDelegate;
import it.tim.topup.integration.proxy.RolProxy;

import it.tim.topup.model.configuration.BuiltInConfiguration;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.exception.BadRequestException;
import it.tim.topup.model.exception.SubsystemException;
import it.tim.topup.model.integration.GestPayS2S;
import it.tim.topup.model.integration.MobileOffersResponse;
import it.tim.topup.model.integration.OPSCData;
import it.tim.topup.model.integration.RechargeAuthorizationResponse;
import it.tim.topup.model.integration.ScratchCardResponse;
import it.tim.topup.model.integration.UserInfoResponse;

import it.tim.topup.model.web.AmountsResponse;
import it.tim.topup.model.web.CheckoutRequest;
import it.tim.topup.model.web.CheckoutResponse;
import it.tim.topup.model.web.ScratchCardRequest;
import it.tim.topup.service.CreditCardTopupService;
import it.tim.topup.service.ScratchCardService;
import it.tim.topup.service.StaticContentService;
import it.tim.topup.web.TopupController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

/**
 * Created by alongo on 30/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
//Tested as in-service integration test
public class TopupControllerTest {

    @Mock
    TimHeaders timHeaders;

    @Mock
    TimSession timHeadersSession;

    @Mock
    SCDelegate creditProxy;

    @Mock
    CmsProxy cmsProxy;

    @Mock
    RolProxy customersProxy;

    @Mock
    BuiltInConfiguration configuration;

    @Mock
    OBGClient obgClient;

    ScratchCardService scratchCardService;
    StaticContentService staticContentService;
    CreditCardTopupService creditCardTopupService;
    TopupController controller;

    @Before
    public void init(){
        scratchCardService = new ScratchCardService(customersProxy, creditProxy);
        staticContentService = new StaticContentService(configuration, cmsProxy);
        creditCardTopupService = new CreditCardTopupService(customersProxy, obgClient);

        controller = new TopupController(timHeaders, scratchCardService, staticContentService, creditCardTopupService);
    }

    @After
    public void cleanup(){
        Mockito.reset(timHeaders, cmsProxy, creditProxy, customersProxy, configuration);
        Mockito.reset(obgClient);

    }



    @Test(expected = BadRequestException.class)
    public void scratchCardKoOnInvalidRequest() throws Exception {
        controller.scratchCard(new ScratchCardRequest(), null, null, null, null, null, null);
    }

    @Test(expected = SubsystemException.class)
    public void scratchCardKoOnExternalServiceUnavailable() throws Exception {
        Mockito.when(timHeadersSession.getUserReference()).thenReturn("a reference");
        Mockito.when(timHeaders.getSession()).thenReturn(timHeadersSession);
        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject()))
                .thenThrow(new SubsystemException("Mobile offers", "TestException", "a test error"));

        controller.scratchCard(new ScratchCardRequest("1111111111111111","3400000001","3400000002", Constants.Subsystems.MYTIMAPP.name()), null, null, null, null, null, null);
    }

//    @Test
//    public void scratchCardOk() throws Exception {
//        Mockito.when(timHeadersSession.getUserReference()).thenReturn("a reference");
//        Mockito.when(timHeaders.getSession()).thenReturn(timHeadersSession);
//
//        MobileOffersResponse r1 = new MobileOffersResponse();
//        UserInfoResponse r2 = new UserInfoResponse();
//        r1.setUserInfo_Response(r2);
//        r2.setOpscdata(new OPSCData());
//        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject()))
//                .thenReturn(r1);
//        Mockito.when(creditProxy.authorizeLineForRecharge(anyString(), anyString(), anyString()))
//                .thenReturn(new RechargeAuthorizationResponse("A","a_line","0"));
//        Mockito.when(creditProxy.rechargeWithScratchCard(Mockito.anyObject(),Mockito.anyObject(),Mockito.anyObject()))
//                .thenReturn(new ScratchCardResponse("a_line", "25.00", "0", "card", "tid"));
//        it.tim.topup.model.web.ScratchCardResponse scratchCardResponse
//                = controller.scratchCard(new ScratchCardRequest("1111", "3400000001", "3400000002", Constants.Subsystems.MYTIMAPP.name()));
//
//        assertNotNull(scratchCardResponse);
//        assertNotNull(scratchCardResponse.getStatus());
//        assertEquals("OK", scratchCardResponse.getStatus());
//        assertNotNull(scratchCardResponse.getAmount());
//        assertEquals(new Double(25d), scratchCardResponse.getAmount());
//    }

    @Test
    public void termsAndConditionsOk() throws Exception {
        BuiltInConfiguration.TermsAndConditionsConfiguration t = new BuiltInConfiguration.TermsAndConditionsConfiguration();
        t.setText("a body");
        t.setTitle("a title");
        Mockito.when(configuration.getTermsAndConditions()).thenReturn(t);
        it.tim.topup.model.web.TermsAndConditionsResponse result
                = controller.termsAndConditions(null, null, null, null, null, null);
        assertNotNull(result);
        assertEquals("a title", result.getTitle());
        assertEquals("a body", result.getText());
        assertEquals("OK", result.getStatus());
    }

    @Test
    public void amountsOk() throws Exception {
        BuiltInConfiguration.AmountsConfiguration amountsConfiguration = new BuiltInConfiguration.AmountsConfiguration();
        amountsConfiguration.setValues(Arrays.asList(1d,2d,3d));
        amountsConfiguration.setDefaultValue(1d);
        amountsConfiguration.setPromoInfo("some info");
        Mockito.when(configuration.getAmounts()).thenReturn(amountsConfiguration);

        AmountsResponse result = controller.amounts(null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(3, result.getAmounts().size());
        assertEquals("some info", result.getPromoInfo());
        assertEquals(new Double(1d), result.getDefaultValue());
        assertEquals("OK", result.getStatus());
    }

    @Test(expected = BadRequestException.class)
    public void checkoutKoOnInvalidRequest() throws Exception {
        controller.checkout(new CheckoutRequest(), null, null, null, null, null, null);
    }

//    @Test
//    public void checkoutOk() throws Exception {
//
//        Mockito.when(timHeadersSession.getUserReference()).thenReturn("a reference");
//        Mockito.when(timHeaders.getSession()).thenReturn(timHeadersSession);
//        Mockito.when(timHeaders.getHeader(TransientHeaderName.DEVICE_TYPE)).thenReturn("JAVA");
//
//        /*
//        MobileOffersResponse r1 = new MobileOffersResponse();
//        UserInfoResponse r2 = new UserInfoResponse();
//        OPSCData opscData = new OPSCData();
//        r1.setUserInfo_Response(r2);
//        r2.setOpscdata(opscData);
//
//        opscData.setTypeOfCard("acard");
//        opscData.setDebit("adebit");
//        opscData..setSimStatus("astatus");
//        proxyResponse .setOpscData(opscData);
//
//        Mockito.when(customersProxy.mobileOffers(anyString(), anyString(), anyObject())).thenReturn(proxyResponse );
//        CallPagamS2SResponse resp = new CallPagamS2SResponse();
//        CallPagamS2SResponse.CallPagamS2SResult value = new CallPagamS2SResponse.CallPagamS2SResult();
//        GestPayS2S gestPayS2S = new GestPayS2S();
//        Whitebox.setInternalState(value,"content",gestPayS2S);
//        resp.setCallPagamS2SResult(value);
//        Mockito.when(obgClient.callPagamS2S(anyObject())).thenReturn(resp);
//		*/
//
//        CheckoutResponse response = controller.checkout(new CheckoutRequest(
//                "3400000001",
//                "3400000001",
//                25,
//                "374006365489900",
//                "10",
//                "2020",
//                "Alessandro Longo",
//                "1234",
//                null,
//                null,
//                null,
//                Constants.Subsystems.MYTIMAPP.name(),
//                null));
//
//        assertNotNull(response);
//        assertEquals("OK", response.getStatus());
//        //FIXME change once integration is done
//        assertEquals(10, response.getTransactionId().length());
//    }

    
    /*
    @Test
    public void composeHeaderDateTimeOnNullFields() {
        assertNull(controller.composeHeaderDateTime(null, "value"));
        assertNull(controller.composeHeaderDateTime("value", ""));
    }
    */

    
}