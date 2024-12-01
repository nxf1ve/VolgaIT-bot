package ru.pyzhov.VolgaIT_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VolgaItBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolgaItBotApplication.class, args);
	}

}
