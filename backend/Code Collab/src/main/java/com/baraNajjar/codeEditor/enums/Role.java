package com.baraNajjar.codeEditor.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    EDITOR,
    VIEWER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
