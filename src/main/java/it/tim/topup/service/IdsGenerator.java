package it.tim.topup.service;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class IdsGenerator {


    public static String generateId10(){
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String generateId24() {
    	return RandomStringUtils.randomAlphanumeric(24);
    }
    
    public static String generateId30() {
    	return RandomStringUtils.randomAlphanumeric(30);
    }
    
    public static String generateId(int lenght) {
    	return RandomStringUtils.randomAlphanumeric(lenght);
    }
    
    
    public static String generateTransactionId(int numchars) {
    	
    	Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        String tid =  sb.toString().substring(0, numchars);
    	
    	return tid;
    }
    
}
