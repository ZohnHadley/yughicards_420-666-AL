package com.cal.yughistore;

import com.cal.yughistore.model.MonsterCard;
import com.cal.yughistore.services.MonsterCardService;
import com.cal.yughistore.services.api.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

	private final  MonsterCardService monsterCardService;
	private final ApiService apiService;
    public YughistoreApplication(MonsterCardService monsterCardService, ApiService apiService) {
        this.monsterCardService = monsterCardService;
        this.apiService = apiService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			apiService.getInformationForAllCards();
		};
	}
}
