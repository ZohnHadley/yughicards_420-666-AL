package com.cal.yughistore.services.storeServices;

import com.cal.yughistore.services.applicaitonuser.ClientUserService;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.services.shoppingcart.ShoppingCartService;
import com.cal.yughistore.services.yughiocard.YughioCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreClientServiceTest {

    @Mock
    private ClientUserService clientUserService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private YughioCardService yughioCardService;

    @InjectMocks
    private StoreClientService storeClientService;

    @Test
    void getShoppingCartByUserID_shouldDelegateToShoppingCartService() {
        Long userId = 1L;
        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(new ArrayList<>())
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);

        ShoppingCartDTO result = storeClientService.getShoppingCartByUserID(userId);

        assertSame(cart, result);
        verify(shoppingCartService).getShoppingCartByUserId(userId);
    }

    @Test
    void getShoppingCartByUserEmail_shouldDelegateToShoppingCartService() {
        String email = "test@example.com";
        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(new ArrayList<>())
                .build();

        when(shoppingCartService.getShoppingCartByUserEmail(email)).thenReturn(cart);

        ShoppingCartDTO result = storeClientService.getShoppingCartByUserEmail(email);

        assertSame(cart, result);
        verify(shoppingCartService).getShoppingCartByUserEmail(email);
    }

    @Test
    void addToShoppingCart_shouldThrowWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.addToShoppingCart(null, 100L)
        );

        assertEquals("userId can't be null", exception.getMessage());
        verifyNoInteractions(shoppingCartService, yughioCardService);
    }

    @Test
    void addToShoppingCart_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.addToShoppingCart(1L, null)
        );

        assertEquals("cardId can't be null", exception.getMessage());
        verifyNoInteractions(yughioCardService);
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void addToShoppingCart_shouldThrowWhenCartNotFound() {
        Long userId = 1L;
        Long cardId = 100L;

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(null);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> storeClientService.addToShoppingCart(userId, cardId)
        );

        assertEquals("Shopping cart not found for userId=1", exception.getMessage());
        verify(shoppingCartService).getShoppingCartByUserId(userId);
        verifyNoInteractions(yughioCardService);
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void addToShoppingCart_shouldInitializeCardsListWhenNull() {
        Long userId = 1L;
        Long cardId = 100L;

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(null)
                .build();

        YughioCardDTO card = YughioCardDTO.builder()
                .id(cardId)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);
        when(yughioCardService.getById(cardId)).thenReturn(card);

        storeClientService.addToShoppingCart(userId, cardId);

        assertNotNull(cart.getCards());
        assertEquals(1, cart.getCards().size());
        assertSame(card, cart.getCards().get(0));
        verify(shoppingCartService).save(cart);
    }

    @Test
    void addToShoppingCart_shouldCopyImmutableListAndAddCard() {
        Long userId = 1L;
        Long existingCardId = 50L;
        Long newCardId = 100L;

        YughioCardDTO existingCard = YughioCardDTO.builder()
                .id(existingCardId)
                .name("Dark Magician")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        YughioCardDTO newCard = YughioCardDTO.builder()
                .id(newCardId)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(List.of(existingCard))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);
        when(yughioCardService.getById(newCardId)).thenReturn(newCard);

        storeClientService.addToShoppingCart(userId, newCardId);

        assertEquals(2, cart.getCards().size());
        assertTrue(cart.getCards() instanceof ArrayList);
        assertEquals(existingCardId, cart.getCards().get(0).getId());
        assertEquals(newCardId, cart.getCards().get(1).getId());
        verify(shoppingCartService).save(cart);
    }

    @Test
    void addToShoppingCart_shouldNotDuplicateCardButStillSave() {
        Long userId = 1L;
        Long cardId = 100L;

        YughioCardDTO existingCard = YughioCardDTO.builder()
                .id(cardId)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(new ArrayList<>(List.of(existingCard)))
                .build();

        YughioCardDTO fetchedCard = YughioCardDTO.builder()
                .id(cardId)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);
        when(yughioCardService.getById(cardId)).thenReturn(fetchedCard);

        storeClientService.addToShoppingCart(userId, cardId);

        assertEquals(1, cart.getCards().size());
        verify(shoppingCartService).save(cart);
    }

    @Test
    void removeFromShoppingCart_shouldThrowWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.removeFromShoppingCart(null, 100L)
        );

        assertEquals("userId can't be null", exception.getMessage());
        verifyNoInteractions(shoppingCartService, yughioCardService);
    }

    @Test
    void removeFromShoppingCart_shouldThrowWhenCardIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.removeFromShoppingCart(1L, null)
        );

        assertEquals("cardId can't be null", exception.getMessage());
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void removeFromShoppingCart_shouldReturnWhenCartIsNull() {
        Long userId = 1L;
        Long cardId = 100L;

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(null);

        assertDoesNotThrow(() -> storeClientService.removeFromShoppingCart(userId, cardId));

        verify(shoppingCartService).getShoppingCartByUserId(userId);
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void removeFromShoppingCart_shouldReturnWhenCardsListIsNull() {
        Long userId = 1L;
        Long cardId = 100L;

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(null)
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);

        assertDoesNotThrow(() -> storeClientService.removeFromShoppingCart(userId, cardId));

        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void removeFromShoppingCart_shouldRemoveCardAndSave() {
        Long userId = 1L;
        Long cardIdToRemove = 100L;

        YughioCardDTO cardToRemove = YughioCardDTO.builder()
                .id(cardIdToRemove)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        YughioCardDTO otherCard = YughioCardDTO.builder()
                .id(200L)
                .name("Dark Magician")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(new ArrayList<>(List.of(cardToRemove, otherCard)))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);

        storeClientService.removeFromShoppingCart(userId, cardIdToRemove);

        assertEquals(1, cart.getCards().size());
        assertEquals(200L, cart.getCards().get(0).getId());
        verify(shoppingCartService).save(cart);
    }

    @Test
    void removeFromShoppingCart_shouldCopyImmutableListRemoveCardAndSave() {
        Long userId = 1L;
        Long cardIdToRemove = 100L;

        YughioCardDTO cardToRemove = YughioCardDTO.builder()
                .id(cardIdToRemove)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        YughioCardDTO otherCard = YughioCardDTO.builder()
                .id(200L)
                .name("Dark Magician")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(List.of(cardToRemove, otherCard))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);

        storeClientService.removeFromShoppingCart(userId, cardIdToRemove);

        assertTrue(cart.getCards() instanceof ArrayList);
        assertEquals(1, cart.getCards().size());
        assertEquals(200L, cart.getCards().get(0).getId());
        verify(shoppingCartService).save(cart);
    }

    @Test
    void removeFromShoppingCart_shouldNotSaveWhenCardWasNotPresent() {
        Long userId = 1L;
        Long cardIdToRemove = 999L;

        YughioCardDTO existingCard = YughioCardDTO.builder()
                .id(100L)
                .name("Blue-Eyes White Dragon")
                .card_images(new ArrayList<>())
                .card_prices(new ArrayList<>())
                .card_sets(new ArrayList<>())
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(10L)
                .cards(new ArrayList<>(List.of(existingCard)))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);

        storeClientService.removeFromShoppingCart(userId, cardIdToRemove);

        assertEquals(1, cart.getCards().size());
        verify(shoppingCartService, never()).save(any());
    }
}