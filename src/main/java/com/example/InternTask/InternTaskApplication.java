package com.example.InternTask;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class  InternTaskApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("spring.mail.username", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("spring.mail.password", dotenv.get("SPRING_MAIL_PASSWORD"));

		SpringApplication.run(InternTaskApplication.class, args);

	}

}
