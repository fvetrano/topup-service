package it.tim.topup.common.headers;

/**
 * Created by alongo on 10/05/18.
 */
public enum TransientHeaderName {

    SESSIONID("sessionID"),
    BUSINESSID("businessID"),
    MESSAGEID("messageID"),
    TRANSACTIONID("transactionID"),
    SOURCESYSTEM("sourceSystem"),
    CHANNEL("channel"),
    INTERACTION_DATE("interactionDate-Date"),
    INTERACTION_TIME("interactionDate-Time"),
    DEVICE_TYPE("deviceType"),
	CLIENT_VERSION("clientVersion");
	 
    private String headerName;

    TransientHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String value() {
        return headerName;
    }
}
