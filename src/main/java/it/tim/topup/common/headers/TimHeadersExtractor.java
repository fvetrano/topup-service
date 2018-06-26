package it.tim.topup.common.headers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by alongo on 09/05/18.
 */
@Slf4j
public class TimHeadersExtractor {

    private TimHeadersExtractor() {}

    public static void populateHeaders(TimHeaders timHeaders, ObjectMapper objectMapper, HttpServletRequest request){

        if(timHeaders == null) throw new IllegalArgumentException("timHeaders input object must not be null");

        //Transient
        log.debug("HTTP-HEADERS received from client:");
        for(TransientHeaderName headerName : TransientHeaderName.values()) {
            timHeaders.setHeader(headerName, request.getHeader(headerName.value()));
                     
            log.debug("Key : " + headerName + " Value : " + request.getHeader(headerName.value()));
           
        }
        
        //Session
        String sessionHeader = request.getHeader(TimHeaders.SESSION_HEADER);
        if(sessionHeader != null){
            timHeaders.setSession(parseSession(sessionHeader, objectMapper));
        }

    }


    static TimSession parseSession(String sessionString, ObjectMapper objectMapper){
        if(StringUtils.isEmpty(sessionString))
            return null;

        try {
            Jwt jwt = JwtHelper.decode(sessionString);
            String decoded = jwt.getClaims();
            return objectMapper.readValue(decoded, TimSession.class);
        }catch (Exception e) {
            log.warn("Cannot parse given sessionJWT token: {}",sessionString,e);
            return null;
        }
    }

}
