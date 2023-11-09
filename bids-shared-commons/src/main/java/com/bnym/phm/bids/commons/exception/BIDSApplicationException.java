package com.bnym.phm.bids.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class BIDSApplicationException extends RuntimeException{

    private final HttpStatus httpStatusCode;

    public BIDSApplicationException(HttpStatus httpStatusCode){
        this.httpStatusCode = httpStatusCode;
    }

    public BIDSApplicationException(HttpStatus httpStatusCode,String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public BIDSApplicationException(HttpStatus httpStatusCode, String message,Throwable cause){
        super(message,cause);
        this.httpStatusCode = httpStatusCode;
    }

    public BIDSApplicationException(HttpStatus httpStatusCode, String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace){
        super(message,cause,enableSuppression,writableStackTrace);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode(){
        return  httpStatusCode;
    }
}
