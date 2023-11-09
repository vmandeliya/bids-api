package com.bnym.phm.bids.edge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class EdgeServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(Appendable.class);

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Bean
	@Order(1)
	public GlobalFilter bidsEndPointElapsedResponseTimeFilter(){
		return (exchange, chain) -> {
			StopWatch timer = new StopWatch();
			timer.start();
			log.debug("Timer starts now... in edge-service");
			log.debug("third pre filter");
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				log.debug("first post filter");
				timer.stop();
				if(!exchange.getResponse().isCommitted()){
					log.debug("total time taken----> {} "+ timer.getTotalTimeSeconds());
					exchange.getResponse().getHeaders().add("X-Response-Bids-ElapsedTime",Double.toString(timer.getTotalTimeSeconds()));
					exchange.getResponse().getHeaders().set("Access-Control-Expose-Headers","X-Response-Bids-ElapsedTime");
					exchange.getResponse().getHeaders().set("Cache-Control","no-store,no-cache,must-revalidate");
					exchange.getResponse().getHeaders().add("Pragma","no-cache");
					exchange.getResponse().getHeaders().add("X-Content-Type-Operations","nosniff");
					exchange.getResponse().getHeaders().add("expires","0");
					exchange.getResponse().getHeaders().add("X-XSS-Protection","1: mode=block");
					exchange.getResponse().getHeaders().add("Content-Security-Policy","default-src 'self'; font-src 'self' fonts-googleapis.com cdnjs.cloudflare.com fonts.gstatic.com; style-src-elem 'self' fonts-googleapis.com cdnjs.cloudflare.com " );
					exchange.getResponse().getHeaders().add("Strict-Transport-Security","max-age=31536000; includeSubDomains");
				}
			}));
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}

}
