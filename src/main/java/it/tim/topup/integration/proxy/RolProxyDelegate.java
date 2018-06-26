package it.tim.topup.integration.proxy;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import it.tim.topup.integration.client.FraudClient;
import it.tim.topup.integration.client.MobileClient;
import it.tim.topup.integration.client.RolClient;
import it.tim.topup.model.integration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.Map;


import static it.tim.topup.service.IdsGenerator.generateTransactionId;

/**
 * Created by alongo on 02/05/18.
 */
@Component
public class RolProxyDelegate {

	private MobileClient mobileClient;
    private FraudClient fraudClient;
    private RolClient rolClient;
  
    @Autowired
    public RolProxyDelegate(MobileClient mobileClient, FraudClient fraudClient, RolClient rolClient) {
		this.mobileClient = mobileClient;
		this.fraudClient = fraudClient;
		this.rolClient = rolClient;
	}

	@HystrixCommand(fallbackMethod = "reliableOnlineRefill")
    public ResponseEntity onlineRefill(OnlineRefillRequest request) {


    	String tid = generateTransactionId(24);
        return rolClient.onlineRefill(request, tid);
    }

    @HystrixCommand(fallbackMethod = "reliableMobileOffers")
    public ResponseEntity<MobileOffersResponse> mobileOffers(String rifCliente, String numLinea
            , Map<String, String> request){
    	
    	String tid = generateTransactionId(24);
        return mobileClient.mobileOffers(rifCliente, numLinea, request, tid);
    }

    @HystrixCommand(fallbackMethod = "reliableFraudsPrevention")
    public ResponseEntity<FraudPreventionResponse> fraudsPrevention(FraudPreventionRequest request){
    	
    	String tid = generateTransactionId(24);
    	
        return fraudClient.fraudsPrevention( request, tid);
    }

    //FALLBACK
    @SuppressWarnings("unused")
    ResponseEntity reliableOnlineRefill(OnlineRefillRequest request, Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }

    @SuppressWarnings("unused")
    ResponseEntity<MobileOffersResponse> reliableMobileOffers(String rifCliente, String numLinea
            , Map<String, String> request, Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }

    @SuppressWarnings("unused")
    public ResponseEntity<FraudPreventionResponse> reliableFraudsPrevention( FraudPreventionRequest request, Throwable throwable){
        return ProxyTemplate.getFallbackResponse(throwable);
    }

    
   
    
}
