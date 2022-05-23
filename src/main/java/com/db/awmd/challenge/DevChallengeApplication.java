package com.db.awmd.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan({ "com.db.awmd.challenge.service" })
@EnableJpaRepositories({ "com.db.awmd.challenge.repository" })
public class DevChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevChallengeApplication.class, args);
	}
}
