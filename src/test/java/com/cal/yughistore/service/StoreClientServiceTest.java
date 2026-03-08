package com.cal.yughistore.service;
import com.cal.yughistore.service.dto.shoppingcart.ShoppingCartDTO;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import com.cal.yughistore.service.shoppingcart.ShoppingCartService;
import com.cal.yughistore.service.storeServices.StoreClientService;
import com.cal.yughistore.service.yughiocard.YughioCardService;
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
    private ShoppingCartService shoppingCartService;

    @Mock
    private YughioCardService yughioCardService;

    @InjectMocks
    private StoreClientService storeClientService;

    @Test
    void getShoppingCartByUserID_shouldDelegateToShoppingCartService() {
        ShoppingCartDTO cart = ShoppingCartDTO.builder().id(1L).build();
        when(shoppingCartService.getShoppingCartByUserId(10L)).thenReturn(cart);

        ShoppingCartDTO result = storeClientService.getShoppingCartByUserID(10L);

        assertSame(cart, result);
        verify(shoppingCartService).getShoppingCartByUserId(10L);
    }

    @Test
    void getShoppingCartByUserEmail_shouldDelegateToShoppingCartService() {
        ShoppingCartDTO cart = ShoppingCartDTO.builder().id(2L).build();
        when(shoppingCartService.getShoppingCartByUserEmail("user@example.com")).thenReturn(cart);

        ShoppingCartDTO result = storeClientService.getShoppingCartByUserEmail("user@example.com");

        assertSame(cart, result);
        verify(shoppingCartService).getShoppingCartByUserEmail("user@example.com");
    }

    @Test
    void addToShoppingCart_shouldThrowWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.addToShoppingCart(null, 1L)
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
        verifyNoInteractions(shoppingCartService, yughioCardService);
    }

    @Test
    void addToShoppingCart_shouldThrowWhenCartIsMissing() {
        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(null);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> storeClientService.addToShoppingCart(1L, 99L)
        );

        assertEquals("Shopping cart not found for userId=1", exception.getMessage());
        verify(shoppingCartService).getShoppingCartByUserId(1L);
        verifyNoInteractions(yughioCardService);
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void addToShoppingCart_shouldInitializeNullCardListAndSave() {
        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(100L)
                .cards(null)
                .build();

        YughioCardDTO card = YughioCardDTO.builder()
                .id(7L)
                .name("Blue-Eyes White Dragon")
                .build();

        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(cart);
        when(yughioCardService.getById(7L)).thenReturn(card);

        storeClientService.addToShoppingCart(1L, 7L);

        assertNotNull(cart.getCards());
        assertEquals(1, cart.getCards().size());
        assertEquals(7L, cart.getCards().get(0).getId());

        verify(shoppingCartService).save(cart);
    }

    @Test
    void addToShoppingCart_shouldConvertUnmodifiableListAndAvoidDuplicates() {
        YughioCardDTO existingCard = YughioCardDTO.builder()
                .id(7L)
                .name("Blue-Eyes White Dragon")
                .build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(101L)
                .cards(List.of(existingCard))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(cart);
        when(yughioCardService.getById(7L)).thenReturn(existingCard);

        storeClientService.addToShoppingCart(1L, 7L);

        assertNotNull(cart.getCards());
        assertEquals(1, cart.getCards().size());
        assertTrue(cart.getCards() instanceof ArrayList);

        verify(shoppingCartService).save(cart);
    }

    @Test
    void addToShoppingCart_shouldAppendCardWhenNotAlreadyPresent() {
        YughioCardDTO existingCard = YughioCardDTO.builder().id(1L).name("Card 1").build();
        YughioCardDTO newCard = YughioCardDTO.builder().id(2L).name("Card 2").build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(102L)
                .cards(new ArrayList<>(List.of(existingCard)))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(5L)).thenReturn(cart);
        when(yughioCardService.getById(2L)).thenReturn(newCard);

        storeClientService.addToShoppingCart(5L, 2L);

        assertEquals(2, cart.getCards().size());
        assertEquals(List.of(1L, 2L), cart.getCards().stream().map(YughioCardDTO::getId).toList());

        verify(shoppingCartService).save(cart);
    }

    @Test
    void removeFromShoppingCart_shouldThrowWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeClientService.removeFromShoppingCart(null, 1L)
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
        verifyNoInteractions(shoppingCartService, yughioCardService);
    }

    @Test
    void removeFromShoppingCart_shouldDoNothingWhenCartIsNull() {
        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(null);

        storeClientService.removeFromShoppingCart(1L, 10L);

        verify(shoppingCartService).getShoppingCartByUserId(1L);
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void removeFromShoppingCart_shouldRemoveCardFromUnmodifiableListAndSave() {
        YughioCardDTO card1 = YughioCardDTO.builder().id(1L).build();
        YughioCardDTO card2 = YughioCardDTO.builder().id(2L).build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(200L)
                .cards(List.of(card1, card2))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(cart);

        storeClientService.removeFromShoppingCart(1L, 2L);

        assertEquals(1, cart.getCards().size());
        assertEquals(1L, cart.getCards().get(0).getId());
        assertTrue(cart.getCards() instanceof ArrayList);

        verify(shoppingCartService).save(cart);
    }

    @Test
    void removeFromShoppingCart_shouldNotSaveWhenNothingWasRemoved() {
        YughioCardDTO card1 = YughioCardDTO.builder().id(1L).build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(201L)
                .cards(new ArrayList<>(List.of(card1)))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(cart);

        storeClientService.removeFromShoppingCart(1L, 99L);

        assertEquals(1, cart.getCards().size());
        verify(shoppingCartService, never()).save(any());
    }

    @Test
    void clearShoppingCart_shouldEmptyCardsAndSave() {
        YughioCardDTO card1 = YughioCardDTO.builder().id(1L).build();
        YughioCardDTO card2 = YughioCardDTO.builder().id(2L).build();

        ShoppingCartDTO cart = ShoppingCartDTO.builder()
                .id(300L)
                .cards(new ArrayList<>(List.of(card1, card2)))
                .build();

        when(shoppingCartService.getShoppingCartByUserId(8L)).thenReturn(cart);

        storeClientService.clearShoppingCart(8L);

        assertNotNull(cart.getCards());
        assertTrue(cart.getCards().isEmpty());
        verify(shoppingCartService).save(cart);
    }
}