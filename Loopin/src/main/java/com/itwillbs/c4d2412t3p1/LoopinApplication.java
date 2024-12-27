package com.itwillbs.c4d2412t3p1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.itwillbs.c4d2412t3p1")
public class LoopinApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoopinApplication.class, args);
	}

}
