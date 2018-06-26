package it.tim.topup.web;

import it.tim.topup.common.headers.TimHeaders;
import it.tim.topup.common.headers.TransientHeaderName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HeadersInterceptorTest {

    @Mock
    ApplicationContext applicationContext;

    @Mock
    HttpServletRequest servletRequest;

    @Mock
    HttpServletResponse servletResponse;


    @InjectMocks
    HeadersInterceptor interceptor;

    private TimHeaders timHeaders;

    @Before
    public void setUp(){
        TimHeaders headers = new TimHeaders();
        headers.setHeader(TransientHeaderName.SOURCESYSTEM,"CBE");
        this.timHeaders = headers;
    }

    @Test
    public void apply() {
        when(applicationContext.getBean(TimHeaders.class)).thenReturn(timHeaders);
        interceptor.preHandle(servletRequest, servletResponse, null);
    }
}