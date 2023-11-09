package com.bnym.phm.bids.ebc.mongo.downloader.service;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.GenericUserPreference;
import com.bnym.phm.bids.commons.exception.BIDSApplicationException;
import com.bnym.phm.bids.commons.utils.CommonConstants;
import com.bnym.phm.bids.ebc.mongo.downloader.repository.UserPreferenceRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class UserPreferenceService {
    private static final Logger log  = LoggerFactory.getLogger(UserPreferenceService.class);

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository){
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Bulkhead(name = CommonConstants.BULK_HEAD_CONFIG)
    public GenericUserPreference finUserPreferencebyCommitID(String commitID){
        GenericUserPreference genericUserPreferenceInfo = userPreferenceRepository.findByCommitId(commitID);
//        genericUserPreferenceInfo.setId("1");
//        genericUserPreferenceInfo.setCommitId("ADCCLOM");
        if(null == genericUserPreferenceInfo){
            throw new BIDSApplicationException(HttpStatus.NOT_FOUND,"User info not found");
        }
        return genericUserPreferenceInfo;
    }

}
