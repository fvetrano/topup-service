package it.tim.topup.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by alongo on 13/04/18.
 */
@Getter
@AllArgsConstructor
public class ScratchCardResponse {

    @ApiModelProperty(notes = "Refill outcome")
    private String status;
    @ApiModelProperty(notes = "Refill amount")
    private Double amount;

}
