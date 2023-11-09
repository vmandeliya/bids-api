package com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain;

import com.bnym.phm.bids.commons.model.dto.request.UserPreference.FilterPreference;
import com.bnym.phm.bids.commons.model.dto.request.UserPreference.Preference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.prefs.Preferences;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@ToString
@Slf4j
public class AgGridPreferenceInfo {

    private String id;
    private String createdBy;
    private String createdDate;
    private Preference preference;
    private String sourceRegion;
    private boolean enableLineageToolTip;

    public static AgGridPreferenceInfo GenericFormatConversionToClientSpecific(GenericUserPreference genericUserPreference){
        AgGridPreferenceInfo agGridPreferenceInfo = new AgGridPreferenceInfo();
        agGridPreferenceInfo.setId(genericUserPreference.getId());
        agGridPreferenceInfo.setCreatedDate(genericUserPreference.getCreatedDate());
        agGridPreferenceInfo.setSourceRegion(genericUserPreference.getSourceRegion());
        agGridPreferenceInfo.setEnableLineageToolTip(genericUserPreference.isEnableLineageToolTip());
        agGridPreferenceInfo.setCreatedBy(genericUserPreference.getCommitId());
        Preference preference = new Preference();
        if((!CollectionUtils.isEmpty(genericUserPreference.getGridViews()))){
            log.debug("Number of grid configurations available: "+genericUserPreference.getGridViews());
            preference.setColumnState(genericUserPreference.getGridViews().get(0).getColumnState());
            preference.setSortState(genericUserPreference.getGridViews().get(0).getSort());
            List<FilterPreference> filterPreferenceList = genericUserPreference.getGridViews().get(0).getFilters();
            if(!(CollectionUtils.isEmpty(filterPreferenceList))){
                preference.setFilterPreferences(filterPreferenceList);
            }
            if(!(CollectionUtils.isEmpty(genericUserPreference.getCharts()))){
                preference.setChartState(genericUserPreference.getCharts());
            }
            agGridPreferenceInfo.setCreatedDate(genericUserPreference.getCreatedDate());
            agGridPreferenceInfo.setPreference(preference);
        }
        return agGridPreferenceInfo;
    }
}
