package it.tim.topup.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class MobileOffersRequest {

    private String operazione;
    private String lineaFissa;
    private String subSystem;
    private String offerInPS;
    private String partnership;


    public MobileOffersRequest(String operation, String subSystem) {
        this.operazione = operation;
        this.subSystem = subSystem;
    }

    @JsonIgnore
    public Map<String, String> toMap(){
        Map<String, String> converted = new HashMap<>();
        if(!StringUtils.isEmpty(operazione))
            converted.put("operazione",operazione);
        if(!StringUtils.isEmpty(lineaFissa))
            converted.put("lineaFissa",lineaFissa);
        if(!StringUtils.isEmpty(subSystem))
            converted.put("subSystem",subSystem);
        if(!StringUtils.isEmpty(offerInPS))
            converted.put("offerInPS",offerInPS);
        if(!StringUtils.isEmpty(partnership))
            converted.put("partnership",partnership);
        return converted;
    }
}
