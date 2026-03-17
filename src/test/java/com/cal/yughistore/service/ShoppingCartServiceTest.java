package com.cal.yughistore.service;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.repository.user.CartItemRepository;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
import com.cal.yughistore.service.dto.user.CartItemDTO;
import com.cal.yughistore.service.dto.user.ClientDTO;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.exception.EntityIdentifierNullException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotFoundException;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotSavedException;
import com.cal.yughistore.service.user.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {


    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private YughioCardRepository yughioCardRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shoppingCartService = new ShoppingCartService(
                shoppingCartRepository,
                cartItemRepository,
                yughioCardRepository,
                applicationUserRepository
        );


    }

    @Test
    void save_ShouldReturnSavedShoppingCartDTO() {
        // Arrange
        ClientDTO savedUSerDTO = ClientDTO.builder().id(101L).build();
        ShoppingCartDTO savedCart = ShoppingCartDTO.builder()
                .id(1L)
                .cartItemsList(new ArrayList<>())
                .applicationUserId(savedUSerDTO.getId())
                .build();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setShoppingCart(savedCart.toShoppingCart());
        cartItem.setCard(new YughioCard());
        savedCart.getCartItemsList().add(CartItemDTO.of(cartItem));

        ShoppingCart shoppingCartEntity = savedCart.toShoppingCart();
        shoppingCartEntity.setApplicationUser(savedUSerDTO.toApplicationUser());

        when(applicationUserRepository.existsById(101L)).thenReturn(true);
        when(applicationUserRepository.findById(101L))
                .thenReturn(Optional.of(savedUSerDTO.toApplicationUser()));

        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenReturn(shoppingCartEntity);


        // Act
        ShoppingCartDTO responseCart = shoppingCartService.save(savedCart);
        // Assert
        assertEquals(savedCart.getId(), responseCart.getId());
        assertEquals(savedCart.getCartItemsList().size(), responseCart.getCartItemsList().size());
        assertEquals(savedCart.getApplicationUserId(), responseCart.getApplicationUserId());
        assertFalse(responseCart.getCartItemsList().isEmpty());
        verify(applicationUserRepository, times(1)).findById(101L);
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }


    @Test
    void save_null_shouldThrowShoppingCartNotSavedException() {
        // Act & Assert
        assertThrows(ShoppingCartNotSavedException.class, () -> shoppingCartService.save(null));
        verify(applicationUserRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }


    @Test
    void getShoppingCartByUserId_ShouldReturnCartDTO_ForValidUserId() {
        // Arrange
        Long userId = 101L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        ApplicationUser savedUser = new ApplicationUser();
        savedUser.setId(userId);
        savedUser.setCredentials(
                Credentials.builder()
                        .role(Role.CLIENT)
                        .build()
        );

        when(applicationUserRepository.existsById(userId)).thenReturn(true);
        when(applicationUserRepository.findById(userId)).thenReturn(Optional.of(savedUser));
        when(shoppingCartRepository.findByApplicationUser_Id(userId)).thenReturn(shoppingCart);

        // Act
        ShoppingCartDTO result = shoppingCartService.getShoppingCartByUserId(userId);

        // Assert
        assertEquals(1L, result.getId());
        verify(shoppingCartRepository, times(1)).findByApplicationUser_Id(userId);
    }

    @Test
    void getShoppingCartByUserId_ShouldThrowException_ForNullUserId() {
        // Act & Assert
        assertThrows(EntityIdentifierNullException.class, () -> shoppingCartService.getShoppingCartByUserId(null));
        verify(shoppingCartRepository, never()).findByApplicationUser_Id(any());
    }


    @Test
    void getShoppingCartByUserEmail_ShouldReturnCartDTO_ForValidEmail() {
        // Arrange
        String email = "test@example.com";
        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        user.setCredentials(Credentials.builder().email(email).role(Role.CLIENT).build());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(10L);
        shoppingCart.setApplicationUser(user);

        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByApplicationUser_Credentials_Email(email)).thenReturn(shoppingCart);

        // Act
        ShoppingCartDTO result = shoppingCartService.getShoppingCartByUserEmail(email);

        // Assert
        assertEquals(10L, result.getId());
        assertEquals(email, result.getApplicationUserEmail());
        verify(applicationUserRepository, times(1)).findApplicationUserByEmail(email);
        verify(shoppingCartRepository, times(1)).findByApplicationUser_Credentials_Email(email);
    }

    @Test
    void getShoppingCartByUserEmail_ShouldThrowException_ForNullEmail() {
        // Act & Assert
        assertThrows(EntityIdentifierNullException.class, () -> shoppingCartService.getShoppingCartByUserEmail(null));
        verify(applicationUserRepository, never()).findApplicationUserByEmail(anyString());
        verify(shoppingCartRepository, never()).findByApplicationUser_Credentials_Email(anyString());
    }

    @Test
    void getShoppingCartByUserEmail_ShouldThrowException_ForNonexistentEmail() {
        // Arrange
        String email = "nonexistent@example.com";

        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShoppingCartNotFoundException.class, () -> shoppingCartService.getShoppingCartByUserEmail(email));

        verify(applicationUserRepository, times(1)).findApplicationUserByEmail(email);
        verify(shoppingCartRepository, never()).findByApplicationUser_Credentials_Email(anyString());
    }
}