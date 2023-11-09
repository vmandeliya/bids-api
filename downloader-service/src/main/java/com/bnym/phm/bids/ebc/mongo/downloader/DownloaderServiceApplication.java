package com.bnym.phm.bids.ebc.mongo.downloader;


import org.bson.types.Decimal128;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.lang.NonNull;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication(scanBasePackages = "com.bnym.phm.bids")
@EnableFeignClients(basePackages = {"com.bnym.phm.bids"})
@EnableScheduling
@EnableDiscoveryClient
public class DownloaderServiceApplication {
	private static final Logger log = LoggerFactory.getLogger(DownloaderServiceApplication.class);

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(DownloaderServiceApplication.class, args);
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
	private static class BigDecimalDecimal128Converter implements Converter<BigDecimal,Decimal128>{
		@Override
		public Decimal128 convert(@NonNull BigDecimal source ) {
				return new Decimal128(source);
		}
	}
	@ReadingConverter
	private static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal>{

		@Override
		public BigDecimal convert(@NotNull  Decimal128 source) {
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
