package com.bnym.phm.bids.commons.security.interceptor;

public class JWTIDTokenHeaderGeneratorInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JWTIDTokenHeaderGeneratorInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        //SystemIDTokenGenerator generator = new SystemIDTokenGenerator();
        String jwt = "tempJwtToken";//generator.getSystemIDToken();
        log.debug("--JWT info on websocket--> "+jwt);
        template.header("x-jwt-assertion",jwt);
    }
}