package it.tim.topup.model.integration;

import lombok.Data;

@Data
public class FraudPreventionResponse {

    private ProcessData processData;
    private CustomerAccount customerAccount;
    private BillingAccount billingAccount;

    @Data
    static class ProcessData{
        private String subSystem;
        private String cause;
    }

    @Data
    static class CustomerAccount{
        private Customer customer;
    }

    @Data
    static class Customer{
        private Individual individual;
    }

    @Data
    static class Individual{
        private String fiscalCode;
        private String vatNumber;
    }

    @Data
    static class BillingAccount{
        private CreditCard creditCard;
    }

    @Data
    static class CreditCard{
        private String cardNumber;
    }

}