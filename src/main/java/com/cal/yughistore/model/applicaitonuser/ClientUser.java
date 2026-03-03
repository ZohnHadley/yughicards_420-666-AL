package com.cal.yughistore.model.applicaitonuser;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.auth.Credentials;
import com.cal.yughistore.model.applicaitonuser.auth.Role;
import com.cal.yughistore.model.yughiocard.properties.CardProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ClientUser extends ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public ClientUser(
            Long id,
            String profilePictureUrl,
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            ShoppingCart shoppingCart,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastLoginAt
    ) {
        super(
                id,
                profilePictureUrl,
                username,
                firstName,
                lastName,
                Credentials.builder()
                        .email(email)
                        .password(password)
                        .role(Role.ADMIN)
                        .build(),
                shoppingCart,
                active,
                createdAt,
                updatedAt,
                lastLoginAt
        );


    }
}
