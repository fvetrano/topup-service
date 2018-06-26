package it.tim.topup.integration.client;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import it.tim.topup.model.integration.MobileOffersResponse;

@FeignClient(
        name="mobile",
        url = "${integration.sdp.ismobilepath}"
)
public interface MobileClient {
    @GetMapping(value = "/v1/clienti/{rifCliente}/consistenze/{numLinea}/prepagatoMobile/offerte")
    ResponseEntity<MobileOffersResponse> mobileOffers(
            @PathVariable("rifCliente") String rifCliente
            , @PathVariable("numLinea") String numLinea
            , @RequestParam("queryParams") Map<String, String> queryParams
            , @RequestHeader("transactionID") String tid
    );
}
