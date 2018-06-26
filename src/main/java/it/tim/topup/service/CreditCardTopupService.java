package it.tim.topup.service;

import it.tim.topup.integration.client.OBGClient;
import it.tim.topup.integration.proxy.RolProxy;
import it.tim.topup.model.configuration.Constants;
import it.tim.topup.model.domain.CreditCardData;
import it.tim.topup.model.exception.GenericException;
import it.tim.topup.model.integration.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static it.tim.topup.service.IdsGenerator.generateId10;

@Service
@Slf4j
public class CreditCardTopupService {

    private static final Pattern CF_PATTERN = Pattern.compile("[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]");
    private static final DateTimeFormatter AUTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private RolProxy customersProxy;
    private OBGClient obgClient;
    
    @Autowired
    public CreditCardTopupService(RolProxy customersProxy, OBGClient obgClient) {
        this.customersProxy = customersProxy;
        this.obgClient = obgClient;
    }

    public String checkout(String userRef, String toMsisdn, Integer amount, CreditCardData cdc, String subSys
            , String tiid, String deviceType, String operationDate, String transId){


        if(tiid == null) {
    		log.info("tiid null - set empty [] ");
    		tiid = "";
    	}
    	
        //Chiamata al servizio 26 per verifica utenza. Recupero dei seguenti campi
        MobileOffersResponse verifyResponse =
                verifyUser(userRef, toMsisdn, Constants.MobileOffersOperations.LIGHT.getValue(), subSys);

        String typeOfCard = verifyResponse.getUserInfo_Response().getOpscdata().getTypeOfCard();
		String debit = verifyResponse.getUserInfo_Response().getOpscdata().getDebit();
		
		log.debug("typeOfCard = " + typeOfCard + " - debit = " + debit);

		if(typeOfCard == null || debit==null) {
			throw new GenericException("RIC015", "Utente non presente su OPSC");
		}


        // Chiamata al serivio 31 per il controllo antifrode
        if(isFiscalCode(userRef))
            verifyFraudPrevention(userRef, null, cdc.getCardNumber());
        else
            verifyFraudPrevention(null, userRef, cdc.getCardNumber());

        // Chiamata al servizio callPagamS2S per il blocco plafond
        LocalDateTime bankAuthDate = LocalDateTime.now();
        String authDate = bankAuthDate.format(AUTH_DATE_FORMATTER);

        String referenceId = generateId10();

        CallPagamS2S pagamS2S = new CallPagamS2S();

        pagamS2S.setShopLogin("GESPAY55555");
        pagamS2S.setUicCode("242");
        pagamS2S.setAmount(String.valueOf(amount));
        
        String shopTransId = generateId10();
        //pagamS2S.setShopTransactionId(tiid);
        pagamS2S.setShopTransactionId(shopTransId);

        pagamS2S.setCardNumber(cdc.getCardNumber());
        pagamS2S.setExpiryMonth(cdc.getExpireMonth());
        pagamS2S.setExpiryYear(cdc.getExpireYear());
        pagamS2S.setBuyerName(cdc.getOwner());
        pagamS2S.setCvv(cdc.getCvv());

        GestPayS2S gestPayS2S = planfondBlock(pagamS2S);

        
        int idx = debit.indexOf(".");
		String formattedDebit = debit.substring(0,idx+3);
		
        // Chiamata al servizio 139 per l’operazione di ricarica
        doCheckout(
                subSys
                , userRef
                , tiid
                , toMsisdn
                , typeOfCard
                , cdc.getCardType().getCircuit()
                , String.valueOf(amount) + ".00"
				, formattedDebit
                , gestPayS2S.getBankTransactionId()
                , gestPayS2S.getShopTransactionId()
                , authDate
                , deviceType
                , referenceId
                , operationDate
                , transId
        );

        return referenceId;
    }


