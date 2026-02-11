package com.cal.yughistore;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumPropertiesConfigType;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
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
//			yughioCardService.loadFromStaticFile();
//			apiService.getInformationForNamedCard("Dark Magician");
//			DTOYughioCard dto = DTOYughioCard.toDTO(
//                    YughioCard
//                    .builder()
//							.type(EnumCardType.LINK_MONSTER)
//                            .name("Test Card")
//                            .build()
//            );
//			yughioCardService.save(apiService.getInformationForNamedCard("Dark Magician"));
		};
	}
}
