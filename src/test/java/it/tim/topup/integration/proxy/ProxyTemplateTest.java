package it.tim.topup.integration.proxy;

import it.tim.topup.model.exception.SubsystemException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

/**
 * Created by alongo on 27/04/18.
 */
public class ProxyTemplateTest {

    public static final String A_SUBSYSTEM = "A SUBSYSTEM";
    public static final String A_BODY = "A_BODY";

    ProxyTemplate template;

    @Before
    public void init(){
        template = new ProxyTemplate() {
            @Override
            String getSubsystemName() {
                return A_SUBSYSTEM;
            }
        };
    }

    @Test
    public void getSubsystemName() throws Exception {
        assertEquals(A_SUBSYSTEM, template.getSubsystemName());
    }

    @Test(expected = SubsystemException.class)
    public void validateKoOnNull() throws Exception {
        template.validate(null);
    }

    @Test(expected = SubsystemException.class)
    public void validateKoOnVoid() throws Exception {
        template.validate(new ResponseEntity<>(HttpStatus.BAD_GATEWAY));
    }

    @Test
    public void validateOk() throws Exception {
        template.validate(new ResponseEntity<>(HttpStatus.OK));
    }

    @Test(expected = SubsystemException.class)
    public void getBodyInvalidResponse() throws Exception {
        assertFalse(template.getBody(null));
    }

    @Test
    public void getBodyOkResponse() throws Exception {
        String body = template.getBody(new ResponseEntity<>(A_BODY, HttpStatus.OK));
        assertEquals(A_BODY, body);
    }

    @Test
    public void getFallBackResponseFromNullThrowable() throws Exception {
        ResponseEntity<Object> fallbackResponse = ProxyTemplate.getFallbackResponse(null);
        assertNotNull(fallbackResponse);
        assertNotNull(fallbackResponse.getHeaders());
        assertNull(fallbackResponse.getHeaders().getFirst(ProxyTemplate.ERROR_CLASS_HEADER));
        assertNull(fallbackResponse.getHeaders().getFirst(ProxyTemplate.ERROR_MSG_HEADER));
    }

    @Test
    public void getFallBackResponseFromNotNullThrowable() throws Exception {
        String message = "an exception message";
        RuntimeException exception = new RuntimeException(message);
        ResponseEntity<Object> fallbackResponse = ProxyTemplate.getFallbackResponse(exception);
        assertNotNull(fallbackResponse);
        assertNotNull(fallbackResponse.getHeaders());
        assertEquals(RuntimeException.class.getName(), fallbackResponse.getHeaders().getFirst(ProxyTemplate.ERROR_CLASS_HEADER));
        assertEquals(message, fallbackResponse.getHeaders().getFirst(ProxyTemplate.ERROR_MSG_HEADER));
    }

    @Test
    public void getHeaderInvalidResponse() throws Exception {
        assertNull(template.getHeader(null, null));
        assertNull(template.getHeader(new ResponseEntity<>(HttpStatus.OK), ""));
        assertNull(template.getHeader(new ResponseEntity<>(HttpStatus.OK), null));
        assertNull(template.getHeader(new ResponseEntity<>(HttpStatus.OK), "a string"));
    }

    @Test
    public void getHeaderOkResponse() throws Exception {

        String message = "an exception message";
        RuntimeException exception = new RuntimeException(message);
        ResponseEntity<Object> fallbackResponse = ProxyTemplate.getFallbackResponse(exception);

        assertEquals(message, template.getHeader(fallbackResponse, ProxyTemplate.ERROR_MSG_HEADER));
    }


}