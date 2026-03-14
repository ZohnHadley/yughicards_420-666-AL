package com.cal.yughistore.model.user;

import com.cal.yughistore.model.user.auth.Credentials;
import com.cal.yughistore.model.user.auth.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "shoppingCart")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String profilePictureUrl;
    private String userName;

    private String firstName;
    private String lastName;

    @Embedded
    private Credentials credentials;

    @OneToOne(mappedBy = "applicationUser", fetch = FetchType.LAZY)
    @JsonManagedReference
    private ShoppingCart shoppingCart;

    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public String getEmail() {
        return credentials != null ? credentials.getEmail() : null;
    }

    public String getPassword() {
        return credentials != null ? credentials.getPassword() : null;
    }

    public Role getRole() {
        return credentials != null ? credentials.getRole() : null;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return credentials != null ? credentials.getAuthorities() : java.util.List.of();
    }
}