    private MobileOffersResponse verifyUser(String userRef, String lineNumber, String operation, String subSystem){
        MobileOffersResponse mobileOffersResponse = customersProxy.mobileOffers(
                userRef
                , lineNumber
                , new MobileOffersRequest(operation, subSystem));

        if(mobileOffersResponse == null || mobileOffersResponse.getUserInfo_Response() == null)
            throw new GenericException("Incomplete response reveived by service 'prepagatoMobile/offerte'");
        return mobileOffersResponse;
    }


    private void verifyFraudPrevention(String cf, String vat, String cardNumber){

        FraudPreventionRequest request = new FraudPreventionRequest(
                FraudPreventionRequest.RECHARGE_PD
                , new FraudPreventionRequest.CustomerAccount(
                new FraudPreventionRequest.Customer(
                        new FraudPreventionRequest.Individual(cf, vat)
                )
                , new FraudPreventionRequest.BillingAccount(
                new FraudPreventionRequest.CreditCard(cardNumber)
        )));

        customersProxy.fraudsPrevention(request);
    }


    /*
    private GestPayS2S planfondBlockOLD(CallPagamS2S pagamS2S){

        CallPagamS2SResponse response = wSw2Client.callPagamS2S(pagamS2S);
        CallPagamS2SResponse.CallPagamS2SResult callPagamS2SResult = response.getCallPagamS2SResult();

        return callPagamS2SResult.getContent();

    }
    */
    
	private GestPayS2S planfondBlock(CallPagamS2S pagamS2S){


		try {
			
			String objReq = getOBJRequest(pagamS2S);
			log.info("------------------------------ OBG REQ: " + objReq);
			
			String objResponse = obgClient.callOBJ(objReq);

			log.info("------------------------------ OBG REPONSE: " + objResponse);

			
			GestPayS2S resp = new GestPayS2S();
			
			String erroCode = getTagValue(objResponse, "ErrorCode" , "-1");
			log.info("erroCode from OBJ = " + erroCode);
			
			if(erroCode!=null && erroCode.equals("0")) {

				String shopTransactionId = getTagValue(objResponse, "ShopTransactionID" , "");
				log.debug("shopTransactionID = " + shopTransactionId);
				
				String bankTransactionId = getTagValue(objResponse, "BankTransactionID" , "");
				log.debug("bankTransactionID = " + bankTransactionId);

				resp.setBankTransactionId(bankTransactionId);
				resp.setShopTransactionId(shopTransactionId);
				
			}
			else {
				throw new GenericException("Error Receive from OBG. ErrorCode " + erroCode);
			}
			
			return resp;
		}
		catch(Exception ex) {
			log.error("OBG EXC " + ex);
			throw new GenericException("Incomplete response reveived by service 'prepagatoMobile/offerte'");
		}

	}



    private void doCheckout(String subSys, String rifCliente, String tiidCliente, String numLinea, String tipoCarta
            , String gestorePagamento, String importoRicarica, String creditoResiduo, String idAutorizzazionePagamento
            , String shopTransId, String dataAutorizzazionePagamento, String deviceType, String referenceId, String operationDate
            , String legacyTransId){

        customersProxy.onlineRefill(new OnlineRefillRequest(
                subSys,
                rifCliente,
                tiidCliente,
                numLinea,
                tipoCarta,
                gestorePagamento,
                importoRicarica,
                creditoResiduo,
                idAutorizzazionePagamento,
                dataAutorizzazionePagamento,
                "",
                "",
                "",
                deviceType,
                referenceId,
                "ROL",
                shopTransId,
                operationDate,
                legacyTransId
        ));
    }


