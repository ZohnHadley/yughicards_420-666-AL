package com.cal.yughistore.service.user;

import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.repository.user.ClientUserRepository;
import com.cal.yughistore.repository.user.ShoppingCartRepository;
import com.cal.yughistore.service.dto.user.ApplicationUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientUserServiceTest {
    private ClientUserService clientUserService;

    @Mock
    private ClientUserRepository clientUserRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientUserService = new ClientUserService(clientUserRepository, shoppingCartRepository);
    }

    @Test
    void testSave_whenValidDTO_thenSavesUserAndShoppingCart() {
        ApplicationUserDTO dto = ApplicationUserDTO.builder()
                .email("test@example.com")
                .password("Valid@1234")
                .userName("testuser")
                .build();

        when(clientUserRepository.existsByCredentialsEmail(dto.getEmail())).thenReturn(false);
        when(clientUserRepository.save(any(ClientUser.class))).thenAnswer(invocation -> {
            ClientUser clientUser = invocation.getArgument(0);
            clientUser.setId(1L);
            return clientUser;
        });

        ApplicationUserDTO result = clientUserService.save(dto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(clientUserRepository, times(1)).save(any(ClientUser.class));
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    void testSave_whenNullDTO_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clientUserService.save(null)
        );

        assertEquals("applicationUserDTO must not be null", exception.getMessage());
        verifyNoInteractions(clientUserRepository, shoppingCartRepository);
    }

    @Test
    void testSave_whenBlankPassword_thenThrowsException() {
        ApplicationUserDTO dto = ApplicationUserDTO.builder()
                .email("test@example.com")
                .userName("testuser")
                .password("   ")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clientUserService.save(dto)
        );

        assertEquals("password must not be blank", exception.getMessage());
        verifyNoInteractions(clientUserRepository, shoppingCartRepository);
    }

    @Test
    void testSave_whenEmailAlreadyExists_thenThrowsException() {
        ApplicationUserDTO dto = ApplicationUserDTO.builder()
                .email("existing@example.com")
                .password("Valid@1234")
                .userName("testuser")
                .build();

        when(clientUserRepository.existsByCredentialsEmail(dto.getEmail())).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> clientUserService.save(dto)
        );

        assertEquals("email is already in use", exception.getMessage());
        verify(clientUserRepository, times(1)).existsByCredentialsEmail(dto.getEmail());
        verify(clientUserRepository, never()).save(any());
        verify(shoppingCartRepository, never()).save(any());
    }
}