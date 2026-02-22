package com.cal.yughistore.model.applicaitonuser.auth;

import java.util.HashSet;
import java.util.Set;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    CLIENT("ROLE_CLIENT");

    private final String string;
    private final Set<Role> managedRoles = new HashSet<>();

    Role(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
