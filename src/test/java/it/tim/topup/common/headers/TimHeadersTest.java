package it.tim.topup.common.headers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TimHeadersTest {

    @Test
    public void setHeaderOk() {
        TimHeaders timHeaders = new TimHeaders();
        timHeaders.setHeader(TransientHeaderName.SOURCESYSTEM, "a value");
        assertEquals("a value", timHeaders.getHeader(TransientHeaderName.SOURCESYSTEM));
    }

    @Test
    public void setHeaderDoesNothingEmptyValues() {
        TimHeaders timHeaders = new TimHeaders();
        timHeaders.setHeader(TransientHeaderName.SOURCESYSTEM, null);
        assertNull(timHeaders.getHeader(TransientHeaderName.SOURCESYSTEM));
        timHeaders.setHeader(TransientHeaderName.SOURCESYSTEM, "");
        assertNull(timHeaders.getHeader(TransientHeaderName.SOURCESYSTEM));
    }

    @Test
    public void getHeaderNullOnEmptyMap() {
        TimHeaders timHeaders = new TimHeaders();
        assertNull(timHeaders.getHeader(TransientHeaderName.SOURCESYSTEM));
    }

    @Test
    public void getSession() {
        TimHeaders timHeaders = new TimHeaders();
        assertNull(timHeaders.getSession());
        timHeaders.setSession(new TimSession());
        assertNotNull(timHeaders.getSession());
    }

    @Test
    public void getTransientHeaders() {
        TimHeaders timHeaders = new TimHeaders();
        timHeaders.setHeader(TransientHeaderName.SOURCESYSTEM, "a value");
        assertNotNull(timHeaders.getTransientHeaders());
    }

    @Test
    public void lombokDataAnnotationOnSession() {
        TimSession session = new TimSession();
        session.setUserReference("UserReference");
        session.setUserAccount("UserAccount");
        session.setAccountType("AccountType");
        session.setDcaCoockie("DcaCoockie");

        assertEquals("UserReference", session.getUserReference());
        assertEquals("UserAccount", session.getUserAccount());
        assertEquals("AccountType", session.getAccountType());
        assertEquals("DcaCoockie", session.getDcaCoockie());
    }
}