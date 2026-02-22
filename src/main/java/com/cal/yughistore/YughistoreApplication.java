package com.cal.yughistore;

import com.cal.yughistore.services.YughioCardService;
import com.cal.yughistore.services.api.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

	private final ApiService apiService;
	private  final YughioCardService yughioCardService;

    public YughistoreApplication(YughioCardService yughioCardService, ApiService apiService) {
        this.yughioCardService = yughioCardService;
        this.apiService = apiService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
		};
	}
}
