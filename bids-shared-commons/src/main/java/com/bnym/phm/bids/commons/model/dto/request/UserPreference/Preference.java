package com.bnym.phm.bids.commons.model.dto.request.UserPreference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Preference {

    private List<ColumnState> columnState = null;
    private List<Object> groupState = null;
    private GenericUserSort sortState = null;
    private List<FilterPreference> filterPreferences = null;
    private List<ChartState> chartState = null;

}
