package com.cal.yughistore.event;

import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
import lombok.Getter;

@Getter
public class PasswordResetRequestEvent {
    private final ApplicationUserDTO user;
    private final String resetToken;

    public PasswordResetRequestEvent(ApplicationUserDTO user, String resetToken) {
        this.user = user;
        this.resetToken = resetToken;
    }
}
