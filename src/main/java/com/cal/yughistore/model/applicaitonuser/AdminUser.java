package com.cal.yughistore.model.applicaitonuser;


import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.auth.Credentials;
import com.cal.yughistore.model.applicaitonuser.auth.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminUser extends ApplicationUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Builder
    public AdminUser(
            Long id,
            String profilePictureUrl,
            String email,
            String password,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime lastLoginAt
    ) {
        super(
                id,
                profilePictureUrl,
                "Administrator",
                "firstName",
                "lastName",
                Credentials.builder()
                        .email(email)
                        .password(password)
                        .role(Role.ADMIN)
                        .build(),
                new ShoppingCart(),
                active,
                createdAt,
                updatedAt,
                lastLoginAt
        );


    }
}
