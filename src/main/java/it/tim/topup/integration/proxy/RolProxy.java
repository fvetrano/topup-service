package it.tim.topup.integration.proxy;

import it.tim.topup.model.integration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alongo on 02/05/18.
 */
@Component
public class RolProxy extends ProxyTemplate {

    static final String SUBSYSTEM_NAME = "Customers Service";

    private RolProxyDelegate delegate;

    @Autowired
    public RolProxy(RolProxyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    String getSubsystemName() {
        return SUBSYSTEM_NAME;
    }

    @SuppressWarnings("unchecked")
    public void onlineRefill(OnlineRefillRequest request){
        validate(delegate.onlineRefill(request));
    }

    public MobileOffersResponse mobileOffers(String rifCliente, String numLinea, MobileOffersRequest request) {
        return getBody(delegate.mobileOffers(rifCliente, numLinea, request.toMap()));
    }

    public FraudPreventionResponse fraudsPrevention(FraudPreventionRequest request){
        return getBody(delegate.fraudsPrevention(request));
    }

}
