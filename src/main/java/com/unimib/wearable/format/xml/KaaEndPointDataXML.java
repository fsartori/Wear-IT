package com.unimib.wearable.format.xml;

import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
public class KaaEndPointDataXML {

    //@JacksonXmlProperty(isAttribute = true, localName = "kaaEndPointDataDTOS")
    //@JsonProperty(value = "kaaEndPointDataDTOS")
    private String endpointId;
    private String dataName;
    private List<InnerData> data;

    @Data
    public static class InnerData{
        private String timeStamp;
        private String value;
    }
}
