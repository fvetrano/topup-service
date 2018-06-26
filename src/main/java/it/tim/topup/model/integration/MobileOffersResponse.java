package it.tim.topup.model.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MobileOffersResponse {

	@JsonProperty("UserInfo_Response")
	private UserInfoResponse UserInfo_Response;

	public UserInfoResponse getUserInfo_Response() {
		return UserInfo_Response;
	}

	public void setUserInfo_Response(UserInfoResponse userInfo_Response) {
		UserInfo_Response = userInfo_Response;
	}

	
	

}
