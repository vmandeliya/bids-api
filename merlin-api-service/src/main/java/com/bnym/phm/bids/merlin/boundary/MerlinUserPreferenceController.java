package com.bnym.phm.bids.merlin.boundary;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.AgGridPreferenceInfo;
import com.bnym.phm.bids.merlin.boundary.interfaces.DownloadInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="User-statistics",description = "User statistics information")
public class MerlinUserPreferenceController{


    @Autowired
    DownloadInterface downloadInterface;

    @Autowired
    public MerlinUserPreferenceController(DownloadInterface downloadInterface){
        this.downloadInterface = downloadInterface;
    }

    @RequestMapping(value= "/user-preference",method= RequestMethod.GET)
    public ResponseEntity<AgGridPreferenceInfo> getUserPreferenceFromMongo() {
        return downloadInterface.getUserPreferenceFromMongo();
    }
}
