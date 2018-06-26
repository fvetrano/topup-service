package it.tim.topup.controller.component;

import org.aspectj.lang.ProceedingJoinPoint;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
//import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Aspect
@Component
public class RestLoggerComponent   implements Ordered {
	
//	@Autowired
//	private Environment env = null;
	
	@Around("execution(public * it.tim.topup.web.TopupController.*(..))")
	public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
//		String enabled = env.getProperty("logging.rest.controller", String.class);
//		if(enabled.indexOf(joinPoint.getTarget().getClass().getCanonicalName()) == -1) return joinPoint.proceed();
		
		Object result=null;
		String executionResult=null;
		
		final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getCanonicalName());
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		MethodSignature signature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = signature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Object[] args = joinPoint.getArgs();
		
		String body = null;
		String username = "";
		for (int i = 0; i < args.length; i++) {
			for (Annotation annotation : parameterAnnotations[i]) {
//				if (annotation instanceof RequestBody) {
//					body = (String) args[i];
//					continue;
//				}
				if (annotation instanceof RequestHeader && "Remote-User".equalsIgnoreCase(((RequestHeader) annotation).value())) {
					username = (String) args[i];
					continue;
				}
			}
		}
		
		if (username != null) {
			MDC.put("username", username);
		}
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		if (body != null) {
			logger.info("Called API {}:{}, method [{}] with body {}", request.getMethod(), request.getServletPath(), method.getName(), body);
		} else {
			logger.info("Called API {}:{}, method [{}]", request.getMethod(), request.getServletPath(), method.getName());
		}
		
		try {
			// execute the original method.
			result = joinPoint.proceed();
			
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
			executionResult=response.getStatus()+"";

		} catch (Exception ex) {			

			
			HttpStatus status=HttpStatus.INTERNAL_SERVER_ERROR;
			ResponseStatus annotation = findAnnotation(ex.getClass(), ResponseStatus.class);
			if (annotation!=null){
				status = annotation.code();
			}
			
			MDC.put("execution-result-detail", ex.getMessage());
			executionResult = status.value()+"";

			logger.error("Error '{}' for API {}:{}, method [{}]", ex.getMessage(), request.getMethod(), request.getServletPath(), method.getName());
			
			throw ex;
		} finally {
			// stops execution measurement and log the data.
			stopWatch.stop();
			String detail = MDC.get("execution-result-detail");
			MDC.put("execution-result", executionResult);
			MDC.put("execution-time", stopWatch.getTotalTimeMillis()+"");
			
			logger.info("Response for API {}:{}{}, method [{}] in {}ms",
					request.getMethod(),
					request.getServletPath(),
					detail!=null?" - "+detail:"",
					method.getName(),
					stopWatch.getTotalTimeMillis());//,
//					response); //TODO trovare come fare print della response
			
			
			MDC.clear();
		}
		return result;
	}

	@Override
	public int getOrder() {
		return 1000;
	}
}
