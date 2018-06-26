package it.tim.topup.integration.proxy;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import it.tim.topup.integration.client.BlackListClient;
import it.tim.topup.integration.client.ScratchCardClient;
import it.tim.topup.model.integration.RechargeAuthorizationResponse;
import it.tim.topup.model.integration.ScratchCardRequest;
import it.tim.topup.model.integration.ScratchCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
class SCProxyDelegate {

    private BlackListClient blClient;
    private ScratchCardClient scClient;

    @Autowired
    SCProxyDelegate(BlackListClient blClient, ScratchCardClient scClient) {
        this.blClient = blClient;
        this.scClient = scClient;
    }

    @HystrixCommand(fallbackMethod = "reliableAuthorizeLineForRecharge")
    ResponseEntity<RechargeAuthorizationResponse> authorizeLineForRecharge(String rifCliente, String numLinea, String transactionIdLegacy) {
        return blClient.authorizeLineForRecharge(rifCliente, numLinea, transactionIdLegacy);
    }

    @HystrixCommand(fallbackMethod = "reliableRechargeWithScratchCard")
    ResponseEntity<ScratchCardResponse> rechargeWithScratchCard(String rifCliente, String numLinea, ScratchCardRequest request){
        return scClient.rechargeWithScratchCard(rifCliente, numLinea, request);
    }

    @SuppressWarnings("unused")
    ResponseEntity<RechargeAuthorizationResponse> reliableAuthorizeLineForRecharge(String rifCliente, String numLinea, String transactionIdLegacy, Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }

    @SuppressWarnings("unused")
    ResponseEntity<ScratchCardResponse> reliableRechargeWithScratchCard(String rifCliente, String numLinea, ScratchCardRequest request, Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }
}
