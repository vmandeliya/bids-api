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
public class GenericUserGridView {

    private String gridName;
    private int rowsPerPage;
    private String eventsType;
    private GenericUserSort sort;
    private List<FilterPreference> filters = null;
    private List<ColumnState> columnState = null;
}
