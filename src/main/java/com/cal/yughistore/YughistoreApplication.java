package com.cal.yughistore;

import com.cal.yughistore.service.applicaitonuser.ApplicationUserService;
import com.cal.yughistore.service.applicaitonuser.AdminUserService;
import com.cal.yughistore.service.applicaitonuser.ClientUserService;
import com.cal.yughistore.service.dto.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.service.storeServices.StoreAdminService;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.service.utils.AuthService;
import com.cal.yughistore.utils.ConsoleLoadingBar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class YughistoreApplication {

    /// user password requirements
    /// ***user password must contain at least 1 special character & 1 upper case & 1 number***
    ///
    private final ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();
    private final AuthService authService;
    private final ApplicationUserService applicationUserService;
    private final AdminUserService adminUserService;
    private final ClientUserService clientUserService;
    private final StoreAdminService storeAdminService;
    private final StoreClientService storeClientService;

    public YughistoreApplication(AuthService authService, ApplicationUserService applicationUserService, AdminUserService adminUserService, ClientUserService clientUserService, StoreAdminService storeAdminService, StoreClientService storeClientService) {
        this.authService = authService;
        this.applicationUserService = applicationUserService;
        this.adminUserService = adminUserService;
        this.clientUserService = clientUserService;
        this.storeAdminService = storeAdminService;
        this.storeClientService = storeClientService;
    }

    public static void main(String[] args) {
        SpringApplication.run(YughistoreApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
//            ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();
//
//            adminUserService.save(
//                    ApplicationUserDTO.builder()
//                            .email("admin@gmail.com")
//                            .password("!Password123")
//                            .build()
//            );
//            ApplicationUserDTO applicationUserDTO = clientUserService.save(
//                    ApplicationUserDTO.builder()
//                            .email("zink@gmail.com")
//                            .password("!Password123")
//                            .build()
//            );
//            /// populate cart
//
//            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 1L);
//            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 2L);
//            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 3L);
//
//            System.out.println(storeClientService.getShoppingCartByUserID(applicationUserDTO.getId()).getCards());
////
////            ///  remove 1 card from cart
//            storeClientService.removeFromShoppingCart(applicationUserDTO.getId(), 1L);
////
//            System.out.println(storeClientService.getShoppingCartByUserID(applicationUserDTO.getId()).getCards());

            List<Long> cardIds = new ArrayList<>();
            System.out.println("stocking up to 1000 cards");
            for (int i = 1; i <= 1000; i++) {
                cardIds.add((long) i);
                storeAdminService.incrementCardStock(((long) i), 30);
                consoleLoadingBar.printProgress(i, 1000);
            }
            consoleLoadingBar.finish();
        };
    }
}
