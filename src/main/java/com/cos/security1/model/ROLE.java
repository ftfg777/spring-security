package com.cos.security1.model;

public enum ROLE {
    ROLE_USER("유저"),
    ROLE_ADMIN("관리자"),
    ROLE_MANAGER("매니저");
    private String roleName;

    ROLE(String roleName) {
        this.roleName = roleName;
    }

    public String getName(){
        return this.roleName;
    }

}
