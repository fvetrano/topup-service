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
public class RechargeAuthorizationResponse {
      
	  private String numLinea;
      private String statoUtenza;
      private String transactionIdLegacy;
}
