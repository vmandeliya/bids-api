package com.bnym.phm.bids.merlin.boundary.interfaces;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.AgGridPreferenceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name= "${feign.servicename.ebc.downloader}")
public interface DownloadInterface {

    @RequestMapping(value="/user-preferencea",method= RequestMethod.GET)
    ResponseEntity<AgGridPreferenceInfo> getUserPreferenceFromMongo();
}
