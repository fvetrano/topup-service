package it.tim.topup.integration.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import it.tim.topup.model.integration.RechargeAuthorizationResponse;

@FeignClient(
        name="blacklist",
        url = "${integration.sdp.blpath}"
)
public interface BlackListClient {
    @PostMapping(value = "/v1/clienti/{rifCliente}/consistenze/{numLinea}/credito/ricarica/blacklist")
    ResponseEntity<RechargeAuthorizationResponse> authorizeLineForRecharge(
    		@PathVariable("rifCliente") String rifCliente,
            @PathVariable("numLinea") String numLinea,
    		@RequestBody String transactionIdLegacy);
}
	