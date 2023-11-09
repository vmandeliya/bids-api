package com.bnym.phm.bids.commons.adaptor.ebc.mongodb.aggregation;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

public class CustomerAggregationOperation implements AggregationOperation {

    private String json;
    public CustomerAggregationOperation(String json){
        this.json = json;
    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext){
        return aggregationOperationContext.getMappedObject(Document.parse(json));
    }

}
