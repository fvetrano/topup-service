package it.tim.topup.model.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by alongo on 02/05/18.
 */
@Getter
@AllArgsConstructor
public class TermsAndConditionsResponse {

    @ApiModelProperty(notes = "Terms and conditions outcome")
    private String status;
    @ApiModelProperty(notes = "Terms and conditions title")
    private String title;
    @ApiModelProperty(notes = "Terms and conditions text")
    private String text;


}
