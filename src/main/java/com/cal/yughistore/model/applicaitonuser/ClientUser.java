package com.cal.yughistore.model.applicaitonuser;

import com.cal.yughistore.model.applicaitonuser.auth.Credentials;
import com.cal.yughistore.model.applicaitonuser.auth.Role;
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
public class ClientUser extends ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public ClientUser(
            Long id,
            String profilePictureUrl,
            String userName,
            String firstName,
            String lastName,
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
                userName,
                firstName,
                lastName,
                Credentials.builder()
                        .email(email)
                        .password(password)
                        .role(Role.CLIENT)
                        .build(),
                active,
                createdAt,
                updatedAt,
                lastLoginAt
        );


    }

}
