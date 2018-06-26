package it.tim.topup.integration.proxy;

import it.tim.topup.model.exception.SubsystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * Created by alongo on 27/04/18.
 */

/**
 * Implements basic behavior for Circuit Breaking proxy Classes
 */
abstract class ProxyTemplate {

    static final String ERROR_MSG_HEADER = "ERROR_MSG_HEADER";
    static final String ERROR_CLASS_HEADER = "ERROR_CLASS_HEADER";

    /**
     * Subsystem identification name
     * @return Subsystem identification name
     */
    abstract String getSubsystemName();

    /**
     * Extracts Body from Client's response
     * @param responseEntity Client's response
     * @param <T> The type of content returned by Client
     * @return if valid, the body included in the respose
     */

    <T> T getBody(ResponseEntity<T> responseEntity){
        validate(responseEntity);
        return responseEntity.getBody();
    }

    /**
     * Validates the response from Client
     * @param responseEntity the response to validateScratchCardRequest
     * @param <T> the type of content returned by Client
     * @throws SubsystemException if the response is not validated
     */
    <T> void validate(ResponseEntity<T> responseEntity){
        boolean valid = responseEntity != null
                && responseEntity.getStatusCode().is2xxSuccessful();
        if(!valid)
            throw new SubsystemException(
                    getSubsystemName(),
                    getHeader(responseEntity, ERROR_CLASS_HEADER),
                    getHeader(responseEntity, ERROR_MSG_HEADER)
            );
    }

    /**
     * Returns the circuit breaking KO respose
     * @param throwable the error that generated the circuit breaking
     * @param <T> the type of content returned by Client
     * @return the fallback response
     */
    static  <T> ResponseEntity<T> getFallbackResponse(Throwable throwable){
        ResponseEntity<T> responseEntity;
        if(throwable != null) {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put(ERROR_MSG_HEADER, Collections.singletonList(throwable.getMessage()));
            headers.put(ERROR_CLASS_HEADER, Collections.singletonList(throwable.getClass().getName()));
            responseEntity = new ResponseEntity<>(headers, HttpStatus.FAILED_DEPENDENCY);
        }
        else
            responseEntity = new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        return responseEntity;
    }

    /**
     * Utility method that extracts an header from a given response
     * @param responseEntity Client's response
     * @param headerName Header's name
     * @param <T> Type of content returned by Client
     * @return the header value if exists, null otherwise
     */
    <T> String getHeader(ResponseEntity<T> responseEntity, String headerName){
        if(responseEntity == null
                || StringUtils.isEmpty(headerName))
            return null;
        return responseEntity.getHeaders().getFirst(headerName);
    }

}
