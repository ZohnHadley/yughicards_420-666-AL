package com.cal.yughistore.event;

import com.cal.yughistore.model.user.ApplicationUser;
import lombok.Getter;



@Getter
public class UserCreatedEvent {
    private final ApplicationUser user;

    public UserCreatedEvent(ApplicationUser user) {
        this.user = user;
    }
}