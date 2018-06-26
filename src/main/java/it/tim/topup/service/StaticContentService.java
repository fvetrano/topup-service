package it.tim.topup.service;

import it.tim.topup.integration.proxy.CmsProxy;
import it.tim.topup.model.configuration.BuiltInConfiguration;
import it.tim.topup.model.domain.Amounts;
import it.tim.topup.model.domain.TermsAndConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alongo on 02/05/18.
 */
@Service
public class StaticContentService {

    @SuppressWarnings("unused")
    private CmsProxy cmsProxy;

    private BuiltInConfiguration configuration;

    @Autowired
    public StaticContentService(BuiltInConfiguration configuration, CmsProxy cmsProxy) {
        this.configuration = configuration;
        this.cmsProxy = cmsProxy;
    }

    public TermsAndConditions getTermsAndConditions(){
        BuiltInConfiguration.TermsAndConditionsConfiguration termsAndConditions = configuration.getTermsAndConditions();
        return new TermsAndConditions(termsAndConditions.getTitle(),termsAndConditions.getText());
    }

    public Amounts getAmounts(){
        BuiltInConfiguration.AmountsConfiguration amounts = configuration.getAmounts();
        return new Amounts(amounts.getValues(), amounts.getDefaultValue(), amounts.getPromoInfo());
    }

}
