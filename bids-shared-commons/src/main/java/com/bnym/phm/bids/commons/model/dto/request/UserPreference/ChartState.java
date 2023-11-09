package com.bnym.phm.bids.commons.model.dto.request.UserPreference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChartState{
    private String charTitle;
    private String valueVolume;
    private String graphTable;
    private String sharedBy;
    private String chartType;
    private String version;
    private String createdDate;
    private String chartRequest;
    private String chartSize;
    private String editedDate;

}
