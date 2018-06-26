package it.tim.topup.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by alongo on 02/05/18.
 */
@Getter
@AllArgsConstructor
public class Amounts {

    private List<Double> values;
    private Double defaultValue;
    private String promoInfo;

}
