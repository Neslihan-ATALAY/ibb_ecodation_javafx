package com.neslihanatalay.ibb_ecodation_javafx.utils;

public enum ECategory {
    PERSONAL("Kişisel"),
    JOB("İş"),
    SCHOOL("Okul");

    private final String description;

    ECategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ECategory fromString(String category) {
        try {
            return ECategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Geçersiz kategori: " + category);
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
