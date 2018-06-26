package it.tim.topup.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by alongo on 16/04/18.
 */
public class CannotRechargeException extends ErrorResponseException {

    private final String outcome;

    public CannotRechargeException(String message, String outcome) {
        super(message, null, null, null, false);
        this.outcome = outcome;
    }

    @Override
    public String getErrorCode(){
        if (outcome.equals("1")) {
            return "RIC004";
        } else {
            return "RIC006";
        }
    }

    @Override
    public String getOutputMessage() {
        if (outcome.equals("1")) {
            return "Codice scheda errato o non esistente";
        } else {
            return "Errore generico non è stato possibile portare a termine l’esecuzione del metodo";
        }
    }

    @Override
    public HttpStatus getHttpStatus() {
        if (outcome.equals("1")) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


}
