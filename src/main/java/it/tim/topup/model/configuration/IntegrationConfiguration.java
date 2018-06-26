package it.tim.topup.model.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by alongo on 26/04/18.
 */
@ConfigurationProperties(prefix = "integration")
@Data
@Component
public class IntegrationConfiguration {

    private SdpConfiguration sdp;
    private SoapConfiguration soap;

    @Data
    public static class SdpConfiguration {
    	private String ismobilepath;
        private String blpath;
        private String scpath;
        private String rolpath;
        private String cmsbasepath;
    }

    @Data
    public static class SoapConfiguration {
        private String sellabasepath;
        private String keystorepath;
    }

}
