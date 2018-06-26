package it.tim.topup.integration.proxy;

import it.tim.topup.model.integration.RechargeAuthorizationResponse;
import it.tim.topup.model.integration.ScratchCardRequest;
import it.tim.topup.model.integration.ScratchCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alongo on 27/04/18.
 */
@Component
public class SCDelegate extends ProxyTemplate {

    static final  String SUBSYSTEM_NAME = "Credit SDP Service";

    private SCProxyDelegate delegate;

    @Autowired
    public SCDelegate(SCProxyDelegate delegate) {
        this.delegate = delegate;
    }


    public RechargeAuthorizationResponse authorizeLineForRecharge(String rifCliente, String numLinea, String transactionIdLegacy){
        return getBody(delegate.authorizeLineForRecharge(rifCliente, numLinea, transactionIdLegacy));
    }

    public ScratchCardResponse rechargeWithScratchCard(String rifCliente, String numLinea, ScratchCardRequest request){
        return getBody(delegate.rechargeWithScratchCard(rifCliente, numLinea, request));
    }

    @Override
    String getSubsystemName() {
        return SUBSYSTEM_NAME;
    }
}
