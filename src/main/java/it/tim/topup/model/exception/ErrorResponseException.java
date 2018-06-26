package it.tim.topup.model.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by alongo on 09/05/18.
 */
public class ErrorResponseException extends RuntimeException{

    private final String outputMessage;
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final boolean toLog;

    public ErrorResponseException(String exceptionMessage, Throwable cause, String outputMessage, String errorCode, HttpStatus httpStatus, boolean toLog) {
        super(exceptionMessage, cause);
        this.outputMessage = outputMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.toLog = toLog;
    }

    public ErrorResponseException(String exceptionMessage, String outputMessage, String errorCode, HttpStatus httpStatus, boolean toLog) {
        super(exceptionMessage);
        this.outputMessage = outputMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.toLog = toLog;
    }

    public ErrorResponseException(String exceptionMessage, Throwable cause, String outputMessage, String errorCode, HttpStatus httpStatus) {
        this(exceptionMessage, cause, outputMessage, errorCode, httpStatus, true);
    }

    public ErrorResponseException(String exceptionMessage, String outputMessage, String errorCode, HttpStatus httpStatus) {
        this(exceptionMessage, outputMessage, errorCode, httpStatus, true);
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public boolean isToLog() {
        return toLog;
    }
}
