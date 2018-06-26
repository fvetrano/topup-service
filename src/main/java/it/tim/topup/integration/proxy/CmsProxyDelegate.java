package it.tim.topup.integration.proxy;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.tim.topup.integration.client.CmsClient;
import it.tim.topup.model.integration.TermsAndConditionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by alongo on 02/05/18.
 */
@Component
public class CmsProxyDelegate {

    private CmsClient cmsClient;

    @Autowired
    public CmsProxyDelegate(CmsClient cmsClient) {
        this.cmsClient = cmsClient;
    }

    @HystrixCommand(fallbackMethod = "reliableGetTermsAndConditions")
    public ResponseEntity<TermsAndConditionsResponse> getTermsAndConditions() {
        return cmsClient.getTermsAndConditions();
    }


    //FALLBACK
    @SuppressWarnings("unused")
    ResponseEntity<TermsAndConditionsResponse> reliableGetTermsAndConditions(Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }

}
