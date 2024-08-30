package com.halfacode.ecommMaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcommMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommMasterApplication.class, args);
	}

}
