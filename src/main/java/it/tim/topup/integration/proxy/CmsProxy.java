package it.tim.topup.integration.proxy;

import it.tim.topup.model.integration.TermsAndConditionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by alongo on 02/05/18.
 */
@Component
public class CmsProxy extends ProxyTemplate {

    static final  String SUBSYSTEM_NAME = "CMS Service";

    private CmsProxyDelegate delegate;

    @Autowired
    public CmsProxy(CmsProxyDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    String getSubsystemName() {
        return SUBSYSTEM_NAME;
    }

    public TermsAndConditionsResponse getTermsAndConditions(){
        return getBody(delegate.getTermsAndConditions());
    }

}
