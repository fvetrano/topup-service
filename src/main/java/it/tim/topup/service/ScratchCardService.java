package it.tim.topup.service;

import it.tim.topup.integration.proxy.SCDelegate;
import it.tim.topup.integration.proxy.RolProxy;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.exception.BadRequestException;
import it.tim.topup.model.exception.CannotRechargeException;
import it.tim.topup.model.exception.GenericException;
import it.tim.topup.model.exception.NotAuthorizedException;
import it.tim.topup.model.integration.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import static it.tim.topup.service.IdsGenerator.generateTransactionId;

/**
 * Created by alongo on 13/04/18.
 */
@Service
@Slf4j
public class ScratchCardService {

    private RolProxy customersProxy;
    private SCDelegate creditProxy;

    @Autowired
    public ScratchCardService(RolProxy customersProxy, SCDelegate creditClient) {
        this.creditProxy = creditClient;
        this.customersProxy = customersProxy;
    }


    public Double rechargeWithScratchCard(String customerRef, String cardNumber,String fromMsisdn
        ,String toMsisdn, String subsystem, String transactionId){
        MobileOffersResponse mobileOffers = getMobileOffers(customerRef, toMsisdn, subsystem);
        
        OPSCData opscData = mobileOffers.getUserInfo_Response().getOpscdata();
    
        String typeOfCard = opscData.getTypeOfCard();
        String debit = opscData.getDebit();
        String debitCO = opscData.getDebitCO();
        
		log.debug("typeOfCard = " + typeOfCard + " - debit = " + debit);

		if(typeOfCard == null || debit==null) {
			throw new GenericException("RIC015", "Utente non presente su OPSC");
		}
        
        String legacyTid = generateTransactionId(24);
        authorize(customerRef, toMsisdn, legacyTid);
    
        return doRecharge(customerRef, toMsisdn, cardNumber, typeOfCard, fromMsisdn, debit, debitCO, transactionId, subsystem);
    }

    MobileOffersResponse getMobileOffers(String customerRef, String msisdn, String subsystem){
        MobileOffersRequest request = new MobileOffersRequest(Constants.MobileOffersOperations.LIGHT.getValue(), subsystem);
        MobileOffersResponse mobileOffersResponse = customersProxy.mobileOffers(customerRef, msisdn, request);

        if(mobileOffersResponse == null || mobileOffersResponse.getUserInfo_Response() == null || mobileOffersResponse.getUserInfo_Response().getOpscdata() == null)
            throw new GenericException("Incomplete response reveived by service 'prepagatoMobile/offerte'");
        return mobileOffersResponse;
    }

    void authorize(String customerRef, String lineNumber, String legacyTid){
        if(StringUtils.isEmpty(lineNumber))
            throw new BadRequestException("Cannot authorize empty line number");
        RechargeAuthorizationResponse rechargeAuthorizationResponse = creditProxy.authorizeLineForRecharge(customerRef, lineNumber, legacyTid);
        if(!rechargeAuthorizationResponse.getStatoUtenza().equals("0"))
            throw new NotAuthorizedException("Authorization service's outcome was: "+rechargeAuthorizationResponse.getStatoUtenza());
    }

    Double doRecharge(String customerRef, String toMsisdn, String cardNumber, String typeOfCard, String fromMsisdn, String debit, String debitCO, String transactionId, String subsystem) {
    	ScratchCardRequest request = new ScratchCardRequest(
    			toMsisdn,
    			cardNumber,
    			typeOfCard,
    			fromMsisdn,
    			debit,
    			debitCO,
    			transactionId,
    			subsystem
    			);		

        log.debug("Sending request to SDP: ", request);
    	
        ScratchCardResponse response = creditProxy.rechargeWithScratchCard(customerRef, toMsisdn, request);
        if(response.getEsito().equals("0"))
            try {
                return Double.parseDouble(response.getImporto());
            }catch (NumberFormatException e){
                throw new GenericException("An error occured while casting returned amount", e);
            }
        else
            throw new CannotRechargeException("Authorization service's outcome was: "+response.getEsito(), response.getEsito());
    }

}
