package it.tim.topup.web;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import it.tim.topup.common.headers.TimHeaders;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Component;
import static it.tim.topup.service.IdsGenerator.generateId;
import static it.tim.topup.service.IdsGenerator.generateTransactionId;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class HeaderForwardInterceptor implements RequestInterceptor{

    @Autowired
    private ApplicationContext context;

    @Override
    public void apply(RequestTemplate template) {
	
        TimHeaders headers = context.getBean(TimHeaders.class);   
        headers.getTransientHeaders().forEach((headerName, headerValue) -> template.header(headerName.value(),headerValue));
        
        log.debug("HTTP-HEADERS before transformation");
        for (Map.Entry<String, Collection<String>> entry : template.headers().entrySet()) {
        	log.debug("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
         
        template.header("sourceSystem","SDP");
        
        Date now = new Date(System.currentTimeMillis());
        
        if(!template.headers().containsKey("channel")) {
        	template.header("channel","MYTIMAPP");
        }
        
        if(!template.headers().containsKey("interactionDate-Date")) {
        	template.header("interactionDate-Date", getDate(now));
        }
        
        if(!template.headers().containsKey("interactionDate-Time")) {
        	template.header("interactionDate-Time", getTime(now));
        }
     
        if(!template.headers().containsKey("sessionID")) {
        	template.header("sessionID", generateTransactionId(24));
        }
        
        if(!template.headers().containsKey("businessID")) {
        	template.header("businessID", generateTransactionId(24));
        }
        
        if(!template.headers().containsKey("transactionID")) {
        	template.header("transactionID", generateTransactionId(24));
        }
        
        template.header("messageID", generateTransactionId(24));
        
        log.debug("HTTP-HEADERS after transformation");
        for (Map.Entry<String, Collection<String>> entry : template.headers().entrySet()) {
        	log.debug("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
    }   

	private static String getTime(Date d){
		SimpleDateFormat sdfTime = new SimpleDateFormat ( "HH:mm:ss.SSS" );
		return sdfTime.format(d);
	}

	private static String getDate(Date d){
		SimpleDateFormat sdfDate = new SimpleDateFormat ( "yyyy-MM-dd" );
		return sdfDate.format(d);
	}
	
    
}
