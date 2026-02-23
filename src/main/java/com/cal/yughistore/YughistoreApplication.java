package com.cal.yughistore;

import com.cal.yughistore.services.dto.applicationuser.AdminUserDTO;
import com.cal.yughistore.services.user.AdminUserService;
import com.cal.yughistore.services.user.ClientUserService;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import com.cal.yughistore.services.api.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

	/// user password requirements
	///***user password must contain at least 1 special character & 1 upper case & 1 number***
	///

	private final ApiService apiService;
	private  final YughioCardService yughioCardService;
	private final AdminUserService adminUserService;
	private final ClientUserService clientUserService;
    public YughistoreApplication(YughioCardService yughioCardService, ApiService apiService, AdminUserService adminUserService, ClientUserService clientUserService) {
        this.yughioCardService = yughioCardService;
        this.apiService = apiService;
        this.adminUserService = adminUserService;
        this.clientUserService = clientUserService;
    }

    public static void main(String[] args) {
		SpringApplication.run(YughistoreApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			try {
				adminUserService.userSignup(
						AdminUserDTO.builder()
								.email("admin@gmail.com")
								.password("!Password123")
								.build()
				);
			}catch (Exception e){
				System.out.println(e.getMessage());
			}
		};
	}
}
