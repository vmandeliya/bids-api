package com.bnym.phm.bids.ebc.mongo.downloader.boundary.interfaces;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.AgGridPreferenceInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface UserPreferenceInterface {

    @RequestMapping(value="/user-preference",method= RequestMethod.GET)
    ResponseEntity<AgGridPreferenceInfo> getUserPreferenceFromMongo();
}
