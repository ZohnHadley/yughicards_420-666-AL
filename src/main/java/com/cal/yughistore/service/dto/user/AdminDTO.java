package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.AdminUser;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminDTO extends ApplicationUserDTO{
    private Role role = Role.ADMIN;

    @Builder
    public AdminDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, String email, String password) {
        super(
                id, profilePictureUrl,
                userName, firstName, lastName, email, password,
                 new ShoppingCartDTO(),
                Role.ADMIN
        );
    }

    public AdminUser toAdminUser() {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(getId());
        adminUser.setProfilePictureUrl(getProfilePictureUrl());
        adminUser.setUserName(getUserName());
        adminUser.setFirstName(getFirstName());
        adminUser.setLastName(getLastName());
        adminUser.setCredentials(
                Credentials.builder()
                        .email(getEmail())
                        .password(getPassword())
                        .role(role)
                        .build()
        );
        adminUser.setShoppingCart(new ShoppingCart());
        return adminUser;
    }
}
