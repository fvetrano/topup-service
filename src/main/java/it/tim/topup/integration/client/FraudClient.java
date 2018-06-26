package it.tim.topup.integration.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import it.tim.topup.model.integration.FraudPreventionRequest;
import it.tim.topup.model.integration.FraudPreventionResponse;

@FeignClient(
        name="fraud",
        url = "${integration.sdp.fraudpath}"
)
public interface FraudClient {
    @PostMapping("/SdpRiskApi/clienti/frodi/prevenzione")
    ResponseEntity<FraudPreventionResponse> fraudsPrevention(@RequestBody FraudPreventionRequest request,
    		@RequestHeader("transactionID") String tid);
}
