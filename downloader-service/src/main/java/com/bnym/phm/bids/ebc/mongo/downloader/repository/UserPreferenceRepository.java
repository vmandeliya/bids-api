package com.bnym.phm.bids.ebc.mongo.downloader.repository;

import com.bnym.phm.bids.commons.adaptor.ebc.mongodb.domain.GenericUserPreference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPreferenceRepository extends MongoRepository<GenericUserPreference,String> {

    GenericUserPreference findByCommitId(String commitID);
}
