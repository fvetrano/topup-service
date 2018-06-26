package it.tim.topup.model.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineRefillRequest {

    private String subSys;
    private String rifCliente;
    private String tiidCliente;
    private String numLinea;
    private String tipoCarta;
    private String gestorePagamento;
    private String importoRicarica;
    private String creditoResiduo;
    private String idAutorizzazionePagamento;
    private String dataAutorizzazionePagamento;
    private String importoBonus;
    private String idPromo;
    private String codicePIN;

    @JsonProperty(value = "applicazione")
    private String deviceType;
    private String referenceID;
    private String tipoRicarica;

    private String shopTransID;
    private String dataOperazione;
    private String transactionIDLegacy;
}
