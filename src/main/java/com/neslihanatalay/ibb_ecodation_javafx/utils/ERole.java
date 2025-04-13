package com.neslihanatalay.ibb_ecodation_javafx.utils;

import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding; 

private final ResourceBundleBinding resourceBundleBinding = new ResourceBundleBinding(); 

public enum ERole {
    USER(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcırol"),
    MODERATOR(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("moderatörrol"),
    ADMIN(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yöneticirol");

    private final String description;

    ERole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ERole fromString(String role) {
        try {
            return ERole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(role);
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
