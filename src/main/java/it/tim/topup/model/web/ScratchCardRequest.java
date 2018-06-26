package it.tim.topup.model.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by alongo on 30/04/18.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScratchCardRequest {

    @JsonProperty("scratchCode")
    @ApiModelProperty(notes = "16 digits scratch card's ID", required = true)
    private String cardNumber;
    @ApiModelProperty(notes = "MSISDN requesting the refill", required = true)
    private String fromMsisdn;
    @ApiModelProperty(notes = "MSISDN receiving the refill", required = true)
    private String toMsisdn;
    @ApiModelProperty(notes = "Refill's source system (MYTIMAPP from APP, MYTIMWEB from WEB)", required = true)
    private String subSys;
}
