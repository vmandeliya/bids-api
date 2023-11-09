package com.bnym.phm.bids.commons.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiFieldError {

    private String field;
    private String code;
    private Object rejectedValue;
    private String message;

    public ApiFieldError(String field,String message){
        this.field = field;
        this.message = message;
    }

    public ApiFieldError(String field,String code,Object rejectedValue,String message){
        this.field = field;
        this.code = code;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
