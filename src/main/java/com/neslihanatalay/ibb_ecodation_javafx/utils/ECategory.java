package com.neslihanatalay.ibb_ecodation_javafx.utils;

import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding; 

private final ResourceBundleBinding resourceBundleBinding = new ResourceBundleBinding(); 

//private static final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory(); 

//static { RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME)); }

public enum ECategory {
    PERSONAL(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kişisel"),
    JOB(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("iş"),
    SCHOOL(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("okul");

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
            throw new RuntimeException(category);
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
