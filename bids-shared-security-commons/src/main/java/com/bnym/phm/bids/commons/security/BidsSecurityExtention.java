package com.bnym.phm.bids.commons.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BidsSecurityExtention extends WebSecurityConfigurerAdapter{

    @Value("${bids.security.endpoints.open}")
    private String[] openEndpoints;

    @Value("${bids.security.endpoints.protected}")
    private String[] protectedEndPoints;

    @Override
    @SuppressWarnings("java:S4502")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable(); // Disable CSRF protection (for simplicity, you might want to enable it in a real application)

        /**
         * below is bnym config
         super.configure(http);
         http.csrf.disable();
         http.requestMatchers().mvcMatchers(protectedEndPoints);

         **/
    }

// below is bnym config
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().mvcMatchers(openEndpoints);
    }

}
