package it.tim.topup.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by alongo on 30/04/18.
 */
public class BadRequestException extends ErrorResponseException {

    public BadRequestException(String message) {
        super(message, "Parametri della richiesta mancanti o errati", "RIC002", HttpStatus.BAD_REQUEST);
    }

}
