package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.ClientUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientDTO extends ApplicationUserDTO{
    private Role role = Role.CLIENT;

    @Builder
    public ClientDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, String email, String password, ShoppingCartDTO shoppingCart) {
        super(
                id, profilePictureUrl,
                userName, firstName, lastName, email, password,
                shoppingCart,
                Role.CLIENT
        );
    }

    public ClientUser toClientUser() {
        ClientUser clientUser = new ClientUser();
        clientUser.setId(getId());
        clientUser.setProfilePictureUrl(getProfilePictureUrl());
        clientUser.setUserName(getUserName());
        clientUser.setFirstName(getFirstName());
        clientUser.setLastName(getLastName());
        clientUser.setCredentials(
                Credentials.builder()
                        .email(getEmail())
                        .password(getPassword())
                        .role(role)
                        .build()
        );
        if (getShoppingCart() != null) {
            clientUser.setShoppingCart(getShoppingCart().toShoppingCart());
        }
        return clientUser;
    }
}
