package com.cal.yughistore;

import com.cal.yughistore.repository.user.AdminUserRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.services.applicaitonuser.ApplicationUserService;
import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.services.applicaitonuser.AdminUserService;
import com.cal.yughistore.services.applicaitonuser.ClientUserService;
import com.cal.yughistore.services.storeServices.StoreClientServices;
import com.cal.yughistore.services.utils.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YughistoreApplication {

    /// user password requirements
    /// ***user password must contain at least 1 special character & 1 upper case & 1 number***
    ///
    private final AuthService authService;
    private final ApplicationUserService applicationUserService;
    private final AdminUserService adminUserService;
    private final ClientUserService clientUserService;
    private final StoreClientServices storeClientServices;



    public YughistoreApplication(AuthService authService, ApplicationUserService applicationUserService, AdminUserService adminUserService, ClientUserService clientUserService, StoreClientServices storeClientServices) {
        this.authService = authService;
        this.applicationUserService = applicationUserService;
        this.adminUserService = adminUserService;
        this.clientUserService = clientUserService;
        this.storeClientServices = storeClientServices;
    }

    public static void main(String[] args) {
        SpringApplication.run(YughistoreApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            AdminUserRepository adminUserRepository,
            AdminUserService adminUserService,
            ClientUserRepository clientUserRepository,
            ClientUserService clientUserService,
            StoreClientServices storeClientServices
    ) {
        return args -> {
            // Créer l'admin si inexistant
            if (!adminUserRepository.existsByCredentialsEmail("admin@gmail.com")) {
                adminUserService.save(
                        ApplicationUserDTO.builder()
                                .email("admin@gmail.com")
                                .password("!Password123")
                                .userName("Administrator")
                                .firstName("Admin")
                                .lastName("Admin")
                                .build()
                );
            }

            // Créer le client de test si inexistant
            if (!clientUserRepository.existsByCredentialsEmail("zink@gmail.com")) {
                ApplicationUserDTO clientDTO = clientUserService.save(
                        ApplicationUserDTO.builder()
                                .email("zink@gmail.com")
                                .password("!Password123")
                                .userName("Zink")
                                .firstName("Zink")
                                .lastName("User")
                                .build()
                );

                // Populate cart
                storeClientServices.addToShoppingCart(clientDTO.getId(), 1L);
                storeClientServices.addToShoppingCart(clientDTO.getId(), 2L);
                storeClientServices.addToShoppingCart(clientDTO.getId(), 3L);
                System.out.println(storeClientServices.getShoppingCartByUserID(clientDTO.getId()).getCards());

                storeClientServices.removeFromShoppingCart(clientDTO.getId(), 1L);
                System.out.println(storeClientServices.getShoppingCartByUserID(clientDTO.getId()).getCards());
            }
        };
    }
}
