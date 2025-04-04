package com.neslihanatalay.ibb_ecodation_javafx.dao;

import com.neslihanatalay.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;

import java.sql.Connection;

public interface IDaoImplements<T> extends ICrud<T>,IGenericsMethod<T> {

    default Connection iDaoImplementsDatabaseConnection() {
        return SingletonPropertiesDBConnection.getInstance().getConnection();
    }
}
