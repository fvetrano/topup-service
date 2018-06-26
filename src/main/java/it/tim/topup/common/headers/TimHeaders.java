package it.tim.topup.common.headers;


import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alongo on 10/05/18.
 */
public class TimHeaders {

    public static final String SESSION_HEADER = "sessionJWT";

    private Map<TransientHeaderName, String> transientHeaders;
    private TimSession session;

    public void setHeader(TransientHeaderName name, String value){
        if(name == null) return;

        if(transientHeaders == null)
            transientHeaders = new HashMap<>();

        if(StringUtils.isEmpty(value))
            transientHeaders.remove(name);
        else
            transientHeaders.put(name, value);
    }

    public String getHeader(TransientHeaderName name){
        if(transientHeaders == null) return null;
        return transientHeaders.get(name);
    }

    public TimSession getSession() {
        return session;
    }

    public void setSession(TimSession session) {
        this.session = session;
    }

    public Map<TransientHeaderName, String> getTransientHeaders() {
        return transientHeaders;
    }

}
