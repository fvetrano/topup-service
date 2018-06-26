package it.tim.topup.integration.client;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import it.tim.topup.model.integration.OnlineRefillRequest;

@FeignClient(
        name="rol",
        url = "${integration.sdp.rolpath}"
)
public interface RolClient {
    @PostMapping("/v1/clienti/consistenze/ricaricaOnLine")
    ResponseEntity onlineRefill(@RequestBody OnlineRefillRequest request, @RequestHeader("transactionID") String tid);
}
