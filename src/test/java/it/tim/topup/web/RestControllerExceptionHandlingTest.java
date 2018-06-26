package it.tim.topup.web;

import it.tim.topup.model.exception.*;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by alongo on 02/05/18.
 */
public class RestControllerExceptionHandlingTest {

    private RestControllerExceptionHandling errorHandler = new RestControllerExceptionHandling();

    @Test
    public void handleGenericException() throws Exception {
        assertNotNull(errorHandler.handleErrorResponseException(new GenericException("An exception", new RuntimeException("a cause")), null));
    }

    @Test
    public void handleBadRequestException() throws Exception {
        assertNotNull(errorHandler.handleErrorResponseException(new BadRequestException("An exception"), null));
    }

    @Test
    public void handleSubsystemException() throws Exception {
        assertNotNull(errorHandler.handleErrorResponseException(new SubsystemException("A Subsystem", "An exception", "An exception message"), null));
    }

    @Test
    public void handleNotAuthorizedException() throws Exception {
        assertNotNull(errorHandler.handleErrorResponseException(new NotAuthorizedException("An exception"), null));
    }

    @Test
    public void handleCannotRechargeException() throws Exception {
        assertNotNull(errorHandler.handleErrorResponseException(new CannotRechargeException("An exception", "KO"), null));
    }

}