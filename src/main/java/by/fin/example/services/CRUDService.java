package by.fin.example.services;

import by.fin.example.entities.ProductModel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CRUDService<T, ID extends Serializable> {
    T save(T t);
    T update(T t);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();

}
