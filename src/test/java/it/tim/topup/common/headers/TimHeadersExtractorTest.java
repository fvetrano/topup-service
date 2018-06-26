package it.tim.topup.common.headers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TimHeadersExtractorTest {

    @Mock
    HttpServletRequest request;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void populateHeaders(){

        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn("HEADER_VALUE");
        Mockito.when(request.getHeader(TimHeaders.SESSION_HEADER)).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlzcyI6Imh0dHBzOi8vZHQtcy1hcGlndzAxLnRlbGVjb21pdGFsaWEubG9jYWw6ODQ0MyJ9.ew0KCSJ1c2VyQWNjb3VudCI6ICJwcmltZTFAdGltLml0IiwNCgkiY2ZfcGl2YSI6ICJaSk9SSkE2MkQyMUwyMTlQIiwNCgkiZGNhQ29vY2tpZSI6ICJaamRqWldRNFlXVXRORFV3TVMwME1HTmxMV0psWkdNdFlURXhabVZtT1RWbFpESm1YMTlmUkVOQlZWUklYMEZWVkVoZlEwOVBTMGxGWDE5ZkxuUnBiUzVwZEE9PSIsDQoJImFjY291bnRUeXBlIjogIkFDQ09VTlRfVU5JQ08iDQp9.t0dJFeFFF5v2FHZkI7y2ALqg4iAGav2_XSqFYzIFpOk");
        TimHeaders timHeaders = new TimHeaders();
        TimHeadersExtractor.populateHeaders(timHeaders, objectMapper, request);

        assertNotNull(timHeaders.getTransientHeaders());
        assertNotNull(timHeaders.getSession());

    }

    @Test(expected = IllegalArgumentException.class)
    public void populateHeadersNullTimHeaders(){
        TimHeadersExtractor.populateHeaders(null, objectMapper, request);
    }

    @Test
    public void parseEmptySessionJWT() throws Exception{
        assertNull(TimHeadersExtractor.parseSession("",objectMapper));
        assertNull(TimHeadersExtractor.parseSession(null,objectMapper));
    }

    @Test
    public void parsSessionJWTproducesError() throws Exception{
        assertNull(TimHeadersExtractor.parseSession("aaaaaaaaaa",objectMapper));

    }

    @Test
    public void parseSessionJWT() throws Exception{
        TimSession timSession = TimHeadersExtractor.parseSession("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlzcyI6Imh0dHBzOi8vZHQtcy1hcGlndzAxLnRlbGVjb21pdGFsaWEubG9jYWw6ODQ0MyJ9.ew0KCSJ1c2VyQWNjb3VudCI6ICJwcmltZTFAdGltLml0IiwNCgkiY2ZfcGl2YSI6ICJaSk9SSkE2MkQyMUwyMTlQIiwNCgkiZGNhQ29vY2tpZSI6ICJaamRqWldRNFlXVXRORFV3TVMwME1HTmxMV0psWkdNdFlURXhabVZtT1RWbFpESm1YMTlmUkVOQlZWUklYMEZWVkVoZlEwOVBTMGxGWDE5ZkxuUnBiUzVwZEE9PSIsDQoJImFjY291bnRUeXBlIjogIkFDQ09VTlRfVU5JQ08iDQp9.t0dJFeFFF5v2FHZkI7y2ALqg4iAGav2_XSqFYzIFpOk", objectMapper);
        assertNotNull(timSession);
        assertNotNull(timSession.getUserReference());
        assertNotNull(timSession.getAccountType());
        assertNotNull(timSession.getUserAccount());
        assertNotNull(timSession.getDcaCoockie());
    }

}