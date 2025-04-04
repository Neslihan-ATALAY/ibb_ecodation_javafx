package com.neslihanatalay.ibb_ecodation_javafx.dao;

import java.util.Optional;

public interface ILogin <T> {

    // Login
    Optional<T> loginUser(String username, String password);
}
