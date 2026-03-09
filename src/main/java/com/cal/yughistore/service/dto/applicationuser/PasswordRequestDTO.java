package com.cal.yughistore.service.dto.applicationuser;
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
