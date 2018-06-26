package it.tim.topup.integration.proxy;

import it.tim.topup.integration.client.CmsClient;
import it.tim.topup.model.integration.TermsAndConditionsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

/**
 * Created by alongo on 02/05/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CmsProxyDelegateTest {

    @Mock
    CmsClient client;

    CmsProxyDelegate delegate;

    @Before
    public void init(){
        delegate = new CmsProxyDelegate(client);
    }

    @After
    public void cleanup(){
        Mockito.reset(client);
    }

    @Test
    public void getTermsAndConditionsOkResponse() throws Exception {
        //GIVEN
        Mockito.when(client.getTermsAndConditions())
                .thenReturn(new ResponseEntity<>(new TermsAndConditionsResponse("a title", "a body"), HttpStatus.OK));
        //WHEN
        ResponseEntity<TermsAndConditionsResponse> out = delegate.getTermsAndConditions();
        //THEN
        assertEquals(HttpStatus.OK, out.getStatusCode());
        TermsAndConditionsResponse body = out.getBody();
        assertNotNull(body);
        assertEquals("a title", body.getTitle());
        assertEquals("a body", body.getText());
    }

    @Test
    public void getTermsAndConditionsKoResponse() throws Exception {
        //GIVEN
        ResponseEntity<TermsAndConditionsResponse> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(client.getTermsAndConditions()).thenReturn(response);
        //WHEN
        ResponseEntity<TermsAndConditionsResponse> out = delegate.getTermsAndConditions();
        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, out.getStatusCode());
        assertNull(out.getBody());
    }

    @Test
    public void reliableGetTermsAndConditions() throws Exception {
        ResponseEntity<TermsAndConditionsResponse> out = delegate.reliableGetTermsAndConditions(null);
        assertEquals(ProxyTemplate.getFallbackResponse(null).getStatusCode(), out.getStatusCode());
    }

}