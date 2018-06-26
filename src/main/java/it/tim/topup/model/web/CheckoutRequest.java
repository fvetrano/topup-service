package it.tim.topup.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @ApiModelProperty(notes = "MSISDN requesting refill", required = true)
    private String fromMsisdn;
    @ApiModelProperty(notes = "MSISDN receiving refill", required = true)
    private String toMsisdn;
    @ApiModelProperty(notes = "Refill amount", required = true)
    private Integer amount;
    @ApiModelProperty(notes = "Credit card number", required = true)
    private String cardNumber;
    @ApiModelProperty(notes = "Credit card expiration month", required = true)
    private String expiryMonth;
    @ApiModelProperty(notes = "Credit card expiration year", required = true)
    private String expiryYear;
    @ApiModelProperty(notes = "Refill buyer's name", required = true)
    private String buyerName;
    @ApiModelProperty(notes = "Credit card CVV", required = true)
    private String cvv;
    @ApiModelProperty(notes = "Refill bonus text")
    private String bonus;
    @ApiModelProperty(notes = "Bonus ID")
    private String idBonus;
    @ApiModelProperty(notes = "Pin number")
    private String pinCode;
    @ApiModelProperty(notes = "Subsystem requesting recharge (MYTIMAPP from APP, MYTIMWEB from WEB)", required = true)
    private String subSys;
    @ApiModelProperty(notes = "TIID")
    private String tiid;

}
