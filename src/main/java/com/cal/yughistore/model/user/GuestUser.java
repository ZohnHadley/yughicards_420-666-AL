package com.cal.yughistore.model.user;

import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GuestUser extends ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public GuestUser(
            Long id,
            String profilePictureUrl,
            String username,
            String firstName,
            String lastName,
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
                        .email("")
                        .password("")
                        .role(Role.GUEST)
                        .build(),
                shoppingCart,
                active,
                createdAt,
                updatedAt,
                lastLoginAt
        );


    }
}
