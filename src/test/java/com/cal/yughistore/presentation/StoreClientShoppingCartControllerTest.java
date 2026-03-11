package com.cal.yughistore.presentation;

import com.cal.yughistore.presentation.client.StoreClientShoppingCartController;
import com.cal.yughistore.service.applicaitonuser.ApplicationUserService;
import com.cal.yughistore.service.dto.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.service.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreClientShoppingCartControllerTest {

    @Mock
    private StoreClientService storeClientService;

    @Mock
    private ApplicationUserService applicationUserService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private StoreClientShoppingCartController controller;

    @Test
    void getShoppingCart_shouldReturnCardsForCurrentUser() {
        String token = "mock-jwt-token";
        Long userId = 7L;
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(1L).name("Dark Magician").build(),
                YughioCardDTO.builder().id(2L).name("Blue-Eyes White Dragon").build()
        );
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .id(100L)
                .cards(cards)
                .build();
        ApplicationUserDTO user = ApplicationUserDTO.builder()
                .id(userId)
                .build();

        try (MockedStatic<JwtTokenUtils> jwtMock = mockStatic(JwtTokenUtils.class)) {
            jwtMock.when(() -> JwtTokenUtils.getTokenFromRequest(request)).thenReturn(token);
            when(applicationUserService.getMe(token)).thenReturn(user);
            when(storeClientService.getShoppingCartByUserID(userId)).thenReturn(shoppingCartDTO);

            ResponseEntity<List<YughioCardDTO>> response = controller.getShoppingCart(request);

            assertEquals(200, response.getStatusCode().value());
            assertEquals(cards, response.getBody());
            verify(applicationUserService).getMe(token);
            verify(storeClientService).getShoppingCartByUserID(userId);
        }
    }

    @Test
    void addToShoppingCart_shouldAddCardAndReturnUpdatedCart() {
        String token = "mock-jwt-token";
        Long userId = 7L;
        Long cardId = 42L;
        List<YughioCardDTO> cards = List.of(
                YughioCardDTO.builder().id(cardId).name("Dark Magician").build()
        );
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .id(100L)
                .cards(cards)
                .build();
        ApplicationUserDTO user = ApplicationUserDTO.builder()
                .id(userId)
                .build();

        try (MockedStatic<JwtTokenUtils> jwtMock = mockStatic(JwtTokenUtils.class)) {
            jwtMock.when(() -> JwtTokenUtils.getTokenFromRequest(request)).thenReturn(token);
            when(applicationUserService.getMe(token)).thenReturn(user);
            when(storeClientService.getShoppingCartByUserID(userId)).thenReturn(shoppingCartDTO);

            ResponseEntity<List<YughioCardDTO>> response = controller.addToShoppingCart(request, cardId, 1);

            assertEquals(200, response.getStatusCode().value());
            assertEquals(cards, response.getBody());
            verify(applicationUserService).getMe(token);
            verify(storeClientService).addToShoppingCart(userId, cardId, 1);
            verify(storeClientService).getShoppingCartByUserID(userId);
        }
    }

    @Test
    void removeFromShoppingCart_shouldRemoveCardAndReturnUpdatedCart() {
        String token = "mock-jwt-token";
        Long userId = 7L;
        Long cardId = 42L;
        List<YughioCardDTO> cards = List.of();
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .id(100L)
                .cards(cards)
                .build();
        ApplicationUserDTO user = ApplicationUserDTO.builder()
                .id(userId)
                .build();

        try (MockedStatic<JwtTokenUtils> jwtMock = mockStatic(JwtTokenUtils.class)) {
            jwtMock.when(() -> JwtTokenUtils.getTokenFromRequest(request)).thenReturn(token);
            when(applicationUserService.getMe(token)).thenReturn(user);
            when(storeClientService.getShoppingCartByUserID(userId)).thenReturn(shoppingCartDTO);

            ResponseEntity<List<YughioCardDTO>> response = controller.removeFromShoppingCart(request, cardId);

            assertEquals(200, response.getStatusCode().value());
            assertEquals(cards, response.getBody());
            verify(applicationUserService).getMe(token);
            verify(storeClientService).removeFromShoppingCart(userId, cardId);
            verify(storeClientService).getShoppingCartByUserID(userId);
        }
    }

    @Test
    void clearShoppingCart_shouldClearCartAndReturnEmptyCart() {
        String token = "mock-jwt-token";
        Long userId = 7L;
        List<YughioCardDTO> cards = List.of();
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .id(100L)
                .cards(cards)
                .build();
        ApplicationUserDTO user = ApplicationUserDTO.builder()
                .id(userId)
                .build();

        try (MockedStatic<JwtTokenUtils> jwtMock = mockStatic(JwtTokenUtils.class)) {
            jwtMock.when(() -> JwtTokenUtils.getTokenFromRequest(request)).thenReturn(token);
            when(applicationUserService.getMe(token)).thenReturn(user);
            when(storeClientService.getShoppingCartByUserID(userId)).thenReturn(shoppingCartDTO);

            ResponseEntity<List<YughioCardDTO>> response = controller.clearShoppingCart(request);

            assertEquals(200, response.getStatusCode().value());
            assertEquals(cards, response.getBody());
            verify(applicationUserService).getMe(token);
            verify(storeClientService).clearShoppingCart(userId);
            verify(storeClientService).getShoppingCartByUserID(userId);
        }
    }
}