package it.tim.topup.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tim.topup.common.headers.TimHeaders;
import it.tim.topup.common.headers.TimHeadersExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by alongo on 10/05/18.
 */
@Component
@Slf4j
public class HeadersInterceptor extends HandlerInterceptorAdapter {

    private TimHeaders headers;
    private ObjectMapper objectMapper;

    @Autowired
    public HeadersInterceptor(TimHeaders headers, ObjectMapper objectMapper) {
        this.headers = headers;
        this.objectMapper = objectMapper;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try{
            TimHeadersExtractor.populateHeaders(headers, objectMapper, request);
        }catch (Exception e){
            log.error("An error occured while extracting headers from request",e);
        }
        return true;
    }

}
