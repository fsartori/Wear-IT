package com.unimib.wearable.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KaaEndPointResponse {

    public List<String> dataNames;

    public List<KaaEndPoint> kaaEndPoint;

    public List<KEP> kaaEP = new ArrayList<>();
    private Object String;
    private Object List;

    //unmarshalling a mano
    @JsonAnySetter
    public void setKaaEP(String key, Object value){
        List<LinkedHashMap<String,List<String>>> response = (List<LinkedHashMap<String,List<String>>>) value;
       // response.stream().map(s -> s.entrySet().stream().collect(Collectors.toMap(String,List<String>)));

    }


}
