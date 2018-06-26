package it.tim.topup.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by alongo on 02/05/18.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    @ApiModelProperty(notes = "Error code")
    private String errorCode;
    @ApiModelProperty(notes = "Error message")
    private String errorMessage;

}
