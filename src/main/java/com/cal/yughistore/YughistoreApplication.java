package com.cal.yughistore;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumConfigType;
import com.cal.yughistore.services.DTOs.DTOYughioCard;
import com.cal.yughistore.services.YughioCardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

	private  final YughioCardService yughioCardService;

    public YughistoreApplication(YughioCardService yughioCardService) {
        this.yughioCardService = yughioCardService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			DTOYughioCard dto = DTOYughioCard.toDTO(
                    YughioCard
                    .builder()
                            .name("Test Card")
                            .build()
            );
			yughioCardService.save(dto);
		};
	}
}
