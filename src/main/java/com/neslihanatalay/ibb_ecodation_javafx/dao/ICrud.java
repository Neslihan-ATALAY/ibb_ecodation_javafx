package com.neslihanatalay.ibb_ecodation_javafx.dao;

import java.util.List;
import java.util.Optional;

public interface ICrud<T> {

    // CREATE
    Optional<T> create(T t);

    // LIST
    Optional<List<T>> list();
	Optional<List<T>> listByUserId(int userId) {

    // FIND
    Optional<T> findByName(String name);

    Optional<T> findById(int id);

    // UPDATE
    Optional<T> update(int id, T t);

    // DELETE
    Optional<T> delete(int id);
}
