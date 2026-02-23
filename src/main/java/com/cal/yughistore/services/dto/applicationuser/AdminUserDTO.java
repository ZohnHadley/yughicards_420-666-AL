package com.cal.yughistore.services.dto.applicationuser;

import com.cal.yughistore.model.applicaitonuser.AdminUser;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class AdminUserDTO extends ApplicationUserDTO {

    @Builder
    public AdminUserDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, String email, String password) {
        super(
                id, profilePictureUrl, userName, firstName, lastName, email, password
        );
    }

    public AdminUserDTO(AdminUser user) {
        super(user);
    }

    public static AdminUserDTO of(AdminUser user) {
        return new AdminUserDTO(user);
    }

    public AdminUser toAdminUser() {
        return AdminUser.builder()
                .id(this.getId())
                .profilePictureUrl(this.getProfilePictureUrl())
                .email(this.getEmail())
                .password(this.getPassword())
                .build();
    }
}