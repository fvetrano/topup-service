package it.tim.topup.integration.client;

import it.tim.topup.model.integration.TermsAndConditionsResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name="cms",
        url = "${integration.sdp.cmsbasepath}/v1/cms/"
)
public interface CmsClient {

    @GetMapping("/terminiECondizioni")
    ResponseEntity<TermsAndConditionsResponse> getTermsAndConditions();

}
