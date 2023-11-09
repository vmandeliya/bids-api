package com.bnym.phm.bids.commons.adaptor.ebc.mongodb.config;

import com.bnym.phm.bids.commons.utils.YamlPropertySourceFactoryWithProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@NoArgsConstructor
@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@PropertySource(factory = YamlPropertySourceFactoryWithProfile.class, value = "classpath:mongoConfig.yml")
@PropertySource(factory = YamlPropertySourceFactoryWithProfile.class, value = "file:${mongo.credentials}")
@ConfigurationProperties
public class MongoDBConnectionConfig {
    private static final Logger log = LoggerFactory.getLogger(MongoDBConnectionConfig.class);
}