    private boolean isFiscalCode(String text){
        return text != null && CF_PATTERN.matcher(text).matches();
    }

    
    
    
    
    
	private String getOBJRequest(CallPagamS2S pagamS2S) {
		StringBuilder buff = new StringBuilder();

		buff.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		buff.append(" <S:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"> ");
		buff.append("<env:Header/>");
		buff.append("<S:Body>");
		buff.append("<ns0:callPagamS2S xmlns:ns0=\"https://ecomms2s.sella.it/\">");
		buff.append("<ns0:shopLogin>GESPAY55555</ns0:shopLogin>");
		buff.append("<ns0:uicCode>242</ns0:uicCode>");
		
		buff.append("<ns0:amount>").append(pagamS2S.getAmount()).append("</ns0:amount>");
		
		buff.append("<ns0:shopTransactionId>").append(pagamS2S.getShopTransactionId()).append("</ns0:shopTransactionId>");
		
		buff.append("<ns0:cardNumber>").append(pagamS2S.getCardNumber() ).append("</ns0:cardNumber>");
		buff.append("<ns0:expiryMonth>").append(pagamS2S.getExpiryMonth() ).append("</ns0:expiryMonth>");
		
		
		String year = pagamS2S.getExpiryYear().substring(2);
		
		buff.append("<ns0:expiryYear>").append(year).append("</ns0:expiryYear>");
		buff.append("<ns0:buyerName>").append(pagamS2S.getBuyerName() ).append("</ns0:buyerName>");
		buff.append("<ns0:cvv>").append(pagamS2S.getCvv() ).append("</ns0:cvv>");
		buff.append("</ns0:callPagamS2S>");
		buff.append("</S:Body>");
		buff.append("</S:Envelope>");


		return buff.toString();
	}

	
	
	
	private static String getTagValue(String resp, String tag, String defaultVal ) {
		String tagValue = defaultVal;
		
		String tag1 = "<"+tag+">";
		String tag2 = "</"+tag+">";
		
		int idx1 = resp.indexOf(tag1);
		int idx2 = resp.indexOf(tag2);
		
		if(idx1>0 && idx2>0) {
			tagValue = resp.substring(idx1 + tag1.length(),idx2);
		}
		
		return tagValue;
	}
	
	
	
	public static void main(String[] args) {
		String anno = "2021";
		String year = anno.substring(2);
		System.out.println("year = " + year);
		
		String objResp = "<TransactionType>PAGAM</TransactionType><TransactionResult>KO</TransactionResult><ShopTransactionID>1234</ShopTransactionID><BankTransactionID>5678900000</BankTransactionID><AuthorizationCode>"
				+ "</AuthorizationCode><Currency></Currency><Amount></Amount><Country></Country><Buyer><BuyerName></BuyerName><BuyerEmail></BuyerEmail></Buyer><CustomInfo></CustomInfo><ErrorCode>1125</ErrorCode><ErrorDescription>Anno di scadenza non valido</ErrorDescription><AlertCode></AlertCode><AlertDescription></AlertDescription><TransactionKey>196704321</TransactionKey><VbV><VbVFlag></VbVFlag><VbVBuyer>KO</VbVBuyer><VbVRisp></VbVRisp></VbV><TOKEN></TOKEN><TokenExpiryMonth></TokenExpiryMonth><TokenExpiryYear></TokenExpiryYear></GestPayS2S></callPagamS2SResult></callPagamS2SResponse></S:Body></S:Envelope>";

		
		String erroCode = getTagValue(objResp, "ErrorCode" , "-1");
		System.out.println("erroCode = " + erroCode);
		
		String shopTransactionID = getTagValue(objResp, "ShopTransactionID" , "");
		System.out.println("shopTransactionID = " + shopTransactionID);
		
		String bankTransactionID = getTagValue(objResp, "BankTransactionID" , "");
		System.out.println("bankTransactionID = " + bankTransactionID);
		
		LocalDateTime bankAuthDate = LocalDateTime.now();
		String authDate = bankAuthDate.format(AUTH_DATE_FORMATTER);
		
		System.out.println("authDate = " + authDate);
		
		

		String debit = "25.00000000000";
		int idx = debit.indexOf(".");
		String db1 = debit.substring(0,idx+3);
		System.out.println("db1= " + db1);
		
		
		UUID transaction = UUID.randomUUID();
    	String tid = transaction.toString();
    	System.out.println("tid="+tid);
		
    	SecureRandom random = new SecureRandom();
    	byte bytes[] = new byte[16];
    	random.nextBytes(bytes);
    	Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    	String token = encoder.encodeToString(bytes);
    	System.out.println(token);
		
	}
    
}
