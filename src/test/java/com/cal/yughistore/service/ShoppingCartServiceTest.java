package com.cal.yughistore.service;

import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.auth.Role;
import com.cal.yughistore.repository.card.YughioCardRepository;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.service.dto.user.ShoppingCartDTO;
import com.cal.yughistore.service.dto.user.UserPublicDTO;
import com.cal.yughistore.service.exception.storeException.ShoppingCartNotSavedException;
import com.cal.yughistore.service.user.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {


    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private YughioCardRepository yughioCardRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private ShoppingCartService shoppingCartService;

    private UserPublicDTO savedUserDTO;
    private ShoppingCartDTO cartDTO;

    @BeforeEach
    void setUp() {
        shoppingCartService = new ShoppingCartService(
                shoppingCartRepository,
                yughioCardRepository,
                applicationUserRepository
        );

        savedUserDTO = UserPublicDTO.builder()
                .id(1L)
                .email("user@email.com")
                .firstName("firstName")
                .lastName("lastName")
                .role(Role.CLIENT)
                .build();

        cartDTO = ShoppingCartDTO.builder()
                .applicationUser(savedUserDTO)
                .build();
    }

    @Test
    void save() {
        //
        ShoppingCartDTO savedCart = ShoppingCartDTO.builder()
                .id(1L)
                .applicationUser(savedUserDTO)
                .build();
        //
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(savedCart.toShoppingCart());
        ShoppingCartDTO responseCart = shoppingCartService.save(cartDTO);
        //
        assertEquals(savedCart.getId(), responseCart.getId());
        assertEquals(savedCart.getCartItemsList().size(), responseCart.getCartItemsList().size());
        assertEquals(savedCart.getApplicationUser().getId(), responseCart.getApplicationUser().getId());
    }

    @Test
    void save_userNull_shouldThrowShoppingCartNotSavedException() {
        ShoppingCartDTO savedCart = ShoppingCartDTO.builder()
                .build();

        assertThrows(ShoppingCartNotSavedException.class, () -> shoppingCartService.save(savedCart));

        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void save_null_shouldThrowShoppingCartNotSavedException() {
        ShoppingCartDTO cart = null;

        assertThrows(ShoppingCartNotSavedException.class, () -> shoppingCartService.save(cart));

        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void getShoppingCartByUserId() {
        ShoppingCartDTO savedCart = ShoppingCartDTO.builder()
                .id(1L)
                .applicationUser(savedUserDTO)
                .build();

        when(shoppingCartRepository.findByApplicationUser_Id(any(Long.class))).thenReturn(savedCart.toShoppingCart());

        ShoppingCartDTO responseCart = shoppingCartService.getShoppingCartByUserId(savedUserDTO.getId());
        //
        assertEquals(savedCart.getId(), responseCart.getId());
        assertEquals(savedCart.getCartItemsList().size(), responseCart.getCartItemsList().size());
        assertEquals(savedCart.getApplicationUser().getId(), responseCart.getApplicationUser().getId());
        assertEquals(savedCart.getApplicationUser().getEmail(), responseCart.getApplicationUser().getEmail());
    }

    @Test
    void getShoppingCartByUserEmail() {
        ShoppingCartDTO savedCart = ShoppingCartDTO.builder()
                .id(1L)
                .applicationUser(savedUserDTO)
                .build();

        when(shoppingCartRepository.findByApplicationUser_Credentials_Email(any(String.class))).thenReturn(savedCart.toShoppingCart());

        ShoppingCartDTO responseCart = shoppingCartService.getShoppingCartByUserEmail(savedUserDTO.getEmail());
        //
        assertEquals(savedCart.getId(), responseCart.getId());
        assertEquals(savedCart.getCartItemsList().size(), responseCart.getCartItemsList().size());
        assertEquals(savedCart.getApplicationUser().getId(), responseCart.getApplicationUser().getId());
        assertEquals(savedCart.getApplicationUser().getEmail(), responseCart.getApplicationUser().getEmail());
    }

}