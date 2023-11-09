package com.bnym.phm.bids.ebc.mongo.downloader.boundary;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.AgGridPreferenceInfo;
import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.GenericUserPreference;
import com.bnym.phm.bids.ebc.mongo.downloader.boundary.interfaces.UserPreferenceInterface;
import com.bnym.phm.bids.ebc.mongo.downloader.service.UserPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Validated
@Slf4j
public class UserPreferenceController implements UserPreferenceInterface {

    private UserPreferenceService userPreferenceService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService){
        this.userPreferenceService = userPreferenceService;
    }
    @Override
    @RequestMapping(value= "/user-preference",method= RequestMethod.GET)
    public ResponseEntity<AgGridPreferenceInfo> getUserPreferenceFromMongo() {
        String commitID = "ADCCLOM";//request.getRemoteUser();
        System.out.println(commitID);
        GenericUserPreference genericUserPreference = userPreferenceService.finUserPreferencebyCommitID(commitID);
        AgGridPreferenceInfo agGridPreferenceInfo = AgGridPreferenceInfo.GenericFormatConversionToClientSpecific(genericUserPreference);
        return ResponseEntity.ok().body(agGridPreferenceInfo);
    }
}
