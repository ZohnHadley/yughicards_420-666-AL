package com.cal.yughistore;

import com.cal.yughistore.service.YughioCardService;
import com.cal.yughistore.service.user.ApplicationUserService;
import com.cal.yughistore.service.user.AdminUserService;
import com.cal.yughistore.service.user.ClientUserService;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
import com.cal.yughistore.service.storeServices.StoreAdminService;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.service.user.ShoppingCartService;
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
    CommandLineRunner commandLineRunner(ApplicationContext context, ShoppingCartService shoppingCartService, YughioCardService yughioCardService) {
        return args -> {
            ConsoleLoadingBar consoleLoadingBar = new ConsoleLoadingBar();

            adminUserService.save(
                    ApplicationUserDTO.builder()
                            .email("admin@gmail.com")
                            .password("!Password123")
                            .build()
            );
            ApplicationUserDTO applicationUserDTO = clientUserService.save(
                    ApplicationUserDTO.builder()
                            .email("zink@gmail.com")
                            .password("!Password123")
                            .userName("Zink")
                            .firstName("Zink")
                            .lastName("User")
                            .build()
            );

            /// stock up cards FIRST
            List<Long> cardIds = new ArrayList<>();
            System.out.println("stocking up to 1000 cards");
            for (int i = 1; i < 1000; i++) {
                if (yughioCardService.getById((long) i) != null)
                    cardIds.add((long) i);
            }
            for (Long cardId : cardIds) {
                storeAdminService.incrementCardStock(cardId, 30);
            }
            consoleLoadingBar.finish();

            /// populate cart AFTER stock is set
            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 1L, 1);
            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 2L, 1);
            storeClientService.addToShoppingCart(applicationUserDTO.getId(), 3L, 1);

            System.out.println(shoppingCartService.getShoppingCartByUserId(applicationUserDTO.getId()).getCards());

            /// remove 1 card from cart
            storeClientService.removeFromShoppingCart(applicationUserDTO.getId(), 1L);

            System.out.println(shoppingCartService.getShoppingCartByUserId(applicationUserDTO.getId()).getCards());
        };
    }
}