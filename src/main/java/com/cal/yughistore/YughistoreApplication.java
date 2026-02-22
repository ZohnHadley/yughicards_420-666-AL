package com.cal.yughistore;

import com.cal.yughistore.services.user.ClientUserService;
import com.cal.yughistore.services.dto.applicationuser.ClientUserDTO;
import com.cal.yughistore.services.yughiocard.YughioCardService;
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
	private final ClientUserService clientUserService;
    public YughistoreApplication(YughioCardService yughioCardService, ApiService apiService, ClientUserService clientUserService) {
        this.yughioCardService = yughioCardService;
        this.apiService = apiService;
        this.clientUserService = clientUserService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			clientUserService.registerUser(
                    ClientUserDTO.builder()
							.userName("TheBigTugh")
							.firstName("jimmy")
							.lastName("johnson")
							.email("johnjohn@gmail.com")
							.password("test123123")
							.build()
            );
		};
	}
}
