package it.tim.topup.integration.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import it.tim.topup.integration.FeignConfiguration;

@FeignClient(
        name="bancaSella",
        url = "${integration.soap.sellabasepath}"
        , configuration = FeignConfiguration.class
)
public interface OBGClient {
    @PostMapping(value = "/bancasellaPayment",  produces = "application/xml", consumes = "application/xml")
    String callOBJ(@RequestBody String request );
}
