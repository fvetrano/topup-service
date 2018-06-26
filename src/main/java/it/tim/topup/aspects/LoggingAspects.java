package it.tim.topup.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspects {

    //NEEDS @EnableAspectJAutoProxy in @Configuration to work
    ObjectMapper objectMapper;

    @Autowired
    public LoggingAspects(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(Loggable)")
    private Object log (ProceedingJoinPoint pjp) throws Throwable {

        long time = System.currentTimeMillis();
        log.info(String.format("Called method: %s", pjp.getSignature()));

        if(pjp.getArgs() != null) {
            for (Object o : pjp.getArgs()) {
                log.debug(String.format("Request Param: %s", getJson(o)));
            }
        }

        Object out = pjp.proceed();
        time = System.currentTimeMillis() - time;
        log.info(String.format("Done [in %d ms] with %s", time, pjp.getSignature()));
        log.debug(String.format("Return data: %s", getJson(out)));

        return out;
    }




    private String getJson(Object o){
        if(o == null) return "null";
        try {
            return  objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(String.format("Cannot write Json String of type %s", o.getClass()), e);
            return "null";
        }
    }

}
