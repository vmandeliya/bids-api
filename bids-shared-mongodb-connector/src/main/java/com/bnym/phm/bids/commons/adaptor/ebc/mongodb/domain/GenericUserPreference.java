package com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain;

import com.bnym.phm.bids.commons.model.dto.request.UserPreference.ChartState;
import com.bnym.phm.bids.commons.model.dto.request.UserPreference.FilterPreference;
import com.bnym.phm.bids.commons.model.dto.request.UserPreference.GenericUserGridView;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "UserPreference")

@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class GenericUserPreference {

    @Id
    private String id;
    private String commitId;
    private String createdDate;
    private String theme;
    private int numberOfBusinessDays;
    private String applicationMnemonic;
    private List<GenericUserGridView> gridViews = null;
    private List<ChartState> charts = null;
    private String sourceRegion;
    private boolean enableLineageToolTip;

    public static GenericUserPreference genericUserPreferenceConversionFromClient(AgGridPreferenceInfo agGridPreferenceInfo){
        GenericUserPreference genericUserPreference = new GenericUserPreference();
        genericUserPreference.setId(agGridPreferenceInfo.getId());
        genericUserPreference.setSourceRegion(agGridPreferenceInfo.getSourceRegion());
        genericUserPreference.setEnableLineageToolTip(agGridPreferenceInfo.isEnableLineageToolTip());
        genericUserPreference.setCreatedDate(agGridPreferenceInfo.getCreatedDate());

        List<GenericUserGridView> genericUserGridViewList = new ArrayList<>();
        GenericUserGridView genericUserGridView = new GenericUserGridView();
        log.debug("Grid name: latestEventsGrid");
        genericUserGridView.setGridName("latestEventsGrid");
        genericUserGridView.setColumnState(agGridPreferenceInfo.getPreference().getColumnState());
        List<FilterPreference> filterPreferences = agGridPreferenceInfo.getPreference().getFilterPreferences();
        if(!(CollectionUtils.isEmpty(filterPreferences))){
            genericUserGridView.setFilters(filterPreferences);
        }
        genericUserGridView.setSort(agGridPreferenceInfo.getPreference().getSortState());
        log.debug("setting row count per page to 25");
        genericUserGridView.setRowsPerPage(25);
        log.debug("setting event type to latest");
        genericUserGridViewList.add(genericUserGridView);
        genericUserPreference.setGridViews(genericUserGridViewList);
        if(!CollectionUtils.isEmpty(agGridPreferenceInfo.getPreference().getChartState())){
            genericUserPreference.setCharts(agGridPreferenceInfo.getPreference().getChartState());
        }
        return genericUserPreference;
    }

}
