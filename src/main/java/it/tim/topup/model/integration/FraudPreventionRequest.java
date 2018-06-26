package it.tim.topup.model.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FraudPreventionRequest {

    public static final ProcessData RECHARGE_PD = new ProcessData("SDP", "500");

    private ProcessData processData;
    private CustomerAccount customerAccount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProcessData{
        //Costante: SDP
        private String subSystem;
        //Il codice
        // - 500 si riferisce alla verifica sulla ricarica
        // - 501 si riferisce all’operazione di ricarica automatica
        private String cause;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerAccount{
        private Customer customer;
        private BillingAccount billingAccount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Customer{
        private Individual individual;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Individual{
        private String fiscalCode;
        private String vatNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BillingAccount{
        private CreditCard creditCard;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditCard{
        private String cardNumber;
    }


}
