package com.cal.yughistore.services.dto.applicationuser;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordRequestDTO {
    String token;
    String newPassword;
}
