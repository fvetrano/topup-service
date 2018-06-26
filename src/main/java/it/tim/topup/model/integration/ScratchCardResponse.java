package it.tim.topup.model.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScratchCardResponse {

       private String numLinea;
       private String importo;
       private String esito;
       private String codiceCartaServizi;
       private String transactionIdLegacy;     
}
