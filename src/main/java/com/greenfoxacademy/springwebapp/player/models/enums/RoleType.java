package com.greenfoxacademy.springwebapp.player.models.enums;

public enum RoleType {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    public final String roleType;

    RoleType(String roleType) {
        this.roleType = roleType;
    }
}
