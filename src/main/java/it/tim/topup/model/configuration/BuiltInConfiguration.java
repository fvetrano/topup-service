package it.tim.topup.model.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by alongo on 26/04/18.
 */
@ConfigurationProperties(prefix = "builtin")
@Data
@Component
public class BuiltInConfiguration {

    private String keystorepath;
    private AmountsConfiguration amounts;
    private TermsAndConditionsConfiguration termsAndConditions;

    
    
    public BuiltInConfiguration() {
	}

	public BuiltInConfiguration(String keystorepath, AmountsConfiguration amounts,
			TermsAndConditionsConfiguration termsAndConditions) {
		super();
		this.keystorepath = keystorepath;
		this.amounts = amounts;
		this.termsAndConditions = termsAndConditions;
	}

	@Data
    public static class AmountsConfiguration{
        private List<Double> values;
        private Double defaultValue;
        private String promoInfo;
    }

    @Data
    public static class TermsAndConditionsConfiguration{
        private String title;
        private String text;
    }

}
