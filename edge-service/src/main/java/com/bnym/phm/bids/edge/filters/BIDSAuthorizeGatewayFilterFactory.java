package com.bnym.phm.bids.edge.filters;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@PropertySource("classpath:application.yml")
public class BIDSAuthorizeGatewayFilterFactory extends
        AbstractGatewayFilterFactory<BIDSAuthorizeGatewayFilterFactory.Config>{

    public static final String JWT_ASSERTION = "x-jwt-assertion";
    public static final String TOKEN_HEADER_NAME = "token";

    public BIDSAuthorizeGatewayFilterFactory(){
        super(Config.class);
    }

    private static final Logger log = LoggerFactory.getLogger(BIDSAuthorizeGatewayFilterFactory.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bidsauth.service}")
    private String authServiceName;

    @Value("${bidsauth.path}")
    private String authServicePath;

    @Value("${bidsauth.scheme}")
    private String scheme;

    @Value("${bidsauth.openendpoints}")
    private String openEndpoints;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            StopWatch timer = new StopWatch();
            try {
                HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
                log.info("path: {} : {} : {}", exchange.getRequest().getPath(), exchange.getRequest().getURI().getRawPath()
                        , exchange.getRequest().getURI().getPath());
                String header = "";
                String token = "";
                HttpHeaders forwaredHeader = new HttpHeaders();
                if (httpHeaders.getFirst(TOKEN_HEADER_NAME) != null) {
                    header = TOKEN_HEADER_NAME;
                    token = httpHeaders.getFirst(TOKEN_HEADER_NAME);
                } else if (httpHeaders.getFirst(JWT_ASSERTION) != null) {
                    header = JWT_ASSERTION;
                    token = httpHeaders.getFirst(JWT_ASSERTION);
                }

                forwaredHeader.add(header, token);
                forwaredHeader.setContentType(MediaType.APPLICATION_JSON);
                forwaredHeader.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                if (authorizePath(exchange.getRequest().getPath().toString())) {
                    log.info("Authorization for path: {}", exchange.getRequest().getPath());
                    HttpEntity<String> entity = new HttpEntity<>(forwaredHeader);
//                    ResponseEntity<String> response = restTemplate.exchange(
//                            scheme + "://" + authServiceName + authServicePath,
//                            HttpMethod.GET, entity, String.class);
                    HttpStatus httpStatus = ResponseEntity.ok("Its green").getStatusCode(); //response.getStatusCode();
                    timer.start();
                    log.info("Gateway filter - Authorization result: {}", httpStatus.getReasonPhrase());

                }
            } catch (HttpClientErrorException e) {
                if (401 == e.getStatusCode().value() || 403 == e.getStatusCode().value()) {
                    log.warn("Unauthorize request for path: {},http status code: {}",
                            exchange.getRequest().getPath(), e.getStatusCode());
                    return this.unAhorizedRequestErr(exchange, HttpStatus.FORBIDDEN, timer);
                } else {
                    log.warn("Unexpected client error while authorizing for path: {},http status code: {}",
                            exchange.getRequest().getPath(), e.getStatusCode(), e);
                    return this.unAhorizedRequestErr(exchange, (HttpStatus) e.getStatusCode(), timer);
                }
            } catch (Exception e) {
                log.warn("Exception while authorizing caller for path: {}," +
                        exchange.getRequest().getPath(), e);
                return this.unAhorizedRequestErr(exchange, HttpStatus.INTERNAL_SERVER_ERROR, timer);
            }
            return chain.filter(exchange);

        };
    }

    public static class Config{
        //Configuration properties
    }

    private boolean authorizePath(String path){
        for(String open: openEndpoints.split(",")){
            //if path contains open endpoints in yaml then bypass authorization
            if(StringUtils.containsIgnoreCase(path,open)){
                log.info("Authorization bypass for path -{}",path);
                return false;
            }
        }
        return true;
    }

    private Mono<Void> unAhorizedRequestErr(ServerWebExchange exchange, HttpStatus httpStatus, StopWatch timer){
        ServerHttpResponse response= exchange.getResponse();
        response.setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add("X-Response-Bids-ElapsedTime",Double.toString(timer.getTotalTimeSeconds()));
        exchange.getResponse().getHeaders().set("Access-Control-Expose-Headers","X-Response-Bids-ElapsedTime");
        exchange.getResponse().getHeaders().set("Cache-Control","no-store,no-cache,must-revalidate");
        exchange.getResponse().getHeaders().add("Pragma","no-cache");
        exchange.getResponse().getHeaders().add("X-Content-Type-Operations","nosniff");
        exchange.getResponse().getHeaders().add("expires","0");
        exchange.getResponse().getHeaders().add("X-XSS-Protection","1: mode=block");
        exchange.getResponse().getHeaders().add("Content-Security-Policy","default-src 'self'; font-src 'self' fonts-googleapis.com cdnjs.cloudflare.com fonts.gstatic.com; style-src-elem 'self' fonts-googleapis.com cdnjs.cloudflare.com " );
        exchange.getResponse().getHeaders().add("Strict-Transport-Security","max-age=31536000; includeSubDomains");
        return response.setComplete();
    }
}