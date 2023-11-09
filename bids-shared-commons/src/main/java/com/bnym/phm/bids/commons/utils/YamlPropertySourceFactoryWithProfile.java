package com.bnym.phm.bids.commons.utils;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.List;

public class YamlPropertySourceFactoryWithProfile implements PropertySourceFactory {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        log.info("loading Resource file..{} ",resource.getResource().getFilename());
        List<PropertySource<?>> sources= new YamlPropertySourceLoader().load(resource.getResource().getFilename(),resource.getResource());
        String activeProfile = getActiveProfile();
        for(PropertySource<?> source:sources){
            log.info("Source profile property selected for matching..{}",source.getProperty("spring.profiles"));
            if(activeProfile.trim().equals("local")){
                log.info("Matching source profile property..{}",source.getProperty("spring.profiles"));
                return source;
            }
        }
        return sources.get(0);
    }

    private String getActiveProfile(){
        String[] activeProfiles = getActiveRuntimeProfiles();
        String activeProfile;
        if(activeProfiles.length ==1){
            activeProfile = activeProfiles[0];
            log.info("Single active profile selected for matching....{} ",activeProfile);
        }else{
            log.info("Cannot parse file - Implementation not supported ,supports only active profile");
            throw new IllegalArgumentException("Cannot parse file- Implementation not supported ,supports only one active profile");
        }
        return activeProfile;
    }

    public String[] getActiveRuntimeProfiles(){
        String profileConfig = "local";//System.getProperty("spring.profiles.active");
        log.info("System set profile..{}",profileConfig);
        profileConfig = SystemPropertyUtils.resolvePlaceholders(profileConfig);
        log.info("Placeholder resolved profile ..{}",profileConfig);
        return StringUtils.isBlank(profileConfig) ? new String[] {} : profileConfig.split(",");
    }
}
