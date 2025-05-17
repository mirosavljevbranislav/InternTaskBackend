package com.example.InternTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class  InternTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternTaskApplication.class, args);

	}

}
