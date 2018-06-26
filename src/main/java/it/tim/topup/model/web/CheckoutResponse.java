package it.tim.topup.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckoutResponse{

    @ApiModelProperty(notes = "Amounts outcome")
    private String status;

    @ApiModelProperty(notes = "Refill transaction id")
    private String transactionId;


}
