package it.tim.topup.controller.component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class DiagnosticsComponent implements Ordered {

	
	private static final Logger logger = LoggerFactory.getLogger(it.tim.topup.controller.component.DiagnosticsComponent.class);
	
	
	@SuppressWarnings("unchecked")
	@Around("execution(* it.tim.topup.web.TopupController.*(..))")
     public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {

		    MethodSignature signature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		    Method method = signature.getMethod();
		    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		    Object[] args = joinPoint.getArgs();
		    
		    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		    MDC.put("remote-address",request.getRemoteAddr());

		    for (int i =0;i<args.length;i++) {
		    	for (Annotation annotation : parameterAnnotations[i]) {
	                if (!(annotation instanceof RequestHeader))
	                    continue;
	                RequestHeader requestHeader = (RequestHeader) annotation;
	                String key = requestHeader.value();
	                String value="";
	                if (args[i] instanceof Optional && ((Optional<String>) args[i]).isPresent()){
	                	value = ((Optional<String>) args[i]).get(); 
	                } else if (args[i] instanceof String) {
	                	value = (String) args[i];
	                } else if (args[i] instanceof Integer) {
	                	value = (Integer) args[i]+"";
	                }else{
	                	continue;
	                }
	                MDC.put(key,value);

	                if(key!=null && ( key.equals("x-up-forwarded-for") || key.equals("x-forwarded-for") ) && !value.equals("")){
	                	String[] v = value.split(",");
	                	if (v!=null && v.length>0)
	                	MDC.put("remote-address",v[0].trim());
	                }
	                
		    	}               
			}		    

            
		    
		    
		    MDC.put("service",method.getName());
		    
//		    MDC.put("category","DIAGNOSTIC");

		    logger.debug("MDC loaded.");

		    try {
		        // execute the original method.
		        return joinPoint.proceed();

		      } finally {
		        // stops execution measurement and log the data.
		        MDC.clear();
		  }
		    
     }


	@Override
	public int getOrder() {
		return 1;
	}
	
}
