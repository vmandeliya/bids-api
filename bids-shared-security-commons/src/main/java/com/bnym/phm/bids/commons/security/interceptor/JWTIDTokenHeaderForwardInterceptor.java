package com.bnym.phm.bids.commons.security.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class JWTIDTokenHeaderForwardInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String jwt = request.getHeader("x-jwt-assertion");
        String token = request.getHeader("token");
        if(jwt !=null){
            template.header("x-jwt-assertion",jwt);
        }
        if(token !=null){
            template.header("token",token);
        }
    }
}
