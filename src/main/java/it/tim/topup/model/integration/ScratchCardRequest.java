package it.tim.topup.model.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by alongo on 13/04/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScratchCardRequest {
	
    private String numLinea;
    private String codiceScratchCard;
    private String tipoCarta;
    private String numLineaDonatore;
    private String creditoResiduo;
    private String creditoAziendale;
    private String transactionIdLegacy;
    private String subSys;

}
