package it.tim.topup.model.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by alongo on 02/05/18.
 */
@Getter
@AllArgsConstructor
public class AmountsResponse {

    @ApiModelProperty(notes = "Amounts outcome")
    private String status;

    @ApiModelProperty(notes = "Topup amounts")
    private List<Double> amounts;

    @ApiModelProperty(notes = "Default topup amount")
    @JsonProperty(value = "default")
    private Double defaultValue;

    @ApiModelProperty(notes = "Optional info bunner")
    @JsonProperty(value = "info_promo")
    private String promoInfo;


}
