package it.tim.topup.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by alongo on 30/04/18.
 */
public class GenericException extends ErrorResponseException {

    public static final String OUTPUT_MESSAGE = "Errore generico non è stato possibile portare a termine l’esecuzione del metodo";
    public static final String ERROR_CODE = "RIC006";

    public GenericException(String message) {
        super(message, OUTPUT_MESSAGE, ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause, OUTPUT_MESSAGE, ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public GenericException(String code, String message) {
        super(message, message, code, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
