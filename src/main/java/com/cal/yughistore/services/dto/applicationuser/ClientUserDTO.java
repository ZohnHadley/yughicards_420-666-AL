package com.cal.yughistore.services.dto.applicationuser;

import com.cal.yughistore.model.applicaitonuser.ClientUser;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ClientUserDTO extends ApplicationUserDTO {

    @Builder
    public ClientUserDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, String email, String password) {
        super(
            id, profilePictureUrl, userName, firstName, lastName, email, password
        );
    }

    public ClientUserDTO(ClientUser user) {
        super(user);
    }

    public static ClientUserDTO of(ClientUser user) {
        return new ClientUserDTO(user);
    }

    public ClientUser toClientUser() {
        return ClientUser.builder()
                .id(this.getId())
                .profilePictureUrl(this.getProfilePictureUrl())
                .userName(this.getUserName())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .email(this.getEmail())
                .password(this.getPassword())
                .build();
    }
}
