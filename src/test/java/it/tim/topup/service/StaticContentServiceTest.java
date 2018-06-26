package it.tim.topup.service;

import it.tim.topup.integration.proxy.CmsProxy;
import it.tim.topup.model.configuration.BuiltInConfiguration;
import it.tim.topup.model.domain.Amounts;
import it.tim.topup.model.domain.TermsAndConditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alongo on 02/05/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticContentServiceTest {

    @Mock
    CmsProxy cmsProxy;

    @Mock
    BuiltInConfiguration configuration;

    StaticContentService service;

    @Before
    public void init(){
        service = new StaticContentService(configuration, cmsProxy);
    }

    @After
    public void cleanup(){
        Mockito.reset(cmsProxy);
    }

    @Test
    public void getTermsAndConditions() throws Exception {
        BuiltInConfiguration.TermsAndConditionsConfiguration t = new BuiltInConfiguration.TermsAndConditionsConfiguration();
        t.setText("a body");
        t.setTitle("a title");
        Mockito.when(configuration.getTermsAndConditions()).thenReturn(t);
        TermsAndConditions result = service.getTermsAndConditions();
        assertNotNull(result);
        assertEquals("a title", result.getTitle());
        assertEquals("a body", result.getText());
    }

    @Test
    public void getAmounts() throws Exception {

        BuiltInConfiguration.AmountsConfiguration amountsConfiguration = new BuiltInConfiguration.AmountsConfiguration();
        amountsConfiguration.setValues(Arrays.asList(1d,2d,3d));
        amountsConfiguration.setDefaultValue(1d);
        amountsConfiguration.setPromoInfo("some info");
        Mockito.when(configuration.getAmounts()).thenReturn(amountsConfiguration);

        Amounts result = service.getAmounts();

        assertNotNull(result);
        assertEquals(3, result.getValues().size());
        assertEquals("some info", result.getPromoInfo());
        assertEquals(new Double(1d), result.getDefaultValue());
    }

}