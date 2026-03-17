package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.*;
import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationUserDTO {
    private Long id;

    private String profilePictureUrl = "";

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4)
    private String userName;

    @Size(min = 4)
    private String firstName;

    @Size(min = 2)
    private String lastName;


    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;


    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$",
            message = "Password must be 8-50 characters long, contain at least one uppercase, " +
                    "one lowercase, one number, and one special character"
    )
    private String password;

    @JsonIgnore
    private ShoppingCartDTO shoppingCart;

    private Role role = Role.CLIENT;

    public static ApplicationUserDTO of(ApplicationUser user) {
        ApplicationUserDTO applicationUserDTO = new ApplicationUserDTO();
        applicationUserDTO.setId(user.getId());
        applicationUserDTO.setProfilePictureUrl(user.getProfilePictureUrl());
        applicationUserDTO.setUserName(user.getUserName());
        applicationUserDTO.setFirstName(user.getFirstName());
        applicationUserDTO.setLastName(user.getLastName());
        applicationUserDTO.setEmail(user.getEmail());
        applicationUserDTO.setPassword(null);
        applicationUserDTO.setRole(user.getRole());

        if (user.getShoppingCart() != null) {
            applicationUserDTO.setShoppingCart(ShoppingCartDTO.of(user.getShoppingCart()));
        }

        return applicationUserDTO;
    }

    public ApplicationUser toApplicationUser() {
        return switch (this.role) {
            case ADMIN -> populateCommonFields(new AdminUser());
            case CLIENT -> populateCommonFields(new ClientUser());
            default -> populateCommonFields(new GuestUser());
        };
    }

    private <T extends ApplicationUser> T populateCommonFields(T user) {
        user.setId(this.id);
        user.setProfilePictureUrl(this.profilePictureUrl);
        user.setUserName(this.userName);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setCredentials(buildCredentials());
        user.setShoppingCart(this.shoppingCart != null ? this.shoppingCart.toShoppingCart() : null);
        return user;
    }

    private Credentials buildCredentials() {
        return Credentials.builder()
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
