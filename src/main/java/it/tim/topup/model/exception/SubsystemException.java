package it.tim.topup.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by alongo on 27/04/18.
 */
public class SubsystemException extends ErrorResponseException {

    private static final String ERROR_PATTERN = "failure '%s' on subsystem '%s': %s";
    private static final String ERROR_MESSAGE = "Errore generico non è stato possibile portare a termine l’esecuzione del metodo";

    public SubsystemException(String subsystem, String causeException, String causeMsg) {
        super(String.format(ERROR_PATTERN,causeException, subsystem, causeMsg),ERROR_MESSAGE,"RIC006", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
