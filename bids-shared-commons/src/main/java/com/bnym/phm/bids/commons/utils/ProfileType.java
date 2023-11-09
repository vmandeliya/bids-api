package com.bnym.phm.bids.commons.utils;

public enum ProfileType {
    PROD("prod"),
    QA("qa"),
    DEV("dev"),
    TEST("test"),
    LOCAL("local") ,
    UAT7("uat7");

    private String profile;

    ProfileType(final String profile){
        this.profile = profile;
    }

    public String getProfile(){
        return this.profile;
    }
}
