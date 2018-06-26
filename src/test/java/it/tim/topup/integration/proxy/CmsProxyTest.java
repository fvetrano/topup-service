package it.tim.topup.integration.proxy;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alongo on 02/05/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CmsProxyTest {

    @Mock
    CmsProxyDelegate delegate;

    CmsProxy proxy;

    @Before
    public void init(){
        proxy = new CmsProxy(delegate);
    }

    @After
    public void cleanup(){
        Mockito.reset(delegate);
    }


    @Test
    public void getSubsystemName() throws Exception {
        assertEquals(CmsProxy.SUBSYSTEM_NAME, proxy.getSubsystemName());
    }

    @Test
    public void getTermsAndConditions() throws Exception {
        //GIVEN
        Mockito.when(delegate.getTermsAndConditions())
                .thenReturn(new ResponseEntity<>(new TermsAndConditionsResponse("a title", "a body"), HttpStatus.OK));
        //WHEN
        TermsAndConditionsResponse out = proxy.getTermsAndConditions();
        //THEN
        assertNotNull(out);
        assertEquals("a title", out.getTitle());
        assertEquals("a body", out.getText());
    }

}