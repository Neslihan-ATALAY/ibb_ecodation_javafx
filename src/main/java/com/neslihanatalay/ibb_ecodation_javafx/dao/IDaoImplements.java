package com.neslihanatalay.ibb_ecodation_javafx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface IGenericsMethod<T> {

    // GENERICS METOTO (LIST,FIND)
    public T mapToObjectDTO(ResultSet resultSet) throws SQLException;

    public Optional<T> selectSingle(String sql, Object... params);

}
