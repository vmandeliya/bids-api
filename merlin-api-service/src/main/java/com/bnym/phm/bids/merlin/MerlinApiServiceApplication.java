package com.bnym.phm.bids.merlin;

import com.bnym.phm.bids.commons.utils.ProfileType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;

@SpringBootApplication(scanBasePackages = {"com.bnym.phm.bids"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bnym.phm.bids"})
@EnableAsync
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@OpenAPIDefinition(info =
@Info(title ="Merlin API",version = "1.0", description = "Documentation department API v1.0"))
public class MerlinApiServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(MerlinApiServiceApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MerlinApiServiceApplication.class, args);
	}

	@Bean
	public Executor taskExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setThreadNamePrefix("merlinElasticSearchAll-async-");
		executor.initialize();
		return  executor;
	}

	@Bean
	public CurieProvider curieProvider(){
		return new DefaultCurieProvider("phm", UriTemplate.of("docs/index.html#{rel}"));
	}

	@Bean
	public OperationCustomizer customerGlobalHeaders(){
		return (Operation operation , HandlerMethod handlerMethod) ->{
			Parameter defaultParameter = new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
					.name("x-jwt-assertion").description("").required(true);
			operation.addParametersItem(defaultParameter);
		return operation;
		};

	}

	@Autowired
	private Environment environment;

	@Bean
	public OpenApiCustomiser serverOpenApiCustomiser(){
		String activeProfile = environment.getActiveProfiles()[0];
		return openApi -> openApi.getServers().forEach(server -> {
			String url = server.getUrl();
			if(!activeProfile.contains(ProfileType.LOCAL.getProfile())){
				url = url.replace("http","https");
				server.setUrl(url);
			}
		});
	}
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Bean
	@SuppressWarnings("java:S5122")
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "CONNECT")
						.allowedHeaders("*");
			}
		};
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions(){
		return new MongoCustomConversions(Arrays.asList(
				new BigDecimalDecimal128Converter(),
				new Decimal128BigDecimalConverter(),
				new DateToZonedDateTimeConverter(),
				new ZonedDateTimeToDateConverter()

		));
	}
	@WritingConverter
	private static class BigDecimalDecimal128Converter implements Converter<BigDecimal, Decimal128> {
		@Override
		public Decimal128 convert(@NonNull BigDecimal source ) {
			return new Decimal128(source);
		}
	}
	@ReadingConverter
	private static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal>{

		@Override
		public BigDecimal convert(@NotNull Decimal128 source) {
			return source.bigDecimalValue();
		}

	}

	@ReadingConverter
	private static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime>{

		@Override
		public ZonedDateTime convert(@NotNull  Date source) {
			return ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
		}

	}

	@WritingConverter
	private static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime,Date>{
		@Override
		public Date convert(@NonNull ZonedDateTime source ) {
			return Date.from(source.toInstant());
		}
	}
}